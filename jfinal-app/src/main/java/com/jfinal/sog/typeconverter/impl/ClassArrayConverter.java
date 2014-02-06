/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.typeconverter.TypeConverterManagerBean;

/**
 * Converts given object to <code>Class</code> array.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>single Class is wrapped in 1-length array</li>
 * <li>string value is converted to string array (from CSV format) and
 * then each element is converted</li>
 * </ul>
 */
public class ClassArrayConverter extends ArrayConverter<Class> {

	public ClassArrayConverter(TypeConverterManagerBean typeConverterManagerBean) {
		super(typeConverterManagerBean, Class.class);
	}

	@Override
	protected Class[] createArray(int length) {
		return new Class[length];
	}

}