package com.willmischlewski.audiorevisor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import android.os.Process;

public class MainActivity extends Activity {

	public static final int CHUNK_LENGTH = 300000;
	
	static public TextView NotesList;
	static public String NotesListContent;
	static private Intent intent;
	
	private Handler mhandler;
	private Runnable DelayedTask;
	
	public boolean CurrentlyPlaying;
	public boolean WritingToFiles;
	
	public int CurrentChunkToPlay;
	public String NextNoteToPlay;
	public String PreviousNotePlayed;
	public int NextDelayAfterNextFile;
	public int PreviousDelay;
	public int RelaxingMusicPosition;
	
	public int DelayBetweenFiles;
	public int RevisionLevel;
	
	public AndroidAudioManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//if(!DoubleCheckChunkLengthConstant())
		//	Log.v("WillDev", "CHUNK_LENGTH Has been badly changed!");
		
		setContentView(R.layout.activity_main);
		
		((Button) findViewById(R.id.pauseButton)).setEnabled(false);
		((Button) findViewById(R.id.nextButton)).setEnabled(false);

		NotesList = (TextView) findViewById(R.id.textView1);
		
		am = new AndroidAudioManager("AudioRevisor");
		
		CurrentlyPlaying = false;
		WritingToFiles = false;
		CurrentChunkToPlay = loadOrInitialise("CurrentChunkToPlay");
		RevisionLevel = loadOrInitialise("RevisionLevel");
		
		if(!fileExists("FirstInChunk" + CurrentChunkToPlay + ".txt") || !fileExists("ChunkRunningTime" + CurrentChunkToPlay + ".txt")) {
			//if FirstInChunk doesn't exist for the chunk we're meant to play then there are no notes to play
			
			//if ChunkRunningTime doesn't exist, the chunk is incomplete.
			
			//TODO deal with the possibility that there are no new notes to revise for the first time, but
			//we can still revise notes from a week ago, month ago etc...
			((Button) findViewById(R.id.playButton)).setEnabled(false);
		}
		
		if(CurrentChunkToPlay<7) {
			NotesList.setText("Today's Chunk to play is: " + CurrentChunkToPlay );
		}
		else if (CurrentChunkToPlay<30) {
			NotesList.setText("Today's Chunks to play are: " + CurrentChunkToPlay + ", " +  (CurrentChunkToPlay - 7));
		}
		else {
			NotesList.setText("Today's Chunks to play are: " + CurrentChunkToPlay + ", " +  (CurrentChunkToPlay - 7) + ", " +  (CurrentChunkToPlay - 30));
		}
		
		mhandler = new Handler();
		
