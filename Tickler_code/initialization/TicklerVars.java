package initialization;

public abstract class TicklerVars {

//	public static String configFileName="Tickler.conf";
	public static String ticklerDir;
	//Default Tickler workspace directory in case Tickler.conf file is not found
//	public static String defaultTicklerDirName="Tickler_workspace/";
//	public static String sdCardPathInitial = "/sdcard/Tickler/";
//	public static int ALLCOMPS=0;
//	public static int ACTIVITY=1;
//	public static int SERVICE=2;
//	public static int PROVIDER=3;
//	public static int RECEIVER=4;
	public static String pkgName;
	public static boolean isLib;
	public static String jarPath, configPath;
	public static String appTickDir,sdCardPath, tickManifestFile,extractedDir,dataDir
	,smaliDir, imageDir, logDir, bgSnapshotsDir,transferDir, libDir, dex2jarPath, dex2jarDir,jClassDir,keyStore,newApkTempDir,mitmXmlName;
	public static String version="2";
		
	public static void setPkgName(String pName){
		if (TicklerVars.pkgName!=null && !TicklerVars.pkgName.equals(pName)){
			System.out.println("WARNING: Changing package name from "+TicklerVars.pkgName+" to "+pName);		
		}
		TicklerVars.pkgName = pName;
	}
	
	public static void updateVars(String pName){
		setPkgName(pName);
		appTickDir = ticklerDir+pkgName+"/";
		tickManifestFile = appTickDir+"AndroidManifest.xml";
		sdCardPath = TicklerConst.sdCardPathInitial+pkgName +"/";
		dataDir = appTickDir+"DataDir/";
		extractedDir = appTickDir+"extracted/";
		smaliDir = extractedDir+"smali/";
		imageDir= appTickDir+"images/";
		logDir = appTickDir+"logs/";
		bgSnapshotsDir = appTickDir+"bgSnapshots/";
		transferDir = appTickDir+"transfers/";
		libDir=jarPath+"Tickler_lib";
		// Will be set anyway whether they exist or not
		dex2jarPath = TicklerVars.libDir+"/dex2jar-2.1/d2j-dex2jar.sh";
		dex2jarDir = TicklerVars.appTickDir+"JavaCode/";
		// Output of Java classes
		jClassDir = TicklerVars.appTickDir+"JavaCode/Code";
		keyStore = libDir+"/Keystore/Tickler.keystore";
		newApkTempDir = appTickDir+TicklerConst.newAppTempDir;

	}
	
	public static void setTicklerDir(String dir){
		ticklerDir = dir;
	}
	
	public static String replaceSpace(String s){
		return s.replaceAll("\\s", "\\\\s");
	}

}
