package cliGui;

public class OutBut {
	
	// Colors
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BOLD = "\u001B[1m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_BLUE = "\u001B[34m";


	public static void printH1(String line){
		String opLine = "\n=================================================================\n\t\t";
		opLine+=ANSI_BOLD+line+ANSI_RESET;
		opLine+="\n=================================================================\n";
		
		System.out.println(opLine);
	}
	
	public static void printH2(String line){
		System.out.println("\n===================== "+line +" =====================");
	}
	
	public static void printH3(String line){
		int length = line.length();
		String underline="";
		
		for (int i=0;i<length;i++)
			underline+="-";
		
		System.out.println(line+"\n"+underline);
	}
	
	public static void printStep(String line){
		System.out.println(">>>>>>>> "+line);
	}
	
	public static void printWarning(String line){
		System.out.println("!!!!!!!! WARNING: "+line);
	}
	
	public static void printError(String line){
		System.out.println(ANSI_RED+ANSI_BOLD+"XXXXX ERROR: "+line +" XXXXX"+ANSI_RESET);
	}
	
	/**
	 * Currently just prints as syso
	 * @param line
	 */
	public static void printNormal(String line){
		System.out.println(line);
	}
	
	public static void printH1Blue(String line){
		String opLine = ANSI_BLUE+"=============================================\n\t";
		opLine+=line;
		opLine+="\n============================================="+ANSI_RESET;
		
		System.out.println(opLine);
	}
}
