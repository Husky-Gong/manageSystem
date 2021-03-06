package manageSystem;

import java.io.Serializable;

public class user implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3982901843477642615L;
	
	
	private int id;
	private String userName;
	private String passWord;
	private int userAge;
	private boolean manager;
	

	public user(int id, String userName, String passWord, int userAge, boolean manager) {
		super();
		this.id = id;
		this.userName = userName;
		this.passWord = passWord;
		this.userAge = userAge;
		this.manager = manager;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	
	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		return "user [id=" + id + ", userName=" + userName + ", passWord=" + passWord + ", userAge=" + userAge
				+ ", manager=" + manager + "]";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (manager ? 1231 : 1237);
		result = prime * result + ((passWord == null) ? 0 : passWord.hashCode());
		result = prime * result + userAge;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		user other = (user) obj;
		if (id != other.id)
			return false;
		if (manager != other.manager)
			return false;
		if (passWord == null) {
			if (other.passWord != null)
				return false;
		} else if (!passWord.equals(other.passWord))
			return false;
		if (userAge != other.userAge)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}
