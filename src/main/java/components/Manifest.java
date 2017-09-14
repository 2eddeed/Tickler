package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="manifest")
public class Manifest {
	
	private List<Intent> intents;
	private List<UsesPermission> usesPermissions;
	private List<Permission> Permissions;
	private Application application;
	private String pkgName;

	public List<Intent> getIntents() {
		return intents;
	}

	@XmlElement(name="intent-filter")
	public void setIntents(List<Intent> intents) {
		this.intents = intents;
	}

	public List<UsesPermission> getUsesPermissions() {
		return usesPermissions;
	}
	@XmlElement(name="uses-permission")
	public void setUsesPermissions(List<UsesPermission> usesPermissions) {
		this.usesPermissions = usesPermissions;
	}

	public List<Permission> getPermissions() {
		return Permissions;
	}
	@XmlElement(name="permission")
	public void setPermissions(List<Permission> permissions) {
		Permissions = permissions;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
	public String getPkgName() {
		return pkgName;
	}
	@XmlAttribute(name="package")
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	
	
	

}
