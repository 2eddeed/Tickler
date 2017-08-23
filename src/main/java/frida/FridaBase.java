package frida;

import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class FridaBase {
	
	private Commando commando;
	
	public FridaBase()
	{
		this.commando = new Commando();
	}
	public void initFrida(){
		if (!(this.isFrida() && this.isFridaServer() && this.isPython())){
			OutBut.printError("Frida is not properly configured");
			System.exit(127);
		}
		
		this.startFridaServer();
		
	}
	
	/**
	 * Frida exists on host
	 * @return
	 */
	private boolean isFrida(){
		String cmd = "frida -h";
		int ret = commando.executeProcessListPrintOP(cmd,false);
		
		if (ret != 0)
			return false;
		
		return true;
	}
	
	/**
	 * If Frida server exists on the device
	 * @return
	 */
	private boolean isFridaServer(){
		String cmd = TicklerVars.fridaServerLoc+" -h";
		String ret = commando.execADB(cmd);
		
		if (ret.toLowerCase().contains("--version"))
			return true;
		return false;
	}
	
	private boolean isPython(){
		String cmd = "python3 -h";
		int ret = commando.executeProcessListPrintOP(cmd,false);
		
		if (ret != 0)
			return false;
		
		return true;
	}

	private void startFridaServer(){
		String cmd = TicklerVars.fridaServerLoc+"&";
		commando.execRoot(cmd);
	}
}
