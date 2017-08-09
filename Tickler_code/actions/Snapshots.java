package actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import base.FileUtil;
import cliGui.OutBut;
import commandExec.Commando;
import initialization.TicklerVars;

public class Snapshots {
	
	private Commando commando;
	private FileUtil fileTrans;
	
	public Snapshots() {
		
		this.commando = new Commando();
		this.fileTrans = new FileUtil();
	}

	public void takeSnapshot(){
		String timestamp = new SimpleDateFormat("dd.MM.yy_HH.mm.ss").format(new Date());
		String imgName = TicklerVars.pkgName+"_"+timestamp+".png";
		String path=TicklerVars.sdCardPath+imgName;
		
		//In case of no package
		this.fileTrans.createDirOnDevice(TicklerVars.sdCardPath);
		
		this.executeSnapshot(path);
		
		String imgDir=TicklerVars.imageDir;
		this.fileTrans.createDirOnHost(imgDir);
		this.fileTrans.pullFromSDcard(path, imgDir);
		
		if (new File(TicklerVars.imageDir+imgName).exists())
			OutBut.printStep("Screenshot taken succesfully and saved at "+TicklerVars.imageDir+imgName);
		
	}
	
	public void executeSnapshot(String path) {
		String command = "screencap -p "+path;
		this.commando.execADB(command);
	}
	
	public void getBackGroundSnapshots(){
		String path = "/data/system/recent_images";
		this.fileTrans.createDirOnHost(TicklerVars.bgSnapshotsDir);
		System.out.println("Copying Background snapshots to host...");
		this.fileTrans.copyDirToHost(path, TicklerVars.bgSnapshotsDir,false);
				
	}
	
}
