/*
 * QDS - Quick Data Signalling Library
 * Copyright (C) 2002-2016 Devexperts LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package com.devexperts.io;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import com.devexperts.util.Base64;
import com.devexperts.util.SystemProperties;

/**
 * Convenient class that opens specified URL for reading.
 * It supports all URL formats supported by Java and also understands file names using both local and absolute paths.
 * It properly configures outgoing connections and releases all resources when closed.
 * <p>
 * To open HTTP resources use standard HTTP URL syntax:
 * <ul>
 * <li> http://www.host.com/path/file.txt
 * <li> http://www.host.com:8080/images/picture.jpg
 * <li> https://www.host.com/mydata/secret.zip
 * <li> https://user:password@www.host.com/mydata/secret.zip
 * </ul>
 * <p>
 * To open FTP resources use standard FTP URL syntax:
 * <ul>
 * <li> ftp://ftp.host.com/path/file.zip                --- anonymous user, local path, default mode
 * <li> ftp://ftp.host.com/%2Fabsolute-path/file.zip    --- anonymous user, absolute path ('%2F' is quoted root '/'), default mode
 * <li> ftp://ftp.host.com/file.zip;type=i              --- anonymous user, local path, binary mode
 * <li> ftp://ftp.host.com/file.txt;type=a              --- anonymous user, local path, ASCII mode
 * <li> ftp://ftp.host.com/path/                        --- anonymous user, local path, UNIX-style file list
 * <li> ftp://ftp.host.com/path;type=d                  --- anonymous user, local path, plain file list (includes 'path/' prefix)
 * <li> ftp://ftp.host.com/path/.;type=d                --- anonymous user, local path, plain file list (without prefix)
 * <li> ftp://user:password@ftp.host.com/file.zip   --- specified user, local path, default mode
 * </ul>
 * <p>
 * To open file use either local file name or standard file URL syntax:
 * <ul>
 * <li> data.txt
 * <li> path/data.txt
 * <li> /absolute-path/data.txt
 * <li> file:data.txt
 * <li> file:path/data.txt
 * <li> file:/absolute-path/data.txt
 * <li> file:/C:/absolute-path/data.txt
 * </ul>
 */
public class URLInputStream extends FilterInputStream {

	// ===================== private static constants =====================

	private static final int CONNECT_TIMEOUT = SystemProperties.getIntProperty(
		URLInputStream.class, "connectTimeout", 30000);
	private static final int READ_TIMEOUT = SystemProperties.getIntProperty(
		URLInputStream.class, "readTimeout", 60000);

	// =====================  public static methods =====================

	/**
	 * Resolves a given URL in the context of the current file.
	 * @param url url, relative, or absolute file name.
	 * @return Resolved url.
	 * @throws MalformedURLException if url cannot be parsed.
	 */
	public static URL resolveURL(String url) throws MalformedURLException {
		if (url.length() > 2 && url.charAt(1) == ':' && File.separatorChar == '\\')
			url = "/" + url; // special case for full file path with drive letter on windows
		return new URL(new File(".").toURL(), url);
	}

	/**
	 * Opens {@link URLConnection} for a specified URL. This method {@link #resolveURL(String) resolves}
	 * specified URL first, for a proper support of file name.
	 * Use {@link #checkConnectionResponseCode(URLConnection) checkConnectionResponseCode} after establishing
	 * connection to ensure that it was Ok.
	 * This is a shortcut for
	 * <code>{@link #openConnection(URL, String, String) openConnection}({@link #resolveURL(String) resolveURL}(url), <b>null</b>, <b>null</b>)</code>.
	 *
	 * @param url the URL.
	 * @return URLConnection
	 * @throws IOException if an I/O error occurs.
	 */
	public static URLConnection openConnection(String url) throws IOException {
		return openConnection(resolveURL(url), null, null);
	}

