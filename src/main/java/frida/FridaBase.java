package frida;

import java.util.ArrayList;

import apk.AppBroker;
import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class FridaBase {
	
	public FridaBase() {
		
	}
	
	public void fridaEnumerateClasses(boolean reuse){
		FridaEnumerateClasses enumClasses = new FridaEnumerateClasses(reuse);
		enumClasses.run();
	}
	
	public void fridaScript(ArrayList<String> args){
//		FridaPythonScript script = new FridaPythonScript(args);
//		script.execute();
		FridaJsScript script = new FridaJsScript(args.get(1));
		script.prepareCommandNoSpawning();
		OutBut.printNormal("\nPlease start the app before running this command\n");
		
		script.run();
	}
	
	public void fridaGetInputAndOutput(ArrayList<String> args, boolean reuse){
		FridaGetArgsAndReturn action = new FridaGetArgsAndReturn(reuse);
		action.run(args);
	}
	
	public void fridaSetValue(ArrayList<String> args, boolean reuse){
		FridaSetValue action = new FridaSetValue(reuse);
		action.run(args);
	}
	
	public void fridaUnpin(ArrayList<String> args, boolean reuse) {
		FridaUnpinSslContext action = new FridaUnpinSslContext(reuse);
		action.run(args);
	}
	
}
