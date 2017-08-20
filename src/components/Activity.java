package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Activity implements IComponent,IActivityService{

	String name, exp, permission;
	boolean isExported;
	List<Intent> intents;

	public String getName() {
		return name;
	}
	@XmlAttribute(name="name",namespace="http://schemas.android.com/apk/res/android")
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name="exported",namespace="http://schemas.android.com/apk/res/android")
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getExp() {
		return exp;
	}
	
	public boolean isExported() {
		return isExported;
	}

	public void setExported(boolean exported) {
		this.isExported = exported;
	}
	public List<Intent> getIntent() {
		return intents;
	}
	@XmlElement(name="intent-filter")
	public void setIntent(List<Intent> intents) {
		this.intents = intents;
	}
	public String getPermission() {
		return permission;
	}
	@XmlElement(name="permission")
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	
}
