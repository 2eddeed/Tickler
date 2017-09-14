package attacks;

import java.util.ArrayList;

import components.Intent;
import components.Receiver;
import manifest.handlers.IntentHandler;

public class Broadcaster {

	String pkgName;
	Receiver rec;
	IntentHandler iHandler;
	
	public Broadcaster(String pkgName) {
		this.pkgName = pkgName;
	}

	public Broadcaster(String pkgName,Receiver rec) {
		this.pkgName = pkgName;
		this.setRec(rec);
	}
	
	public Receiver getRec() {
		return rec;
	}

	public void setRec(Receiver rec) {
		this.rec = rec;
	}
	
	public ArrayList<String> generateBroadcast(Receiver rec) {
		ArrayList<String> commands = new ArrayList<String>();
		String baseCommand = "am broadcast -n "+this.pkgName+"/"+rec.getName();
		commands.add(baseCommand);
		
		if (rec.getIntent() != null)
			for (Intent i : rec.getIntent()){
				iHandler = new IntentHandler(baseCommand,i);
				commands.addAll(iHandler.fullIntent());
			}
		return commands;
	}
	
}
