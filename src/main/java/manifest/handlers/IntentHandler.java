package manifest.handlers;

import java.util.ArrayList;

import components.Action;
import components.Category;
import components.DataUri;
import components.Intent;

public class IntentHandler {
	private String command;
	private Intent intent;
	private ArrayList<String> commands,additionalCommands;
	
	public IntentHandler(String origCommand,Intent intent) {
		this.command = origCommand;
		this.intent = intent;
		this.commands = new ArrayList<String>();
		this.additionalCommands = new ArrayList<String>();
	}
	
	/**
	 * Gets all combinations of Intent actions,and DAta URI
	 * For now: the categories are excluded
	 * @return
	 */
	public ArrayList<String> fullIntent() {
		
		this.commands.add(this.command);
		this.addActions();
		this.addData();
		
		return this.commands;
	}
	
	private void addActions() {
		if (this.intent.getAction()!=null) {
			for (Action a : this.intent.getAction()) 
				this.updateCommandsList(" -a "+a.getName());
			this.commands.addAll(this.additionalCommands);
			this.additionalCommands.clear();
		}
	}
	
	
	private void addData(){
		if (this.intent.getData() != null)
		{
			DataUriHandler dH = new DataUriHandler(this.intent);
			dH.doIt();
			ArrayList<DataUri> totalDU = dH.getTotalDU();
			for (DataUri d : totalDU){
				this.updateCommandsList(dH.getStartCommand(d));
			}
			this.commands.addAll(this.additionalCommands);
			this.additionalCommands.clear();
		}
	}
	
	private void updateCommandsList(String s) {
		for (String str:this.commands){
			this.additionalCommands.add(str+s);
		}
	}


}
