package com.trbboi.audiohandy;

import com.trbboi.android.SceneHelper;
import com.trbboi.opengl.OpenGLUtils;

import android.opengl.Matrix;

public class BeepMarker {
	float []_matrix;
	float []_color;
	BeepPitch _pitch;
	boolean _active;

	public BeepMarker() {
		_matrix = new float[16];
		Matrix.setIdentityM( _matrix, 0 );
		_pitch = BeepPitch.None;
		_color = new float[4];
		_color[0] = _color[1] = _color[2] = _color[3] = 1.0f;
	}

	public void setScale( float scale ) {
		SceneHelper.setScale( _matrix, scale, scale );
	}

	public void setScale( float x, float y ) {
		SceneHelper.setScale( _matrix, x, y );
	}

	public void setPosition( float x, float y, float z ) {
		SceneHelper.setPosition( _matrix, x, y );
	}

	public void setColor( float r, float g, float b, float a ) {
		_color[0] = r; _color[1] = g; _color[2] = b; _color[3] = a;
	}

	public void setPitch( BeepPitch pitch ) {
		_pitch = pitch;
		_color = pitchToColor( _pitch, _active );
	}

	public void setActive( boolean b ) {
		_active = b;
		_color = pitchToColor( _pitch, _active );
	}

	public float []getColor() {
		return _color;
	}

	public float []getMatrix() {
		return _matrix;
	}

	public float []getRect() {
		float []minMax = {
				_matrix[12] - _matrix[0] * 0.5f, _matrix[13] - _matrix[5] * 0.5f, 
				_matrix[12] + _matrix[0] * 0.5f, _matrix[13] + _matrix[5] * 0.5f 
		};
		return minMax;
	}

	private static float []pitchToColor( BeepPitch p, boolean b ) {
		if ( b ) {
			if ( p == BeepPitch.None ) return OpenGLUtils.GREY;
			if ( p == BeepPitch.Low )  return OpenGLUtils.LIGHT_YELLOW;
			if ( p == BeepPitch.Mid )  return OpenGLUtils.LIGHT_ORANGE;
			if ( p == BeepPitch.High ) return OpenGLUtils.LIGHT_RED;
		}
		else {
			if ( p == BeepPitch.None ) return OpenGLUtils.DARK_GREY;
			if ( p == BeepPitch.Low )  return OpenGLUtils.DARK_YELLOW;
			if ( p == BeepPitch.Mid )  return OpenGLUtils.DARK_ORANGE;
			if ( p == BeepPitch.High ) return OpenGLUtils.DARK_RED;
		}
		return OpenGLUtils.BLACK;
	}
}
