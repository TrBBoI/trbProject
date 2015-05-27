package com.trbboi.audiohandy;

public class StringUtils {
	public static String[] valueOf( int []intArray ) {
		int length = intArray.length;
		String []strArray = new String[length];
		for ( int i = 0; i < length; ++i )
			strArray[i]= String.valueOf( intArray[i] );
		return strArray;
	}

	public static String removeExtension( String file ) {
		int i = file.lastIndexOf( "." );
		if (i == -1) return file;

		return file.substring( 0, i );
	}

	public static String removeFilePath( String filepath ) {
		int i = filepath.lastIndexOf( "/" );
		if (i == -1) return filepath;

		return filepath.substring( i, filepath.length() );
	}

	public static String getFileString( String filepath ) {
			int i = filepath.lastIndexOf( "/" );
			int j = filepath.lastIndexOf( "." );
			if (i == -1 && j == -1) return filepath;
			
			if (i == -1) i = 0;
			if (j == -1) j = 10000000;
	
			return filepath.substring( i, j );
	}
}
