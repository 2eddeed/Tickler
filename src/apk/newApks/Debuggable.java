package apk.newApks;

import java.io.IOException;

import base.FileUtil;
import initialization.TicklerConst;
import initialization.TicklerVars;

public class Debuggable implements INewApk {

	private String newAppDir;
	private FileUtil fU;
	
	public Debuggable(){
		this.newAppDir = TicklerVars.appTickDir+TicklerConst.newAppTempDir;
		this.fU = new FileUtil();
	}
	
	@Override
	public String getNewApkName() {
		return TicklerVars.appTickDir+TicklerConst.debuggableName;
		
	}

	@Override
	public void changeManifest() {
		
		try {
			String manString = this.fU.readFile(newAppDir+"AndroidManifest.xml");
			//DEbuggable
			if (manString.contains("android:debuggable"))
				manString = manString.replaceAll("debuggable=\"false\"", "debuggable=\"true\"");
			else
				manString = manString.replaceAll("<application ", "<application android:debuggable=\"true\" ");
			
			this.fU.writeFile(newAppDir+"AndroidManifest.xml", manString);
		}
		catch (IOException e)	{
			e.printStackTrace();
		}
		
		
	}

}
