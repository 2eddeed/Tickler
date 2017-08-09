package components;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class Intent {
	List<Action> actions;
	List<Category> categories;
	List<DataUri> data;
	int priority;
	
	
	public List<Action> getAction() {
		return actions;
	}
	@XmlElement
	public void setAction(List<Action> action) {
		this.actions = action;
	}
	public List<Category> getCategory() {
		return categories;
	}
	@XmlElement
	public void setCategory(List<Category> category) {
		this.categories = category;
	}
	public List<DataUri> getData() {
		return data;
	}
	@XmlElement
	public void setData(List<DataUri> data) {
		this.data = data;
	}
	public int getPriority() {
		return priority;
	}
	@XmlAttribute
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String toString() {
		String toReturn=">>Intent: Data "+this.getData()+" Priority "+Integer.toString(this.getPriority())+"\n";
		for (Action a : this.actions)
		{
			toReturn+=a.toString();
		}
		for (Category c : this.categories)
		{
			toReturn+=c.toString();
		}
		return toReturn;
	}
	
}

