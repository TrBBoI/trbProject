package com.trbboi.audiohandy;

import android.util.Log;

public class Logger {
	public static void ReportNullObject( Object obj, String name ) {
		if (obj == null) Log.e( name, name + " is null." );
	}

	public static void e(String error) {
		Log.e( "Metronome", error );
	}
	
	public static void i(String info) {
		Log.i( "Metronome", info);
	}

	public static void v(String verbose) {
		Log.v( "Metronome", verbose);
	}

	public static void w(String warning) {
		Log.w( "Metronome", warning);
	}

	public static void d(String debug) {
		Log.d( "Metronome", debug);
	}

	public static void wtf(String failure) {
		Log.wtf( "Metronome", failure);
	}
}
