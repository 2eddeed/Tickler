package frida;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FridaCli {

	private FridaBase base;
	private FridaInit init;
	
	public FridaCli(){
		this.base = new FridaBase();
		this.init = new FridaInit();
		
		this.init.initFrida();
	}
	
	/**
	 * arg0: Name of function
	 * arg1- : args of the function
	 * @param args
	 */
	public void fridaThis(String [] args, boolean reuse){
		String functionName = args[0];
		ArrayList<String> scriptArgs = new ArrayList<>(Arrays.asList(args)); 
		
		switch(functionName){
		case "enum":
			this.base.fridaEnumerateClasses();
			break;
			
		case "script":
			this.base.fridaScript(scriptArgs);
		
		
		case "vals":
			this.base.fridaGetInputAndOutput(scriptArgs, reuse);
			break;
			
		case "set":
			this.base.fridaSetValue(scriptArgs, reuse);
			break;
			
		case "unpin":
			this.base.fridaUnpin(scriptArgs, reuse);
			break;
		
		}
		
		
	}
}
