package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Receiver implements IComponent{
	
	boolean isExported;
	String name,permission,exp;
	List<Intent> intents;
	
	public boolean isExported() {
		return isExported;
	}
	@XmlAttribute(name="exported",namespace="http://schemas.android.com/apk/res/android")
	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}
	
	public String getExp() {
		return exp;
	}
	@XmlAttribute(name="exported",namespace="http://schemas.android.com/apk/res/android")
	public void setExp(String exp) {
		this.exp = exp;
	}
	
	public String getName() {
		return name;
	}
	@XmlAttribute(name="name",namespace="http://schemas.android.com/apk/res/android")
	public void setName(String name) {
		this.name = name;
	}
	public String getPermission() {
		return permission;
	}
	@XmlAttribute(name="permission",namespace="http://schemas.android.com/apk/res/android")
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public List<Intent> getIntent() {
		return intents;
	}
	@XmlElement(name="intent-filter")
	public void setIntent(List<Intent> intents) {
		this.intents = intents;
	}
	
	

}
