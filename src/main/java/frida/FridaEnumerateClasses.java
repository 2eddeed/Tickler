package frida;

import java.util.ArrayList;

import initialization.TicklerVars;

public class FridaEnumerateClasses extends FridaJsAction{

	
	public FridaEnumerateClasses(boolean reuseScript) {
		this.script = new FridaJsScript(FridaVars.ENUM_LOC);
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.ENUM_CODE;
	}
	
//	public void run(){
//		this.execute(this.code);
//	}
	
	public void execute(String code){
		this.script.writeCodeInScript(this.code);
		this.script.prepareCommandNoSpaining();
		this.script.run();
	}
	
//
//	private String prepareCode(ArrayList<String> args){
//		return this.code;
//	}
}

