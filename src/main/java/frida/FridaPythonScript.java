package frida;

import java.util.ArrayList;

import base.OtherUtil;
import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class FridaPythonScript {

	public String path;
	private Commando commando;
	private ArrayList<String> args;
	
	public FridaPythonScript(){
		this.commando = new Commando();
	}
	
	public FridaPythonScript(ArrayList<String>args){
		this();
		this.prepareArgs(args);
			
	}
	
	private void prepareArgs(ArrayList<String>args){
		String origPath = args.get(1);
		String scriptPath = OtherUtil.getAbsolutePath(origPath);
		if (scriptPath == null){
			OutBut.printError("Path of the script: "+origPath+" does not exist");
			System.exit(127);
		}
		else{
			this.setPath(scriptPath);
//			this.args = new ArrayList)args.subList(1, args.size());
			this.args = new ArrayList<>( args.subList(2, args.size()));
			this.args.add(0, TicklerVars.pkgName);
		}
	}
	
	
	public void execute(){
		String command = "python3 "+this.path;
		if (this.args!= null){
			for (String s:this.args)
				command=command+" "+s;
		}

		commando.executePythonScript(command);
	}
	
	public void execute(ArrayList<String> args){
		this.args = args;
		this.execute();
	}
	
	public ArrayList<String> executeReturnOutput(ArrayList<String> args){
		String command = "python3 "+this.path;
		if (args!= null){
			for (String s:args)
				command=command+" "+s;
		}
		
		commando.executeProcessListPrintOP(command, true);
		return new ArrayList<String>();
	}
	
	public String getOutput(ArrayList<String> args){
		return this.executeReturnOutput(args).get(1);
	}
	
	public void setPath(String path){
		String scriptPath = OtherUtil.getAbsolutePath(path);
		if (scriptPath == null){
			OutBut.printError("Path of the script: "+path+" does not exist");
			System.exit(127);
		}
		else
			this.path = scriptPath; 
	}
	
}
