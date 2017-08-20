package actions;

import java.util.ArrayList;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;

import base.Base64Util;
import base.CopyUtil;
import base.FileUtil;
import base.OtherUtil;
import base.SearchUtil;
import cliGui.OutBut;
import db.DatabaseTester;
import info.InfoGathering;
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
	public void sC(String key,boolean all){
		File stringsXml = new File(TicklerVars.extractedDir+"res/values/strings.xml");
		ArrayList<SimpleEntry> hits = this.searchInCodeWithOption(key, all);
		OtherUtil.printSimpleEntryArray(hits, TicklerVars.jClassDir, "[Java_Code_Dir]");
		
		OutBut.printH2("Searching for "+key+" in res/strings.xml");
		ArrayList<String> hits2 = this.searchUtil.findInFile(stringsXml, key);
		
		if (!hits.isEmpty())
			for (String s: hits2)
				OutBut.printNormal(" "+s+"\n");
		
	}
	
	/**
	 * Searches in code based on option: sc_all --> all = true, sc --> all=false
	 * @param key
	 * @param all
	 * @return
	 */
	private ArrayList<SimpleEntry> searchInCodeWithOption(String key, boolean all){
		ArrayList<SimpleEntry> hits = new ArrayList<>();
		
		if (all){
			OutBut.printH2("Searching for "+key+" in Decompiled Java Code");
			hits = this.searchUtil.search4KeyInDirFName(TicklerVars.jClassDir, key) ;
		}
		else {
			OutBut.printH2("Searching for "+key+" in Decompiled Code of the app");
			hits = this.searchUtil.searchForKeyInJava(key);
		}
		
		return hits;
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
		copyz.copyDataDir();
		
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

		//search in external memory
		this.searchExternalMemory(key);
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
	
	private void searchExternalMemory(String key) {
		InfoGathering info = new InfoGathering();
//		CopyUtil cp = new CopyUtil();
		FileUtil fU = new FileUtil();
		String extDir = info.getSdcardDirectory().replaceAll("\\n", "");
		String destExtDir=TicklerVars.transferDir+"externalMemory";
		if (!extDir.isEmpty()){
			OutBut.printH2("Searching the app's external memory");
//			if (new File(destExtDir).exists())
//				fU.deleteFromHost(destExtDir);
			fU.copyDirToHost(extDir, destExtDir,true);
			System.out.println("");
			ArrayList<SimpleEntry> hits = this.searchUtil.search4KeyInDirFName(destExtDir, key);
			OtherUtil.printSimpleEntryArray(hits, extDir, "[External_Dir]");
		}
	}

}
