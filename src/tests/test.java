package tests;
import com.mooshmoosh.audioreviser.audioreviserbusinesslogic.*;


/* test.java
 * 
 * Copyright 2014 mooshmoosh <developerwill1@gmail.com>
 */

public class test {
	
	public static void main (String[] args) {
		try {
			AudioReviserBusinessLogic_InitialisationWithNoDatabase();
			AudioReviserDatabaseManager_set_StringInteger();
			AudioReviserDatabaseManager_set_StringString();
			AudioReviserDatabaseManager_loadDataBase();
			AudioReviserDatabaseManager_entryExists();
			AudioReviserDatabaseManager_save_testDataBasetxt_loadtheSavedDataBaseTestItsValuesMatchtestDataBasetxt();
			AudioReviserBusinessLogic_InitialisationWithDatabaseWith1Chunk();
			AudioReviserBusinessLogic_InitialisationWithNoDatabaseWith10Chunks();
			AudioReviserBusinessLogic_InitialisationWithNoDatabaseWith31Chunks();
			AudioReviserBusinessLogic_switchToAddNotesFragment_NoNotesCreatedYet();
			AudioReviserBusinessLogic_startStopButtonWork_NoNotesCreatedYet();
			
			
			System.out.println("All tests ok!");
		}
		catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
	
	private static void AudioReviserBusinessLogic_InitialisationWithNoDatabase() throws Exception {
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "TestDirectory","dataBase.txt");
		assert(blu.buttonIsChanged[blu.pause_button]==true);
		assert(blu.isEnabled[blu.pause_button]==false);
		assert(blu.buttonIsChanged[blu.next_button]==true);
		assert(blu.isEnabled[blu.next_button]==false);
		assert(blu.textViewIsChanged[blu.notes_list]==true);
		assert(blu.newText[blu.notes_list]=="Nothing to play");
		assert(blu.dbase.valueOf("RevisionLevel")==0);
		assert(blu.dbase.valueOf("CurrentChunkToPlay")==0);
		
	}
	
	private static void AudioReviserDatabaseManager_set_StringInteger() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.set("Hello",5);
		assert(dbm.valueOf("Hello")==5);
		dbm.set("Hello",12);
		assert(dbm.valueOf("Hello")==12);
		dbm.set("Hello",13);
		assert(dbm.valueOf("Hello")==13);
	}
	
	private static void AudioReviserDatabaseManager_set_StringString() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.set("Hello","Gerald");
		assert(dbm.get("Hello").equals("Gerald"));
		dbm.set("Hello","Geraldine");
		assert(dbm.get("Hello").equals("Geraldine"));
	}
	
	private static void AudioReviserDatabaseManager_loadDataBase() throws Exception{
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.loadDatabase("TestDirectory/testDataBase.txt");
		assert(dbm.valueOf("test1")==5);
		assert(dbm.valueOf("test2")==6);
		assert(dbm.valueOf("test3")==7);
		assert(dbm.get("Hello1").equals("Gerald"));
		assert(dbm.get("Hello2").equals("GeraldJohnson"));
		assert(dbm.get("Hello3").equals("Gerald Someone Else"));
		
	}
	
	private static void AudioReviserDatabaseManager_entryExists() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		assert(dbm.entryExists("hello")==false);
		dbm.set("hello",12);
		assert(dbm.entryExists("hello")==true);
	}
	
	private static void AudioReviserDatabaseManager_save_testDataBasetxt_loadtheSavedDataBaseTestItsValuesMatchtestDataBasetxt() throws Exception {
		AudioReviserDatabaseManager dbm = new AudioReviserDatabaseManager();
		dbm.loadDatabase("TestDirectory/testDataBase.txt");
		dbm.save("TestDirectory/loadedAndSavedTestDataBase.txt");
		
		AudioReviserDatabaseManager dbm2 = new AudioReviserDatabaseManager();
		dbm2.loadDatabase("TestDirectory/loadedAndSavedTestDataBase.txt");
		assert(dbm2.valueOf("test1")==5);
		assert(dbm2.valueOf("test2")==6);
		assert(dbm2.valueOf("test3")==7);
		assert(dbm2.get("Hello1").equals("Gerald"));
		assert(dbm2.get("Hello2").equals("GeraldJohnson"));
		assert(dbm2.get("Hello3").equals("Gerald Someone Else"));
	}
	
	private static void AudioReviserBusinessLogic_InitialisationWithDatabaseWith1Chunk() throws Exception {
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "TestDirectory","TestDirectory/1chunkdb.txt");
		assert(blu.buttonIsChanged[blu.pause_button]==true);
		assert(blu.isEnabled[blu.pause_button]==false);
		assert(blu.buttonIsChanged[blu.next_button]==true);
		assert(blu.isEnabled[blu.next_button]==false);
		assert(blu.textViewIsChanged[blu.notes_list]==true);
		assert(blu.dbase.valueOf("RevisionLevel")==0);
		assert(blu.dbase.valueOf("CurrentChunkToPlay")==0);
		
		assert(blu.newText[blu.notes_list].equals("Today's chunk to play is: 0"));
		
	}
	
	private static void AudioReviserBusinessLogic_InitialisationWithNoDatabaseWith10Chunks() throws Exception {
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "TestDirectory","TestDirectory/10chunkdb.txt");
		assert(blu.buttonIsChanged[blu.pause_button]==true);
		assert(blu.isEnabled[blu.pause_button]==false);
		assert(blu.buttonIsChanged[blu.next_button]==true);
		assert(blu.isEnabled[blu.next_button]==false);
		assert(blu.textViewIsChanged[blu.notes_list]==true);
		assert(blu.newText[blu.notes_list].equals("Today's chunks to play are: 9, 2"));
		assert(blu.dbase.valueOf("RevisionLevel")==0);
		assert(blu.dbase.valueOf("CurrentChunkToPlay")==9);
	}
	
	private static void AudioReviserBusinessLogic_InitialisationWithNoDatabaseWith31Chunks() throws Exception {
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "TestDirectory","TestDirectory/31chunkdb.txt");
		assert(blu.buttonIsChanged[blu.pause_button]==true);
		assert(blu.isEnabled[blu.pause_button]==false);
		assert(blu.buttonIsChanged[blu.next_button]==true);
		assert(blu.isEnabled[blu.next_button]==false);
		assert(blu.textViewIsChanged[blu.notes_list]==true);
		assert(blu.newText[blu.notes_list].equals("Today's chunks to play are: 30, 23, 0"));
		assert(blu.dbase.valueOf("RevisionLevel")==0);
		assert(blu.dbase.valueOf("CurrentChunkToPlay")==30);
	}
	
	private static void AudioReviserBusinessLogic_switchToAddNotesFragment_NoNotesCreatedYet() throws Exception{
		AndroidAudioManagerMock audioManager = new AndroidAudioManagerMock();
		AudioReviserBusinessLogic blu = new AudioReviserBusinessLogic(audioManager, "TestDirectory","TestDirectory/NonExistantDataBase.txt");
		blu.switchToAddNotesFragment();
		
		String currentTimeStamp = blu.currentDateFormatted(); //assume this function works. don't want to overload the Date Object
		
		assert(audioManager.isCurrentlyRecording==false);
		assert(audioManager.isCurrentlySavingAFile==false);
		//assert(
		assert(blu.textViewIsChanged[blu.date_stamp]==true);
		assert(blu.newText[blu.date_stamp].equals(currentTimeStamp));
		
	}
	
	private static void AudioReviserBusinessLogic_startStopButtonWork_NoNotesCreatedYet() {
		
	}
}

