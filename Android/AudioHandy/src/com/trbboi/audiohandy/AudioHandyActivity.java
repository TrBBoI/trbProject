package com.trbboi.audiohandy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import com.trbboi.android.AndroidViewUtils;
import com.trbboi.android.InputManager;
import com.trbboi.android.TouchDownListener;
import com.trbboi.audiohandy.R;
import com.trbboi.math.RealInt;
import com.trbboi.opengl.MetronomeRenderer;
import com.trbboi.opengl.MetronomeRenderer.Requester;
import com.trbboi.opengl.MetronomeSurfaceView;
import com.trbboi.sound.SoundManager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AudioHandyActivity extends Activity {

	// Activity variables
	private AssetManager _assetManager;
	private final Semaphore semaphore = new Semaphore(1, false);

	// private Button _btnStart;
	private SeekBar _seekBarTempo;
	private SeekBar _seekBarVolume;
	private TextView _textTempoValue;
	private Spinner _spinnerBeat;
	private Spinner _spinnerBar;
	private Spinner _spinnerSubdivision;
	private MetronomeSurfaceView _surfaceViewMetronomeDisplay = null;
	private MetronomeRenderer _renderer = null;
	private ToggleButton []_toggleButtons = null;

	private ArrayList<Long> _tapRecord;

	// Metronome Variables
	private int _beatsPerMin;
	private boolean _hasStarted;
	private boolean _isPaused;

	private MetronomeBar _currMetronomeBar;
	private List<MetronomeBar> _metronomeBarList;
	private List<BeepMarker> _markers = null;

	private int _nextBeat = 0;

	private Thread beeper;

	public static final int MIN_BEATS_PER_MIN = 16;
	public static final int MAX_BEATS_PER_MIN = 320;
	public static final int BEATS_PER_MIN_RANGE = MAX_BEATS_PER_MIN - MIN_BEATS_PER_MIN;
	public static final long ONE_SECOND_IN_NANO = 1000000000L;
	public static final long ONE_MINUTE_IN_NANO = 60 * ONE_SECOND_IN_NANO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.i( "onCreate" );
    super.onCreate(savedInstanceState);

    setContentView( R.layout.layout_audio_handy );
    setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

    RelativeLayout layout = (RelativeLayout) findViewById( R.id.mainLayout );

    // adding the gl view content in.
    _renderer = new MetronomeRenderer();
    _surfaceViewMetronomeDisplay = new MetronomeSurfaceView( this, _renderer );
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
    params.addRule( RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE );
    params.addRule( RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE );
    params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE );
    params.addRule( RelativeLayout.ABOVE, R.id.btnAddBar );

    layout.addView( _surfaceViewMetronomeDisplay, params );

    _seekBarTempo = (SeekBar)findViewById( R.id.seekBarTempo );
    Logger.ReportNullObject( _seekBarTempo, "_seekBarTempo" );

    _seekBarVolume = (SeekBar)findViewById( R.id.seekBarVolume );
    Logger.ReportNullObject( _seekBarVolume, "_seekBarVolume" );

    _textTempoValue = (TextView)findViewById( R.id.textTempoValue );
    Logger.ReportNullObject( _textTempoValue, "_textTempoValue" );

    _spinnerBeat = (Spinner)findViewById( R.id.spinnerBeat );
    Logger.ReportNullObject( _spinnerBeat, "_spinnerBeat" );

    _spinnerBar = (Spinner)findViewById( R.id.spinnerBar );
    Logger.ReportNullObject( _spinnerBar, "_spinnerBar" );

    _spinnerSubdivision = (Spinner)findViewById( R.id.spinnerSubdivision );
    Logger.ReportNullObject( _spinnerSubdivision, "_spinnerSubdivision" );
    
    _toggleButtons = new ToggleButton[4];
    for ( int i = 0; i < _toggleButtons.length; ++i ) {
    	_toggleButtons[i] = (ToggleButton)findViewById( R.id.tbtnSave1 + i );
      Logger.ReportNullObject( _toggleButtons[i], "_toggleButtons["+i+"]" );
      _toggleButtons[i].setVisibility( View.INVISIBLE );
    }

    boolean b = _AppInit();
    if (b == false) {
    	Logger.e( "App Init failed.");
    }

    _seekBarTempo.setOnSeekBarChangeListener(
	    	new SeekBar.OnSeekBarChangeListener() {
	    		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
	
	    			progress = progress + MIN_BEATS_PER_MIN;
	    			SetBeatPerMin( progress );
	    			_textTempoValue.setText( String.valueOf( progress ) );
	    		}
	
	    		public void onStartTrackingTouch(SeekBar seekBar) {}
	    		public void onStopTrackingTouch(SeekBar seekBar) {}
	    	}
    );

    _seekBarTempo.setOnSeekBarChangeListener(
	    	new SeekBar.OnSeekBarChangeListener() {

	    		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

	    			progress = progress + MIN_BEATS_PER_MIN;
	    			SetBeatPerMin( progress );
	    			_textTempoValue.setText( String.valueOf( progress ) );
	    		}

	    		public void onStartTrackingTouch(SeekBar seekBar) {}
	    		public void onStopTrackingTouch(SeekBar seekBar) {}
	    	}
    );

    _seekBarVolume.setOnSeekBarChangeListener(
	    	new SeekBar.OnSeekBarChangeListener() {

	    		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

	    			SoundManager.setVolumeLevel( progress );
	    		}

	    		public void onStartTrackingTouch(SeekBar seekBar) {}
	    		public void onStopTrackingTouch(SeekBar seekBar) {}
	    	}
    );

    _spinnerBar.setOnItemSelectedListener(
    		new AdapterView.OnItemSelectedListener() {

					@Override
          public void onItemSelected(AdapterView<?> parent, View view,
              int pos, long id) {
						int beatLength = 1 << pos;
						SetBeatLength( beatLength );
						int barLength = GetBarLength();

						if (barLength >= beatLength * 2 ) {
							barLength = beatLength;
						}
						_RefreshSpinnerBeat( barLength, beatLength );
          }

					@Override
          public void onNothingSelected(AdapterView<?> parent) {}
    		}
    );

    _spinnerBeat.setOnItemSelectedListener(
    		new AdapterView.OnItemSelectedListener() {

					@Override
          public void onItemSelected(AdapterView<?> parent, View view,
              int pos, long id) {
						SetBarLength( pos + 1 );
						_RefreshMarker();
          }

					@Override
          public void onNothingSelected(AdapterView<?> parent) {}
    		}
    );

    _spinnerSubdivision.setOnItemSelectedListener(
    		new AdapterView.OnItemSelectedListener() {

					@Override
          public void onItemSelected(AdapterView<?> parent, View view,
              int pos, long id) {
							SetSubdivision( pos );
          }

					@Override
          public void onNothingSelected(AdapterView<?> parent) {}
    		}
    );

    for ( int i = 0; i < _toggleButtons.length; ++i ) {
    	_toggleButtons[i].setOnClickListener( new ToggleButton.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    				  // TODO Auto-generated method stub
    				  int i = v.getId() - R.id.tbtnSave1;
    				  SelectBar( i );

    				  for( int j = 0; j < _toggleButtons.length; ++j ) {
    				  	_toggleButtons[j].setChecked( i == j );
    				  }

    				  _RefreshMarker();
    				}
    			} 
    	);
    }
	}

	@Override
	public void onStart() {
		Logger.i( "onStart" );
		super.onStart();
		_renderer.onStart();
    setVolumeControlStream( AudioManager.STREAM_MUSIC );
	}

	@Override
	public void onResume() {
		Logger.i( "onResume" );
		super.onResume();
		_surfaceViewMetronomeDisplay.onResume();
		Resume();
	}

	@Override
	public void onPause() {
		Logger.i( "onPause" );
		Pause();
		_surfaceViewMetronomeDisplay.onPause();
		super.onPause();
	}

	@Override
	public void onStop() {
		Logger.i( "onStop" );
		_renderer.onStop();
		super.onStop();
    setVolumeControlStream( AudioManager.STREAM_RING );
	}

	@Override
	public void onDestroy() {

		Logger.i( "onDestroy" );
		StopBeating();

		SoundManager.free();

		super.onStop();
	}

	private boolean _AppInit() {
		Logger.i( "_AppInit" );

		_MetronomeInit();
    _assetManager = getAssets();
		_tapRecord = new ArrayList<Long>();

		SoundManager.init( (AudioManager) getSystemService(Context.AUDIO_SERVICE) );

		try {
			String []audioList = _assetManager.list("audio");
			for ( int i = 0; i < audioList.length; ++i ) {
				AssetFileDescriptor fd = _assetManager.openFd( "audio/" + audioList[i] );
				String audioName = StringUtils.getFileString( audioList[i] );
				SoundManager.addSound( audioName, fd );
			}
		}
		catch (IOException e){
			Logger.e( e.toString() );
		}

		_RefreshTempoBar();
		_RefreshVolumeBar();

    int base = GetBeatLength();
		_RefreshSpinnerBar( base );
    _RefreshSpinnerBeat( base, base );
    _RefreshSpinnerSubdivision( 4 );

    _RefreshSave();

		return true;
	}

	public void OnClickPlay( View view ) {
		int bpm = GetBeatPerMin();

		if (bpm < 120 )
			SoundManager.playSound( "Low" );
		else if (bpm > 200)
			SoundManager.playSound( "High" );
		else 
			SoundManager.playSound( "Mid" );
	}

	public void OnClickStart( View view ) {
		ToggleBeating();
	}

	public void OnClickTapTempo( View view ) {

		final long THRESHOLD = 10000000000L;
		final int SIZE_THRESHOLD = 20;
		long nanoTime = System.nanoTime();

		// Remove old tap timing
		int size = _tapRecord.size();

		if (size > 0 ) {
			while ( _tapRecord.size() != 0 && nanoTime - THRESHOLD > _tapRecord.get( 0 ) ) {
				_tapRecord.remove( 0 );
			}

			size = _tapRecord.size();
			if (size > SIZE_THRESHOLD ) {
				_tapRecord.remove( 0 );
			}
		}

		size = _tapRecord.size();

		// Add new tap timing
		_tapRecord.add( nanoTime );

		size = _tapRecord.size();
		// Estimate tempo 
		long totalTime = 0;
		for (int i = 1; i < size; ++i )
		{
			long diff = _tapRecord.get( i ) - _tapRecord.get( i - 1 );
			totalTime += diff;
		}

		if (size < 2) return;

		double bpm = (double)(size-1) / (double)totalTime * 60000000000d;

		SetBeatPerMin( Math.round( (float)bpm ) );
		_RefreshTempoBar();
	}

	public void OnClickPlusTempo( View view ) {
		int bpm = GetBeatPerMin();

		SetBeatPerMin( ++bpm );
		_RefreshTempoBar();
	}

	public void OnClickMinusTempo( View view ) {
		int bpm = GetBeatPerMin();

		SetBeatPerMin( --bpm );
		_RefreshTempoBar();
	}

	public void OnClickAddBar( View view ) {
		int j = SaveBar();
		for (int i = 0; i < _toggleButtons.length; ++i) {
			_toggleButtons[i].setChecked( i == j );
		}
		_RefreshSave();
	}

	public void OnClickRemoveBar( View view ) {
		RemoveBar();
		for (int i = 0; i < _toggleButtons.length; ++i) {
			_toggleButtons[i].setChecked( i == 0 );
		}
		_RefreshMarker();
		_RefreshSave();
	}

	private void _RefreshTempoBar() {
		_seekBarTempo.setMax( BEATS_PER_MIN_RANGE );
		int progress = Math.round( GetBeatPerMin() - MIN_BEATS_PER_MIN );

		_seekBarTempo.setProgress( progress );
		_textTempoValue.setText( String.valueOf( GetBeatPerMin() ) );

	}

	private void _RefreshVolumeBar() {
		_seekBarVolume.setMax( SoundManager.getMaxVolumeLevel() );
		_seekBarVolume.setProgress( SoundManager.getVolumeLevel() );
	}

	private void _RefreshSpinnerBar( int select ) {
    int []notesLength = NoteType.GetNoteListLength();
    AndroidViewUtils.SetSpinnerItems( this, _spinnerBar, notesLength );

    int idx = 0;
    while ( ( select >>= 1 ) > 0 ) {
    	++idx;
    }
    _spinnerBar.setSelection( idx );
	}

	private void _RefreshSpinnerBeat( int select, int base ) {
    int []countOptions = new int[base * 2];
    for ( int i = 0; i < countOptions.length; ++i ) {
    	countOptions[i] = i+1;
    }
    AndroidViewUtils.SetSpinnerItems( this, _spinnerBeat, countOptions );
    _spinnerBeat.setSelection( select - 1 );
	}

	private void _RefreshSpinnerSubdivision( int select ) {
		int []subdivision = { 
				R.drawable.dotted_semibreve,  // 6 / 1
				R.drawable.semibreve,         // 4 / 1
				R.drawable.dotted_minim,      // 3 / 1
				R.drawable.minim,             // 2 / 1
				R.drawable.crochet,           // 1 / 1
				R.drawable.crochet5,          // 4 / 5
				R.drawable.crochet7,          // 4 / 7
				R.drawable.dotted_crochet,    // 2 / 3
				R.drawable.quaver,            // 2 / 4 
				R.drawable.quaver5,           // 2 / 5
				R.drawable.quaver7,           // 2 / 7
				R.drawable.semi_quaver,       // 1 / 4
				// R.drawable.demi_semi_quaver   // 1 / 8 
		};

		AndroidViewUtils.SetSpinnerImageItems( this, _spinnerSubdivision, subdivision );
		_spinnerSubdivision.setSelection( select );
		_spinnerSubdivision.setDropDownWidth( LayoutParams.MATCH_PARENT );
	}

	private void _RefreshSave() {
		int count = GetSavedBarCount();
		for ( int i = 0; i < 4; ++i ) {
			if ( i < count ) { 
				_toggleButtons[i].setVisibility( View.VISIBLE );
			}
			else
				_toggleButtons[i].setVisibility( View.INVISIBLE );
		}
	}

	// Metronome functions
	private void _MetronomeInit() {

		_beatsPerMin = 120;
		_hasStarted = false;
		_isPaused = false;
		_currMetronomeBar = new MetronomeBar( 4, NoteType.Crochet );
		_metronomeBarList = new ArrayList<MetronomeBar>();
		_metronomeBarList.add( _currMetronomeBar );

		_markers = new Vector<BeepMarker>();
		_renderer.setRequester( new Requester() {
					@Override
					public List<BeepMarker> request() {
						// TODO Auto-generated method stub
						// return new Vector<BeepMarker>( _markers );

					while ( !semaphore.tryAcquire() ) {};
						List<BeepMarker> l = new Vector<BeepMarker>( _markers );
						semaphore.release();
						return l;
					}
				} 
		);

		InputManager.setTouchDownListener( new TouchDownListener() {

			@Override
      public boolean touchDown(float x, float y) {
	      return OnTouch( x, y );
      }
		});
	}

	public void StartBeating() {
		_hasStarted = true;
		Logger.i("start");
		beeper = new Thread() {

			@Override 
			public void run() {
				RealInt lastBeat = new RealInt( System.nanoTime() );
				RealInt currTime = new RealInt();
				//_RefreshMarker();
				//_RequestRender();

				while ( _hasStarted ) {
					long time = System.nanoTime();
					currTime.assign( time );

					if (currTime.compareTo( lastBeat ) >= 0 ) {

						RealInt increment = _GetTimeTileNextBeat();
						lastBeat.add( increment );

						int totalBeats = _currMetronomeBar.GetBeatsPerBar();
						if ( _nextBeat >= totalBeats ) {
							_nextBeat = 0;

							int idx = _metronomeBarList.indexOf( _currMetronomeBar );
							idx = (idx + 1) % _metronomeBarList.size();
							_currMetronomeBar = _metronomeBarList.get( idx );

							_RefreshMarker();
						}

						while ( !semaphore.tryAcquire() ) {};

						BeepPitch pitch = _currMetronomeBar.GetDownBeat( _nextBeat );
						if ( pitch != BeepPitch.None && !_isPaused ) {
							SoundManager.playSound( pitch.toString() );
						}

						_markers.get( _nextBeat ).setActive( true );

						int prevBeat = ( _nextBeat - 1 + totalBeats ) % totalBeats; 
						_markers.get( prevBeat ).setActive( false );
						semaphore.release();
						_RequestRender();
						++_nextBeat;
					}
				}
			}
		};
		beeper.start();
	}

	private RealInt _GetTimeTileNextBeat() {

		Subdivision subdiv = _currMetronomeBar.GetSubdivision();
		return CalculateBeatDuration( _beatsPerMin, _currMetronomeBar.GetBeat(), subdiv );
	}

	public void StopBeating() {
		Logger.i("stop");
		_hasStarted = false;
		_nextBeat = 0;

		if ( beeper == null ) return;
		try {
	    beeper.join();
    } catch (InterruptedException e) {
	    e.printStackTrace();
    }

		int size = _markers.size();
		for (int i = 0; i < size; ++i ) {
			_markers.get( i ).setActive( false );
		}
	}

	public void ToggleBeating() {
		if (_hasStarted) 	StopBeating();
		else              StartBeating();
	}

	public void Pause() {
		_isPaused = true;
	}

	public void Resume() {
		_isPaused = false;
	}

	public void SetBeatLength( int beat ) {
		NoteType base = NoteType.GetNote( beat );
		Subdivision subdiv = _currMetronomeBar.GetSubdivision();
		CalculateBeatDuration( _beatsPerMin, base, subdiv );
		_currMetronomeBar.SetBeat( base );
	}

	public int GetBeatLength() {
		return _currMetronomeBar.GetBeat().GetNoteLength();
	}

	public void SetBarLength( int length ) {
		_currMetronomeBar.SetBeatsPerBar( length );
	}

	public int GetBarLength() {
		return _currMetronomeBar.GetBeatsPerBar();
	}

	public void SetBeatPerMin( int bpm ) {
		_beatsPerMin = bpm;
	}

	public int GetBeatPerMin() {
		return _beatsPerMin;
	}

	public final MetronomeBar getCurrBar() {
		return _currMetronomeBar;
	}

	public void SetSubdivision( int division ) {
		_currMetronomeBar.SetSubdivision( Subdivision.get( division ) );
		NoteType base = _currMetronomeBar.GetBeat();
		Subdivision subdiv = _currMetronomeBar.GetSubdivision();
		CalculateBeatDuration( _beatsPerMin, base, subdiv );
	}

	public int SaveBar() {
		MetronomeBar newBar = new MetronomeBar( _currMetronomeBar );
		_metronomeBarList.add( newBar );
		SelectBar( _metronomeBarList.size() - 1 );
		return _metronomeBarList.size() - 1;
	}

	public void RemoveBar() {
		int size = _metronomeBarList.size();
		if ( size == 1 ) return;

		_metronomeBarList.remove( _currMetronomeBar );
		_currMetronomeBar = _metronomeBarList.get( 0 );
	}

	public void SelectBar( int i ) {
		_currMetronomeBar = _metronomeBarList.get( i );
	}

	public int GetSavedBarCount() {
		return _metronomeBarList.size();
	}

	public void _RefreshMarker() {

		float x = 0f, y = 0f, z = 0f;
		float w = 1f, h = 1f;
		float xPad = 5f;
		float yPad = 2.5f;

		MetronomeBar bar = _currMetronomeBar;
		int length = bar.GetBeatsPerBar();

		int rows = length / 8 + ( ( length % 8 > 0 ) ? 1 : 0 );
		int columns = length / rows + ( ( length % rows > 0 ) ? 1 : 0 );
		int left = length % rows;

		float xInterval = ( 160f - xPad ) / columns;
		float yInterval = 0f;
		if ( rows <= 4 ) {
			yInterval = 20f + yPad;
		}
		else {
			yInterval = ( 90f - yPad ) / rows;
		}

		y = ( rows - 1 ) * yInterval * 0.5f;

		w = xInterval - xPad;
		h = yInterval - yPad;

		// TODO add mutex

		while ( !semaphore.tryAcquire() ) {};

		_ClearMarker();
		int k = 0;
		for (int i = 0; i < rows; ++i) {

			int currColumns;
			if (left > i || left == 0) 	currColumns = columns;
			else 												currColumns = columns - 1;
			x = - ( currColumns - 1 ) * xInterval * 0.5f;

			while (currColumns-- != 0) {
				BeepPitch p = bar.GetDownBeat( k++ );
				_AddMarker( p, x, y, z, w, h );
				x += xInterval;
			}
			y -= yInterval;
		}
		semaphore.release();
	}

	private boolean OnTouch( float x, float y ) {
		Logger.i("OnTouch");
		if ( _hasStarted ) return false;
		Logger.i("size " + _markers.size());

		int size = _markers.size();
		for (int i = 0; i < size; ++i ) {
			float []rect = _markers.get( i ).getRect();
			if ( rect[0] < x && rect[1] < y && rect[2] > x && rect[3] > y ) {

				BeepPitch pitch = _currMetronomeBar.GetDownBeat( i ).NextBeep();
				_currMetronomeBar.SetDownBeat( i, pitch );
				_markers.get( i ).setPitch( pitch );
				SoundManager.playSound( pitch.name() );
				_RequestRender();
				return true;
			}
		}
		return false;
	}

	private static RealInt CalculateBeatDuration( int bpm, NoteType beat, Subdivision subdivision ) {
		int newbpm = bpm * beat.GetNoteLength() * subdivision.getDenominator();
		long time = 60000000000L * NoteType.Crochet.GetNoteLength() * subdivision.getNumerator();
		return new RealInt( time, newbpm );
	}
	
	private void _RequestRender() {
		// _renderer.CopyMarkers( _markers );
		// _surfaceViewMetronomeDisplay.requestRender();
	}

	private void _ClearMarker() {
		_markers.clear();
	}

	private void _AddMarker( BeepPitch pitch, float x, float y, float z, float w, float h ) {
		BeepMarker newMarker = new BeepMarker();
		newMarker.setScale( w, h );
		newMarker.setPosition( x, y, z );

		newMarker.setPitch( pitch );
		newMarker.setActive( false );
		_markers.add( newMarker );

	}

}
