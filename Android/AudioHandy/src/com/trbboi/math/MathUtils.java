package com.trbboi.math;

public class MathUtils {
	public static long gcd( long a, long b ) {
		if ( a < b ) return gcd( b, a );
		
		while ( b > 0 ) {
			long temp = b;
			b = a % b; 
			a = temp;
		}
		return a;
	}

	public static int gcd( int a, int b ) {
		if ( a < b ) return gcd( b, a );
		
		while ( b > 0 ) {
			int temp = b;
			b = a % b; 
			a = temp;
		}
		return a;
	}

	public static long gcd( long []array ) {
		long r = array[0];
    for(int i = 1; i < array.length; i++) r = gcd(r, array[i]);
    return r;
	}

	public static int gcd( int []array ) {
		int r = array[0];
    for(int i = 1; i < array.length; i++) r = gcd(r, array[i]);
    return r;
	}

	public static long lcm( long a, long b ) {
		return a * b / gcd( a, b );
	}

	public static int lcm( int a, int b ) {
		return a * b / gcd( a, b );
	}

	public static long lcm( long []array ) {
		long r = array[0];
    for(int i = 1; i < array.length; i++) r = lcm(r, array[i]);
    return r;
	}

	public static int lcm( int []array ) {
		int r = array[0];
    for(int i = 1; i < array.length; i++) r = lcm(r, array[i]);
    return r;
	}

}
