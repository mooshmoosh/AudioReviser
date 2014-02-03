package tests;
import com.mooshmoosh.audioreviser.audioreviserbusinesslogic.*;


/* test.java
 * 
 * Copyright 2014 mooshmoosh <developerwill1@gmail.com>
 */

public class test {
	
	public static void main (String[] args) {
		try {
			AudioReviserBusinessLogic_Initialisation();
			AudioReviserDatabaseManager_set_StringInteger();
			AudioReviserDatabaseManager_set_StringString();
			AudioReviserDatabaseManager_loadDataBase();
			
			
			System.out.println("All tests ok!");
		}
		catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
	
	private static void AudioReviserBusinessLogic_Initialisation() throws Exception {
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		
			AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "WorkingDir");
			assert(blu.buttonIsChanged[blu.pause_button]==true);
			assert(blu.isEnabled[blu.pause_button]==false);
			assert(blu.buttonIsChanged[blu.next_button]==true);
			assert(blu.isEnabled[blu.next_button]==false);
			assert(blu.textViewIsChanged[blu.notes_list]==true);
			assert(blu.newText[blu.notes_list]=="Today's Chunk to play is: ");
			//TODO add tests for initialisation of database
		
	}
	
	private static void AudioReviserDatabaseManager_set_StringInteger() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.set("Hello",5);
		assert(dbm.valueOf("Hello")==5);
	}
	
	private static void AudioReviserDatabaseManager_set_StringString() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.set("Hello","Gerald");
		assert(dbm.get("Hello").equals("Gerald"));
	}
	
	private static void AudioReviserDatabaseManager_loadDataBase() throws Exception{
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.loadDatabase("WorkingDir/testDataBase.txt");
		assert(dbm.valueOf("test1")==5);
		assert(dbm.valueOf("test2")==6);
		assert(dbm.valueOf("test3")==7);
		assert(dbm.get("Hello1").equals("Gerald"));
		assert(dbm.get("Hello2").equals("GeraldJohnson"));
		assert(dbm.get("Hello3").equals("Gerald Someone Else"));
		
	}
}

