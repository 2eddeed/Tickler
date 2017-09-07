package frida;

import base.FileUtil;
import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class FridaInit {
	
	private Commando commando;
	private FileUtil fU;
	
	public FridaInit()
	{
		this.commando = new Commando();
		this.fU = new FileUtil();
	}
	
	
	public void initFrida(){
		if (!(this.isFrida() && this.isFridaServer() && this.isPython())){
			OutBut.printError("Frida is not properly configured");
			System.exit(127);
		}
		
		this.fU.createDirOnHost(TicklerVars.fridaScriptsDir);
		
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

	public void startFridaServer(){
		String cmd = TicklerVars.fridaServerLoc+"&";
		commando.execRoot(cmd);
	}
	
	public void stopFridaServer(){
		String[] serverName = TicklerVars.fridaServerLoc.split("/");
		String serverPSOutput = commando.execRoot("ps | grep "+serverName[serverName.length -1]);
		if (!serverPSOutput.isEmpty()){
			String serverPs = serverPSOutput.split("\\s+")[1];
			commando.execRoot("kill -9 "+serverPs);
			try {
				Thread.sleep(5000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
	
	private void restartFridaServer() {
		this.stopFridaServer();
		this.startFridaServer();
	}
	
}
