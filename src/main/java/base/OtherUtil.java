package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cliGui.OutBut;
import initialization.TicklerVars;

/**
 * Mainly regex operations now
 * @author aabolhadid
 *
 */
public class OtherUtil {
	/**
	 * Gets all instances that match a regex in a string
	 * @param s
	 * @param regex
	 * @return
	 */
	public static ArrayList<String> getRegexFromString(String s, String regex){
		
		ArrayList<String> result = new ArrayList<>();
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while(m.find()){			
				result.add(m.group(1));
		}
		
		return result;
	}
	
	/**
	 * Gets all instances that match a regex in a string
	 * @param s
	 * @param regex
	 * @return
	 */
	public static boolean isRegexInString(String s, String regex){
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if (m.matches())			
				return true;
		
		return false;
	}
	
	public static ArrayList<String> removeDuplicates(ArrayList<String> orig){
		return new ArrayList<String>(new LinkedHashSet<String>(orig));
		
	}
	
	public static String pressAnykey(){
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Press Enter to continue, or enter snapshot to take a snapshot ........");
		
		return OtherUtil.readInput();
	}
	
	public static String pressAnyKeySilent(){
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		return OtherUtil.readInput();
	}
	
	private static String readInput(){
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		
		try {
			key = is.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}
	
	/**
	 * As in different functions in Searcher
	 * @param hits	ARraylist of SimpleEntry, obtained after searching for a key
	 * @param toBeReplaced	Replace the long path name, such as TicklerVars.jClassDir
	 * @param replacement	Replacement such as [Data_Dir]
	 */
	public static void printSimpleEntryArray(ArrayList<SimpleEntry> hits, String toBeReplaced, String replacement) {
		if (!hits.isEmpty()){
			OutBut.printNormal(hits.size()+" Search Results are found :\n===============================\n");
			for (SimpleEntry e: hits){
				String filePath = e.getKey().toString().replaceAll(toBeReplaced, replacement); 
				System.out.println("#FileName: "+filePath);
				System.out.println(" "+e.getValue()+"\n");
			}
			
			OutBut.printStep("Where "+replacement+" is "+toBeReplaced);
		}
	}
}
