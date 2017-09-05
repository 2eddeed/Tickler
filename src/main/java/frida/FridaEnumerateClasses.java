package frida;

import java.util.ArrayList;

import initialization.TicklerVars;

public class FridaEnumerateClasses {

	private FridaPythonScript script;
	private ArrayList<String> output;
	
	public FridaEnumerateClasses(){
		this.script = new FridaPythonScript();
		this.output = new ArrayList<>();
		this.script.setPath(FridaVars.ENUM_LOC);
	}
	
	public void run(){
		ArrayList<String> input = new ArrayList<>();
		input.add(TicklerVars.pkgName);
		this.output = this.script.executeReturnOutput(input);
	}
	
	public ArrayList<String> getOutput(){
		return this.output;
	}
}
