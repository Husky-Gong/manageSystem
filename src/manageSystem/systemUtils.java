package manageSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class systemUtils implements manageSystem{

	private static Hashtable<String, user> userTable = new Hashtable<>();
	private static Hashtable<String, item> itemTable = new Hashtable<>();
	private static Hashtable<String, Integer> shopCart = new Hashtable<>();
	
	static {
		/*
		 * 1. Check 'users'/'items' folder -- create or not?
		 * 2. loop the 'users'/'items' folder and deserialize all users/items from every user/item file
		 * 3. get every user/item object and store into the 'userTable'/'itemTable' (Hashtable.put(key, value))
		 */
		File file = new File("users");
		File file2 = new File("items");
		
		if(!file.exists()) file.mkdir();
		if(!file2.exists()) file2.mkdir();
		
		try{
			File fullItemsList[] = file2.listFiles();
			for(File itemsFile: fullItemsList) {
				item eachItem = getObj(itemsFile.getAbsolutePath());
				itemTable.put(eachItem.getItemName(), eachItem);
			}
			
			File fullFileList[] = file.listFiles();
			for(File userFile: fullFileList) {
				user eachUser = getObj(userFile.getAbsolutePath());
				userTable.put(eachUser.getUserName(), eachUser);
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
	
	private static void writeUserObj(user newUser, File finalPath) throws IOException {
		try(FileOutputStream fileOut = new FileOutputStream(finalPath);
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				){
			objOut.writeObject(newUser);
		}
	}
	
	/*
	 * input a user object as one parameter
	 * check whether this user exists or not
	 * serialize the new user object into the 'users' folder
	 */
	@Override
	public void register(user newUser) {
		String newUserName = newUser.getUserName();
		if(userTable.containsKey(newUserName)) System.out.println("This username exists!");
		else {
			File finalPath = new File(new File("users"),newUser.getUserName());
			try{
				writeUserObj(newUser, finalPath);}
			catch(Exception e) {
				e.printStackTrace();
				}
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
	public void login() {
		Scanner input = new Scanner(System.in);
		System.out.println("Your username: ");
		String userName = input.next();
		System.out.println("Your password: ");
		String passWord = input.next();
		
		if(userTable.containsKey(userName) && userTable.get(userName).getPassWord().equals(passWord)) {
			System.out.println("Login successfully.");
		}
		else {
			System.out.println("Wrong userName or passWord!");
		}
		input.close();
	}
	
	/*
	 * print items in shopping cart and 
	 * ask whether continue shopping or
	 * delete items
	 */
	@Override
	public void shopCart() {
		printItems();
		System.out.println("1 : Continue shopping\n2 : Delete items.");
		try(Scanner input = new Scanner(System.in);){
			int choice = input.nextInt();
			if(choice == 1) 
		}catch(Exception e) {
			System.out.println("Invalid input");
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * add items into shopping cart
	 */
	private void buyItems() {
		!!这里要加打印所有商品的列表
		
		System.out.println("Which one you want to buy? input its name:");
		Scanner input = new Scanner(System.in);
		String itemName = input.next();
		System.out.println("How many you want?");
		int amount = input.nextInt();
		
		// get total number of one item in shopping cart and 
		// check whether has enough items to sell
		int totalNum = shopCart.get(itemName) + amount;

		if(!itemTable.containsKey(itemName)) System.out.println("No such item!");
		
		else if(itemTable.containsKey(itemName) && itemTable.get(itemName).getAmount() < totalNum) System.out.println("No enough items!");
		else {
			System.out.println("Items added successfully!");
		}
		
	}
	
	
	/*
	 * Print all items in user's shopping cart
	 * Print total price
	 */
	@Override
	public void printItems() {
		System.out.println("------Items in your shopping cart------");
		double inTotal = 0.0;
		for(Map.Entry<String, Integer> item: shopCart.entrySet()) {
			inTotal += itemTable.get(item.getKey()).getPrice() * item.getValue();
			System.out.println(item);
		}
		System.out.println("Total Price : " + inTotal);
	}
	
	@Override
	public void purchaseItems(int price) {
		
	}

}
