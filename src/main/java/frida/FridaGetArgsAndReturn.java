package frida;


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
public class FridaGetArgsAndReturn extends FridaJsAction{

	
	public FridaGetArgsAndReturn(boolean reuseScript) {
		this.script = new FridaJsScript(FridaVars.GET_VALS_JS);
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.GET_VALS_CODE;
	}
	
	public void run(ArrayList<String> args){
		String finalCode = this.prepareCode(args);
		this.execute(finalCode);

	}
	

	private String prepareCode(ArrayList<String> args){
		String tempCode = this.code.replaceAll("\\$className", args.get(1)).replaceAll("\\$method_name", args.get(2));
		int numberOfArgs = new Integer(args.get(3));
		ArrayList<String> methodArgs = this.getMethodArguments(numberOfArgs);
		
		String methodArguments="";
		String consoleLogArgs = "";
		for (String s: methodArgs){
			methodArguments+=s+", ";
			consoleLogArgs+="console.log(\"Input: \"+"+s+");\n"; 
		}
		
		methodArguments = methodArguments.substring(0, methodArguments.length()-2);
		
		tempCode = tempCode.replaceAll("\\$args", methodArguments);
		tempCode = tempCode.replaceAll("\\$console_log_inputs", consoleLogArgs);
		
		return tempCode;
	}
}
