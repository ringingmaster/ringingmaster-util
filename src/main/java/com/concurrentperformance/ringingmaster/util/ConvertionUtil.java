package com.concurrentperformance.ringingmaster.util;


import java.util.Collection;

/**
 * Helper class to perform various data type conversion
 *
 * @author Stephen Lake
 */
public class ConvertionUtil {

	public static int[] integerCollectionToArray(final Collection<Integer> integerCollection) {
		final int[] result = new int[integerCollection.size()];
		int i=0;
		for (final Integer value : integerCollection) {
			result[i++] = value;
		}
		return result;
	}
}

