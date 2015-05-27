package com.trbboi.audiohandy;

public enum NoteType {
	// Rest ( 0 ),
	Semibreve ( 1 ),
	Minim ( 2 ),
	Crochet ( 4 ),
	Quaver ( 8 ),
	SemiQuaver ( 16 ),
	DemiSemiQuaver ( 32 );

	private short _noteDivision;
	NoteType( int noteDivision ) {
	  this._noteDivision = (short)noteDivision;
	}

	public int GetNoteLength() {
	  return _noteDivision;
	}

	private static final NoteType []_notetype = NoteType.values();
	public static NoteType GetNote(int subdivision) {
	  for(int i = 0; i < _notetype.length; i++) { 
	  	if(_notetype[i].GetNoteLength() == (subdivision))
	  		return _notetype[i];
	  }
	  return _notetype[0];
	}

	public static final NoteType []GetNoteList() {
		int length = _notetype.length;
		NoteType []n = new NoteType[length];
		for ( int i = 0; i < length; ++i ) {
			n[i] = _notetype[i];
		}
		return n;
	}
	
	public static final int []GetNoteListLength() {
		int length = _notetype.length;
		int []n = new int[length];
		for ( int i = 0; i < length; ++i ) {
			n[i] = _notetype[i].GetNoteLength();
		}
		return n;
	}
}
