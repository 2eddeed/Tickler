package frida;

import java.util.ArrayList;

import cliGui.OutBut;
import initialization.TicklerVars;

public class FridaEnumerateClasses extends FridaJsAction{

	
	public FridaEnumerateClasses(boolean reuseScript) {
		this.script = new FridaJsScript(FridaVars.ENUM_LOC);
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.ENUM_CODE;
	}
	
	public void run(){
		OutBut.printNormal("\nPlease start the app before running this command\n");
		this.executeNoSpawn(this.code);
	}
	
}

