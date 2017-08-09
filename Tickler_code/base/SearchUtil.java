package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class SearchUtil {
		
	/**
	 * Search for a key in a directory
	 * @param path
	 * @param key
	 * @return ArrayList<String> All lines containing this key
	 */
	public ArrayList<String> search4KeyInDir(String path, String key){
		List<File> files = this.search4FileInDir(path, null);
		ArrayList<String> hits = new ArrayList<String>();
		
		for (File f : files){
			ArrayList<String> results = this.findInFile(f, key);
			hits.addAll(results);
		}
		
		return hits;
	}
	
	
	
	/**
	 * Searches for a key in a directory and returns the hits and their file names
	 * @param path
	 * @param key
	 * @return ArrayList<SimpleEntry> of all lines containing this key and the file name of each hit
	 */
	public ArrayList<SimpleEntry> search4KeyInDirFName(String path,String key){
		ArrayList<SimpleEntry> hits = new ArrayList<>();
		List<File> files = this.search4FileInDir(path, null);
		for (File f : files){
			ArrayList<String> results = this.findInFile(f, key);
			//Print file path instead of just the name (better results for obfuscated code)
			String fName = f.getAbsolutePath();
			for (String s:results){
				SimpleEntry e = new SimpleEntry<String, String>(fName, s);
				hits.add(e);
			}
		}
		
		return hits;
	}
	
	/**
	 * Excluding com/google trash
	 * @param key
	 * @param loc
	 */
	
	public ArrayList<SimpleEntry> searchForKeyInJava(String key){
		
		ArrayList<SimpleEntry> hits = new ArrayList<>();
		List<File> files = this.search4FileInDir(TicklerVars.jClassDir, null);
		for (File f : files){
			String fName = f.getAbsolutePath();
			if (!fName.contains("com/google")){
				ArrayList<String> results = this.findInFile(f, key);
				for (String s:results){
					SimpleEntry e = new SimpleEntry<String, String>(fName, s);
					hits.add(e);
				}
			}
		}
		
		return hits;
		
//		for (SimpleEntry e: hits){
//			if (!e.getKey().toString().contains("com/google")) {
//				String filePath = e.getKey().toString().replaceAll(TicklerVars.jClassDir, "[Java_Code_Dir]"); 
//				System.out.println("#FileName: "+filePath);
//				System.out.println(" "+e.getValue()+"\n");
//			}
//		}
//		
//		OutBut.printStep("Where [Java_Code_Dir] is "+TicklerVars.jClassDir);
	}
	
	
	
	/**
	 * After searching for a key in files (which is faster), Refine the search by searching for a regex in the first search's result. 
	 * @param eArray
	 * @param regex
	 * @return
	 */
	public ArrayList<SimpleEntry> refineSearch(ArrayList<SimpleEntry> eArray, String regex){
		ArrayList<SimpleEntry> result = new ArrayList<>();
		ArrayList<String> regexResult;
		for (SimpleEntry e: eArray){
			
			regexResult = OtherUtil.getRegexFromString(e.getValue().toString(), regex);
			if (!regexResult.isEmpty())
				result.add(e);
		}
		
		return result;
	}
	
	/**
	 * USed with multiline comments disclosure for now.
	 * 
	 * @param eArray
	 * @param regex
	 * @return
	 */
	public ArrayList<SimpleEntry> refineSearchMatch(ArrayList<SimpleEntry> eArray, String regex){
		ArrayList<SimpleEntry> result = new ArrayList<>();

		for (SimpleEntry e: eArray){
			
		if (OtherUtil.isRegexInString(e.getValue().toString(), regex))
				result.add(e);
		}
		
		return result;
	}
	

	/**
	* Search for extensions in a directory (or list all files in directory)
	* @param path
	* @param key filter by extension types
	* @return
	*/
	public List<File> search4FileInDir(String path, String[] key){
		File dir = new File(path);
		List<File> files;
		if (key != null){
			files = (List<File>)FileUtils.listFiles(dir, key, true);
			
		}
		else {
			files = (List<File>)FileUtils.listFiles(dir, null, true);
		}
		return files;
		
	}
	
	/**
	 * Search for a file on the host
	 * @param path
	 * @param key
	 * @return
	 */
	public String searchOnDevice(String path, String key){
		String command = "find "+path+ " -name "+key;
		Commando commando = new Commando();
		String result = commando.execRoot(command);
		
		return result;
	}
	
	/**
	* Search for a key in a file (without regex)
	* @param f
	* @param key
	* @return
	*/
	
	public ArrayList<String> findInFile(File f, String key){
		return this.searchInFile(f, key, false);
	}
	
	/**
	 * Search for a regex in a file
	 * @param f
	 * @param regex
	 * @return
	 */
	public ArrayList<String> findRegexInFile(File f, String regex){
		return this.searchInFile(f, regex,true);
	}
	
	
	/**
	* Searches for a key in a file, whether it is a normal string or a regex
	* The search is CASE INSENSITIVE
	* The main function for findInFile and findRegexInFile
	* @param f
	* @param key
	* @param regex true: search for regex, false: search for a String
	* @return
	*/
	private ArrayList<String> searchInFile(File f, String key,boolean regex){
		ArrayList<String> results = new ArrayList<String>();
		String line;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while ((line =reader.readLine())!= null) {
				if (this.checkLineAndRegex(line,key,regex))
					results.add(line);
				
			}
			reader.close();
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return results;
	}
	
	/**
	* Searches for a string in a line (CASE INSENSITIVE), or for a regex in a line
	* Considered the main check of findInFile and FindRegexInFile
	* @param line
	* @param key
	* @param regex
	* @return
	*/
	private boolean checkLineAndRegex(String line, String key, boolean regex){
		boolean result;
		//Search with regex
		if (regex){
			Matcher m = Pattern.compile(key).matcher(line);
			result = m.matches();
		}
		//Normal contains()
		else
		{
			result = line.toLowerCase().contains(key.toLowerCase());
		}
		
		return result;
	}
	
	



}
