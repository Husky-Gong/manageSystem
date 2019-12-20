package manageSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

public class systemUtils implements manageSystem{

	private static Hashtable<String, user> userTable = new Hashtable<>();
	
	static {
		/*
		 * 1. Check 'users' folder -- create or not?
		 * 2. loop the 'users' folder and deserialize all users from every user file
		 * 3. get every user object and store into the 'userTable' (Hashtable.put(key, value))
		 */
		File file = new File("users");
		if(!file.exists()) file.mkdir();
		
		try{
			File fullFileList[] = file.listFiles();
			for(File userFile: fullFileList) {
				user eachUser = getUserObj(userFile.getAbsolutePath());
				userTable.put(eachUser.getUserName(), eachUser);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static user getUserObj(String filePath) {
		try(FileInputStream fileIn = new FileInputStream(filePath);
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				){
			user userObj = (user) objIn.readObject();
			return userObj;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void login() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(user newUser) {
		// TODO Auto-generated method stub
		
	}

}
