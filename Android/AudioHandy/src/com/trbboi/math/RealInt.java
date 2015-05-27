package com.trbboi.math;

public class RealInt implements Comparable<RealInt> {
	private long _num;
	private int _numerator;
	private int _denominator;

	public RealInt() {
		_num = 0;
		_numerator = 0;
		_denominator = 1;
	}

	public RealInt( RealInt copy ) {
		_num = copy._num;
		_numerator = copy._numerator;
		_denominator = copy._denominator;
	}

	public RealInt( long num ) {
		_num = num;
		_numerator = 0;
		_denominator = 1;
	}

	public RealInt( long numerator, int denominator ) {
		_num = numerator / denominator;
		_numerator = (int)(numerator % denominator);
		_denominator = denominator;
	}

	public RealInt( long num, int numerator , int denominator ) {
		_num = num + numerator / denominator;
		_numerator = numerator % denominator;
		_denominator = denominator;
	}

	public RealInt assign( RealInt copy ) {
		_num = copy._num;
		_numerator = copy._numerator;
		_denominator = copy._denominator;
		return this;
	}

	public RealInt assign( long num ) {
		_num = num;
		_numerator = 0;
		_denominator = 1;
		return this;
	}

	public RealInt assign( long numerator, int denominator ) {
		_num = numerator / denominator;
		_numerator = (int)(numerator % denominator);
		_denominator = denominator;
		return this;
	}

	public RealInt assign( long num, int numerator , int denominator ) {
		_num = num + numerator / denominator;
		_numerator = numerator % denominator;
		_denominator = denominator;
		return this;
	}

	public RealInt add( int num ) {
		_num += num;
		return this;
	}

	public RealInt add( RealInt rhs ) {
		if ( rhs == this ) {
			_num *= 2;
			_numerator *= 2;
		}
		else {
			_num += rhs._num;
	
			if ( _denominator == rhs._denominator ) {
				_numerator += rhs._numerator;
			}
			else {
				int newDenominator = MathUtils.gcd( _denominator, _numerator );
				newDenominator = MathUtils.gcd( newDenominator, rhs._numerator );
				newDenominator = MathUtils.gcd( newDenominator, rhs._denominator );
	
				_numerator = _numerator / newDenominator;
				int rhsNumerator = rhs._numerator / newDenominator;
				_numerator += rhsNumerator;
				_denominator = newDenominator;
			}

			UpdateNumerator();
		}

		return this;
	}

	public RealInt mul( int m ) {
		_num *= m;
		_numerator *= m;
		UpdateNumerator();
		return this;
	}

	public long num() {
		return _num;
	}

	@Override
  public int compareTo( RealInt rhs ) {
		if ( _num == rhs._num ) {

			int newDenominator = MathUtils.gcd( _denominator, _numerator );
			newDenominator = MathUtils.gcd( newDenominator, rhs._numerator );
			newDenominator = MathUtils.gcd( newDenominator, rhs._denominator );

			_numerator = _numerator / newDenominator;
			int rhsNumerator = rhs._numerator / newDenominator;
			return _numerator - rhsNumerator;
		}
		else if ( _num < rhs._num ) {
			return -1;
		} 
		else {
			return 1;
		}
  }
	
	private void UpdateNumerator() {
		if ( _numerator > _denominator ) {
			_numerator -= _denominator;
			_num += 1;
		}
	}

	public static RealInt add( RealInt lhs, RealInt rhs ) {
		return (new RealInt(lhs)).add( rhs );
	}

	public static RealInt add( int lhs, RealInt rhs ) {
		return (new RealInt(rhs)).add( lhs );
	}

	public static RealInt add( RealInt lhs, int rhs ) {
		return (new RealInt(lhs)).add( rhs );
	}

	public static RealInt mul( int lhs, RealInt rhs ) {
		return (new RealInt(rhs)).mul( lhs );
	}

	public static RealInt mul( RealInt lhs, int rhs ) {
		return (new RealInt(lhs)).mul( rhs );
	}
	
}
