package frida;

import java.util.ArrayList;

import initialization.TicklerVars;

public class FridaEnumerateClasses extends FridaScript{

	
	public FridaEnumerateClasses(){
		super();
		this.path="/home/aabolhadid/workspace/TicklerFrida/fridaScripts/enumerate_classes.py";
	}
	
	public ArrayList<String> executeScript(ArrayList<String> args){
		ArrayList<String> input = new ArrayList<>();
		input.add(TicklerVars.pkgName);
		return super.executeScript(input);
	}
}
