package com.trbboi.android;

public class AndroidUtils {
	public static Integer[] intToIntegerArray( int[] array ) {
		Integer[] intBuffer = new Integer[array.length];
		for (int i = 0; i < array.length; ++i) {
			intBuffer[i] = Integer.valueOf( array[i] );
		}
		return intBuffer;
	}
}
