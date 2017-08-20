package initialization;

public abstract class TicklerVars {

	public static String ticklerDir;
	public static String pkgName;
	public static boolean isLib;
	public static String jarPath, configPath;
	public static String appTickDir,sdCardPath, tickManifestFile,extractedDir,dataDir
	,smaliDir, imageDir, logDir, bgSnapshotsDir,transferDir, libDir, libJarDir, libNotJarLib, dex2jarPath, dex2jarDir,jClassDir,keyStore,newApkTempDir,mitmXmlName, fridaServerLoc;
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
		tickManifestFile = appTickDir+TicklerConst.MANIFEST_NAME;
		sdCardPath = TicklerConst.sdCardPathDefault+pkgName +"/";
		dataDir = appTickDir+TicklerConst.DATA_DIR_NAME;
		extractedDir = appTickDir+TicklerConst.EXTRACTED_NAME;
		smaliDir = extractedDir+TicklerConst.SMALI_DIR_NAME;
		imageDir= appTickDir+TicklerConst.IMAGES_DIR_NAME;
		logDir = appTickDir+TicklerConst.LOGS_DIR_NAME;
		bgSnapshotsDir = appTickDir+TicklerConst.BG_DIR_NAME;
		transferDir = appTickDir+TicklerConst.TRANSFER_DIR_NAME;
		
		libDir=jarPath+TicklerConst.generalLibName;
		libJarDir=libDir+TicklerConst.jarsLibName;
		libNotJarLib=libDir+TicklerConst.notJarsLibName;
		
		// Will be set anyway whether they exist or not
		
		dex2jarPath = libNotJarLib+TicklerConst.DEX2JAR_EXEC;
		dex2jarDir = appTickDir+TicklerConst.DEX2JAR_OP_DIR_NAME;
		// Output of Java classes
		jClassDir = appTickDir+TicklerConst.JAVA_CODE_DIR_NAME;
		keyStore = jarPath+TicklerConst.KEY_STORE_DIR_NAME;
		newApkTempDir = appTickDir+TicklerConst.newAppTempDir;
		
		fridaServerLoc=TicklerConst.FRIDA_SERVER_EXEC;
	}
	
	public static void setTicklerDir(String dir){
		ticklerDir = dir;
	}
	
	public static String replaceSpace(String s){
		return s.replaceAll("\\s", "\\\\s");
	}

}
