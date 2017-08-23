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
		this.libsAndComponents();
		this.logInCode();
		this.storage();
		this.externalStorageInCode();
		this.sqBackend();
		this.commentsInCode();
		this.credentialsInCode();
		this.weakCyphers();
		this.crypto();
		this.getStringsInCode();
	}
	
	///////////////////////////// Search in Code ////////////////////////7
	/**
	 * Search For all logcat commands In Code
	 */
	public void logInCode(){
		OutBut.printH2("Logging messages in logcat");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("Log") ;
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, ".*Log\\.\\w\\((\\+?)");
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
	
	private void storage(){
		OutBut.printH2("Storage: DBs and shared preferences");
		String[] keys = {"sharedpref", "SQLiteDatabase", "AndroidKeyStore", "KeyStore"};
		ArrayList<SimpleEntry> eArray = this.returnFNameLineGroup(keys, false);
		this.printE(eArray);
	}
	
	private void libsAndComponents(){
		OutBut.printH2("Imports");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("import") ;
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "^(?:(?!(import\\sjava|import\\sandroid)).)*$");
		ArrayList<String> files=this.returnValues(eResult);
		
		for (String s:files)
			OutBut.printNormal(s);
		
		OutBut.printH2("Components and libraries");
		String[] comps = {"Webview", "Okhttp", "Sqlcipher"};
		ArrayList<SimpleEntry> hits = new ArrayList<>();
		 files = new ArrayList<>();
		
		for (String c:comps){
			hits=this.sU.searchForKeyInJava(c);
			if (!hits.isEmpty()){
				OutBut.printH3(c);
				files=this.returnFileNames(hits);
			
				for (String s:files)
					OutBut.printNormal(s);
				OutBut.printNormal("\n---------------------------------------------------------------");
			}
		}
		
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
		
		String[] keys = {"pass","password", "pwd", "username", "uname", "userID", "credential", "admin"};
		ArrayList<SimpleEntry> eA = this.returnFNameLineGroup(keys, false);
		
		this.printE(this.removeDuplicatedSimpleEntries(eA));
	}
	
	/**
	 * Search for weak hashes
	 */
	private void weakCyphers() {
		System.out.println("\n");
		String[] weakCrypto = {"Rot13", "MD4", "MD5", "RC2", "RC4", "SHA1"};
		OutBut.printH2("Possible use of weak Ciphers/hashes");
		
		ArrayList<SimpleEntry> eA = new ArrayList<>();
		
		for (String c : weakCrypto)
			eA.addAll(this.sU.searchForKeyInJava(c));
		
		this.printE(eA);
	}
	
	/**
	 * Use of cryptography 
	 */
	private void crypto(){
		OutBut.printH2("Crypto");
		String[] keys = {"aes", "crypt", "cipher"};
		ArrayList<SimpleEntry> eA = this.returnFNameLineGroup(keys, false);
		
		this.printE(this.removeDuplicatedSimpleEntries(eA));
	}
	
	private void getStringsInCode(){
		System.out.println("\n");
		OutBut.printH2("Strings");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("\"");
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "[^:](\".+\")");
		
//		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "(\"\\w{32}\"|\"\\w{40}\"|\"\\w{56}\"|\"\\w{64}\")");
		this.printE(this.removeDuplicatedSimpleEntries(eResult));
	}
	
	private void getHashes(){
		System.out.println("\n");
		OutBut.printH2("Strings");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("\"");

		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "(\"\\w{32}\"|\"\\w{40}\"|\"\\w{56}\"|\"\\w{64}\")");
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
	
	
	///////////////////////////////// URIs ///////////////////////////////////
	
	private void sqBackend(){
		OutBut.printH2("URLs in code");
		this.getHttpUris();
	}
	
	public void getHttpUris(){
		ArrayList<SimpleEntry> hits = this.sU.searchForKeyInJava("http"); 
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(hits, "http(s?)://(.+?)[\"'\\s]");

		this.printE(eResult);
		
		
	}
	
	
	
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
	
	/**
	 * Get file names from a hits arraylist. Removes duplicates and sort
	 * @param hits
	 * @return
	 */
	private ArrayList<String> returnFileNames(ArrayList<SimpleEntry> hits){
		ArrayList<String> files = new ArrayList<>();
		for (SimpleEntry e:hits)
			files.add(e.getKey().toString().replaceAll(TicklerVars.jClassDir, "[Java_Code_Dir]"));
		 
		ArrayList<String> ret= new ArrayList<String>(new LinkedHashSet<String>(files));
		return ret;
	}
	
	private ArrayList<String> returnValues(ArrayList<SimpleEntry> hits){
		ArrayList<String> files = new ArrayList<>();
		for (SimpleEntry e:hits)
			files.add(e.getValue().toString());
	 
		ArrayList<String> ret= new ArrayList<String>(new LinkedHashSet<String>(files));
		return ret;
	}
	

	/**
	 * Searches for a group of Keys and print each occurrence and its file name. 
	 * @param keys
	 */
	private ArrayList<SimpleEntry> returnFNameLineGroup(String[] keys, boolean printName){
		ArrayList<SimpleEntry> eA = new ArrayList<>();
		ArrayList<SimpleEntry> keyRes = new ArrayList<>();
		
		for (String s: keys){
			keyRes = this.sU.searchForKeyInJava(s);
			if (printName && (!keyRes.isEmpty()))
				OutBut.printH3(s);
			
			eA.addAll(this.sU.searchForKeyInJava(s));
		}
		
		return eA;
		
	}
	
	
}
