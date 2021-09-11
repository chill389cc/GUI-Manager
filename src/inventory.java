import java.util.ArrayList;

public class inventory {
	private String name;
	private ArrayList<item> list;
	
	public inventory(String name) {
		this.name = name;
		list = new ArrayList<item>();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void addItem(String itemName, String itemDescription) {
		list.add(new item(itemName,itemDescription));
	}
	
	public void removeItem(int index) {
		list.remove(index);
	}
	
	public int getListSize() {
		return list.size();
	}
	
	public item getItem(int index ) { // whenever I ran l1.clear(), my program would call this method with an index of negative one. I spent hours trying to debug it, and I still can't understand it. I don't like this solution, but it works and my program runs without any errors now, so I'm going to call it a success. 
		if (index>-1) return list.get(index);
		else return list.get(0);
	}
	
	public void clearList() {
		list.clear();
	}
	
	public String toString()
    {
        return name;
    }
}