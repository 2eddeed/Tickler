package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.io.File;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

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
	private String codeRoot;
	
	public JavaSqueezer(){
		this.sU = new SearchUtil();
		this.codeRoot = TicklerVars.jClassDir;
	}
	
	public void report(String codeRoot){
		if (codeRoot != null){
			String codeRootNotHome=codeRoot.replace("~", System.getProperty("user.home"));
			File cR = new File(codeRootNotHome);
			if (cR.exists()){
				this.codeRoot = codeRootNotHome;
				this.report();
			}
		
			else
			{
				OutBut.printError("The code location you entered "+codeRoot+" does not exist");
			}
		}
		else {
			this.report();
		}
	}
	public void report(){
		//Components
		this.libsAndComponents();
		this.libFiles();
		//Storage
		this.storage();
		this.externalStorageInCode();
		//Communication
		this.httpUrls();
		this.pinning();
		//Crypto
		this.weakCyphers();
		this.crypto();
		//Disclosure
		this.commentsInCode();
		this.credentialsInCode();
		this.logInCode();
		this.testDisclosure();
//		this.getStringsInCode();
		
		OutBut.printStep("Where [Java_Code_Dir] is "+this.codeRoot);
	}
	
	
	//////////////////////////////// Storage //////////////////////////////////
	/**
	 * Search for any possible use of external storage
	 */
	public void externalStorageInCode(){
		OutBut.printH2("Possible External Storage");
		ArrayList<SimpleEntry> eArray = this.sU.searchForKeyInJava("getExternal",this.codeRoot);
		
		this.printE(eArray);
		
	}
	
	private void storage(){
		OutBut.printH2("Storage: DBs and shared preferences");
		String[] keys = {"sharedpref", "SQLiteDatabase", "CacheDir","AndroidKeyStore", "KeyStore"};
		ArrayList<SimpleEntry> eArray = this.returnFNameLineGroup(keys, false);
		this.printE(eArray);
	}
	
	
	/////////////////////// Components //////////////////////////
	
	private void libsAndComponents(){
		OutBut.printH2("Imports");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("import",this.codeRoot) ;
		ArrayList<SimpleEntry> e1 = this.sU.refineSearch(e,"(^import.+)") ;
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e1, "^(?:(?!(import\\sjava|import\\sandroid)).)*$");
		ArrayList<String> files=this.returnValues(eResult);
		
		
		for (String s:files)
			OutBut.printNormal(s);
		
		OutBut.printH2("Components and libraries");
		String[] comps = {"Webview", "Okhttp", "Sqlcipher"};
		ArrayList<SimpleEntry> hits = new ArrayList<>();
		 files = new ArrayList<>();
		
		for (String c:comps){
			hits=this.sU.searchForKeyInJava(c,this.codeRoot);
			if (!hits.isEmpty()){
				OutBut.printH3(c);
				files=this.returnFileNames(hits);
			
				for (String s:files)
					OutBut.printNormal(s);
				OutBut.printNormal("\n---------------------------------------------------------------");
			}
		}
		
	}
	
	private void libFiles(){
		String[] extenstions = {"so"};
		List<File> soLibs = this.sU.search4FileInDir(TicklerVars.extractedDir, extenstions);
		if (!soLibs.isEmpty()) {
			OutBut.printH2("Library files in the app");
			for (File f:soLibs ){
				OutBut.printNormal(f.getAbsolutePath().replaceAll(TicklerVars.extractedDir, "[Extracted_Apk]"));
			}
			OutBut.printNormal("\nWhere [Extracted_Apk] is "+TicklerVars.extractedDir);
		}
	}
	
	/////////////////////// Crypto ////////////////////////
		
	/**
	 * Search for weak hashes
	 */
	private void weakCyphers() {
		System.out.println("\n");
		String[] weakCrypto = {"Rot13", "MD4", "MD5", "RC2", "RC4", "SHA1"};
		OutBut.printH2("Possible use of weak Ciphers/hashes");
		
		ArrayList<SimpleEntry> eA = new ArrayList<>();
		
		for (String c : weakCrypto)
			eA.addAll(this.sU.searchForKeyInJava(c,this.codeRoot));
		
		this.printE(eA);
	}
	
	/**
	 * Use of cryptography 
	 */
	private void crypto(){
		OutBut.printH2("Crypto and hashing keywords");
		String[] keys = {"aes", "crypt", "cipher", "sha1", "sha2"};
		ArrayList<SimpleEntry> eA = this.returnFNameLineGroup(keys, false);
		
		this.printE(this.removeDuplicatedSimpleEntries(eA));
	}
	
	
	
	///////////////////////////// excluded /////////////////////////
	
	private void getStringsInCode(){
		System.out.println("\n");
		OutBut.printH2("Strings");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("\"",this.codeRoot);
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "[^:](\".+\")");
		