		PreviousDelay=0; 
		
		

	}
	
	@Override
	public void onResume() {
	    super.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	}
	
	@Override
	public void onDestroy() {
		NotesList = null;
		NotesListContent = null;
		intent = null;
		am.release();
	    super.onDestroy();
	    Process.killProcess(Process.myPid());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loadRecordMoreNotesActivity(View view)
	{
		intent = new Intent(this, CreateMoreNotes.class);
		startActivity(intent);
	}
	
	public void playButtonClick(View view) {
		((Button) findViewById(R.id.setEnabled)).pauseButton(true);
		((Button) findViewById(R.id.nextButton)).setEnabled(true);
		((Button) findViewById(R.id.playButton)).setEnabled(false);
		//if this button is pressed again, we start playing the chunk from the beginning again
		CurrentlyPlaying = true;
		am.PlayAudioFile("relax_music.mp3");
		beginPlayingChunk(CurrentChunkToPlay);
		
		
	}
	
	public void pauseButtonClick(View view) {
		if(WritingToFiles) {
			return;
		}
		if(CurrentlyPlaying) {
			WritingToFiles = true;
			if(!fileExists(PreviousNotePlayed + "next.txt")) {
				//I've had trouble if we're at the end of the chunk
				return;
			}
			RelaxingMusicPosition = am.getAudioPosition();
			am.StopEverything();
			mhandler.removeCallbacks(DelayedTask);
			((Button) view).setText(getString(R.string.MainActivity_pause_button_resume_text));
			NextNoteToPlay = null;
			CurrentlyPlaying=false;
			WritingToFiles = false;
		}
		else {
			WritingToFiles = true;
			if(PreviousDelay==0) {
				//this is the first in a chunk;
				PreviousDelay = Integer.parseInt(readStringFromFile(PreviousNotePlayed + "length.txt", 20)) + DelayBetweenFiles;
			}
			am.resumeAudioPlaybackAt("relax_music.mp3", RelaxingMusicPosition);
			PlayFileInChain(PreviousNotePlayed,PreviousDelay);
			((Button) view).setText(getString(R.string.MainActivity_pause_button_pause_text));
			CurrentlyPlaying=true;
			WritingToFiles = false;
			
		}
	}
	
	public void nextButtonClick(View view) {
		if(!CurrentlyPlaying) {
			return;
		}
		//TODO When next is clicked, find out the next file to be played and set the variables accordingly
		//but don't call any functions.
		if(fileExists(NextNoteToPlay + "next.txt")) {
			PreviousNotePlayed = NextNoteToPlay;
			NextNoteToPlay = readStringFromFile(NextNoteToPlay + "next.txt",20);
		}
	}
	

	
	public void PlayFileInChain(String fname, int delay) {
		
		am.PlayAudioFileVoice(fname);
		
		//Create a handle and timer to call this function on the next file after delay milliseconds
		//the delay given is {fname}length.txt + DelayBetweenFiles
		
		if(!fileExists(fname + "next.txt")) {
			PreviousDelay = 0;
			//this means we're at the end of the current chunk
			RevisionLevel++;
			if(RevisionLevel==1) {
				//play the chunk 7 chunks ago, or end audio
				if(CurrentChunkToPlay>=7) {
					CurrentChunkToPlay -= 7;
					writeStringToFile(String.valueOf(CurrentChunkToPlay),"CurrentChunkToPlay.txt");
					scheduleNewChunk(delay);
				}
				else {
					RevisionLevel=0;
					writeStringToFile("0","RevisionLevel.txt");
					CurrentChunkToPlay++;
					writeStringToFile(String.valueOf(CurrentChunkToPlay),"CurrentChunkToPlay.txt");
					scheduleEndAudio(delay);
				}
			}
			else if(RevisionLevel==2) {
				//play the chunk 30 chunks ago, or end audio
				if(CurrentChunkToPlay>=30) {
					CurrentChunkToPlay -= 30;
					writeStringToFile(String.valueOf(CurrentChunkToPlay),"CurrentChunkToPlay.txt");
					scheduleNewChunk(delay);
				}
				else {
					RevisionLevel = 0;
					writeStringToFile("0","RevisionLevel.txt");
					//restore the value of CurrentChunkToPlay, and add one
					CurrentChunkToPlay += 8;
					writeStringToFile(String.valueOf(CurrentChunkToPlay),"CurrentChunkToPlay.txt");

					scheduleEndAudio(delay);
				}
			}
			else if(RevisionLevel==3) {
				//restore the value of CurrentChunkToPlay, and add one
				CurrentChunkToPlay += 38;
				writeStringToFile(String.valueOf(CurrentChunkToPlay),"CurrentChunkToPlay.txt");
				RevisionLevel = 0;
				writeStringToFile(String.valueOf(RevisionLevel),"RevisionLevel.txt");
				scheduleEndAudio(delay);
			}
			return;
		}
		PreviousNotePlayed = NextNoteToPlay;
		NextNoteToPlay = readStringFromFile(fname + "next.txt",20);
		PreviousDelay = delay;
		NextDelayAfterNextFile = Integer.parseInt(readStringFromFile(NextNoteToPlay + "length.txt", 20)) + DelayBetweenFiles;
		
		DelayedTask = new Runnable() {
			@Override
			public void run() {
				PlayFileInChain(NextNoteToPlay,NextDelayAfterNextFile);
			}
		};
		
		mhandler.postDelayed(DelayedTask, delay);
		
	}
	
	public void scheduleNewChunk(int delay) {
		DelayedTask = new Runnable() {
			@Override
			public void run() {
				beginPlayingChunk(CurrentChunkToPlay);
			}
		};
		
		mhandler.postDelayed(DelayedTask, delay);
	}
	
	public void scheduleEndAudio(int delay) {
		//When we're normally supposed to play the next file after {delay} milliseconds, instead this function
		//ends all audio after the current file has finished playing
		//delay always = length of current file + delay between files.
		DelayedTask = new Runnable() {
			@Override
			public void run() {
				am.StopPlayingAudio();
				((Button) findViewById(R.id.pauseButton)).setEnabled(false);
				((Button) findViewById(R.id.nextButton)).setEnabled(false);
			}
		};
		mhandler.postDelayed(DelayedTask, delay - DelayBetweenFiles);
	}
	
	public void beginPlayingChunk(int chunkNumber) {
		int totalDelay = CHUNK_LENGTH -  Integer.parseInt(readStringFromFile("ChunkRunningTime" + chunkNumber + ".txt", 20));
		
		int totalNumberOfFilesInThisChunk = Integer.parseInt(readStringFromFile("chunk" + chunkNumber + "NoteCount.txt", 20));
		
		if(totalNumberOfFilesInThisChunk<=1) {
			//if there is only one file in this chunk, then the delay between files is irrelevant
			DelayBetweenFiles = 0;
		}
		else {
			//There are (totalNumberOfFilesInThisChunk - 1) gaps between files. The delay between each is equal,
			//and the delay is evenly spread.
			DelayBetweenFiles = totalDelay / (totalNumberOfFilesInThisChunk - 1);
		}
		
		//TODO Call PlayFileInChain on the first file, with DelayBetweenFiles
		
		NextNoteToPlay =  readStringFromFile("FirstInChunk" + chunkNumber + ".txt",20);
		//in this example, Previous Note Played hasn't been set yet.
		PreviousNotePlayed = NextNoteToPlay;
		NextDelayAfterNextFile = Integer.parseInt(readStringFromFile(NextNoteToPlay + "length.txt", 20)) + DelayBetweenFiles;

		PlayFileInChain(NextNoteToPlay,NextDelayAfterNextFile);
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
			while(Character.isDigit(bufferToString.charAt(i)) || bufferToString.charAt(i)== Character.valueOf('-') & i<bufferToString.length()) {
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
