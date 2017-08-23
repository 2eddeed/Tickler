package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class Provider implements IComponent{
	
	String name,permission,authorities;
	boolean isExported;
	List<Intent> intent;
	
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
	public String getAuthorities() {
		return authorities;
	}
	@XmlAttribute(name="authorities",namespace="http://schemas.android.com/apk/res/android")
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	public boolean isExported() {
		return isExported;
	}
	@XmlAttribute(name="exported",namespace="http://schemas.android.com/apk/res/android")
	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}
	public List<Intent> getIntent() {
		return intent;
	}
	public void setIntent(List<Intent> intent) {
		this.intent = intent;
	}
	
	public String getExp() {
		return null;
	}
	
	
	

}
