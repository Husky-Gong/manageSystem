package manageSystem;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class systemUtils implements manageSystem{
	private static Hashtable<String, user> userTable = new Hashtable<>();
	private static Hashtable<String, item> itemTable = new Hashtable<>();
	private static Hashtable<String, Integer> shopCart = new Hashtable<>();
	private static String curUser;
	
	static {
		/*
		 * 1. Check 'users'/'items' folder -- create or not?
		 * 2. loop the 'users'/'items' folder and de-serialize all users/items from every user/item file
		 * 3. get every user/item object and store into the 'userTable'/'itemTable' (Hashtable.put(key, value))
		 */
		File file = new File("users");
		File file2 = new File("items");
		
		// Define a filter to filter file ".DS_Store"
		FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
            	if(pathname.getName().endsWith("DS_Store")) return false;
            	else return true;
            }
         };
     
		
		try{
			if(!file2.exists()) file2.mkdir();
			else {
				File fullItemsList[] = file2.listFiles(filter);
				for(File itemsFile: fullItemsList) {
					item eachItem = getObj(itemsFile.getAbsolutePath());
					itemTable.put(eachItem.getItemName(), eachItem);
				}
			}
			
			// initialize "user" folder and add "manager" into this repository.
			if(!file.exists()) {
				file.mkdir();
				user manager = new user(0, "manager", "abcdef", 30, true);
				userTable.put("manager", manager);
				writeObj(manager,new File(new File("users"),"manager"));
			}
			else {
				File fullFileList[] = file.listFiles(filter);
				for(File userFile: fullFileList) {
					user eachUser = getObj(userFile.getAbsolutePath());
					userTable.put(eachUser.getUserName(), eachUser);
				}
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private static <T> T getObj(String filePath) {
		try(FileInputStream fileIn = new FileInputStream(filePath);
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				){
			return (T) objIn.readObject();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * input a user object as one parameter
	 * check whether this user exists or not
	 * serialize the new user object into the 'users' folder
	 */
	@Override
	public void register() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		try {
			while(true) {
				System.out.println("Your new username:");
				String newName = input.next();
				if(userTable.containsKey(newName)) 
					System.out.println("This username exists! Please try again!");
				else {
					File finalPath = new File(new File("users"),newName);
					System.out.println("your Password:");
					String password = input.next();
					System.out.println("Your age:");
					int age = input.nextInt();
					user newUser = new user(userTable.size()+1,newName,password,age,false);
					userTable.put(newName,newUser);
					writeObj(newUser, finalPath);
					System.out.println("Regist successfully!");
					break;
				}
			}
		}
		catch(InputMismatchException | IOException e) {
			System.out.println("Invalid Input!Please try again!!");
		}
	}
	
	
	private static <T> void writeObj(T object, File finalPath) throws IOException {
		try(FileOutputStream fileOut = new FileOutputStream(finalPath);
				
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				){
			objOut.writeObject(object);
		}
	}
	
	
	/*
	 * Ask for user information
	 * (we can use the userTable to help us check user object)
	 * 1. check userName
	 * 2. check password
	 * 3. return true or false
	 */
	@Override
	@SuppressWarnings("resource")
	public boolean login() {
		try {
			Scanner input = new Scanner(System.in);
			System.out.println("Your username: ");
			String userName = input.next();
			System.out.println("Your password: ");
			String passWord = input.next();
			
			if(userTable.containsKey(userName) && userTable.get(userName).getPassWord().equals(passWord)) {
				System.out.println("Login successfully.");
				// save current user
				curUser = userName;
				return true;
			}
			else {
				System.out.println("Wrong userName or passWord!");
				return false;
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid input!");
			e.printStackTrace();
			return false;
		}
	}

	
	/*
	 * print items in shopping cart and 
	 * ask whether continue shopping or
	 * delete items
	 */
	@Override
	@SuppressWarnings("resource")
	public void shopCart() {
		try {
			while(true) {
				Scanner input = new Scanner(System.in);
				System.out.println("1 : Shop.\n2 : Cancel items.\n3. Manager Model.\n4. Home Page.\n5. Exit.\n******************\n");
				int choice = input.nextInt();
				if(choice == 1) buyItems(); 
				else if(choice == 2)cancelItems();
				else if(choice == 3)managerModel();
				else if(choice == 4){
					System.out.println("Back to home page...");
					return;
				}
				else if(choice == 5){
					System.out.println("Exit program.");
					System.exit(0);
				}
				else {
					System.out.println("Invalid input, please try again!");
				}
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid Input!");
		}
	}
	
	/*
	 * help manager add items or
	 * delete items
	 */
	@SuppressWarnings("resource")
	private void managerModel() {
		Scanner input = new Scanner(System.in);
		try {
			if(userTable.get(curUser).isManager()) {
				while(true) {
					printItemsTable();
					System.out.println("------Manage Model------\n1. Add items\n2. Delete items\n3. Go back.");
					int choice = input.nextInt();
					if(choice == 1) addItems();
					else if(choice == 2) deleteItems();
					else if(choice == 3) break;
				}
			}
			else{
				System.out.println("You are not a manager!");
			}
		}catch(InputMismatchException e) {
			System.out.println("Invalid Input, please try again!");
			return;
		}
		return;
	}
	
	@SuppressWarnings("resource")
	private void addItems() {
		try {
			// Ask for item information
			System.out.println("What items do you want to add? isert its name:");
			Scanner input = new Scanner(System.in);
			String itemName = input.next();
			System.out.println("Items you want to add:(number)");
			int amount = input.nextInt();
			System.out.println("Price you want to increase:");
			double price = input.nextDouble();
			
			File finalPath = new File(new File("items"),itemName);
			item newItem;
			
			// Add new generated item into item's folder 
			if(!itemTable.containsKey(itemName)) {
				newItem = new item(itemName, amount, price);
				}
			else {
				newItem = itemTable.get(itemName);
				newItem.setAmount(amount + newItem.getAmount());
				newItem.setPrice(price + newItem.getPrice());
			}
			itemTable.put(itemName, newItem);
			writeObj(newItem, finalPath);
		
			
		} catch (InputMismatchException | IOException e) {
			System.out.println("Invalid Input!");
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings("resource")
	private void deleteItems() {
		try {
			if(itemTable.isEmpty()) {
				System.out.println("No items in the list!");
				return;
			}
			// Ask for item information
			System.out.println("What items do you want to delete? isert its name:");
			Scanner input = new Scanner(System.in);
			String itemName = input.next();
			System.out.println("items you want to delete:(number)");
			int amount = input.nextInt();
			System.out.println("Price you want to decrease:");
			double price = input.nextDouble();
			
			File finalPath = new File(new File("items"),itemName);
			item newItem;
			
			// Add new generated item into item's folder 
			if(!itemTable.containsKey(itemName)) {
				System.out.println("No such Items!");
				return;
				}
			else {
				newItem = itemTable.get(itemName);
				newItem.setAmount(newItem.getAmount() - amount);
				newItem.setPrice(newItem.getPrice() - price);
			}
			writeObj(newItem, finalPath);
			if(newItem.getAmount() <= 0) {
				finalPath.delete();
				itemTable.remove(newItem.getItemName());
			}
			
		} catch (InputMismatchException | IOException e) {
			System.out.println("Invalid Input!!");
			e.printStackTrace();
		}
		
	}
	

	/*
	 * Ask for information to delete items in shopping cart
	 */
	@SuppressWarnings("resource")
	private void cancelItems() {
		try {
			if(shopCart.isEmpty()) {
				System.out.println("Nothing in your shopping cart, please add something!");
				return;
			}
			printShopCart();
			System.out.println("Which one you want to delete? input its name:");
			Scanner input = new Scanner(System.in);
			String itemName = input.next();
			System.out.println("How many you want to delete?");
			int amount = input.nextInt();
			
			// Compare item amount with the number user want to delete
			if(!shopCart.containsKey(itemName)) System.out.println("No such items!");
			else if(shopCart.containsKey(itemName) && shopCart.get(itemName) <= amount) System.out.println("Invalid amount!");
			else {
				updateTables(itemName,-amount);
				System.out.println("Items deleted successfully!");
			}
			printShopCart();
		}catch(InputMismatchException e) {
			System.out.println("Invalid Input!!");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * add items into shopping cart
	 */
	@SuppressWarnings("resource")
	private void buyItems() {
		try {
			if(itemTable.isEmpty()) {
				System.out.println("Nothing here!! Please contact manager to add items~!~!");
				return;
			}
			printItemsTable();
			System.out.println("Which one you want to buy? input its name:");
			Scanner input = new Scanner(System.in);
			String itemName = input.next();
			System.out.println("How many you want?");
			int amount = input.nextInt();
			
			// get total number of one item in shopping cart and 
			// check whether has enough items to sell
			int totalNum;
			if(shopCart.get(itemName)==null) 
				totalNum = 0 + amount;
			else 
				totalNum = shopCart.get(itemName) + amount;
	
			if(!itemTable.containsKey(itemName)) 
				System.out.println("No such item!");
			
			else if(itemTable.containsKey(itemName) && itemTable.get(itemName).getAmount() < totalNum) 
				System.out.println("No enough items!");
			else {
				updateTables(itemName, amount);
				System.out.println("Items added successfully!");
			}
			printShopCart();
		}catch(InputMismatchException e) {
			System.out.println("Invalid Input!!");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * input item name and update value(from delete function or add function)
	 * then update item Hash table
	 */
	private void updateTables(String itemName, int amount) {
		itemTable.get(itemName).setAmount(itemTable.get(itemName).getAmount() - amount);
		// if to buy things
		if(!shopCart.containsKey(itemName) && amount >= 0) 
			shopCart.put(itemName, amount);
		// if to cancel things
		else if(!shopCart.containsKey(itemName) && amount <= 0) 
			System.out.println("No such items!");
		else if(shopCart.containsKey(itemName) && shopCart.get(itemName)+amount <0) 
			System.out.println("Exceeds amount limit!");
		else if(shopCart.containsKey(itemName) && shopCart.get(itemName) + amount >= 0) 
			shopCart.put(itemName, shopCart.get(itemName)+amount);
		
		//Update file information
		try {
			writeObj(itemTable.get(itemName),new File(new File("items"),itemName));
		} catch (IOException e) {
			System.out.println("Error!");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Print all items in user's shopping cart
	 * Print total price
	 */
	private void printShopCart() {
		System.out.println("------Items in your shopping cart------");
		double inTotal = 0.0;
		for(Map.Entry<String, Integer> item: shopCart.entrySet()) {
			inTotal += itemTable.get(item.getKey()).getPrice() * item.getValue();
			System.out.println(item);
		}
		System.out.println("Total Price : " + inTotal);
	}
	
	
	private void printItemsTable() {
		System.out.println("------Items List------");
		for(Entry<String, item> entry:itemTable.entrySet()) {
			System.out.println(entry.getValue());
		}
		System.out.println("----------------------");
	}
}
