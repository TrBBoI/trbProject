package com.trbboi.sound;

import java.util.Map;
import java.util.TreeMap;

import com.trbboi.audiohandy.Logger;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	private static Map<String, Integer> _audioMap = new TreeMap<String, Integer>();
	private static SoundPool _soundpool = null;
	private static AudioManager _audioManager;
	
	private static ErrorType _error;

	public static void init( AudioManager mgr ) {
		Logger.i( "init sound manager" );
		_audioManager = mgr;
		if ( _soundpool == null ) {
			_soundpool = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
		}
	}

	public static boolean addSound( String audioName, AssetFileDescriptor audioFd ) {
		int id = _soundpool.load( audioFd.getFileDescriptor(), audioFd.getStartOffset(), audioFd.getLength(), 0 );
		if ( _checkValidId( id ) == false ) return false;

		if( _checkKeyExist( audioName ) == false ) return false;
		_audioMap.put( audioName, id );

		return true;
	}

	public static void playSound( String audioName ) {
		if ( _checkInit() == false ) return;
		if ( _audioMap.containsKey( audioName ) ) {
			int id = _soundpool.play( _audioMap.get( audioName ), 1, 1, 0, 0, 1 );
			_checkPlaySound( id );
		}
	}

	public static void free() {
		Logger.i( "free sound manager" );
		if ( _soundpool != null ) {
			_soundpool.release();
			_soundpool = null;
		}
		_audioMap.clear();
	}

	public static void setVolumeLevel( int i ) {
		if ( _checkInit() == false ) return;
		if ( i < getMaxVolumeLevel() )
			_audioManager.setStreamVolume( AudioManager.STREAM_MUSIC, i, 0 );
	}

	public static int getVolumeLevel() {
		if ( _checkInit() == false ) return -1;
		return _audioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
	}

	public static int getMaxVolumeLevel() {
		if ( _checkInit() == false ) return -1;
		return _audioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
	}

	public static boolean hasError() {
		return _error == ErrorType.NoError;
	}

	public static String getErrorMessage() {
		return _error.ToString();
	}

	private static boolean _checkInit() {
		if ( _soundpool == null || _audioManager == null ) {
			_error = ErrorType.SoundManagerNotInitialize;
			_logError();
			return false;
		}
		return true;
		
	}

	private static boolean _checkValidId( int id ) {
		if (id > 0) return true;
		_error = ErrorType.LoadSoundFail;
		_logError();
		return false;
	}
	
	private static boolean _checkPlaySound( int id ) {
		if (id > 0 ) return true;
		_error = ErrorType.PlaySoundFail;
		_logError();
		return false;
	}

	private static boolean _checkKeyExist( String key ) {
		if ( !_audioMap.containsKey( key ) ) return true;

		_error = ErrorType.KeyAlreadyExist;
		_logError();
		return false;
	}

	private static void _logError() {
		Logger.d( _error.ToString() );
	}
}
