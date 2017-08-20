package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Service implements IComponent,IActivityService{
	String name,exp, permission;
	boolean isExported;
	List<Intent> intent;
	
	public Service() {
		this.isExported = false;
	}
	
	public String getName() {
		return name;
	}
	@XmlAttribute(name="name",namespace="http://schemas.android.com/apk/res/android")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExp() {
		return exp;
	}
	@XmlAttribute(name="exported",namespace="http://schemas.android.com/apk/res/android")
	public void setExp(String exp) {
		this.exp = exp;
	}

	public boolean isExported() {
		return isExported;
	}
	
	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}
	
	public List<Intent> getIntent() {
		return intent;
	}
	@XmlElement(name="intent-filter")
	public void setIntent(List<Intent> intent) {
		this.intent = intent;
	}
	
	public String getPermission() {
		return permission;
	}
	@XmlElement(name="permission")
	public void setPermission(String permission) {
		this.permission = permission;
	}

}
