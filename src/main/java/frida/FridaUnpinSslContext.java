package frida;

import java.util.ArrayList;

import base.FileUtil;
import commandExec.Commando;

public class FridaUnpinSslContext extends FridaJsAction{
	private FileUtil fU;

	public FridaUnpinSslContext(boolean reuseScript) {
		this.fU = new FileUtil();
		
		this.script = new FridaJsScript(FridaVars.SSL_CONTEXT_UNPIN_JS);
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.sslContextUnpin;
	}
	
	public void run(ArrayList<String> args){
		String finalCode = this.prepareCode(args);
		this.executeNoSpawn(finalCode);
	}
	
	private String prepareCode(ArrayList<String> args) {
		Commando commando = new Commando();
		String certLoc = args.get(1);
		String certName= this.fU.getFileNameFromPath(certLoc);
		String certOnDevice="/data/local/tmp/"+certName;
		this.fU.copyToDevice(certLoc, certOnDevice);
		String finalCode = this.code.replaceAll("\\$mitmCert", certOnDevice);
		commando.execRoot("chmod 666 "+certOnDevice);
		return finalCode;
	}
}
