package cliGui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;

import base.OtherUtil;
import base.SearchUtil;
import base.Tickler;
import frida.FridaBase;
import frida.FridaEnumerateClasses;
import frida.FridaScript;
import initialization.TicklerConst;
import initialization.TicklerVars;


public class TicklerCLI {
	private Options options;
	private CommandLineParser parser;

	public static void main(String[] args) {
		
		TicklerCLI cli = new TicklerCLI();
//		cli.test();
		
		cli.parser = new BasicParser();
		cli.options = new Options();
		
		Option findPkg = new Option("findPkg",null, true, "Search for a package name");
		Option listPack = new Option("pkgs",null, false,"List all installed packages on this device");
	
		Option pack = new Option("pkg",null, true, "Name of an installed package");
		Option trigger = new Option("t","trigger",false,"Start components");
		Option all = new Option("all",null,false,"Start all components");
		Option exp = new Option("exp",null,false,"Apply only to Exported components");
		Option attackComp = new Option("comp",null, true, "Attack a specific component. The name should be as in the manifest file");
		Option help = new Option("h", "help", false, "Print this help menu");
		Option activities = new Option("act","activities",false,"Attack all activities");
		Option services = new Option("ser","services",false,"Attack all services");
		Option providers = new Option("prov","providers",false,"Attack all content providers");
		Option receivers = new Option("rec","receivers",false,"Attack all broadcast receivers");
		Option list = new Option("l","list",false,"List components");
		Option info = new Option("i","info",false,"List information about the app");
		Option snap = new Option("screen","screenshot",false,"takes a snapshot of the device's screen");
		Option searchInCode = new Option("sc","searchCode",true,"Search for a specific key in Java code");
		Option searchInCodeAll = new Option("sc_all",null,true,"Search for a specific key in Java code");
		Option searchInSandbox = new Option("sd","searchDataDir",true, "Search for a key in the Data Dir of an app (Files and DB)");
		Option log = new Option("log",null,false,"capture LogCat messages");
		Option details = new Option("v",null,false,"List in details");
		Option bgSnap = new Option("bg","backGroundSnapshots",false,"Get Background images from the device");
		Option debuggable = new Option("dbg","debuggable",false,"Create a debuggable APK");
		Option decompile = new Option("decomp",null,false, "decompile apk into java code");
		Option squeeze = new Option("squeeze",null,false, "Look for info disclosure in code");
		Option noDevice = new Option("nodev",null,false, "Work without a connected device");
		Option version = new Option("version",null,false, "Print version");
		Option noCopy = new Option("nu","noUpdate",false, "Doesn't update DataDir before execution (with db and big dataDir)");
		Option mitm = new Option("mitm",null,false, "Allows user CA in Android 7");
		
		Option copy2host = OptionBuilder.create("cp2host");
		copy2host.setOptionalArg(true);
		copy2host.setArgs(2);
		
		Option diffDir = OptionBuilder.create("diff");
		diffDir.setOptionalArg(true);
		diffDir.setArgs(2);
		
		Option db = OptionBuilder.create("db");
		db.setOptionalArg(true);
		db.setArgs(1);
		
		Option dataDir = OptionBuilder.create("dataDir");
		dataDir.setOptionalArg(true);
		dataDir.setArgs(1);
		
		cli.options.addOption(pack);
		cli.options.addOption(trigger);
		cli.options.addOption(all);
		cli.options.addOption(exp);
		cli.options.addOption(attackComp);
		cli.options.addOption(diffDir);
		cli.options.addOption(db);
		cli.options.addOption(help);
		cli.options.addOption(activities);
		cli.options.addOption(services);
		cli.options.addOption(providers);
		cli.options.addOption(receivers);
		cli.options.addOption(list);
		cli.options.addOption(info);
		cli.options.addOption(findPkg);
		cli.options.addOption(listPack);
		cli.options.addOption(snap);
		cli.options.addOption(searchInCode);
		cli.options.addOption(searchInCodeAll);
		cli.options.addOption(searchInSandbox);
		cli.options.addOption(log);
		cli.options.addOption(bgSnap);
		cli.options.addOption(debuggable);
		cli.options.addOption(copy2host);
		cli.options.addOption(details);
		cli.options.addOption(dataDir);
		cli.options.addOption(decompile);
		cli.options.addOption(squeeze);
		cli.options.addOption(noDevice);
		cli.options.addOption(version);
		cli.options.addOption(noCopy);
		cli.options.addOption(mitm);
		
		try {
			CommandLine cl = cli.parser.parse(cli.options, args,false);
			
			cli.startTickler(cl);
			
		}
		catch(UnrecognizedOptionException e){
			OutBut.printError("Unrecognized option "+e.getOption());
			System.out.println(TicklerConst.helpMessage);
		}
		catch(MissingArgumentException e){
			OutBut.printError("Missing parameter of option "+e.getOption().getOpt());
			System.out.println(TicklerConst.helpMessage);
		}

		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void test(){
		SearchUtil sU = new SearchUtil();
		String s= "Log.d(\"Successful Login:\", \", account=\" + DoLogin.this.username + \":\" + DoLogin.this.password);";
		
		String regex = ".*Log\\.\\w\\((\\+?)";
		ArrayList<String> result = new ArrayList<>();
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while(m.find()){			
//				result.add(m.group(1));
			System.out.println("Got it !!!!");
		}
		
		
//		regexResult = OtherUtil.getRegexFromString(e.getValue().toString(), regex);
//		if (!regexResult.isEmpty())
		
//		String appName = "de.qbo.qbo.dev";
//		TicklerVars.updateVars(appName);
//		FridaBase fB = new FridaBase();
//		fB.initFrida();
//		
//		FridaEnumerateClasses enu = new FridaEnumerateClasses(); 
//		System.out.println(enu.getOutput(null));
	}
	
	public void startTickler(CommandLine cl){
		boolean exported = false;
		boolean details = false;		
		int target = TicklerConst.ALLCOMPS;
		
		if (cl.hasOption("h") || cl.hasOption("help"))
		{
			System.out.println(TicklerConst.helpMessage);

		}
		
		if (cl.hasOption("findPkg")){
			Tickler t = new Tickler("search", "search");
			t.searchPackage(cl.getOptionValue("findPkg"));
		}
		if (cl.hasOption("pkgs")){
			Tickler t = new Tickler("search", "search");
			t.printPackages();
		}
		

		
		////////////////////////////////// Pkg /////////////////////////////
		if (cl.hasOption("pkg")) {
			
			Tickler t = new Tickler("pkg", cl.getOptionValue("pkg"));
			
			if (cl.hasOption("exp"))
				exported = true;
			
			//Logcat
			if (cl.hasOption("log")){
				t.setLog(true);
			}
			else if (cl.hasOption("mitm")){
				t.createMitM();;
			}
			else if (cl.hasOption("debuggable")){
				t.createDebuggable();;
			}
			
			
			///// Targets
			
			
			List<String> args = cl.getArgList();
			
			if (cl.hasOption("act"))
				target =TicklerConst.ACTIVITY;
			
			else if (cl.hasOption("ser"))
				target = TicklerConst.SERVICE;
			
			else if (cl.hasOption("prov"))
				target = TicklerConst.PROVIDER;
			
			else if (cl.hasOption("rec"))
				target = TicklerConst.RECEIVER;
						
			
			////////// Actions /////////////
			//Trigger
			if (cl.hasOption("trigger") || cl.hasOption("t") ){
				//Trigger a component
				if (cl.hasOption("comp")){
					t.attackComponent(cl.getOptionValue("comp")); 
				}
				//Component name is added without -comp flag
				else if (!args.isEmpty()){
					t.attackComponent(args.get(0));
				}
				else {
					t.start(target, exported);
				}	
			}
			////List components 
			else if (cl.hasOption("l")||cl.hasOption("list")){
				if (cl.hasOption("v")){
					details=true;
				}
				//List a specific component
				if (cl.hasOption("comp")){
					t.listComponent(cl.getOptionValue("comp"));
				}
				// a component name without -comp flag
				else if (!args.isEmpty()){
					t.listComponent(args.get(0));
				}
				else
					t.list(target,exported,details);
				
			}
//				t.listType(target,exported);
			
			//Search in Code
			else if (cl.hasOption("sc") || cl.hasOption("searchCode"))
				t.searchInCode(cl.getOptionValue("sc"));
			
			else if (cl.hasOption("sc_all") )
				t.searchInCodeAll(cl.getOptionValue("sc_all"));
			
			// Search for a key in DataDir files and Unencrypted Databases 
			else if (cl.hasOption("sd"))
				t.searchInDataDir(cl.getOptionValue("sd"));
			
			
			//Diff data directory before and after an operation
			else if (cl.hasOption("diff")) 
				t.diffDataDir(cl.getOptionValue("diff"));
			
			//Check encryption of Databases
			else if (cl.hasOption("db")){
				boolean isCopy=true;
				if (cl.hasOption("noUpdate") || cl.hasOption("nu"))
					isCopy = false;
				
				t.databases(cl.getOptionValue("db"),isCopy);
//			
			}
			
			//Information
			else if (cl.hasOption("info"))
				t.informationGathering();
			
			//Disclosure
			else if (cl.hasOption("squeeze"))
				t.squeezeCode();
			
			else if (cl.hasOption("dataDir")){
				String dest = cl.getOptionValue("dataDir");
				t.copyDataDir(dest);
			}
				
			
			else if (cl.hasOption("decomp"))
				t.decompileApk();
			
			
		}
		// Snapshot, with or without package name
		if(cl.hasOption("screen")){		
			Tickler t = new Tickler("snapshot", "snapshot");
			t.snapshot();
		}
		
		// Background screenshots
		if (cl.hasOption("bg")){
			Tickler t = new Tickler("snapshot", "snapshot");
			t.backgroundSnapshots();
		}
		
		if (cl.hasOption("cp2host")){
			Tickler t = new Tickler("snapshot","shanpshot");
			String[] args = cl.getOptionValues("cp2host");
			String src = args[0];
			String dest;
			if (args.length >1){
				dest = args[1];
			}
			else {
				dest = null;
			}
			
			t.copyToHost(src, dest);
		}
		
		//Version
		if (cl.hasOption("version")){
			Tickler t = new Tickler("version","version");
			t.version();
		}
	}
	
	public void executeLogcat(String command, File logFile){
		Process process;
		try {
			process=Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader( process.getInputStream()));
			PrintWriter writer = new PrintWriter(logFile);
			String line = "";			
			while ((line = reader.readLine())!= null) {
				writer.println("line");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	

}
