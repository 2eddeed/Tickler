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
public class FridaSetValue {

	private FridaJsScript script;
	private String code;
	
	public FridaSetValue(boolean reuseScript) {
		this.script = new FridaJsScript(FridaVars.SET_VALS_JS);
		reuseScript = false;
		
		if (reuseScript)
			this.code = this.script.getCodeFromScript();
		else
			this.code = FridaVars.SET_VALS_JS;
	}
	
	public void run(ArrayList<String> args){
		String finalCode = this.prepareCode(args);
		this.script.writeCodeInScript(finalCode);
		this.script.prepareCommand();
		this.script.run();
	
	}
	

	// args would be like: set, ClassName, MethodName, number_of_args, valNum, newValue  
	// myArgs : pkgName, ClassName, MethodName, methodArgs(arg0,1...etc) 
	private String prepareCode(ArrayList<String> args){
		String tempCode = this.code.replaceAll("\\$className", args.get(1)).replaceAll("\\$methodName", args.get(2));
		int numberOfArgs = new Integer(args.get(3));
		
		ArrayList<String> methodArgs = this.getMethodArguments(numberOfArgs); 
		
		String methodArguments="";
		String consoleLog = "";
		for (String s: methodArgs){
			methodArguments+=s+", ";
		}
		
		methodArguments = methodArguments.substring(0, methodArguments.length()-2);
		
		tempCode = tempCode.replaceAll("\\$args", methodArguments);
		
		int numberOfTarget = new Integer(args.get(4));
		String newValue = args.get(5);
		
		if (numberOfTarget>numberOfArgs) {
			//Modify return value
			tempCode = tempCode.replaceAll("\\$returnValue", newValue);
			tempCode = tempCode.replaceAll("\\$output_line", "console.log(\"Old return value: \"+orig_return.toString()+ \". New return value: \"+"+newValue+");");
		}
		else {
			//Modify an argument
			tempCode = tempCode.replaceAll("\\$returnValue", "orig_return");
			tempCode = tempCode.replaceAll("\\$output_line", "console.log(\"Arg number $inputNum: old value: \"+arg$inuputNum+ \". New value: \"+"+newValue+");");
		}
		
		tempCode = tempCode.replaceAll("\\$output_line", consoleLog);
		
		return tempCode;
	}
		
	
	private ArrayList<String> getMethodArguments(int num){
		ArrayList<String> methodArgs = new ArrayList<>();
		for (int i=0;i<num;i++){
			methodArgs.add("arg"+i);
		}
		return methodArgs;
	}
	
}
