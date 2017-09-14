package components;

import javax.xml.bind.annotation.XmlAttribute;

public class Permission {

	String name,protectionLevel;

	public String getName() {
		return name;
	}
	@XmlAttribute(name="name",namespace="http://schemas.android.com/apk/res/android")
	public void setName(String name) {
		this.name = name;
	}

	public String getProtectionLevel() {
		return protectionLevel;
	}
	@XmlAttribute(name="protectionLevel",namespace="http://schemas.android.com/apk/res/android")
	public void setProtectionLevel(String protectionLevel) {
		this.protectionLevel = protectionLevel;
	}
	
	
}
