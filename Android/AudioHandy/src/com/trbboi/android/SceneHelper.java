package com.trbboi.android;

import com.trbboi.math.Vector3f;

public class SceneHelper {
	private static float _ww = 1f, _wh = 1f;
	private static float _dw = 1f, _dh = 1f;
	private static float _wAspectRatio = 1f;
	private static float _dAspectRatio = 1f;
	private static float _widthRatio = 1f;
	private static float _heightRatio = 1f;
	private static boolean _stretch = false;

	public static void setToStretch( boolean b ) {
		_stretch = b;
	}

	public static void setDisplayDimension( int w, int h ) {
		_dw = w; _dh = h;
		_dAspectRatio = _dw / (float)_dh;
		_widthRatio = _dw / _ww;
		_heightRatio = _dh / _wh;
	}

	public static void setWorkingDimension( int w, int h ) {
		_ww = w; _wh = h;
		_wAspectRatio = _ww / (float)_wh;
		_widthRatio = _dw / _ww;
		_heightRatio = _dh / _wh;
	}

	public static void setPosition( float []mat, float x, float y ) {
		mat[12] = _widthRatio * x;
		mat[13] = _heightRatio * y;
	}

	public static void addPosition( float []mat, int x, int y ) {
		mat[12] += _widthRatio * x;
		mat[13] += _heightRatio * y;
	}

	public static void setScale( float[] mat, float x, float y ) {

		float sx, sy;
		if ( _stretch ) {
			sx = _widthRatio * x;
			sy = _heightRatio * y;
		}
		else {
			if ( _wAspectRatio > _dAspectRatio ) {
				sx = _widthRatio * x;
				sy = _widthRatio * y;
			}
			else {
				sx = _heightRatio * x;
				sy = _heightRatio * y;
			}
		}

		Vector3f.normalize( mat, 0 );
		Vector3f.scale( mat, 0, mat, 0, sx );
		Vector3f.normalize( mat, 4 );
		Vector3f.scale( mat, 4, mat, 4, sy );
	}

	public static void mulScale( float[] mat, int x, int y ) {

		float sx, sy;
		if ( _stretch ) {
			sx = _widthRatio * x;
			sy = _heightRatio * y;
		}
		else {
			if ( _wAspectRatio > _dAspectRatio ) {
				sx = _widthRatio * x;
				sy = _widthRatio * y;
			}
			else {
				sx = _heightRatio * x;
				sy = _heightRatio * y;
			}
		}

		Vector3f.scale( mat, 0, mat, 0, sx );
		Vector3f.scale( mat, 4, mat, 4, sy );
	}
}
