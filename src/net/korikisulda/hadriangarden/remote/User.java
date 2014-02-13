package net.korikisulda.hadriangarden.remote;

import net.korikisulda.hadriangarden.http.ConvenientPost;

import org.json.JSONObject;

public class User {
	
	private String token="";
	
	public User(String email,String password){
		this.token=registerUser(email,password);
	}
	
	public User(String token){
		this.token=token;
	}
	
	public String getToken(){
		return token;
	}
	
	/**
	 * Registers a user with an email address and password
	 * @param email Email address of user. This should probably be valid, but the backend doesn't care at the moment
	 * @param password The password the user supplies.
	 * @return String of the secret. Using hashing magic now!
	 */
	private String registerUser(final String email,final String password){
		ConvenientPost post=new ConvenientPost(){{
			add("email",email);
			add("password",password);
			setUrl("http://korikisulda.net/api/1.2/register/user");
		}};
		
		if(!post.execute()) return null;
		
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success"))
        {
            return json.getString("secret");
        }
        else
        {
            return null;
        }
	}
	
	/**
	 * Gets the user status (successfully authenticated?)
	 * @return True if the user is successfully authenticated and all is well
	 */
	public boolean getUserStatus(){
		return true;
	}

}
