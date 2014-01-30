package com.mooshmoosh.audioreviser;

import java.io.File;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AndroidAudioManager {

	private MediaRecorder mRecorder;
	private MediaPlayer mediaPlayer;
	private MediaPlayer mediaPlayerVoice;

	public String AppFolder;
	
	public AndroidAudioManager() {
		
	}
	
	public AndroidAudioManager(String folder) {
		createAppDirectory(folder);
	}
	
    public boolean createAppDirectory(String FolderName)
    {
    	AppFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FolderName;
    	File folder = new File(AppFolder);
    	if(!folder.exists())
    	{
    		return folder.mkdir();
    	}
    	else
    	{
    		return true;
    	}
    }
	
	public void beginRecordingAudio(String fname)
    {
    	String mFileName = AppFolder;
        mFileName += "/" +  fname + ".3gp";

    	mRecorder = new MediaRecorder();
    	mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	mRecorder.setOutputFile(mFileName);
    	mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	try
    	{
    		mRecorder.prepare();
    	}
    	catch (IOException e)
    	{
    		Log.v("WillDev", "Exception when preparing mRecorder");
    		return;
    	}
    	mRecorder.start();
    }
    
    public void endRecordingAudio()
    {
    	mRecorder.stop();
    	mRecorder.release();
    	mRecorder = null;
    }
    
    public int getRecordingDuration(String fname)
    {
		mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	try{
    		mediaPlayer.setDataSource(AppFolder + "/" + fname + ".3gp");
    	}
    	catch (Exception e)
    	{
    		Log.v("WillDev","Exception while trying to get duration of file " + fname);
    		Log.v("WillDev",e.getMessage());
    		return -1;
    	}
    	try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
    		Log.v("WillDev","Exception while trying to prepare media player " + fname);
		} catch (IOException e) {
    		Log.v("WillDev","Exception while trying to prepare media player " + fname);
		}
    	int result = mediaPlayer.getDuration();
		mediaPlayer.release();
    	return result;
    }
    
    public void PlayAudioFile(String fname)
    {
    	String mFileName = AppFolder;
        mFileName += "/" +  fname;
    	
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	try{
    		mediaPlayer.setDataSource(mFileName);
    	}
    	catch (Exception e)
    	{
    		Log.v("WillDev","Exception while trying to set data source to play file");
    		return;
    	}
    	
    	try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
    		Log.v("WillDev","Exception while trying to prepare media player");
		} catch (IOException e) {
    		Log.v("WillDev","Exception while trying to prepare media player");
		}
    	mediaPlayer.start();
    }
    
    public int getAudioPosition() {
    	return mediaPlayer.getCurrentPosition();
    }
    
    public void resumeAudioPlaybackAt(String fname, int time) {
    	PlayAudioFile(fname);
    	mediaPlayer.seekTo(time);
    }
    
    public void StopPlayingAudio()
    {
    	mediaPlayer.pause();
    	mediaPlayer.release();
    }
    
    public void PlayAudioFileVoice(String fname)
    {
    	String mFileName = AppFolder;
        mFileName += "/" +  fname + ".3gp";
    	
    	mediaPlayerVoice = new MediaPlayer();
    	mediaPlayerVoice.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	try{
    		mediaPlayerVoice.setDataSource(mFileName);
    	}
    	catch (Exception e)
    	{
    		Log.v("WillDev","Exception while trying to set data source to play file");
    		return;
    	}
    	
    	try {
    		mediaPlayerVoice.prepare();
		} catch (IllegalStateException e) {
    		Log.v("WillDev","Exception while trying to prepare media player");
		} catch (IOException e) {
    		Log.v("WillDev","Exception while trying to prepare media player");
		}
    	mediaPlayerVoice.start();
    }
    
    public void StopPlayingAudioVoice()
    {
    	mediaPlayerVoice.pause();
    	mediaPlayerVoice.release();
    }
	
    public void StopEverything()
    {
    	try {
    		if(mediaPlayerVoice.isPlaying()) {
    			StopPlayingAudioVoice();
    		}
    	}
    	catch (Exception e) {
    		
    	}
    	try {
    		if(mediaPlayer.isPlaying()) {
    			StopPlayingAudio();
    		}
    	}
    	catch (Exception e) {
    		
    	}
    }
}
