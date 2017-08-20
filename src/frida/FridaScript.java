package frida;

import java.util.ArrayList;

import commandExec.Commando;

public class FridaScript {

	public String path;
	private Commando commando;
	
	public FridaScript(){
		this.commando = new Commando();
	}
	
	public ArrayList<String> executeScript(ArrayList<String> args){
		String command = "python3 "+this.path;
		if (args!= null){
			for (String s:args)
				command=command+" "+s;
		}
		
		ArrayList<String> op =commando.executeProcessString(command);
		return op;
	}
	
	public String getOutput(ArrayList<String> args){
		return this.executeScript(args).get(1);
	}
}
