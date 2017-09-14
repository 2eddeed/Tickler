package frida;


import java.io.IOException;
import java.util.ArrayList;

import base.FileUtil;
import commandExec.Commando;
import initialization.TicklerVars;

/**
 * GEt input and output of a method, provided the following:
 * 1) the method is not overloaded: only one method of this name
 * 2) inputs and outputs are of primitive data types or String
 * @author aabolhadid
 *
 */
public class FridaJsScript {

	private String scriptPath;
	private String command ;
	private FileUtil fU;
	private Commando commando;
	public FridaJsScript(String path) {
		this.fU = new FileUtil();
		this.commando = new Commando();
		this.scriptPath=path;
	}
	
	public void run(){
		
//		commando.executeProcessListPrintOPError(command);	
		commando.executeProcessListPrintOP(command, true);
		
	}
	
	public void prepareCommand(){
		this.command = "frida -U -f "+TicklerVars.pkgName+" -l "+this.scriptPath+" --no-pause" ;
		
	}
	
	public void prepareCommandNoSpawning(){
		this.command = "frida -U "+TicklerVars.pkgName+" -l "+this.scriptPath+" --no-pause" ;
	}


	
	public void writeCodeInScript(String code){
		this.fU = new FileUtil();
		this.fU.writeFile(this.scriptPath, code);
	}
	
	public String getCodeFromScript(){
		String code ="";
		try {
			code = this.fU.readFile(this.scriptPath);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return code;
	}
	
	public ArrayList<String> getMethodArguments(int num){
		ArrayList<String> methodArgs = new ArrayList<>();
		for (int i=0;i<num;i++){
			methodArgs.add("arg"+i);
		}
		return methodArgs;
	}
}
