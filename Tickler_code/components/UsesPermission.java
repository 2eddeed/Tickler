package components;

import javax.xml.bind.annotation.XmlAttribute;

public class UsesPermission {

	private String name;

	public String getName() {
		return name;
	}
	
	@XmlAttribute(name="name",namespace="http://schemas.android.com/apk/res/android")
	public void setName(String name) {
		this.name = name;
	}
	
	
}
