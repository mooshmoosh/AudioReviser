package com.willmischlewski.audiorevisor;

import com.willmischlewski.audiorevisor.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class CreateMoreNotes extends Activity {

	public static final int CHUNK_LENGTH = 300000;
	
	public TextView DateStamp;
	
	public String CurrentRecordingName;
	public String PreviousRecordingName;
	
	public int ChunkRunningTime;
	public int CurrentRecordingChunk;
	public int CurrentChunkNoteCount;
	
	public boolean CurrentlyRecording;
	public boolean CurrentlySavingFiles;
	
	public AndroidAudioManager am;
		
	public Intent intent;
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		intent = null;
		am.release();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    am = new AndroidAudioManager("AudioRevisor");
	}

	@Override
	public void onPause() {
	    super.onPause();
	    am.release();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_more_notes);
		// Show the Up button in the action bar.
		setupActionBar();
		intent = getIntent();
		DateStamp = (TextView) findViewById(R.id.datestamp);
		CurrentRecordingName = currentDateFormatted();
		DateStamp.setText(CurrentRecordingName);
		
		am = new AndroidAudioManager("AudioRevisor");
		
		CurrentlyRecording = false;
		CurrentlySavingFiles = false;
		
		ChunkRunningTime = loadOrInitialise("runningtime");
		((TextView) findViewById(R.id.ChunkTimeRemainingLabel)).setText(getTimeRemainingInChunk());
		
		if(fileExists("last.txt")) {
			PreviousRecordingName = readStringFromFile("last.txt", 25);
		}
		else {
			PreviousRecordingName = null;
		}
		
		CurrentRecordingChunk = loadOrInitialise("CurrentRecordingChunk");
		((TextView) findViewById(R.id.currentchunk)).setText(String.valueOf(CurrentRecordingChunk));
		
		CurrentChunkNoteCount = loadOrInitialise("CurrentChunkNoteCount");
			
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_more_notes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void ButtonClick(View view)
	{
		if(CurrentlySavingFiles)
		{
			return;
		}
		if(CurrentlyRecording)
		{
			CurrentlySavingFiles = true;
			am.endRecordingAudio();
			((Button) view).setText(R.string.record_more_notes_button_text3);
			CurrentlyRecording = false;
			((TextView) findViewById(R.id.textView1)).setText(R.string.instruction_text_record_more_notes);
			
			//save the duration of the file just recorded
			int lastNoteDuration = am.getRecordingDuration(CurrentRecordingName);
			writeStringToFile(String.valueOf(lastNoteDuration), CurrentRecordingName + "length.txt");
			
			//determine if this file belongs in the previous chunk, or should start a new one
			if(lastNoteDuration + ChunkRunningTime > CHUNK_LENGTH)
			{
				writeStringToFile(String.valueOf(CurrentChunkNoteCount),"chunk" + CurrentRecordingChunk + "NoteCount.txt");
				
				writeStringToFile(String.valueOf(ChunkRunningTime),"ChunkRunningTime" + CurrentRecordingChunk + ".txt");
				
				CurrentRecordingChunk++;
				writeStringToFile(String.valueOf(CurrentRecordingChunk),"CurrentRecordingChunk.txt");
				((TextView) findViewById(R.id.currentchunk)).setText(String.valueOf(CurrentRecordingChunk));
				
				writeStringToFile(CurrentRecordingName, "FirstInChunk" + String.valueOf(CurrentRecordingChunk)+".txt");
				
				writeStringToFile(String.valueOf(lastNoteDuration),"runningtime.txt");
				ChunkRunningTime = lastNoteDuration;
				Log.v("WillDev","New chunk, ChunkRunningTime set to " + lastNoteDuration);
				
				//when we decide a note has to go into a new chunk, the new chunk starts off with one note in it.
				CurrentChunkNoteCount=1;
				writeStringToFile("1","CurrentChunkNoteCount.txt");
				PreviousRecordingName = CurrentRecordingName;
			}
			else
			{
				if(PreviousRecordingName==null) {
					writeStringToFile(CurrentRecordingName, "FirstInChunk0.txt");
				}
				else {
					writeStringToFile(CurrentRecordingName, PreviousRecordingName + "next.txt");
				}
				PreviousRecordingName = CurrentRecordingName;
				ChunkRunningTime += lastNoteDuration;
				writeStringToFile(String.valueOf(ChunkRunningTime),"runningtime.txt");
				
				CurrentChunkNoteCount++;
				writeStringToFile(String.valueOf(CurrentChunkNoteCount),"CurrentChunkNoteCount.txt");
			}
			writeStringToFile(CurrentRecordingName, "last.txt");
			((Button) view).setText(R.string.record_more_notes_button_text1);
			((TextView) findViewById(R.id.ChunkTimeRemainingLabel)).setText(getTimeRemainingInChunk());
			CurrentlySavingFiles = false;
		}
		else
		{
			CurrentRecordingName = currentDateFormatted();
			am.beginRecordingAudio(CurrentRecordingName);
			DateStamp.setText(currentDateFormatted());
			((Button) view).setText(R.string.record_more_notes_button_text2);
			((TextView) findViewById(R.id.textView1)).setText(R.string.instruction_text_record_more_notes2);
			CurrentlyRecording = true;
			
		}
	}
	
	public boolean fileExists(String fname) {
		try
		{
			File file = new File(am.AppFolder + "/" + fname);
			if(file.exists()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e)
		{
			Log.v("WillDev","exception when checking file's existance file: " + e.getMessage());
			return false;
		}
	}
	
    public void writeStringToFile(String s, String fname)
	{
    	BufferedOutputStream out;
    	File file = new File(am.AppFolder + "/" + fname);
		try
		{
			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(s.getBytes("UTF-8"));
			out.close();
		} catch(Exception e)
		{
			Log.v("WillDev","exception when writing file: " + e.getMessage());
		}
	}

	public String readStringFromFile(String fname, int MaximumLength)
	{
		//Maximum Length is the number of UTF-8 bytes to load, not the length of the string
		byte[] buffer = new byte[MaximumLength];
		InputStream in = null;
		File file = new File(am.AppFolder + "/" + fname);
		try
		{
			in = new BufferedInputStream(new FileInputStream(file));
			in.read(buffer,0,MaximumLength);
			in.close();
			
		}
		catch(Exception e)
		{
			Log.v("WillDev","exception when reading file: " + e.getMessage());
			return "";
		}
		try
		{
			String bufferToString = new String(buffer,"UTF-8");
			int i=0;
			while((bufferToString.charAt(i)!=0) & i<bufferToString.length()) {
				i++;
			}
			if(i<bufferToString.length()) {
				return bufferToString.substring(0,i);
			}
			else {
				return bufferToString;
			}
			
		}
		catch (Exception e)
		{
			Log.v("WillDev", "Exception converting string after reading");
			Log.v("WillDev", e.getMessage());
			return "";
		}
	}
	

	public String currentDateFormatted() {
		Calendar CurrentDate = Calendar.getInstance();
		return String.format(Locale.getDefault(),"%04d-%02d-%02d-%02d-%02d-%02d",CurrentDate.get(Calendar.YEAR),CurrentDate.get(Calendar.MONTH),CurrentDate.get(Calendar.DAY_OF_MONTH),CurrentDate.get(Calendar.HOUR_OF_DAY),CurrentDate.get(Calendar.MINUTE),CurrentDate.get(Calendar.SECOND));
	}
	
	public String getTimeRemainingInChunk() {
		return String.valueOf((float) (CHUNK_LENGTH-ChunkRunningTime)/1000);
		
	}
	
	public int loadOrInitialise(String fname) {
		//loads the integer contained in the file fname and returns it
		//if the file is not found, creates the file, and puts a zero in it, then returns zero.
		if(fileExists(fname + ".txt")) {
			int loadedNumber = Integer.parseInt(readStringFromFile(fname + ".txt", 20));
			Log.v("WillDev",fname + " loaded, = " + loadedNumber);
			return loadedNumber;
		}
		else {
			writeStringToFile("0",fname + ".txt");
			Log.v("WillDev",fname + " set to zero");
			return 0;
		}
	}


}
