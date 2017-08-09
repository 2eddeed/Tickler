package code;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import actions.Searcher;
import base.FileUtil;
import base.OtherUtil;
import base.SearchUtil;
import cliGui.OutBut;
import info.InfoGathering;
import initialization.TicklerVars;

/**
 * Extracts information from code such as log messages, comments, credentials, encryption and strings
 * @author aabolhadid
 *
 */

public class JavaSqueezer {
	private SearchUtil sU;
	
	public JavaSqueezer(){
		this.sU = new SearchUtil();
	}
	
	public void report(){
		this.logInCode();
		this.externalStorageInCode();
		this.commentsInCode();
		this.credentialsInCode();
		this.weakCyphers();
		this.getStringsInCode();
	}
	
	///////////////////////////// Search in Code ////////////////////////7
	/**
	 * Search For all logcat commands In Code
	 */
	public void logInCode(){
		OutBut.printH2("Logging messages in logcat");
		ArrayList<SimpleEntry> e = this.sU.search4KeyInDirFName(TicklerVars.jClassDir, "Log") ;
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, ".*Log\\.\\w\\((\\w+?)");
		eResult.addAll(this.sU.refineSearch(e, ".*getLogger\\(\\)\\.(\\w)"));
		
		this.printE(eResult);
		
	}
	
	/**
	 * Search for any possible use of external storage
	 */
	public void externalStorageInCode(){
		OutBut.printH2("Possible External Storage");
		ArrayList<SimpleEntry> eArray = this.sU.searchForKeyInJava("getExternal");
		
		this.printE(eArray);
		
	}
	
	/**
	 *  stuff like // and maybe what's between /* and *\/ 
	 *  Regex for comment: // not proceeded by a : (not a URI), proceeded by any space then no digit (eliminate undecompiled code) 
	 */
	public void commentsInCode(){
		System.out.println("\n");
		OutBut.printH2("Comments");
		
		ArrayList<SimpleEntry> eComments = new ArrayList<>();
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("//");
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "[^:]//(\\s*[a-zA-Z]+)");
		eComments.addAll(eResult);
		
		ArrayList<SimpleEntry> eM = this.sU.searchForKeyInJava("*");
		ArrayList<SimpleEntry> eResultMLine = this.sU.refineSearchMatch(eM, "\\s*/\\*.+");
		ArrayList<SimpleEntry> eResultMLine2 = this.sU.refineSearchMatch(eM, "\\s*\\*.+");
		
		eComments.addAll(eResultMLine);
		eComments.addAll(eResultMLine2);
		this.printE(this.removeDuplicatedSimpleEntries(eComments));
		
	}
	/**
	 * Search for anything that can disclose credentials:
	 * pass, password, username, userID, credentials
	 */
	public void credentialsInCode(){
		System.out.println("\n");
		OutBut.printH2("Possible credentials disclosure");
		ArrayList<SimpleEntry> eA = this.sU.searchForKeyInJava("pass");
		eA.addAll(this.sU.searchForKeyInJava("password"));
		eA.addAll(this.sU.searchForKeyInJava("pwd"));
		eA.addAll(this.sU.searchForKeyInJava("username"));
		eA.addAll(this.sU.searchForKeyInJava("uname"));
		eA.addAll(this.sU.searchForKeyInJava("userID"));
		eA.addAll(this.sU.searchForKeyInJava("credential"));
		
		this.printE(this.removeDuplicatedSimpleEntries(eA));
	}
	
	/**
	 * Search for weak hashes
	 */
	private void weakCyphers() {
		System.out.println("\n");
		OutBut.printH2("Possible use of weak Ciphers/hashes");
		ArrayList<SimpleEntry> eA = this.sU.searchForKeyInJava("sha1");
		eA.addAll(this.sU.searchForKeyInJava("rc4"));
		eA.addAll(this.sU.searchForKeyInJava("md5"));
		
		this.printE(eA);
	}
	
	private void getStringsInCode(){
		System.out.println("\n");
		OutBut.printH2("Strings");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("\"");
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "[^:](\".+\")");
		this.printE(this.removeDuplicatedSimpleEntries(eResult));
	}
	
	
	/**
	 * Checks in a directory for the keywords of external storage
	 */
	
	public boolean isExternalStorage(){
		ArrayList<SimpleEntry> eArray = this.sU.searchForKeyInJava("getExternal");
		if (!this.sU.refineSearch(eArray, "(getExternalFilesDir)").isEmpty() || !this.sU.refineSearch(eArray, "getExternalStoragePublicDirectory").isEmpty())
			return true;
		
		return false;
	}
	
	/////////////////////////////////////// Utilitiles ///////////////////////////////////
	
//	public void searchInJavaCode(String key){
//		OutBut.printH1("Searching for "+key+" in the decompiled Java code");
//		ArrayList<SimpleEntry> searchResult = this.sU.searchForKeyInJava(key);
//		this.printE(searchResult);
//		if (!searchResult.isEmpty()){
//			OutBut.printNormal("\n\n");
//			OutBut.printStep("Where [Java_Code_Dir] is "+TicklerVars.jClassDir);
//		}
//	}
//	

	
//	/**
//	 * After searching for a key in files (which is faster), Refine the search by searching for a regex in the first search's result. 
//	 * @param eArray
//	 * @param regex
//	 * @return
//	 */
//	private ArrayList<SimpleEntry> refineSearch(ArrayList<SimpleEntry> eArray, String regex){
//		ArrayList<SimpleEntry> result = new ArrayList<>();
//		ArrayList<String> regexResult;
//		for (SimpleEntry e: eArray){
//			
//			regexResult = OtherUtil.getRegexFromString(e.getValue().toString(), regex);
//			if (!regexResult.isEmpty())
//				result.add(e);
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * USed with multiline comments disclosure for now.
//	 * 
//	 * @param eArray
//	 * @param regex
//	 * @return
//	 */
//	private ArrayList<SimpleEntry> refineSearchMatch(ArrayList<SimpleEntry> eArray, String regex){
//		ArrayList<SimpleEntry> result = new ArrayList<>();
//
//		for (SimpleEntry e: eArray){
//			
//		if (OtherUtil.isRegexInString(e.getValue().toString(), regex))
//				result.add(e);
//		}
//		
//		return result;
//	}
	
	/**
	 * Print squeeze output to stdout
	 * @param eArray
	 */
	private void printE(ArrayList<SimpleEntry> eArray){
		for (SimpleEntry e: eArray){
			String fileName=e.getKey().toString().replaceAll(TicklerVars.jClassDir, "[Java_Code_Dir]");
			System.out.println("#FileName: "+fileName);
			System.out.println(" "+e.getValue()+"\n");
		}
			
	}
	
	private ArrayList<SimpleEntry> removeDuplicatedSimpleEntries(ArrayList<SimpleEntry> orig) {
		return new ArrayList<SimpleEntry>(new LinkedHashSet<SimpleEntry>(orig));
	}
	
	
	

	
	
}
