package apk.newApks;

import java.io.IOException;

import base.FileUtil;
import exceptions.TNotFoundEx;
import initialization.TicklerConst;
import initialization.TicklerVars;

public class NougatMitM implements INewApk{

	private String newAppDir,netSecConfFilePath;
	private FileUtil fU;
	
	public NougatMitM() {
		this.newAppDir = TicklerVars.appTickDir+TicklerConst.newAppTempDir;
		this.netSecConfFilePath = newAppDir+"res/xml/"+TicklerConst.mitmXmlName;
		this.fU = new FileUtil();
	}
	
	@Override
	public String getNewApkName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void changeManifest() throws TNotFoundEx {
		try{
			String manString = this.fU.readFile(newAppDir+"AndroidManifest.xml");
			
			if (!manString.toLowerCase().contains("android:networkSecurityConfig"))
				manString = manString.replaceAll("<application ", "<application android:networkSecurityConfig=\"@xml/"+TicklerConst.mitmXmlName+"\" ");
			else{
				throw new TNotFoundEx("Manifest file already has a networkSecurityConfig entry, please check the configuration manually");
			}
			
			this.fU.writeFile(newAppDir+"AndroidManifest.xml", manString);
			this.createNetSecConf();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private void createNetSecConf(){
		String xmlFile="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+"<network-security-config>\n"
				+"<base-config>\n"
				+"<trust-anchors> \n"
				+"<certificates src=\"system\" />\n"
				+"  <certificates src=\"user\" />\n"
				+"</trust-anchors>\n"
				+"</base-config>\n"
				+"</network-security-config>";
		
		this.fU.writeFile(netSecConfFilePath, xmlFile);
		
	}
	
	
}
