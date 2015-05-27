package com.trbboi.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Quad {
	
	private static final float [] vertexData = {
			-0.5f,  0.5f, 0.0f,   // top left
      -0.5f, -0.5f, 0.0f,   // bottom left
       0.5f, -0.5f, 0.0f,   // bottom right
       0.5f,  0.5f, 0.0f    // top right
	};

	private static final int vertexCount = 4;
	private static final int _posAttribPointer = 0;
	private static final int _posAttribCount = 3;
	private static final int _vertexStride = _posAttribCount * vertexCount;

  private static final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

  private static int []_vertexBuffer = null;

  public void init() {

  	if ( _vertexBuffer != null ) return;

  	_vertexBuffer = new int[2];

  	GLES20.glGenBuffers( 2, _vertexBuffer, 0 );

  	FloatBuffer fb = OpenGLUtils.CreateFloatBuffer( vertexData );
  	ShortBuffer sb = OpenGLUtils.CreateShortBuffer( drawOrder );

  	GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertexBuffer[0] );
  	GLES20.glBufferData( GLES20.GL_ARRAY_BUFFER, fb.capacity() * OpenGLUtils.BYTES_PER_FLOAT, fb, GLES20.GL_STATIC_DRAW );

    GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _vertexBuffer[1] );
  	GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, sb.capacity() * OpenGLUtils.BYTES_PER_SHORT, sb, GLES20.GL_STATIC_DRAW );
  }

  public void free() {

		GLES20.glDeleteBuffers( 2, _vertexBuffer, 0 );
		_vertexBuffer = null;
  }

  public void enableAttributePointers() {
    // TODO get a more centralize way to enable the vertex attributes.
  	GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, _vertexBuffer[0] );

    GLES20.glEnableVertexAttribArray(_posAttribPointer);
    GLES20.glVertexAttribPointer( _posAttribPointer, _posAttribCount,
        GLES20.GL_FLOAT, false, _vertexStride, 0 );

  	GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, _vertexBuffer[1] );
  }

	public void draw() {
    GLES20.glDrawElements( GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, 0 );
	}

  public void disableAttributePointers() {
    GLES20.glDisableVertexAttribArray(_posAttribPointer);
  }
  
}
