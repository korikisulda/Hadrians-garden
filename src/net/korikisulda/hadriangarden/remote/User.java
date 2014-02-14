package net.korikisulda.hadriangarden.remote;


import net.korikisulda.hadriangarden.http.ConvenientPost;
import org.json.JSONObject;

public class User {
	
	private String token="";
	private String email="";
	/**
	 * Constructs a user with an email address and password. This will cause the user to be registered
	 * With the backend. Use getToken() after this, and make sure you save the token.
	 * @param email Email address of user
	 * @param password Password of user
	 */
	public User(String email,String password){
		this.token=registerUser(email,password);
		this.setEmail(email);
	}
	/**
	 * Constructs a user with a secret provided by the server (I call it a token).
	 * You must provide the email address using setEmail() after this.
	 * @param token Shared secret issued by server
	 */
	public User(String token){
		this.token=token;
	}
	
	/**
	 * Sets the email address. This is necessary for most user functions.
	 * @param email Email address of user
	 */
	public void setEmail(String email){
		this.email=email;
	}
	
	/**
	 * Gets the token for this user.
	 * @return Token for this user. Null if registration failed.
	 */
	public String getToken(){
		return token;
	}
	
	/**
	 * Returns the email address corresponding to the invading alien fleet
	 * @return Email address
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Registers a user with an email address and password
	 * @param email Email address of user. This should probably be valid, but the backend doesn't care at the moment
	 * @param password The password the user supplies.
	 * @return String of the secret. Null if something went horribly wrong.
	 */
	private String registerUser(final String email,final String password){
		ConvenientPost post=new ConvenientPost(){{
			add("email",email);
			add("password",password);
			setUrl("http://korikisulda.net/api/1.2/register/user");
		}};
		
		if(!post.execute()) return null;
		
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success")) return json.getString("secret");
        else return null;
	}
	/**
	 * Submit a URL for analysis, and get the ID.
	 * @param url URL to submit
	 * @return ID of URL, otherwise, -1.
	 */
	public int submitUrl(final String url){
		final String token=getToken();
		ConvenientPost post=new ConvenientPost(){{
			add("email",email);
			add("url",url);
			add("signature",sign(url,token));
			
			setUrl("http://korikisulda.net/api/1.2/submit/url");
		}};
		
		if(!post.execute()) return -1;
		
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success")) return json.getInt("uuid");
        else return -1;
	}
	
	/**
	 * Gets the user status (successfully authenticated?)
	 * @return True if the user is successfully authenticated and all is well
	 */
	public boolean getStatus(){
		final String token=getToken();
		ConvenientPost post=new ConvenientPost(){{
			add("email",email);
			add("signature",sign(email,token));
			
			setUrl("http://korikisulda.net/api/1.2/status/user");
		}};
		
		if(!post.execute()) return false;
		
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success"))
        {
            return true;
        }
        else return false;
	}

}
