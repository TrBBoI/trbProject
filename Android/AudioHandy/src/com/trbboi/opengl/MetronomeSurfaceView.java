package com.trbboi.opengl;

import com.trbboi.android.InputManager;
import com.trbboi.audiohandy.Logger;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MetronomeSurfaceView extends GLSurfaceView {
	private SurfaceHolder _holder;

	public MetronomeSurfaceView(Context context, MetronomeRenderer renderer) {
	  super( context );

	  setEGLContextClientVersion(2); // must be set before setRenderer
	  setRenderer( renderer );
	  // setRenderMode( GLSurfaceView.RENDERMODE_WHEN_DIRTY );
	  setRenderMode( GLSurfaceView.RENDERMODE_CONTINUOUSLY );

	  _holder = getHolder();
	  _holder.addCallback(this);
	  _holder.setFormat( PixelFormat.RGB_565 );
  }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	  // TODO Auto-generated method stub
	  super.surfaceChanged( holder, format, w, h );
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		float x = e.getX() - getWidth() * 0.5f;
		float y = getHeight() * 0.5f - e.getY();

		if ( InputManager.OnTouch( x, y ) ) {
			return true;
		}
	  return super.onTouchEvent( e );
	}

}
