package com.trbboi.audiohandy;

public enum Subdivision {
	DottedSemibreve ( 6, 1 ),
	Semibreve       ( 4, 1 ),
	DottedMinim     ( 3, 1 ),
	Minim           ( 2, 1 ),
	Crochet         ( 1, 1 ),
	Crochet5        ( 4, 5 ),
	Crochet7        ( 4, 7 ),
	DottedCrochet   ( 2, 3 ),
	Quaver          ( 1, 2 ),
	Quaver5         ( 2, 5 ),
	Quaver7         ( 2, 7 ),
	SemiQuaver      ( 1, 4 ),
	DemiSemiQuaver  ( 1, 8 ); 

	int _numerator, _denominator;
	Subdivision( int a, int b ) {
		_numerator = a;
		_denominator = b;
	}

	public int getNumerator() { return _numerator; }
	public int getDenominator() { return _denominator; }

	private static Subdivision[] _values = Subdivision.values();
	public static Subdivision get( int i ) {
		return _values[i];
	}
}
