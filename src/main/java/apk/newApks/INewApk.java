package apk.newApks;

import exceptions.TNotFoundEx;

public interface INewApk {
	
	public String getNewApkName();
	public void changeManifest() throws TNotFoundEx ;

}
