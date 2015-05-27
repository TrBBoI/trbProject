package com.trbboi.opengl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.trbboi.android.SceneHelper;
import com.trbboi.audiohandy.BeepMarker;
import com.trbboi.audiohandy.BeepPitch;
import com.trbboi.audiohandy.Logger;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class MetronomeRenderer implements Renderer {
	
	public interface Requester {
		public List<BeepMarker> request();
	}

	private Requester _requester = null;

	private Shader _basicShader = null;
	private Quad _quad = null;

	private float[] _proj;

	private List<BeepMarker> _markers = new ArrayList<BeepMarker>();

	@Override
  public void onDrawFrame(GL10 unused) {
		float []_wvpMat = new float[16];
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	  

		if ( _requester != null )
			_markers = _requester.request();

		// Logger.i( "onDrawFrame" );
		_basicShader.bindProgram();

		_quad.enableAttributePointers();

		// TODO add mutex
		int size = _markers.size();
		for ( int i = 0; i < size; ++i ) {
			_basicShader.setColor( _markers.get( i ).getColor() );

			Matrix.multiplyMM( _wvpMat, 0, _proj, 0, _markers.get( i ).getMatrix(), 0 );
			_basicShader.setTransform( _wvpMat );

			_quad.draw();

		}
		_quad.disableAttributePointers();

  }

	@Override
  public void onSurfaceChanged(GL10 unused, int w, int h) {
		GLES20.glViewport(0, 0, w, h);

		Logger.i( "onSurfaceChanged" );
		// TODO create buttons for setting.

		Matrix.setIdentityM( _proj, 0 );
		Matrix.orthoM( _proj, 0, -w/2, w/2, -h/2, h/2, 0, 100 );

		SceneHelper.setToStretch( false );
		SceneHelper.setWorkingDimension( 160, 90 );
		SceneHelper.setDisplayDimension( w, h );

		float []cam = new float[16];
		Matrix.setLookAtM( cam, 0, 0, 0, 10, 0, 0, 0, 0, 1, 0 );
		Matrix.multiplyMM( _proj, 0, _proj, 0, cam, 0 );
  }

	@Override
  public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		Logger.i( "onSurfaceCreated" );
		GLES20.glClearColor( 0f, 0f, 0f, 1f );

		_basicShader.init();
		_quad.init();

		Matrix.setIdentityM( _proj, 0 );
	}

	public void onStart() {
		if( _basicShader == null ) {
			_basicShader = new Shader();
		}

		if( _quad == null ) {
			_quad = new Quad();
		}

		if (_proj == null) {
			_proj = new float[16];
		}
	}

	public void onStop() {
		if( _basicShader != null ) {
			_basicShader.free();
			_basicShader = null;
		}

		if( _quad != null ) {
			_quad.free();
			_quad = null;
		}

		if (_proj != null) {
			_proj = null;
		}
	}

	public BeepMarker AddMarker( BeepPitch pitch, float x, float y, float z, float w, float h ) {

		BeepMarker newMarker = new BeepMarker();
		newMarker.setScale( w, h );
		newMarker.setPosition( x, y, z );

		newMarker.setPitch( pitch );
		newMarker.setActive( false );

		_markers.add( newMarker );
		return newMarker;

	}

	public void Clear() {
		_markers.clear();
	}

	public void setRequester( Requester requester ) {
		_requester = requester;
	}
}
