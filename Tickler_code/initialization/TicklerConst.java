package initialization;

public class TicklerConst {

	public static String configFileName="Tickler.conf";
	public static String ticklerDir;
	public static String defaultTicklerDirName="Tickler_workspace/";
	public static String sdCardPathInitial = "/sdcard/Tickler/";
	public static int ALLCOMPS=0;
	public static int ACTIVITY=1;
	public static int SERVICE=2;
	public static int PROVIDER=3;
	public static int RECEIVER=4;	
	public static String version="1.0";
	public static final int debuggable=0;
	public static final int mitm=1;
	public static String mitmApkName = "mitm.apk";
	public static String debuggableName = "debuggable.apk";
	public static String newAppTempDir = "newAppTemp/";
	public static String mitmXmlName="tickler_network_security_config";
	public static String[] compNames ={"All components","Activities", "Services", "Content Providers",
	"Broadcast Receivers"};
	
	
	public static String helpMessage="\n	usage: Tickler	\n"
	 +"        ==============\n"
	 +"	Please make sure that the configuration file: Tickler.conf is at the same directory as\n"
	 +"	the Tickler .jar file. Tickler.conf file defines Tickler's directory on the host and the\n"
	 +"	temp directory on the android device.\n\n"
	 +"	Options\n"
	 +"	---------\n"
	 +"	-h, --help			Prints this message\n"
	 +"        -screen				Take a screenshot of the device\n"
	 +"        -findPkg <key>			Search for a package name	\n"
	 +"        -version			Print version	\n"
	 +"	-pkgs				List all installed packages on the android device	\n"
	 +"	-pkg				Specify package name of the app		\n"
	 +"		\n"
	 +"	Options of pkg:	\n"
	 +"	--------------	\n"
	 +"	-info				List information about the app	\n"
	 +"	-squeeze			All strings, log functions, possible credentials in decompiled APK	\n"
	 +"	-dbg, --debuggable		Create a debuggable version of the app	\n"
	 +"	-dataDir [name]			Copies data directory of app to Tickler Directory (DataDir or transfers/name)	\n"
	 +"	-diff				Copies app's data directory before and after a user's action, then diffs between them\n"
	 +" 	-diff 	[d| detailed]		like diff, but also shows the changes in case a text file or an unecrypted database is changed\n"
	 +"	-db [option] [-nu]		Database checks. By default app's Data Dir is copied to host before checks\n"
	 +" 		[-nu | --noUpdate]	Does not update DataDir on host before checks \n"
	 +" 		[option] can be:\n"
	 +"			[ |e|encryption]	Checks whether the database files of the app are encrypted or not\n"
	 +"			[l |list]		Lists Databases of the app \n"
	 +"			[d |dump]		Dumps an unencrypted database\n"
	 +" 	-sc, --searchCode <key>		Search for \"key\" in the app's decompiled Java code\n"
	 +" 	-sd, --searchDataDir <key>		Search for \"key\" in the app's Data Directory\n"	
	 +"	-t [target] [-log]	Tickels a target. The type of the attack depends on the target	\n"
	 +" 	   -log					Captures logcat messages \n"
	 +"	-l,--list [target] [-v]		List components of type target	\n"
	 + "	   -v 					List component(s) in details\n"
	 +"		\n"
	 +"		\n"
	 +"	Targets:	\n"
	 +"	--------	\n"
	 +"	-act,--activities		Activities	\n"
	 +"	-ser,--services			Services	\n"
	 +"	-prov,--providers		Content providers	\n"
	 +"	-rec,--receivers		Broadcast receivers	\n"
	 +"	[ | -comp] <name>		A specific component, its name has to be exactly as in Manifest file (also displayed by -l -all)	\n"
	 +"	-exp				Applies [trigger | list] action to exported components only	\n"
	 +"	[ | -all]			All components (default if none of the above targets are specified)	\n"
	 +"		\n"
	
	 + "	Options that work with and without -pkg option\n"
	 + "	-----------------------------------------------\n"
	 + " 	-screen				Captures a screenshot of the device\n"
	 + " 	-cp2host <source> [destName]	Copies any file/directory to the tickler's app directoy on the host\n"
	 + " 	-bg,--bgSnapshots		Copies background screenshots that are saved on the device\n\n"
	 +"	Examples:	\n"
	 +"	---------	\n"
	 +"	1) List all components of package com.test.package, with detailed information	\n"
	 +"	-----------------------------------------------------------------------------	\n"
	 +"	java -jar Tickler.jar -pkg com.test.package -l -v	\n"
	 +"		\n"
	 +"	2) Trigger exported activities of package com.test.package	\n"
	 +"	-----------------------------------------------------------	\n"
	 +"	java -jar Tickler.jar -pkg com.test.package -t -act -exp	\n\n"
	 +"	3) Squeeze app's decompiled Java code	for strings, log messages...etc\n"
	 +"	-----------------------------------------------------------------------	\n"
	 +"	java -jar Tickler.jar -pkg com.test.package -squeeze > output.txt	\n"
	 ;
	

	

}
