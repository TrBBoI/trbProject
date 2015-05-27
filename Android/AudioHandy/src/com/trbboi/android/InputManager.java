package com.trbboi.android;

import java.util.ArrayList;
import java.util.List;

public class InputManager {
	private static List<TouchDownListener> listener = new ArrayList<TouchDownListener>();

	public static void setTouchDownListener( TouchDownListener l ) {
		listener.add( l );
	}

	public static boolean OnTouch( float x, float y ) {
		int size = listener.size();
		for ( int i = 0; i < size; ++i ) {
			listener.get( i ).touchDown( x, y );
		}
		return false;
	}
}
