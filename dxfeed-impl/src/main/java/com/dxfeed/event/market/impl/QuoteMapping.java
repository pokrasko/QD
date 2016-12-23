/*
 * QDS - Quick Data Signalling Library
 * Copyright (C) 2002-2016 Devexperts LLC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package com.dxfeed.event.market.impl;

import com.devexperts.qd.DataRecord;
import com.devexperts.qd.ng.RecordCursor;
import com.devexperts.qd.util.Decimal;
import com.devexperts.qd.util.MappingUtil;
import com.devexperts.util.TimeUtil;

public class QuoteMapping extends MarketEventMapping {
// BEGIN: CODE AUTOMATICALLY GENERATED: DO NOT MODIFY. IT IS REGENERATED BY com.dxfeed.api.codegen.ImplCodeGen
	private final int iSequence;
	private final int iTimeNanoPart;
	private final int iBidTime;
	private final int iBidExchangeCode;
	private final int iBidPrice;
	private final int iBidSize;
	private final int iAskTime;
	private final int iAskExchangeCode;
	private final int iAskPrice;
	private final int iAskSize;

	public QuoteMapping(DataRecord record) {
		super(record);
		iSequence = MappingUtil.findIntField(record, "Sequence", false);
		iTimeNanoPart = MappingUtil.findIntField(record, "TimeNanoPart", false);
		iBidTime = MappingUtil.findIntField(record, "Bid.Time", false);
		iBidExchangeCode = MappingUtil.findIntField(record, "Bid.Exchange", false);
		iBidPrice = MappingUtil.findIntField(record, "Bid.Price", true);
		iBidSize = MappingUtil.findIntField(record, "Bid.Size", true);
		iAskTime = MappingUtil.findIntField(record, "Ask.Time", false);
		iAskExchangeCode = MappingUtil.findIntField(record, "Ask.Exchange", false);
		iAskPrice = MappingUtil.findIntField(record, "Ask.Price", true);
		iAskSize = MappingUtil.findIntField(record, "Ask.Size", true);
		putNonDefaultPropertyName("Bid.Exchange", "BidExchangeCode");
		putNonDefaultPropertyName("Ask.Exchange", "AskExchangeCode");
	}

	public int getSequence(RecordCursor cursor) {
		if (iSequence < 0)
			return 0;
		return getInt(cursor, iSequence);
	}

	public void setSequence(RecordCursor cursor, int sequence) {
		if (iSequence < 0)
			return;
		setInt(cursor, iSequence, sequence);
	}

	public int getTimeNanoPart(RecordCursor cursor) {
		if (iTimeNanoPart < 0)
			return 0;
		return getInt(cursor, iTimeNanoPart);
	}

	public void setTimeNanoPart(RecordCursor cursor, int timeNanoPart) {
		if (iTimeNanoPart < 0)
			return;
		setInt(cursor, iTimeNanoPart, timeNanoPart);
	}

	public long getBidTimeMillis(RecordCursor cursor) {
		if (iBidTime < 0)
			return 0;
		return getInt(cursor, iBidTime) * 1000L;
	}

	public void setBidTimeMillis(RecordCursor cursor, long bidTime) {
		if (iBidTime < 0)
			return;
		setInt(cursor, iBidTime, TimeUtil.getSecondsFromTime(bidTime));
	}

	public int getBidTimeSeconds(RecordCursor cursor) {
		if (iBidTime < 0)
			return 0;
		return getInt(cursor, iBidTime);
	}

	public void setBidTimeSeconds(RecordCursor cursor, int bidTime) {
		if (iBidTime < 0)
			return;
		setInt(cursor, iBidTime, bidTime);
	}

	@Deprecated
	public char getBidExchange(RecordCursor cursor) {
		if (iBidExchangeCode < 0)
			return recordExchange;
		return (char)getInt(cursor, iBidExchangeCode);
	}

	@Deprecated
	public void setBidExchange(RecordCursor cursor, char bidExchange) {
		if (iBidExchangeCode < 0)
			return;
		setInt(cursor, iBidExchangeCode, bidExchange);
	}

	public char getBidExchangeCode(RecordCursor cursor) {
		if (iBidExchangeCode < 0)
			return recordExchange;
		return (char)getInt(cursor, iBidExchangeCode);
	}

	public void setBidExchangeCode(RecordCursor cursor, char bidExchangeCode) {
		if (iBidExchangeCode < 0)
			return;
		setInt(cursor, iBidExchangeCode, bidExchangeCode);
	}

	public double getBidPrice(RecordCursor cursor) {
		return Decimal.toDouble(getInt(cursor, iBidPrice));
	}

	public void setBidPrice(RecordCursor cursor, double bidPrice) {
		setInt(cursor, iBidPrice, Decimal.compose(bidPrice));
	}

	public int getBidPriceDecimal(RecordCursor cursor) {
		return getInt(cursor, iBidPrice);
	}

	public void setBidPriceDecimal(RecordCursor cursor, int bidPrice) {
		setInt(cursor, iBidPrice, bidPrice);
	}

	public int getBidSize(RecordCursor cursor) {
		return getInt(cursor, iBidSize);
	}

	public void setBidSize(RecordCursor cursor, int bidSize) {
		setInt(cursor, iBidSize, bidSize);
	}

	public long getAskTimeMillis(RecordCursor cursor) {
		if (iAskTime < 0)
			return 0;
		return getInt(cursor, iAskTime) * 1000L;
	}

	public void setAskTimeMillis(RecordCursor cursor, long askTime) {
		if (iAskTime < 0)
			return;
		setInt(cursor, iAskTime, TimeUtil.getSecondsFromTime(askTime));
	}

	public int getAskTimeSeconds(RecordCursor cursor) {
		if (iAskTime < 0)
			return 0;
		return getInt(cursor, iAskTime);
	}

	public void setAskTimeSeconds(RecordCursor cursor, int askTime) {
		if (iAskTime < 0)
			return;
		setInt(cursor, iAskTime, askTime);
	}

	@Deprecated
	public char getAskExchange(RecordCursor cursor) {
		if (iAskExchangeCode < 0)
			return recordExchange;
		return (char)getInt(cursor, iAskExchangeCode);
	}

	@Deprecated
	public void setAskExchange(RecordCursor cursor, char askExchange) {
		if (iAskExchangeCode < 0)
			return;
		setInt(cursor, iAskExchangeCode, askExchange);
	}

	public char getAskExchangeCode(RecordCursor cursor) {
		if (iAskExchangeCode < 0)
			return recordExchange;
		return (char)getInt(cursor, iAskExchangeCode);
	}

	public void setAskExchangeCode(RecordCursor cursor, char askExchangeCode) {
		if (iAskExchangeCode < 0)
			return;
		setInt(cursor, iAskExchangeCode, askExchangeCode);
	}

	public double getAskPrice(RecordCursor cursor) {
		return Decimal.toDouble(getInt(cursor, iAskPrice));
	}

	public void setAskPrice(RecordCursor cursor, double askPrice) {
		setInt(cursor, iAskPrice, Decimal.compose(askPrice));
	}

	public int getAskPriceDecimal(RecordCursor cursor) {
		return getInt(cursor, iAskPrice);
	}

	public void setAskPriceDecimal(RecordCursor cursor, int askPrice) {
		setInt(cursor, iAskPrice, askPrice);
	}

	public int getAskSize(RecordCursor cursor) {
		return getInt(cursor, iAskSize);
	}

	public void setAskSize(RecordCursor cursor, int askSize) {
		setInt(cursor, iAskSize, askSize);
	}
// END: CODE AUTOMATICALLY GENERATED
}