//		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "(\"\\w{32}\"|\"\\w{40}\"|\"\\w{56}\"|\"\\w{64}\")");
		this.printE(this.removeDuplicatedSimpleEntries(eResult));
	}
	
	private void getHashes(){
		System.out.println("\n");
		OutBut.printH2("Strings");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("\"",this.codeRoot);

		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "(\"\\w{32}\"|\"\\w{40}\"|\"\\w{56}\"|\"\\w{64}\")");
		this.printE(this.removeDuplicatedSimpleEntries(eResult));
	}
	
	
	
	///////////////////////////////// Communication ///////////////////////////////////
	
	private void httpUrls(){
		OutBut.printH2("URLs in code");
		this.getHttpUris();
	}
	
	public void getHttpUris(){
		ArrayList<SimpleEntry> hits = this.sU.searchForKeyInJava("http",this.codeRoot); 
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(hits, "http(s?)://(.+?)[\"'\\s]");

		this.printE(eResult);
		
		OutBut.printH3("HTTP URL summary:");
		ArrayList<String> uris = new ArrayList<>();
		for (SimpleEntry e: hits){
			uris.add(this.correctUrl((String)e.getValue()));
//			OutBut.printNormal(this.correctUrl((String)e.getValue()));
		}
		
		OtherUtil.printStringArray(OtherUtil.removeDuplicates(uris));
		
	}
	
	private void pinning(){
		OutBut.printH2("Pinning");
		ArrayList<SimpleEntry> hits = this.sU.searchForKeyInJava("certificatePinner",this.codeRoot);
		this.printE(hits);
	}
	
	
	private String correctUrl(String line) {
		String url="";
		Matcher m = Pattern.compile("http(s?)://(.+?)[\"'\\s]").matcher(line);
		if (m.find()) {
			url = line.substring(m.start(0),m.end(0)-1);
		}
		return url;
	}
	
	//////////////////////////// Disclosure ////////////////////////////////////
	
	

	/**
	* Search For all logcat commands In Code
	*/
	public void logInCode(){
		OutBut.printH2("Logging messages in logcat");
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("Log",this.codeRoot) ;
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, ".*Log\\.\\w\\((\\+?)");
		eResult.addAll(this.sU.refineSearch(e, ".*getLogger\\(\\)\\.(\\w)"));
		
		this.printE(eResult);
	
	}
	
	private void testDisclosure(){
		OutBut.printH2("Test");
		ArrayList<SimpleEntry> hits = this.sU.searchForKeyInJava("test",this.codeRoot);
		hits = this.sU.refineSearch(hits, "(.*(test|TEST).*)");
		this.printE(hits);
	}
	
	
	/**
	 *  stuff like // and maybe what's between /* and *\/ 
	 *  Regex for comment: // not proceeded by a : (not a URI), proceeded by any space then no digit (eliminate undecompiled code) 
	 */
	public void commentsInCode(){
		System.out.println("\n");
		OutBut.printH2("Comments");
		
		ArrayList<SimpleEntry> eComments = new ArrayList<>();
		ArrayList<SimpleEntry> e = this.sU.searchForKeyInJava("//",this.codeRoot);
		ArrayList<SimpleEntry> eResult = this.sU.refineSearch(e, "//(\\s*[a-zA-Z]+)");
		eResult= this.sU.refineSearch(eResult, "^(?!\\s*//\\s*from\\s*to\\s*target\\s*type)(.*$)");
		eResult= this.sU.refineSearch(eResult, "^(?!\\s*//\\s*Byte\\s*code:)(.*$)");
		eResult= this.sU.refineSearch(eResult, "^(?!\\s*//\\s*start\\s*length\\s*slot\\s*name\\s*signature)(.*$)");
		eResult= this.sU.refineSearch(eResult, "^(?!\\s*//\\s*Local\\s*variable\\s*table)(.*$)");
		eResult= this.sU.refineSearch(eResult, "^(?!\\s*//\\s*Exception\\s*table:)(.*$)");
	
		eComments.addAll(eResult);
		
		ArrayList<SimpleEntry> eM = this.sU.searchForKeyInJava("*",this.codeRoot);
		ArrayList<SimpleEntry> eResultMLine = this.sU.refineSearchMatch(eM, "\\s*/\\*.+");
		eResultMLine = this.sU.refineSearchMatch(eResultMLine, "^(?!\\s*/\\*\\s*Error\\s*\\*/)(.*$)");
//		ArrayList<SimpleEntry> eResultMLine2 = this.sU.refineSearchMatch(eM, "\\s*\\*.+");
		
		eComments.addAll(eResultMLine);
//		eComments.addAll(eResultMLine2);
		this.printE(this.removeDuplicatedSimpleEntries(eComments));
		
	}
	/**
	 * Search for anything that can disclose credentials:
	 * pass, password, username, userID, credentials
	 */
	public void credentialsInCode(){
		System.out.println("\n");
		OutBut.printH2("Possible credentials disclosure");
		
		String[] keys = {"pass","password", "pwd", "username", "user name", "userID", "credential", "admin"};
		ArrayList<SimpleEntry> eA = this.returnFNameLineGroup(keys, false);
		
		this.printE(this.removeDuplicatedSimpleEntries(eA));
	}

