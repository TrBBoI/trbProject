package com.trbboi.audiohandy;

import com.trbboi.math.MathUtils;

public class MetronomeBar {
	private NoteType _beatNote;
	private int _beatsPerBar;
	private BeepPitch []_downBeat;
	private Subdivision _subdivision;

	public MetronomeBar( int beatsPerBar, NoteType beats ) {
		_beatNote = beats;
		_beatsPerBar = beatsPerBar;
		_subdivision = Subdivision.Crochet;

		_SetDefaultDownBeat();
	}

	public MetronomeBar( MetronomeBar bar ) {
		_beatNote = bar._beatNote;
		_beatsPerBar = bar._beatsPerBar;
		_subdivision = bar._subdivision;

		_downBeat = new BeepPitch[bar._beatsPerBar];
		for (int i = 0; i < _downBeat.length; ++i) {
			_downBeat[i] = bar._downBeat[i];
		}
	}

	public void SetDownBeat( int beat, BeepPitch pitch ) {
		if (beat >= _beatsPerBar) return;
		_downBeat[beat] = pitch;
	}

	public void UnsetDownBeat(int beat) {
		if (beat >= _beatsPerBar) return;
		_downBeat[beat] = BeepPitch.None;
	}

	public void ToggleDownBeat(int beat) {
		_downBeat[beat] = _downBeat[beat].NextBeep();
	}

	public BeepPitch GetDownBeat(int beat) {
		if (beat >= _beatsPerBar) return BeepPitch.None;
		return _downBeat[beat];
	}

	public boolean IsDownBeat(int beat) {
		if (beat >= _beatsPerBar) return false;
		return _downBeat[ beat ] != BeepPitch.None;
	}

	public void ClearBeat() {
		for (int i = 0; i < _downBeat.length; ++i) {
			_downBeat[i] = BeepPitch.None;
		}
	}

	public int GetBarLength() {
		return _beatNote.GetNoteLength() * _beatsPerBar;
	}

	public void SetBeat( NoteType beatNote ) {
		_beatNote = beatNote;
		_SetDefaultDownBeat();
	}

	public NoteType GetBeat() {
		return _beatNote;
	}

	public void SetBeatsPerBar( int bpb ) {
		_beatsPerBar = bpb;
		_SetDefaultDownBeat();
	}

	public int GetBeatsPerBar() {
		return _beatsPerBar;
	}

	public void SetSubdivision( Subdivision division ) {
		_subdivision = division;
	}

	public Subdivision GetSubdivision() {
		return _subdivision;
	}

	private void _SetDefaultDownBeat() {
		if ( _downBeat != null && _beatsPerBar == _downBeat.length ) return;

		_downBeat = new BeepPitch[ _beatsPerBar ];

		//if (_beatNote.GetNoteLength() <= NoteType.Crochet.GetNoteLength() ) {
			for (int i = 0; i < _downBeat.length; ++i ) {
				_downBeat[i] = BeepPitch.Mid;
			}
		/*}
		else if ( MathUtils.gcd( _beatsPerBar, 3 ) != 1 ) {

			for (int i = 0; i < _downBeat.length; ++i ) {
				if (i % 3 == 0) {
					_downBeat[i] = BeepPitch.Mid;
				}
				else {
					_downBeat[i] = BeepPitch.None;
				}
			}
		} else {
			int interval = _beatNote.GetNoteLength() / 4;
			for (int i = 0; i < _downBeat.length; ++i ) {
				if (i % interval == 0) {
					_downBeat[i] = BeepPitch.Mid;
				}
				else {
					_downBeat[i] = BeepPitch.None;
				}
			}
		}*/
		_downBeat[0] = BeepPitch.High;
	}
}
