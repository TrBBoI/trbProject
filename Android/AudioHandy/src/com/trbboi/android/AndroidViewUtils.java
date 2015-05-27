package com.trbboi.android;

import com.trbboi.audiohandy.StringUtils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AndroidViewUtils {

	public static void SetSpinnerItems( Context context, Spinner spinner, int []intArray ) {
		SetSpinnerItems( context, spinner, StringUtils.valueOf( intArray ) );
	}

	public static void SetSpinnerItems( Context context, Spinner spinner, String []strArray ) {
		if( spinner == null ) return;

    ArrayAdapter<String> barAdapter = new ArrayAdapter<String>(context,
        android.R.layout.simple_spinner_item, strArray);
    spinner.setAdapter( barAdapter );
	}

	public static void SetSpinnerImageItems( Context context, Spinner spinner, int []intArray ) {
		if( spinner == null ) return;

		Integer[] intBuffer = AndroidUtils.intToIntegerArray( intArray );
    ImageAdapter barAdapter = new ImageAdapter(context,
        android.R.layout.simple_spinner_item, intBuffer );
    spinner.setAdapter( barAdapter );
	}
}
