/*
 * QDS - Quick Data Signalling Library
 * Copyright (C) 2002-2016 Devexperts LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package com.dxfeed.event.candle.impl;

import com.devexperts.qd.DataRecord;
import com.devexperts.qd.ng.RecordCursor;
import com.devexperts.qd.util.Decimal;
import com.devexperts.qd.util.MappingUtil;
import com.devexperts.util.TimeUtil;

public class TradeHistoryMapping extends CandleEventMapping {
// BEGIN: CODE AUTOMATICALLY GENERATED: DO NOT MODIFY. IT IS REGENERATED BY com.dxfeed.api.codegen.ImplCodeGen
	private final int iTime;
	private final int iSequence;
	private final int iExchangeCode;
	private final int iClose;
	private final int iVolume;
	private final int iBidPrice;
	private final int iAskPrice;

	public TradeHistoryMapping(DataRecord record) {
		super(record);
		iTime = MappingUtil.findIntField(record, "Time", true);
		iSequence = MappingUtil.findIntField(record, "Sequence", true);
		iExchangeCode = MappingUtil.findIntField(record, "Exchange", false);
		iClose = MappingUtil.findIntField(record, "Price", true);
		iVolume = MappingUtil.findIntField(record, "Size", true);
		iBidPrice = MappingUtil.findIntField(record, "Bid", false);
		iAskPrice = MappingUtil.findIntField(record, "Ask", false);
		putNonDefaultPropertyName("Exchange", "ExchangeCode");
		putNonDefaultPropertyName("Price", "Close");
		putNonDefaultPropertyName("Size", "Volume");
		putNonDefaultPropertyName("Bid", "BidPrice");
		putNonDefaultPropertyName("Ask", "AskPrice");
	}

	public long getTimeMillis(RecordCursor cursor) {
		return getInt(cursor, iTime) * 1000L;
	}

	public void setTimeMillis(RecordCursor cursor, long time) {
		setInt(cursor, iTime, TimeUtil.getSecondsFromTime(time));
	}

	public int getTimeSeconds(RecordCursor cursor) {
		return getInt(cursor, iTime);
	}

	public void setTimeSeconds(RecordCursor cursor, int time) {
		setInt(cursor, iTime, time);
	}

	public int getSequence(RecordCursor cursor) {
		return getInt(cursor, iSequence);
	}

	public void setSequence(RecordCursor cursor, int sequence) {
		setInt(cursor, iSequence, sequence);
	}

	@Deprecated
	public char getExchange(RecordCursor cursor) {
		if (iExchangeCode < 0)
			return '\0';
		return (char)getInt(cursor, iExchangeCode);
	}

	@Deprecated
	public void setExchange(RecordCursor cursor, char exchange) {
		if (iExchangeCode < 0)
			return;
		setInt(cursor, iExchangeCode, exchange);
	}

	public char getExchangeCode(RecordCursor cursor) {
		if (iExchangeCode < 0)
			return '\0';
		return (char)getInt(cursor, iExchangeCode);
	}

	public void setExchangeCode(RecordCursor cursor, char exchangeCode) {
		if (iExchangeCode < 0)
			return;
		setInt(cursor, iExchangeCode, exchangeCode);
	}

	@Deprecated
	public double getPrice(RecordCursor cursor) {
		return Decimal.toDouble(getInt(cursor, iClose));
	}

	@Deprecated
	public void setPrice(RecordCursor cursor, double price) {
		setInt(cursor, iClose, Decimal.compose(price));
	}

	@Deprecated
	public int getPriceDecimal(RecordCursor cursor) {
		return getInt(cursor, iClose);
	}

	@Deprecated
	public void setPriceDecimal(RecordCursor cursor, int price) {
		setInt(cursor, iClose, price);
	}

	public double getClose(RecordCursor cursor) {
		return Decimal.toDouble(getInt(cursor, iClose));
	}

	public void setClose(RecordCursor cursor, double close) {
		setInt(cursor, iClose, Decimal.compose(close));
	}

	public int getCloseDecimal(RecordCursor cursor) {
		return getInt(cursor, iClose);
	}

	public void setCloseDecimal(RecordCursor cursor, int close) {
		setInt(cursor, iClose, close);
	}

	@Deprecated
	public int getSize(RecordCursor cursor) {
		return getInt(cursor, iVolume);
	}

	@Deprecated
	public void setSize(RecordCursor cursor, int size) {
		setInt(cursor, iVolume, size);
	}

	public int getVolume(RecordCursor cursor) {
		return getInt(cursor, iVolume);
	}

	public void setVolume(RecordCursor cursor, int volume) {
		setInt(cursor, iVolume, volume);
	}

	@Deprecated
	public double getBid(RecordCursor cursor) {
		if (iBidPrice < 0)
			return Double.NaN;
		return Decimal.toDouble(getInt(cursor, iBidPrice));
	}

	@Deprecated
	public void setBid(RecordCursor cursor, double bid) {
		if (iBidPrice < 0)
			return;
		setInt(cursor, iBidPrice, Decimal.compose(bid));
	}

	@Deprecated
	public int getBidDecimal(RecordCursor cursor) {
		if (iBidPrice < 0)
			return 0;
		return getInt(cursor, iBidPrice);
	}

	@Deprecated
	public void setBidDecimal(RecordCursor cursor, int bid) {
		if (iBidPrice < 0)
			return;
		setInt(cursor, iBidPrice, bid);
	}

	public double getBidPrice(RecordCursor cursor) {
		if (iBidPrice < 0)
			return Double.NaN;
		return Decimal.toDouble(getInt(cursor, iBidPrice));
	}

	public void setBidPrice(RecordCursor cursor, double bidPrice) {
		if (iBidPrice < 0)
			return;
		setInt(cursor, iBidPrice, Decimal.compose(bidPrice));
	}

	public int getBidPriceDecimal(RecordCursor cursor) {
		if (iBidPrice < 0)
			return 0;
		return getInt(cursor, iBidPrice);
	}

	public void setBidPriceDecimal(RecordCursor cursor, int bidPrice) {
		if (iBidPrice < 0)
			return;
		setInt(cursor, iBidPrice, bidPrice);
	}

	@Deprecated
	public double getAsk(RecordCursor cursor) {
		if (iAskPrice < 0)
			return Double.NaN;
		return Decimal.toDouble(getInt(cursor, iAskPrice));
	}

	@Deprecated
	public void setAsk(RecordCursor cursor, double ask) {
		if (iAskPrice < 0)
			return;
		setInt(cursor, iAskPrice, Decimal.compose(ask));
	}

	@Deprecated
	public int getAskDecimal(RecordCursor cursor) {
		if (iAskPrice < 0)
			return 0;
		return getInt(cursor, iAskPrice);
	}

	@Deprecated
	public void setAskDecimal(RecordCursor cursor, int ask) {
		if (iAskPrice < 0)
			return;
		setInt(cursor, iAskPrice, ask);
	}

	public double getAskPrice(RecordCursor cursor) {
		if (iAskPrice < 0)
			return Double.NaN;
		return Decimal.toDouble(getInt(cursor, iAskPrice));
	}

	public void setAskPrice(RecordCursor cursor, double askPrice) {
		if (iAskPrice < 0)
			return;
		setInt(cursor, iAskPrice, Decimal.compose(askPrice));
	}

	public int getAskPriceDecimal(RecordCursor cursor) {
		if (iAskPrice < 0)
			return 0;
		return getInt(cursor, iAskPrice);
	}

	public void setAskPriceDecimal(RecordCursor cursor, int askPrice) {
		if (iAskPrice < 0)
			return;
		setInt(cursor, iAskPrice, askPrice);
	}
// END: CODE AUTOMATICALLY GENERATED
}
