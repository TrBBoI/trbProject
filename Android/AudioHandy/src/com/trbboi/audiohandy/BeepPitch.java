package com.trbboi.audiohandy;

public enum BeepPitch {
	None,
	Low,
	Mid,
	High;

	private static BeepPitch [] _value = BeepPitch.values();
	public BeepPitch NextBeep() {
		int idx = ( this.ordinal() + 1 ) % _value.length;
		return _value[idx];
	}
}
