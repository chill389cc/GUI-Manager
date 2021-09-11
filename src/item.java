public class item {
	private String name;
	private String description;
	
	public item(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String toString() //this is what is displayed in the JList
    {
        return name;
    }
}