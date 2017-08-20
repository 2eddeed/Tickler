package apk.newApks;

import java.io.File;

import apk.ApkSigner;
import apk.ApkToolClass;
import apk.AppBroker;
import base.FileUtil;
import cliGui.OutBut;
import exceptions.TNotFoundEx;
import initialization.TicklerVars;
import initialization.TicklerConst;

/**
 * Creates new APK versions of the original APK
 * MITM: a modification to network secrutiy configuration of the app in order to accept user added certificates to trust store
 * @author aabolhadid
 *
 */
public class CreateApk {
	private ApkToolClass apktool;
	private FileUtil ft;
	private ApkSigner signer;
	private String newAppDir,netSecConfFileName,netSecConfFilePath,apk;
	private boolean isMitm;
	private INewApk newApk;
	
	public CreateApk(int apkID){
		this.apktool = new ApkToolClass();
		this.ft = new FileUtil(); 
		this.signer = new ApkSigner();
		this.newAppDir = TicklerVars.appTickDir+TicklerConst.newAppTempDir;
		this.netSecConfFileName = TicklerConst.mitmXmlName; 
		this.netSecConfFilePath = newAppDir+"res/xml/"+netSecConfFileName;	
		
		this.initNewApp(apkID);
		
		
	}
	
	private void initNewApp(int apkID){
		
		switch(apkID){
		case TicklerConst.debuggable:
			newApk = new Debuggable();
			break;
			
		case TicklerConst.mitm:
			newApk = new NougatMitM();
			break;
			
		}
		
	}
	
	/**
	 * Create a new APK, debuggable or mitm compatible
	 * @param isInstall whether to install the apk after creation 
	 */
	public void createNewApk(){
		boolean successfulSign;
		try {
			OutBut.printWarning("Decompiling and Recompiling of the APK might have some errors, which might lead to incorrect behavior of the modified app");
			this.apk = this.newApk.getNewApkName();
			
			this.ft.copyOnHost(TicklerVars.extractedDir, TicklerVars.newApkTempDir, true);
			this.newApk.changeManifest();
			
			this.apktool.apkToolCompile(this.newAppDir,this.apk);
			this.afterCompilation();
			successfulSign =this.signer.signApk(this.apk);
			if (successfulSign)
				this.reinstallNewApk();
		}
		catch(TNotFoundEx e){
			OutBut.printError(e.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void afterCompilation() {
		this.ft.deleteFromHost(TicklerVars.newApkTempDir);
		
		if ( this.ft.isExist(this.apk)){
			System.out.println("\n\n");
			OutBut.printStep("App is created successfully at "+this.apk+" \n");
		}
	}

	private void reinstallNewApk(){
		AppBroker broker = new AppBroker(TicklerVars.pkgName);
		broker.reinstall(this.apk);
	}
	
	
}
