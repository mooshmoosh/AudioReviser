package com.mooshmoosh.audioreviser;

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
	
	AndroidAudioManager mAudioManager;

	public AudioReviserBusinessLogic(AndroidAudioManager audioManager) {
		// TODO Auto-generated constructor stub
		mAudioManager = audioManager;
	}
	
	public void init() {
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

	public void nextButtonClick() {
		// TODO Auto-generated method stub
		
		//this next line is just a test of the interface
		setTextViewText(notes_list,"Hello");
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
		newText[textView] = newValue;
		textViewIsChanged[textView] = true;
	}
}
