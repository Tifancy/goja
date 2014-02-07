/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.kit.common.CsvKit;
import com.jfinal.sog.typeconverter.TypeConversionException;
import com.jfinal.sog.typeconverter.TypeConverter;
import com.jfinal.sog.typeconverter.TypeConverterManagerBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Converts given object to <code>byte[]</code>.
 */
public class ByteArrayConverter implements TypeConverter<byte[]> {

    protected final TypeConverterManagerBean typeConverterManagerBean;

    public ByteArrayConverter(TypeConverterManagerBean typeConverterManagerBean) {
        this.typeConverterManagerBean = typeConverterManagerBean;
    }

    public byte[] convert(Object value) {
        if (value == null) {
            return null;
        }

        Class valueClass = value.getClass();

        if (!valueClass.isArray()) {
            // source is not an array
            return convertValueToArray(value);
        }

        // source is an array
        return convertArrayToArray(value);
    }

    /**
     * Converts type using type converter manager.
     */
    protected byte convertType(Object value) {
        return typeConverterManagerBean.convertType(value, byte.class);
    }

    /**
     * Creates an array with single element.
     */
    protected byte[] convertToSingleElementArray(Object value) {
        return new byte[]{convertType(value)};
    }

    /**
     * Converts non-array value to array. Detects various
     * types and collections, iterates them to make conversion
     * and to create target array.
     */
    protected byte[] convertValueToArray(Object value) {
        if (value instanceof Blob) {
            Blob blob = (Blob) value;
            try {
                long length = blob.length();
                if (length > Integer.MAX_VALUE) {
                    throw new TypeConversionException("Blob is too big.");
                }
                return blob.getBytes(1, (int) length);
            } catch (SQLException sex) {
                throw new TypeConversionException(value, sex);
            }
        }

        if (value instanceof File) {
            try {
                return FileUtils.readFileToByteArray((File) value);
            } catch (IOException ioex) {
                throw new TypeConversionException(value, ioex);
            }
        }

        if (value instanceof List) {
            List list = (List) value;
            byte[] target = new byte[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Object element = list.get(i);
                target[i] = convertType(element);
            }

            return target;
        }

        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            byte[] target = new byte[collection.size()];

            int i = 0;
            for (Object element : collection) {
                target[i] = convertType(element);
                i++;
            }

            return target;
        }

        if (value instanceof Iterable) {
            Iterable iterable = (Iterable) value;

            int count = 0;
            for (Object element : iterable) {
                count++;
            }

            byte[] target = new byte[count];
            int i = 0;
            for (Object element : iterable) {
                target[i] = convertType(element);
                i++;
            }

            return target;
        }

        if (value instanceof CharSequence) {
            String[] strings = CsvKit.toStringArray(value.toString());
            return convertArrayToArray(strings);
        }

        // everything else:
        return convertToSingleElementArray(value);
    }

    /**
     * Converts array value to array.
     */
    protected byte[] convertArrayToArray(Object value) {
        Class valueComponentType = value.getClass().getComponentType();

        if (valueComponentType == byte.class) {
            // equal types, no conversion needed
            return (byte[]) value;
        }

        byte[] result;

        if (valueComponentType.isPrimitive()) {
            // convert primitive array to target array
            result = convertPrimitiveArrayToArray(value, valueComponentType);
        } else {
            // convert object array to target array
            Object[] array = (Object[]) value;
            result = new byte[array.length];

            for (int i = 0; i < array.length; i++) {
                result[i] = convertType(array[i]);
            }
        }

        return result;
    }


    /**
     * Converts primitive array to target array.
     */
    protected byte[] convertPrimitiveArrayToArray(Object value, Class primitiveComponentType) {
        byte[] result = null;

        if (primitiveComponentType == byte[].class) {
            return (byte[]) value;
        }

        if (primitiveComponentType == int.class) {
            int[] array = (int[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == long.class) {
            long[] array = (long[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == float.class) {
            float[] array = (float[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == double.class) {
            double[] array = (double[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == short.class) {
            short[] array = (short[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == char.class) {
            char[] array = (char[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) array[i];
            }
        } else if (primitiveComponentType == boolean.class) {
            boolean[] array = (boolean[]) value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = (byte) (array[i] ? 1 : 0);
            }
        }
        return result;
    }

}