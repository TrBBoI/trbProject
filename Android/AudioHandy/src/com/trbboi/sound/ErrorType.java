package com.trbboi.sound;

public enum ErrorType {
	NoError("No error."),
	SoundManagerNotInitialize("Sound Manager is not initialized."),
	KeyAlreadyExist("The key is already exist."),
	LoadSoundFail("Failed to load sound"),
	PlaySoundFail("Failed to play sound")
	;
	
	private String _str;
	
	ErrorType( String str ) {
		_str = str;
	}
	
	public final String ToString() {
		return _str;
	}
}
