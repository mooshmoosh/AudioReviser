package com.mooshmoosh.audioreviser.audioreviserbusinesslogic;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/*
 * AudioReviserDatabaseManager.java
 * 
 * Copyright 2014 mooshmoosh <developerwill1@gmail.com>
 * 
 */


public class AudioReviserDatabaseManager {
	private Map<String, Integer> intValues;
	private Map<String,String> stringValues;
	private Map<String,AudioNoteDatabaseEntry> audioNotes;
	private Map<String,AudioChunkDatabaseEntry> chunks;
	
	public AudioReviserDatabaseManager() {
		intValues = new HashMap<String, Integer>();
		stringValues = new HashMap<String,String>();
		//audioNotes = new HashMap<String,AudioNoteDatabaseEntry>();
		//chunks = new HashMap<String,AudioChunkDatabaseEntry>();
	}
	
	public void loadDatabase(String filename) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int breaker;
        while((line = br.readLine()) != null) {
             breaker = line.indexOf(":");
             if(breaker!=-1) {
				 if(isNumeric(line.substring(breaker+1))) {
					 set(line.substring(0,breaker),Integer.parseInt(line.substring(breaker+1)));
				 }
				 else {
					 set(line.substring(0,breaker),line.substring(breaker+1));
				 }
			 }
        }
	}
	
	public boolean isNumeric(String str)
	{
		//From StackOverflow user ibrahim-arief. (http://stackoverflow.com/a/7092110)
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	public void set(String key,Integer value) {
		intValues.put(key,value);
	}
	
	public int valueOf(String key) throws Exception{
		if(intValues.containsKey(key)) {
			return (int) intValues.get(key);
		}
		else throw new Exception("intValues does not contain key:" + key);
	}
	
	public void set(String key,String value) {
		stringValues.put(key,value);
	}
	
	public String get(String key) throws Exception{
		if(stringValues.containsKey(key)) {
			return stringValues.get(key);
		}
		else throw new Exception("stringValues does not contain key:" + key);
	}
	
	public boolean entryExists(String key) {
		return stringValues.containsKey(key) || intValues.containsKey(key);
	}
	
	public String toString() {
		String result="";
		for (Map.Entry<String, Integer> entry : intValues.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			result+=key + ":" + value + "\n";
		}
		for (Map.Entry<String, String> entry : stringValues.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			result+=key + ":" + value + "\n";
		}
		return result;
	}
	
	public void save(String filename) {
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter( new FileWriter( filename));
			writer.write( this.toString());

		}
		catch ( java.io.IOException e)
		{
		}
		finally
		{
			try
			{
				if ( writer != null)
				writer.close( );
			}
			catch ( java.io.IOException e)
			{
			}
		}
	}
	
	
	public int CurrentChunkToPlay;
	public String NextNoteToPlay;
	public String PreviousNotePlayed;
	public int NextDelayAfterNextFile;
	public int PreviousDelay;
	public int RelaxingMusicPosition;
	
	public int DelayBetweenFiles;
	public int RevisionLevel;

}

