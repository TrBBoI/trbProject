package com.trbboi.opengl;

import com.trbboi.audiohandy.Logger;

import android.opengl.GLES20;

public class Shader {
	private int []_shaderId = null;
	
	private int _color = -1;
	private int _wvpMat = -1;

	public void init() {
		if (_shaderId == null) {
			_shaderId = new int[3];

			int []params = new int[1];

			_shaderId[0] = GLES20.glCreateProgram();
			_shaderId[1] = GLES20.glCreateShader( GLES20.GL_VERTEX_SHADER );
			_shaderId[2] = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );

			GLES20.glShaderSource( _shaderId[1], vertexShaderCode );
			GLES20.glCompileShader( _shaderId[1] );

			GLES20.glGetShaderiv( _shaderId[1], GLES20.GL_COMPILE_STATUS, params, 0 );
			if ( params[0] != GLES20.GL_TRUE ) {
				Logger.i( GLES20.glGetShaderInfoLog( _shaderId[1] ) );
				return;
			}

			GLES20.glShaderSource( _shaderId[2], fragmentShaderCode );
			GLES20.glCompileShader( _shaderId[2] );

			GLES20.glGetShaderiv( _shaderId[2], GLES20.GL_COMPILE_STATUS, params, 0 );
			if ( params[0] != GLES20.GL_TRUE ) {
				Logger.i( GLES20.glGetShaderInfoLog( _shaderId[2] ) );
				return;
			}

			GLES20.glAttachShader( _shaderId[0], _shaderId[1] );
			GLES20.glAttachShader( _shaderId[0], _shaderId[2] );
			GLES20.glLinkProgram( _shaderId[0] );

			GLES20.glGetProgramiv( _shaderId[0], GLES20.GL_LINK_STATUS, params, 0 );
			if( params[0] != GLES20.GL_TRUE ) {
				Logger.i( GLES20.glGetProgramInfoLog( _shaderId[0] ) );
			}

			GLES20.glBindAttribLocation( _shaderId[0], 0, "pos" );

			_color = GLES20.glGetUniformLocation( _shaderId[0], "color");
			if (_color == -1 ) {
				Logger.i( "_color has failed" );
			}
			_wvpMat = GLES20.glGetUniformLocation( _shaderId[0], "wvpMat");
			if (_wvpMat == -1 ) {
				Logger.i( "_wvpMat has failed" );
			}
		}
	}

	public void free() {
		if (_shaderId != null ) {
				GLES20.glDeleteShader( _shaderId[2] );
				GLES20.glDeleteShader( _shaderId[1] );
				GLES20.glDeleteProgram( _shaderId[0] );

				_shaderId = null;
				_color = -1;
				_wvpMat = -1;
		}
	}
	
	public void setTransform( float[] mat ) {
    GLES20.glUniformMatrix4fv( _wvpMat, 1, false, mat, 0 );
	}

	public void setColor( float[] c) {
    GLES20.glUniform4fv( _color, 1, c, 0);
	}

	public void bindProgram() {
		GLES20.glUseProgram( _shaderId[0] );
	}

	private final String vertexShaderCode =
			"uniform mat4 wvpMat;" +
			"attribute vec4 pos;" +
			"" +
			"void main() {" +
			"  gl_Position = wvpMat * pos;" +
			"}";

	private final String fragmentShaderCode =
	    "precision mediump float;" +
	    "uniform vec4 color;" +
	    "void main() {" +
	    "  gl_FragColor = color;" +
	    "}";
}
