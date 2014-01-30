package net.korikisulda.hadriangarden.remote;

public class User {
	
	/**
	 * Registers a user with an email address and password
	 * @param email Email address of user. This should probably be valid, but the backend doesn't care at the moment
	 * @param password The password the user supplies.
	 * @return String of the secret. At the moment, this (would) be a private key, but we'll be using hashing magic next.
	 */
	public String registerUser(String email,String password){
		return "";
	}
	/**
	 * Gets the user status (successfully authenticated?)
	 * @return True if the user is successfully authenticated and all is well
	 */
	public boolean getUserStatus(){
		return true;
	}

}