//////////////////////////////////////Utils /////////////////////////////////	
	
	/**
	 * Print squeeze output to stdout
	 * @param eArray
	 */
	private void printE(ArrayList<SimpleEntry> eArray){
		for (SimpleEntry e: eArray){
			String fileName=e.getKey().toString().replaceAll(this.codeRoot, "[Java_Code_Dir]");
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
			files.add(e.getKey().toString().replaceAll(this.codeRoot, "[Java_Code_Dir]"));
		 
		ArrayList<String> ret= new ArrayList<String>(new LinkedHashSet<String>(files));
		return ret;
	}
	
	private ArrayList<String> returnValues(ArrayList<SimpleEntry> hits){
		ArrayList<String> files = new ArrayList<>();
		for (SimpleEntry e:hits)
			files.add(e.getValue().toString());
		
		Collections.sort(files);
	 
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
			keyRes = this.sU.searchForKeyInJava(s,this.codeRoot);
			if (printName && (!keyRes.isEmpty()))
				OutBut.printH3(s);
			
			eA.addAll(this.sU.searchForKeyInJava(s,this.codeRoot));
		}
		
		return eA;
		
	}


	/**
	 * Checks in a directory for the keywords of external storage
	 */
	
	public boolean isExternalStorage(){
		ArrayList<SimpleEntry> eArray = this.sU.searchForKeyInJava("getExternal",this.codeRoot);
		if (!this.sU.refineSearch(eArray, "(getExternalFilesDir)").isEmpty() || !this.sU.refineSearch(eArray, "getExternalStoragePublicDirectory").isEmpty())
			return true;
		
		return false;
	}
	
	
}
