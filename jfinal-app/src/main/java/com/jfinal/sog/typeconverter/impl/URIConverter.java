/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.typeconverter.TypeConversionException;
import com.jfinal.sog.typeconverter.TypeConverter;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Convert given object to <code>URI</code>.
 * Conversion rules:
 * <ul></ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>File</code> is converted</li>
 * <li><code>URL</code> is converted</li>
 * <li><code>String</code> representation is used for creating URI</li>
 * </ul>
 */
public class URIConverter implements TypeConverter<URI> {

	public URI convert(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof URI) {
			return (URI) value;
		}

		if (value instanceof File) {
			File file = (File) value;
			return file.toURI();
		}

		if (value instanceof URL) {
			URL url = (URL) value;
			try {
				return url.toURI();
			} catch (URISyntaxException usex) {
				throw new TypeConversionException(value, usex);
			}
		}

		try {
			return new URI(value.toString());
		} catch (URISyntaxException usex) {
			throw new TypeConversionException(value, usex);
		}
	}

}
