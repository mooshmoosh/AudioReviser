package com.mooshmoosh.audioreviser;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class AudioreviserMainActivity extends Activity {
	private FragmentManager fm;
	private ListenToNotesFragment listenToNotesFragment;
	private RecordMoreNotesFragment recordMoreNotesFragment;
	public AudioReviserBusinessLogic businessLogicUnit;
	
	public static int TEXTVIEW_WIDGET_COUNT = AudioReviserBusinessLogic.TEXTVIEW_WIDGET_COUNT;	
	public TextView[] textViewWidgets;
	
	public static int BUTTON_WIDGET_COUNT = AudioReviserBusinessLogic.BUTTON_WIDGET_COUNT;
	public Button[] buttonWidgets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audioreviser_mainactivity);
		
		recordMoreNotesFragment = new RecordMoreNotesFragment();
		listenToNotesFragment = new ListenToNotesFragment();
		
		businessLogicUnit = new AudioReviserBusinessLogic(new AndroidAudioManagerMock());
		businessLogicUnit.init();
		
		fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.container, listenToNotesFragment);
		ft.commit();
	}
	
	protected void onStart() {
		super.onStart();
		buttonWidgets = new Button[BUTTON_WIDGET_COUNT];
		buttonWidgets[businessLogicUnit.add_notes_button] = (Button)findViewById(R.id.add_notes_button);
		buttonWidgets[businessLogicUnit.next_button] = (Button)findViewById(R.id.next_button);
		buttonWidgets[businessLogicUnit.pause_button] = (Button)findViewById(R.id.pause_button);
		buttonWidgets[businessLogicUnit.play_button] = (Button)findViewById(R.id.play_button);
		buttonWidgets[businessLogicUnit.start_stop_button] = (Button)findViewById(R.id.start_stop_button);
		
		textViewWidgets = new TextView[TEXTVIEW_WIDGET_COUNT];
		textViewWidgets[businessLogicUnit.chunk_time_remaining] = (TextView) findViewById(R.id.chunk_time_remaining);
		textViewWidgets[businessLogicUnit.current_chunk] = (TextView) findViewById(R.id.current_chunk);
		textViewWidgets[businessLogicUnit.date_stamp] = (TextView) findViewById(R.id.date_stamp);
		textViewWidgets[businessLogicUnit.instruction_text_view] = (TextView) findViewById(R.id.instruction_text_view);
		textViewWidgets[businessLogicUnit.notes_list] = (TextView) findViewById(R.id.notes_list);
	}
	
	public void switchToAddNotesFragment(View dummy) {
		Log.d("WillDev","switchToAddNotesFragment");
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, recordMoreNotesFragment);
		ft.addToBackStack(null);
		ft.commit();
		updateUI();
	}
	
	public void nextButtonClick(View dummy) {
		businessLogicUnit.nextButtonClick();
		Log.d("WillDev","nextButtonClick");
		updateUI();
	}
	
	public void pauseButtonClick(View dummy) {
		businessLogicUnit.pauseButtonClick();
		Log.d("WillDev","pauseButtonClick");
		//this is a test delete the next line
		String val = null;
		
		if(val==null) {
			Log.d("WillDev","nul does equal null");
		}
		
		updateUI();
	}
	
	public void playButtonClick(View dummy) {
		businessLogicUnit.playButtonClick();
		Log.d("WillDev","playButtonClick");
		updateUI();
	}
	
	public void startStopButtonClick(View dummy) {
		businessLogicUnit.startStopButtonClick();
		updateUI();
		/* Starting and stopping recording can take some time, so the startStopButtonClick function
		 * should disable the button and set its text to "wait" while the real work is being done.
		 * This real work is moved into the startStopButtonWork function.
		 */
		
		businessLogicUnit.startStopButtonWork();
		Log.d("WillDev","startStopButtonClicked");
		updateUI();
	}
	
	private void updateUI() {
		for(int i=0;i<TEXTVIEW_WIDGET_COUNT;i++) {
			if(businessLogicUnit.textViewIsChanged[i]) {
				if(businessLogicUnit.newText[i]!=null) {
					textViewWidgets[i].setText(businessLogicUnit.newText[i]);
					businessLogicUnit.newText[i] = null;
				}
				businessLogicUnit.textViewIsChanged[i] = false;
			}
		}
		for(int i=0;i<BUTTON_WIDGET_COUNT;i++) {
			if(businessLogicUnit.buttonIsChanged[i]) {
				buttonWidgets[i].setEnabled(businessLogicUnit.isEnabled[i]);
				if(businessLogicUnit.newButtonText[i]!=null) {
					textViewWidgets[i].setText(businessLogicUnit.newButtonText[i]);
				}
				businessLogicUnit.buttonIsChanged[i] = false;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audioreviser_mainactivity, menu);
		return true;
	}

}
