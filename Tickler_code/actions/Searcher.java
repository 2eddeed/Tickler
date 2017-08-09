package actions;

import java.util.ArrayList;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;

import base.Base64Util;
import base.CopyUtil;
import base.OtherUtil;
import base.SearchUtil;
import cliGui.OutBut;
import db.DatabaseTester;
import initialization.TicklerVars;

public class Searcher {
	
	private SearchUtil searchUtil;
	
	public Searcher() {
		this.searchUtil = new SearchUtil();
	}

	
	/**
	 * Searches for a key in decompiled java code and in strings.xml
	 * @param key
	 */
	public void sC(String key){
		File stringsXml = new File(TicklerVars.extractedDir+"res/values/strings.xml");
		OutBut.printH2("Searching for "+key+" in Decompiled Java Code");
		ArrayList<SimpleEntry> hits = this.searchUtil.searchForKeyInJava(key);
		
		OtherUtil.printSimpleEntryArray(hits, TicklerVars.jClassDir, "[Java_Code_Dir]");
		
		OutBut.printH2("Searching for "+key+" in res/strings.xml");
		ArrayList<String> hits2 = this.searchUtil.findInFile(stringsXml, key);
		
		if (!hits.isEmpty())
			for (String s: hits2)
				OutBut.printNormal(" "+s+"\n");
		
	}
	
	
	
	/**
	 * Search for a key in the Data directory of an App, including files and unencrypted databases.
	 * Also search for the key in clear text and in Base64 format
	 * Also check in External Dir if exists
	 * @param key
	 */
	public void searchForKeyInDataDir(String key){
		
		OutBut.printStep("Updating Data Directory");
		CopyUtil copyz = new CopyUtil();
		copyz.copyDataDir(null);
		
		//Search in files
		OutBut.printH2("Searching Files in Data Directory of the app");
		ArrayList<SimpleEntry> hits = this.searchUtil.search4KeyInDirFName(TicklerVars.dataDir, key);
		
		OtherUtil.printSimpleEntryArray(hits, TicklerVars.dataDir, "[Data_Dir]");
		
		// Search Base64
		this.base64Search(key);
		
		//Search in DB
		OutBut.printH2("Searching Files in the app's unencrypted databases");
		DatabaseTester db = new DatabaseTester();
		db.searchForKeyInDb(key);

		
	}
	

	
	private void base64Search(String key){
		Base64Util b64 = new Base64Util();
		ArrayList<String> base64Hits = b64.searchB64DataDir(key);
		
		if (!base64Hits.isEmpty()){
			OutBut.printH2("The key is base64 encrypted in the following file(s)");
			for (String s: base64Hits){
				String filePath = s.replaceAll(TicklerVars.jClassDir, "[Data_Dir]"); 
				System.out.println("#FileName: "+filePath);
			}
			
		}
		
	}

}