	/**
	 * Opens {@link URLConnection} for a specified URL with a specified basic user and password credentials.
	 * Use {@link #checkConnectionResponseCode(URLConnection) checkConnectionResponseCode} after establishing
	 * connection to ensure that it was Ok.
	 * Credentials are used only when both user and password are non-null and non-empty.
	 * Specified credentials take precedence over authentication information that is supplied to this method
	 * as part of URL user info like {@code "http://user:password@host:port/path/file"}.
	 *
	 * @param url the URL.
	 * @param user the user name (may be null).
	 * @param password the password (may be null).
	 * @return URLConnection
	 * @throws IOException if an I/O error occurs.
	 */
	public static URLConnection openConnection(URL url, String user, String password) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setAllowUserInteraction(false);
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		String auth;
		if (user != null && !user.isEmpty() && password != null && !password.isEmpty())
			auth = user + ":" + password;
		else
			auth = url.getUserInfo();
		if (auth != null && !auth.isEmpty())
			connection.setRequestProperty("Authorization", "Basic " +
				Base64.DEFAULT.encode(auth.getBytes(StandardCharsets.UTF_8)));
		return connection;
	}

	/**
	 * Checks connection response code and throws {@link IOException} if it is not Ok.
	 * @param connection URLConnection
	 * @throws IOException if an I/O error occurs
	 */
	public static void checkConnectionResponseCode(URLConnection connection) throws IOException {
		if (connection instanceof HttpURLConnection && ((HttpURLConnection)connection).getResponseCode() != HttpURLConnection.HTTP_OK)
			throw new IOException("Unexpected response: " + connection.getHeaderField(0));
	}

	/**
	 * Reads content for specified URL and returns it as a byte array.
	 *
	 * @param url the URL to read
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] readURL(String url) throws IOException {
		try (URLInputStream in = new URLInputStream(url)) {
			ByteArrayOutput out = new ByteArrayOutput(Math.max(in.connection.getContentLength(), 10000));
			for (int n; (n = in.read(out.getBuffer(), out.getPosition(), out.getLimit() - out.getPosition())) >= 0; ) {
				out.setPosition(out.getPosition() + n);
				out.ensureCapacity(out.getPosition() + 10000);
			}
			return out.toByteArray();
		}
	}

	/**
	 * Returns last modification time for a specified URL. This method never returns 0.
	 * This is a shortcut for
	 * <code>{@link #getLastModified(URL, String, String) getLastModified}({@link #resolveURL(String) resolveURL}(url),
	 *          <b>null</b>, <b>null</b>)</code>.
	 * @param url the URL.
	 * @throws IOException if there is some problem retrieving last modification time or it is not known.
	 */
	public static long getLastModified(String url) throws IOException {
		return getLastModified(resolveURL(url), null, null);
	}

	/**
	 * Returns last modification time for a specified URL. This method never returns 0.
	 * @param url the URL.
	 * @param user the user name (may be null).
	 * @param password the password (may be null).
	 * @throws IOException if there is some problem retrieving last modification time or it is not known.
	 */
	public static long getLastModified(URL url, String user, String password) throws IOException {
		URLConnection connection = openConnection(url, user, password);
		if (connection instanceof HttpURLConnection)
			((HttpURLConnection)connection).setRequestMethod("HEAD");
		try {
			long lastModified = connection.getLastModified();
			checkConnectionResponseCode(connection);
			if (lastModified == 0)
				throw new IOException("Last modified time is not known");
			return lastModified;
		} finally {
			// NOTE: "getLastModified" implicitly performs "connect" and we must close input stream that we don't need
			connection.getInputStream().close();
		}
	}

	// =====================  instance fields =====================

	protected final URLConnection connection;

	// =====================  constructor and instance methods =====================

	/**
	 * Creates a <tt>URLInputStream</tt> for specified URL.
	 *
	 * @param url the URL to open
	 * @throws IOException if an I/O error occurs
	 */
	public URLInputStream(String url) throws IOException {
		super(null);
		connection = openConnection(url);
		in = connection.getInputStream();
		boolean ok = false;
		try {
			checkConnectionResponseCode(connection);
			ok = true;
		} finally {
			if (!ok)
				try {
					close();
				} catch (Exception ignored) {
					// Stream is closed because of connecting error - throw original exception, not closing one.
				}
		}
	}

	/**
	 * This method returns last modification time from this {@code URLInputStream}.
	 * Returns 0 when last modification time is not known.
	 */
	public long getLastModified() {
		return connection.getLastModified();
	}

	@Override
	public void close() throws IOException {
		if (in != null)
			in.close();
	}

	@Override
	protected void finalize() throws IOException {
		close();
	}
}
