package frida;

import java.util.ArrayList;

public class FridaUnpinSslContext extends FridaJsAction{

	public FridaUnpinSslContext(boolean reuseScript) {
		this.script = new FridaJsScript(FridaVars.SSL_CONTEXT_UNPIN_JS);
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.sslContextUnpin;
	}
	
	public void run(ArrayList<String> args){
		String finalCode = this.prepareCode(args);
		this.execute(finalCode);
	}
	
	private String prepareCode(ArrayList<String> args) {
		String certLoc = args.get(1);
		String finalCode = this.code.replaceAll("\\$mitmCert", certLoc);
		return finalCode;
	}
}
