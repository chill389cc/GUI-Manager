import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.Writer;
import java.io.File;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InventoryManager implements ActionListener{
	JFrame f;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int[] bounds  = {(int)screenSize.getWidth(),(int)screenSize.getHeight()-40 /*-40 is to make the bottom above the windows taskbar*/ }; //change these values to change the screen dimensions.
	int[] offsets = {16,39};   //change these values to change the screen corner calibration for setting relative bounds
	int[] lBs     = {0,0};
	
	DefaultListModel<item> l1       = new DefaultListModel<>();
	JList<item>            list     = new JList<item>();
	File                   datafile = null;
	ArrayList<JButton>     buttons;
	ArrayList<inventory>   inventories;
	JButton                newInvButton, printButton, cinButton, cidButton, niButton, diButton, renameButton, ceButton, deleteButton, addItemButton, changeItemDescButton;
	JLabel                 inLabel,      listIDLabel, jtitle,    title1,    title2,   changeDT;
	JTextArea              idArea,       ta1,         input1,    input2,    input3;
	JDialog                ipanel,       panel2;
	JPanel                 panel;
	JScrollPane            scroll,       nItemScroll, changeIDB;
	int inventoryAmount        = 0;
	int selectedInventoryIndex = 0;
	int selectedItemIndex      = 0;
	boolean success;
	
	InventoryManager(){//sets up the GUI
		JFrame.setDefaultLookAndFeelDecorated(true);
		f            = new JFrame();
		list         = new JList<>(l1);
		panel        = new JPanel();
		scroll       = new JScrollPane(list);
		idArea       = new JTextArea("This is the Item Description Label");
		inLabel      = new JLabel("This is the Item Name Label");
		listIDLabel  = new JLabel("Current Inventory:");
		newInvButton = new JButton("New Inventory");
		printButton  = new JButton("Print Inventory");
		renameButton = new JButton("Rename Inventory");
		deleteButton = new JButton("Delete Inventory");
		cinButton    = new JButton("Change Item Name");
		cidButton    = new JButton("Change Item Description");
		niButton     = new JButton("Add Item");
		diButton     = new JButton("Delete Item");
		ceButton     = new JButton("Save & Exit");
		setCornerRelativeBounds(newInvButton, 0, 240, 40,  10,  10);
		setCornerRelativeBounds(listIDLabel,  0, 270, 30,  262, 00);
		setCornerRelativeBounds(inLabel,      1, 180, 30,  10,  210);
		setCornerRelativeBounds(idArea,       1, 180, 200, 10,  250);
		setCornerRelativeBounds(cinButton,    1, 180, 40,  10,  60);
		setCornerRelativeBounds(cidButton,    1, 180, 40,  10,  110);
		setCornerRelativeBounds(niButton,     1, 180, 40,  10,  10);
		setCornerRelativeBounds(diButton,     1, 180, 40,  10,  160);
		setCornerRelativeBounds(ceButton,     2, 240, 40,  10,  10);
		setCornerRelativeBounds(printButton,  3, 180, 40,  10,  10);
		setCornerRelativeBounds(renameButton, 3, 180, 40,  10,  60);
		setCornerRelativeBounds(deleteButton, 3, 180, 40,  10,  110);
		
		panel.add(idArea);
		panel.add(scroll);
		panel.add(inLabel);
		panel.add(listIDLabel);
		panel.add(cinButton);
		panel.add(cidButton);
		panel.add(niButton);
		panel.add(diButton);
		panel.add(ceButton);
		panel.add(newInvButton);
		panel.add(printButton);
		panel.add(renameButton);
		panel.add(deleteButton);
		
		f.setResizable(false);
	    removeMinMaxClose(f);
		scroll.setBounds(260,30,bounds[0]-offsets[0]-460,bounds[1]-offsets[1]-40);
		idArea.setWrapStyleWord(true);
		idArea.setLineWrap(true);
		panel.setSize(bounds[0], bounds[1]);
		panel.setLayout(null);
		panel.setVisible(true);
		readAllData();
		newItemPopup();
		changeItemDescriptionPopup();
		
		newInvButton. addActionListener(this);
		printButton.  addActionListener(this);
		renameButton. addActionListener(this);
		deleteButton. addActionListener(this);
		cinButton.    addActionListener(this);
		cidButton.    addActionListener(this);
		niButton.     addActionListener(this);
		diButton.     addActionListener(this);
		ceButton.     addActionListener(this);
		list.         addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
		        selectedItemIndex=list.getSelectedIndex();
		        inLabel.setText(inventories.get(selectedInventoryIndex).getItem(selectedItemIndex).getName());
		        idArea.setText(inventories.get(selectedInventoryIndex).getItem(selectedItemIndex).getDescription());
		    }
		});

		f.add(panel);
		setCorrectLocation();
		f.setLocation(lBs[0],lBs[1]);
		f.setSize(bounds[0], bounds[1]);
		f.setLayout(null);
		f.setVisible(true);
	}
	public void changeSelectedInventory(inventory inv) {
		selectedInventoryIndex=inventories.indexOf(inv);
		try {
			l1.clear();
		}catch (Exception e1) {System.out.println(e1);}	
		for (int m=0; m<inv.getListSize(); m++) {
			l1.add(m,inv.getItem(m));
		}
		listIDLabel.setText("Current Inventory: "+inv.getName());
	}
	public void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
	public void actionPerformed(ActionEvent e) {//the code that makes it work
		for (int i=0; i<inventoryAmount; i++) {
			if (e.getSource()==buttons.get(i)) {
				changeSelectedInventory(inventories.get(i));
			}
		}
		inventoryAmount=inventories.size();
		if (e.getSource()==cinButton) {
			String tempName = JOptionPane.showInputDialog(f,"What do you want the new items name to be?");
			inventories.get(selectedInventoryIndex).getItem(selectedItemIndex).setName(tempName);
			changeSelectedInventory(inventories.get(selectedInventoryIndex));
			inLabel.setText(tempName);
		}
		else if (e.getSource()==cidButton) {
			input3.setText(inventories.get(selectedInventoryIndex).getItem(selectedItemIndex).getDescription());
			panel2.setVisible(true);
		}
		else if (e.getSource()==changeItemDescButton) {
			panel2.setVisible(false);
			String tempDescription = input3.getText();
			inventories.get(selectedInventoryIndex).getItem(selectedItemIndex).setDescription(tempDescription);
			idArea.setText(tempDescription);
		}
		else if (e.getSource()==ceButton) {
			saveAllInventories();
			f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		}
		else if (e.getSource()==niButton ) {
			input1.setText("");
			input2.setText("");
			ipanel.setVisible(true);
		}
		else if (e.getSource()==addItemButton) {
			inventories.get(selectedInventoryIndex).addItem(input1.getText(),input2.getText());
			ipanel.setVisible(false);
			changeSelectedInventory(inventories.get(selectedInventoryIndex));
		}
		else if (e.getSource()==diButton) {
			if ("yes".equals(JOptionPane.showInputDialog(f,"Are you sure you want to delete this item? Type 'yes' to confirm.").toLowerCase())) {
				inventories.get(selectedInventoryIndex).removeItem(selectedItemIndex);
				changeSelectedInventory(inventories.get(selectedInventoryIndex));
			}
			else {
				JOptionPane.showMessageDialog(f, "Failed to delete selected item");
			}
		}
		else if (e.getSource()==newInvButton) {
			inventories.add(new inventory(JOptionPane.showInputDialog(f,"What do you want name your inventory?")));
			buttons.add(new JButton("button"+inventoryAmount));
			buttons.get(inventoryAmount).setBounds(10,60+50*inventoryAmount,240,40);
			buttons.get(inventoryAmount).addActionListener(this);
			buttons.get(inventoryAmount).setText(inventories.get(inventoryAmount).getName());
			panel.add(buttons.get(inventoryAmount));
			success = (new File("inventory"+inventoryAmount)).mkdirs(); //make a new inventory folder when you create a new inventory
			if (!success) {
			    // Directory creation failed
				System.out.println("Directory was not created");
			}
			inventoryAmount++;
			changeSelectedInventory(inventories.get(inventoryAmount-1));
		}
		else if (e.getSource()==renameButton) {
			String tempname = JOptionPane.showInputDialog(f,"What do you want the new name of "+inventories.get(selectedInventoryIndex).getName()+" to be?");
			inventories.get(selectedInventoryIndex).setName(tempname);
			listIDLabel.setText("Current Inventory: "+inventories.get(selectedInventoryIndex).getName());
			buttons.get(selectedInventoryIndex).setText(tempname);
		}
		else if (e.getSource()==printButton) {
			try {
				Writer w = new FileWriter("export.txt");
				w.write("Inventory name: "+inventories.get(selectedInventoryIndex).getName());
				System.out.println("Export this: "+inventories.get(selectedInventoryIndex).getName());
				w.write(System.getProperty("line.separator"));
				for (int n=0; n<inventories.get(selectedInventoryIndex).getListSize(); n++) {
					w.write(n+". "+inventories.get(selectedInventoryIndex).getItem(n).getName()+" : "+inventories.get(selectedInventoryIndex).getItem(n).getDescription());
					System.out.println(n+". "+inventories.get(selectedInventoryIndex).getItem(n).getName()+" : "+inventories.get(selectedInventoryIndex).getItem(n).getDescription());
					w.write(System.getProperty("line.separator"));
				}
				w.close();
			}catch (Exception e1) {System.out.println(e1);}	
			
			JOptionPane.showMessageDialog(f, "Inventory Successfully Exported "+inventories.get(selectedInventoryIndex).getName()+" to \'export.txt\'");
		}
		else if (e.getSource()==deleteButton) {
			if ("yes".equals(JOptionPane.showInputDialog(f,"Are you sure you want to delete this inventory? Type 'yes' to confirm.").toLowerCase())) {
				inventories.remove(selectedInventoryIndex);
				buttons.get(selectedInventoryIndex).setVisible(false);
				panel.remove(buttons.get(selectedInventoryIndex));
				inventoryAmount=inventories.size();
				deleteDir(new File("inventory"+selectedInventoryIndex));
				for (int i=selectedInventoryIndex; i<inventories.size(); i++) {
					File dir = new File("inventory"+(i+1));
					File newName = new File("inventory"+i);
	                if ( dir.isDirectory() ) {
	                        dir.renameTo(newName);
	                }
	                //TODO move all the buttons down when you delete an inventory
				}
				changeSelectedInventory(inventories.get(0));
				JOptionPane.showMessageDialog(f, "Inventory Successfully Deleted");
			}
			else {
				JOptionPane.showMessageDialog(f, "Failed to delete selected inventory");
			}
		}
	}
	public void newItemPopup() {
		ipanel = new JDialog();
		jtitle = new JLabel("Add an Item:");
		jtitle.setBounds(10,10,140,15);
		title1 = new JLabel("Item Name");
		title1.setBounds(10,30,140,15);
		input1 = new JTextArea("");
		input1.setBounds(10,50,140,20);
		title2 = new JLabel("Item Description");
		title2.setBounds(10,75,140,15);
		input2 = new JTextArea("");
		input2.setEditable(true);
		input2.setLineWrap(true);
		input2.setWrapStyleWord(true);
		nItemScroll= new JScrollPane(input2);
		nItemScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		nItemScroll.setBounds(10,95,140,40);
		addItemButton = new JButton("Add Item");
		addItemButton.setBounds(10,145,140,35);
		addItemButton.addActionListener(this);
		ipanel.add(jtitle);
		ipanel.add(title1);
		ipanel.add(input1);
		ipanel.add(title2);
		ipanel.add(nItemScroll);
		ipanel.add(addItemButton);
		ipanel.setSize(178,227);
		ipanel.setLocation(bounds[0]/2-ipanel.getWidth()/2,bounds[1]/2-ipanel.getHeight()/2);
		ipanel.setLayout(null);
	}
	public void changeItemDescriptionPopup() {
		panel2 = new JDialog();
		changeDT = new JLabel("Change Description:");
		changeDT.setBounds(10,10,140,15);
		input3 = new JTextArea("");
		input3.setEditable(true);
		input3.setLineWrap(true);
		input3.setWrapStyleWord(true);
		changeIDB= new JScrollPane(input3);
		changeIDB.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		changeIDB.setBounds(10,30,140,80);
		changeItemDescButton = new JButton("Change Description");
		changeItemDescButton.setBounds(10,145,140,35);
		changeItemDescButton.addActionListener(this);
		panel2.add(changeDT);
		panel2.add(changeIDB);
		panel2.add(changeItemDescButton);
		panel2.setSize(178,227);
		panel2.setLocation(bounds[0]/2-panel2.getWidth()/2,bounds[1]/2-panel2.getHeight()/2);
		panel2.setLayout(null);
	}
	public void setCorrectLocation() {//this function is not critical, and just relocates the window if I am working at home (I use dual monitors at home, and a single monitor at school). 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (screenSize.getHeight()==1920) { //basically, if I'm at home, set the window to here. This if for streamlined testing purposes, and would be removed for the final product. 
			lBs[0] = -1600;
			lBs[1] = 300;
		}
	}
	public void readAllData() {
        datafile     = new File("data.txt");
		if (!(datafile.isFile())) {  //makes sure the data file exists. 
			System.out.println("No data found, creating new file.");
			try { //if it doesn't, create the necessary data
				Writer w = new FileWriter("data.txt");
				w.write("0");
				w.close();
			}catch (Exception e1) {System.out.println(e1);}
		}
		else //if it does, set up the file structure based on the existing inventories
		{
			System.out.println("Data file exists, reading...");
			try { //first, read how many inventories there are. 
				Scanner reader = new Scanner(new File("data.txt"));
				int line = 0;
				while (reader.hasNext()){
					   String str = reader.nextLine();
					   switch (line) 
					   {
					   case 0:
						   inventoryAmount = Integer.parseInt(str);
						   break;
					   }
					   line++;
				}
				reader.close();
			}catch (Exception e1) {System.out.println(e1);}	
			buttons = new ArrayList<JButton>(inventoryAmount); //adds buttons depending on how many inventories you have, stored in data.txt
			inventories = new ArrayList<inventory>(inventoryAmount);
			for (int col=0; col<inventoryAmount; col++) { //for each inventory, do these things
				// so basically, I couldn't understand why you would use your method of making multiple JBUttons because then you have no way to access the JButtons. I don't know how you are going to be declaring an event listener for something that has no real name. So I did this. I hope it works
				//JButton buttonName = new JButton("button"+col); //first, create a button for them     ||       Yeah this just seems really weird to me. so its gone now. 
				buttons.add(new JButton("button"+col));
				buttons.get(col).setBounds(10,60+50*col,240,40);
				buttons.get(col).addActionListener(this);
				panel.add(buttons.get(col));
				
				try { //second, read the file structure for each one
					Scanner reader = new Scanner(new File("inventory"+col+"/name.txt"));
					int line = 0;
					int itemAmount=0;
					String tempName="name";
					while (reader.hasNext()){
						   String str = reader.nextLine();
						   switch (line)
						   {
						   case 0:
							   tempName=str;
							   buttons.get(col).setText(tempName);
							   break;
						   case 1:
							   itemAmount=Integer.parseInt(str);
							   break;
						   }
						   line++;
					}
					reader.close();
					inventories.add(new inventory(tempName)); //create the arraylist item for the inventory
					for (int row=0; row<itemAmount; row++) { //for each item in each inventory, do these things
						String tempItemName = "";
						String tempItemDesc = "";
						try {
							Scanner reader2 = new Scanner(new File("inventory"+col+"/item"+row+".txt"));
							int line2 = 0;
							
							while (reader2.hasNext()){
								   String str2 = reader2.nextLine();
								   switch (line2) 
								   {
								   case 0:
									   tempItemName=str2;
									   break;
								   default:
									   tempItemDesc=tempItemDesc+str2+"\n";
									   break;
								   }
								   line2++;
							}
							reader2.close();
						}catch (Exception e1) {System.out.println(e1);}						
						inventories.get(col).addItem(tempItemName,tempItemDesc);
					}
					
				}catch (Exception e1) {System.out.println(e1);}
			}
		}
	}
	public void saveAllInventories() {
		try { //save the data file
			Writer w = new FileWriter("data.txt"); 
			w.write(""+inventoryAmount);
			w.close();
			System.out.println("Saving "+inventoryAmount+" inventories");
		}catch (Exception e1) {System.out.println(e1);}	
		for (int i=0; i<inventoryAmount; i++) {
			int inventorySize = inventories.get(i).getListSize();
			try { //save the inventory data for each inventory
				Writer w = new FileWriter("inventory"+i+"/name.txt"); 
				w.write(""+inventories.get(i).getName());
				w.write(System.getProperty("line.separator"));
				w.write(""+inventorySize);
				System.out.println("Saving "+inventories.get(i).getName()+"'s items, of which there are "+inventorySize);
				w.close();
			}catch (Exception e1) {System.out.println(e1);}	
			for (int z=0; z<inventorySize; z++) {
				String tempname = inventories.get(i).getItem(z).getName();
				String tempdesc = inventories.get(i).getItem(z).getDescription();
				try { //save the item data for each item in each inventory
					Writer w = new FileWriter("inventory"+i+"/item"+z+".txt"); 
					w.write(""+tempname);
					w.write(System.getProperty("line.separator"));
					String lines[] = tempdesc.split("\\r?\\n");
					for (int b=0; b<lines.length; b++) {
						w.write(lines[b]);
						w.write(System.getProperty("line.separator"));
					}
					w.close();
				}catch (Exception e1) {System.out.println(e1);}	
			}
		}
		System.out.println("Everything Saved!");
	}
	public void setCornerRelativeBounds(Component comp, int corner, int x, int y, int lx, int ly) {
		comp.setSize(x,y);
		int farx=bounds[0]-comp.getWidth()-offsets[0]-lx;
		int fary=bounds[1]-comp.getHeight()-offsets[1]-ly;
		int[][] options = {{lx,ly},{farx,ly},{lx,fary},{farx,fary}};
		comp.setLocation(options[corner][0],options[corner][1]);
	}
	public void removeMinMaxClose(Component comp) {//I got this method online, it removes the X button so I can have my own X button. 
		if(comp instanceof AbstractButton)
		{
			comp.getParent().remove(comp);
		}
		if (comp instanceof Container)
		{
			Component[] comps = ((Container)comp).getComponents();
			for(int x = 0, y = comps.length; x < y; x++)
			{
				removeMinMaxClose(comps[x]);
			}
		}
	}
	public static void main(String[] args) {//needed to make the thing go
		new InventoryManager();
	}
}