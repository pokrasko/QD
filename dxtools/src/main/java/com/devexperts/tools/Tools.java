/*
 * QDS - Quick Data Signalling Library
 * Copyright (C) 2002-2016 Devexperts LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package com.devexperts.tools;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import com.devexperts.services.Services;
import com.devexperts.util.InvalidFormatException;

public final class Tools {
	private static final List<Class<? extends AbstractTool>> TOOLS;
	static {
		List<Class<? extends AbstractTool>> tools = Services.loadServiceClasses(AbstractTool.class, null);
		Collections.sort(tools, new ToolNameComparator());
		TOOLS = tools;
	}

	private static class ToolArgs {
		private final AbstractTool tool;
		private final String[] args;

		ToolArgs(AbstractTool tool, String[] args) {
			this.tool = tool;
			this.args = args;
		}

		public void parse() {
			tool.parse(args);
		}

		void execute() {
			tool.execute();
		}
	}

	private static class ToolNameComparator implements Comparator<Class<? extends AbstractTool>> {
		@Override
		public int compare(Class<? extends AbstractTool> a, Class<? extends AbstractTool> b) {
			return a.getSimpleName().compareTo(b.getSimpleName());
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: com.devexperts.tools.Tools <tool> [...]");
			System.err.println("Where <tool> is one of:");
			System.err.print(Help.listAllTools(Help.DEFAULT_WIDTH));
			System.err.println();
			System.err.println(Help.format(
				"To get detailed help on some tool use \"com.devexperts.tools.Help <tool name>\". \n" +
				"To get information about executing several tools under one JVM see \"com.devexperts.tools.Help Batch\".",
				Help.DEFAULT_WIDTH));
			return;
		}
		boolean ok = invoke(args);
		System.exit(ok ? 0 : 1); // kill even non-daemon threads and return non-zero code on error
	}

	// It is designed to be used by tests.
	public static boolean invoke(String... args) {
		try {
			List<ToolArgs> tools = new ArrayList<>();
			int i = 0;
			for (int j = 0; j <= args.length; j++) {
				if ((j >= args.length) || (args[j].equals("+"))) {
					if (i == j) {
						if (j == args.length) {
							throw new BadToolParametersException("Wrong format: '+' at the end of arguments list");
						} else {
							throw new BadToolParametersException("Wrong format: two consecutive '+' in arguments list");
						}
					}
					String[] singleToolArgs = new String[j - i - 1];
					System.arraycopy(args, i + 1, singleToolArgs, 0, singleToolArgs.length);
					String toolName = args[i];
					AbstractTool tool = getTool(toolName);
					if (tool == null)
						throw new BadToolParametersException("Unknown tool \"" + toolName + "\"");
					tools.add(new ToolArgs(tool, singleToolArgs));
					i = j + 1;
				}
			}
			// parse all options first
			for (ToolArgs ta : tools)
				try {
					ta.parse();
				} catch (Throwable t) {
					handleToolError(ta, t);
					return false;
				}
			// during parsing we might have modified System Properties.
			// Now we can initialize all startups
			Services.startup();
			// then execute all tools
			for (ToolArgs ta : tools)
				try {
					ta.execute();
				} catch (Throwable t) {
					handleToolError(ta, t);
					return false;
				}
			// wait for thread(s) to finish if needed
			// Note: this wait may be interrupted (will return from it with interruption flag set)
			for (ToolArgs ta : tools)
				waitForThread(ta.tool.mustWaitForThread());
			// clean up tools (close all their resources)
			for (ToolArgs ta : tools)
				closeOnExit(ta.tool.closeOnExit());
		} catch (Throwable t) {
			return false;
		}
		return true; // success
	}

	private static void handleToolError(ToolArgs ta, Throwable t) {
		String name = ta.tool.getClass().getSimpleName();
		if (t instanceof NoArgumentsException) {
			// Special signal to print detailed help
			System.err.println(name + ": " + ta.tool.generateHelpSummary(Help.DEFAULT_WIDTH));
			System.err.println("Use \"com.devexperts.tools.Help " + name + "\" for more detailed help.");
		} else if (t instanceof InvalidFormatException) {
			// Log with stack trace first, then show help message on the screen as last line
			// See [QD-251] Better logging when QD-filter can't be created/loaded
			System.err.println();
			System.err.println(name + ": " + t.getMessage());
			System.err.println("Use \"com.devexperts.tools.Help " + name + "\" for usage info.");
		}
	}

	private static void waitForThread(Thread thread) {
		if (thread != null)
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // reassert interruption flag
			}
	}

	private static void closeOnExit(List<Closeable> list) {
		if (list == null)
			return;
		for (Closeable closeable : list) {
			try {
				closeable.close();
			} catch (IOException e) {}
		}
	}

	public static void executeSingleTool(Class<? extends AbstractTool> tool, String[] args) {
		String[] argsWithToolName = new String[args.length + 1];
		argsWithToolName[0] = tool.getSimpleName();
		System.arraycopy(args, 0, argsWithToolName, 1, args.length);
		main(argsWithToolName);
	}

	/**
	 * Creates and returns new instance of a tool with specified name.
	 * @param name tool name.
	 * @return new instance of a tool with specified name, or null if couldn't
	 * find such tool or failed to create instance.
	 */
	public static AbstractTool getTool(String name) {
		for (Class<? extends AbstractTool> tool : TOOLS) {
			if (name.equalsIgnoreCase(tool.getSimpleName())) {
				try {
					return tool.newInstance();
				} catch (InstantiationException e) {
					return null;
				} catch (IllegalAccessException e) {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Returns all available tool names.
	 * @return all available tool names.
	 */
	public static List<String> getToolNames() {
		List<String> names = new ArrayList<>();
		for (Class<? extends AbstractTool> tool : TOOLS) {
			names.add(tool.getSimpleName());
		}
		return names;
	}
}
