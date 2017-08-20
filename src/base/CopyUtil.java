package base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import commandExec.Commando;
import initialization.TicklerVars;

public class CopyUtil {

	private FileUtil fileTrans;
	
	public CopyUtil() {
		this.fileTrans = new FileUtil();
	}
	
/////////////////////////////////// Copy Data ////////////////////////////////////////////////
	/**
	* Copies DAta directory from the device, replaces any space in any file or dir name with __
	*/
	public void copyDataDir() {
		this.copyDataDir(TicklerVars.dataDir);
	}
	
	public void copyDataDir(String dest){
		String src = "/data/data/"+TicklerVars.pkgName;
		this.fileTrans.warnOverrideAndDelete(dest);
		System.out.println("\n!!! NOTE: Space in Files' and Directories' names are replaced by two underscores __ !!!\n");
		this.fileTrans.copyDirToHost(src, dest,false);
		
		//Escape space in file names
		FileUtil ft = new FileUtil();
		ft.escapeSpaceInDir(new File(dest));
	
	}
	
	public void copyDataDirName(String name){
		String dest;
		if (name==null){
			dest = TicklerVars.dataDir;
		}
		else
		{
			dest =TicklerVars.transferDir+name;
			FileUtil fU = new FileUtil();
			fU.createDirOnHost(TicklerVars.transferDir);
		}
		
		this.copyDataDir(dest);
	
	}
	
	// Copy any file or directory from the device to the host
	//Create a new directory for each transfer
	public void copyToHost(String src, String dest){
	
		String timestamp = new SimpleDateFormat("dd-MM-yy_HH.mm.ss").format(new Date());
		String srcName =  fileTrans.getFileNameFromPath(src);
		String dstDirName;
		
		if (dest == null){
			dstDirName = srcName+"_"+timestamp;
		}
		else {
			dstDirName = dest;
		}
		
		String destDir = TicklerVars.transferDir+dstDirName;
		
		this.fileTrans.copyDirToHost(src, destDir,false);
		
		//Check
		if (new File(destDir).exists())
		System.out.println(src+" has been copied successfully to "+destDir);
	}


	
}
