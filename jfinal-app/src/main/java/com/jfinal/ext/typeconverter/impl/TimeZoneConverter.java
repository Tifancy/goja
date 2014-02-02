/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;


import com.jfinal.ext.typeconverter.TypeConverter;

import java.util.TimeZone;

/**
 * Converts given object to Java <code>TimeZone</code>.
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>finally, string representation of the object is used for getting the time zone</li>
 * </ul>
 */
public class TimeZoneConverter implements TypeConverter<TimeZone> {

	public TimeZone convert(Object value) {
		if (value == null) {
			return null;
		}

		if (value.getClass() == TimeZone.class) {
			return (TimeZone) value;
		}

		return TimeZone.getTimeZone(value.toString());

	}
}
