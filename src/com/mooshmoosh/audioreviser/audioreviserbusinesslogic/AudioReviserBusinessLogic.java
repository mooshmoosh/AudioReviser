package com.mooshmoosh.audioreviser.audioreviserbusinesslogic;

import java.io.File;

public class AudioReviserBusinessLogic {
	
	//textViewWidgets
	public boolean[] textViewIsChanged;
	public String[] newText;
	
	public static int TEXTVIEW_WIDGET_COUNT = 5;
	
	//TextViewWidget constants, the names match those in R.id.*
	public int chunk_time_remaining = 0;
	public int current_chunk = 1;
	public int date_stamp = 2;
	public int instruction_text_view = 3;
	public int notes_list = 4;
	
	//buttonWidgets
	public boolean[] buttonIsChanged;
	public boolean[] isEnabled;
	public String[] newButtonText;
	
	public static int BUTTON_WIDGET_COUNT = 5;
	
	//buttonWidget constants, the names match those in R.id.*
	public int add_notes_button = 0;
	public int next_button = 1;
	public int pause_button = 2;
	public int play_button = 3;
	public int start_stop_button = 4;
	
	public AndroidAudioManagerMock mAudioManager;
	public String mWorkingDirectory;

	public AudioReviserBusinessLogic(AndroidAudioManagerMock audioManager, String WorkingDirectory) throws Exception{
		mAudioManager = audioManager;
		if(!createAppDirectory(WorkingDirectory)) {
			throw new Exception("Could Not Create Working Directory");
		}
		mWorkingDirectory = WorkingDirectory + "/";
		resetWidgetControls();
		
		initialiseWidgets();
	}
	
	private boolean createAppDirectory(String directory) {
		File folder = new File(directory);
		if(!folder.exists()) {
			return folder.mkdir();
		}
		else {
			return true;
		}
	}
	
	public void resetWidgetControls() {
		textViewIsChanged = new boolean[TEXTVIEW_WIDGET_COUNT];
		newText = new String [TEXTVIEW_WIDGET_COUNT];
		
		buttonIsChanged = new boolean [TEXTVIEW_WIDGET_COUNT];
		isEnabled = new boolean [TEXTVIEW_WIDGET_COUNT];
		newButtonText = new String [TEXTVIEW_WIDGET_COUNT];
		
		for(int i=0;i<TEXTVIEW_WIDGET_COUNT;i++) {
			textViewIsChanged[i] = false;
			newText[i] = null;
		}
		for(int i=0;i<BUTTON_WIDGET_COUNT;i++) {
			buttonIsChanged[i] = false;
			isEnabled[i] = false;
			newButtonText[i] = null;
		}
	}
	
	private void initialiseWidgets() {
		disableButton(pause_button);
		disableButton(next_button);
		setTextViewText(notes_list,"Today's Chunk to play is: ");
	}

	public void nextButtonClick() {
		// TODO Auto-generated method stub
	}

	public void pauseButtonClick() {
		// TODO Auto-generated method stub
		
	}

	public void startStopButtonClick() {
		// TODO Auto-generated method stub
		
	}

	public void playButtonClick() {
		// TODO Auto-generated method stub
		
	}

	public void startStopButtonWork() {
		// TODO Auto-generated method stub
		
	}
	
	private void setTextViewText(int textView, String newValue) {
		if(0<=textView&& textView <TEXTVIEW_WIDGET_COUNT) {
			newText[textView] = newValue;
			textViewIsChanged[textView] = true;
		}
	}
	
	private void enableButton(int button) {
		if(0 <= button && button < BUTTON_WIDGET_COUNT) {
			buttonIsChanged[button] = true;
			isEnabled[button] = true;
			newButtonText[button] = null;
		}
	}
	
	private void disableButton(int button) {
		if(0<=button&& button <BUTTON_WIDGET_COUNT) {
			buttonIsChanged[button] = true;
			isEnabled[button] = false;
			newButtonText[button] = null;
		}
	}
}
