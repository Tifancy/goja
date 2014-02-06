/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;



import com.jfinal.sog.kit.CsvKit;
import com.jfinal.sog.typeconverter.TypeConverter;
import com.jfinal.sog.typeconverter.TypeConverterManagerBean;

import java.util.Collection;
import java.util.List;

/**
 * Converts given object to <code>float[]</code>.
 */
public class FloatArrayConverter implements TypeConverter<float[]> {

    protected final TypeConverterManagerBean typeConverterManagerBean;

    public FloatArrayConverter(TypeConverterManagerBean typeConverterManagerBean) {
        this.typeConverterManagerBean = typeConverterManagerBean;
    }

    public float[] convert(Object value) {
        if (value == null) {
            return null;
        }

        Class valueClass = value.getClass();

        if (valueClass.isArray() == false) {
            // source is not an array
            return convertValueToArray(value);
        }

        // source is an array
        return convertArrayToArray(value);
    }

    /**
     * Converts type using type converter manager.
	 */
	protected float convertType(Object value) {
		return typeConverterManagerBean.convertType(value, float.class).floatValue();
	}

	/**
	 * Creates an array with single element.
	 */
	protected float[] convertToSingleElementArray(Object value) {
		return new float[] {convertType(value)};
	}

	/**
	 * Converts non-array value to array. Detects various
	 * collection types and iterates them to make conversion
	 * and to create target array.
 	 */
	protected float[] convertValueToArray(Object value) {
		if (value instanceof List) {
			List list = (List) value;
			float[] target = new float[list.size()];

			for (int i = 0; i < list.size(); i++) {
				Object element = list.get(i);
				target[i] = convertType(element);
			}

			return target;
		}

		if (value instanceof Collection) {
			Collection collection = (Collection) value;
			float[] target = new float[collection.size()];

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

			float[] target = new float[count];
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
	protected float[] convertArrayToArray(Object value) {
		Class valueComponentType = value.getClass().getComponentType();

		if (valueComponentType == float.class) {
			// equal types, no conversion needed
			return (float[]) value;
		}

		float[] result;

		if (valueComponentType.isPrimitive()) {
			// convert primitive array to target array
			result = convertPrimitiveArrayToArray(value, valueComponentType);
		} else {
			// convert object array to target array
			Object[] array = (Object[]) value;
			result = new float[array.length];

			for (int i = 0; i < array.length; i++) {
				result[i] = convertType(array[i]);
			}
		}

		return result;
	}


	/**
	 * Converts primitive array to target array.
	 */
	protected float[] convertPrimitiveArrayToArray(Object value, Class primitiveComponentType) {
		float[] result = null;

		if (primitiveComponentType == float[].class) {
			return (float[]) value;
		}

		if (primitiveComponentType == int.class) {
			int[] array = (int[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}
		}
		else if (primitiveComponentType == long.class) {
			long[] array = (long[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}
		}
		else if (primitiveComponentType == double.class) {
			double[] array = (double[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = (float) array[i];
			}
		}
		else if (primitiveComponentType == short.class) {
			short[] array = (short[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}
		}
		else if (primitiveComponentType == byte.class) {
			byte[] array = (byte[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}
		}
		else if (primitiveComponentType == char.class) {
			char[] array = (char[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}
		}
		else if (primitiveComponentType == boolean.class) {
			boolean[] array = (boolean[]) value;
			result = new float[array.length];
			for (int i = 0; i < array.length; i++) {
				result[i] = array[i] ? 1 : 0;
			}
		}
		return result;
	}

}