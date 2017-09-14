package components;

import java.util.List;

public interface IComponent {

	public boolean isExported();
	public void setExported(boolean isExported);
	public List<Intent> getIntent();
	public String getName();
	public String getExp();
	public String getPermission();

}
