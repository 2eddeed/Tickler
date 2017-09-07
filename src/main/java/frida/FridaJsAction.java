package frida;

import java.util.ArrayList;

public class FridaJsAction {
	protected FridaJsScript script;
	protected String code;
	
	public void execute(String finalCode){
		this.script.writeCodeInScript(finalCode);
		this.script.prepareCommand();
		this.script.run();
	
		
	}
	
	public void executeNoSpawn(String finalCode){
		this.script.writeCodeInScript(finalCode);
		this.script.prepareCommandNoSpawning();
		this.script.run();
	
		
	}
	
	protected ArrayList<String> getMethodArguments(int num){
		ArrayList<String> methodArgs = new ArrayList<>();
		for (int i=0;i<num;i++){
			methodArgs.add("arg"+i);
		}
		return methodArgs;
	}

}
