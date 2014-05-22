package net.korikisulda.hadriangarden.remote;


import net.korikisulda.hadriangarden.http.ConvenientGet;
import net.korikisulda.hadriangarden.http.ConvenientPost;
import net.korikisulda.hadriangarden.remote.credentials.UserCredentials;
import net.korikisulda.hadriangarden.remote.results.Result;

import org.json.JSONObject;

/**
 * Represents the user, can be used to initially register, get current status, and is needed for registering
 * probes.
 */
public class User {
	private UserCredentials userCredentials;
	/**
	 * Construct a new User.
	 * @param userCredentials Credentials corresponding to the user either in the database, or to be created.
	 */
	public User(UserCredentials userCredentials){
		this.userCredentials=userCredentials;
	}
	
	public static final String domain="https://api.blocked.org.uk/";
	
	/**
	 * Get the credentials associated with this user
	 * @return Credentials corresponding to this user
	 */
	public UserCredentials getUserCredentials(){
		return userCredentials;
	}
	
	/**
	 * Gets the token for this user.
	 * @return Token for this user. Null if registration failed.
	 */
	public String getToken(){
		return userCredentials.getToken();
	}
	
	/**
	 * Gets the probe secret associated with this user. You need it to make new probes.
	 * @return Probe token
	 */
	public String getProbeToken(){
		return userCredentials.getProbeToken();
	}
	
	/**
	 * Returns the email address corresponding to the invading alien fleet
	 * @return Email address
	 */
	public String getEmail() {
		return userCredentials.getEmail();
	}
	
	/**
	 * Submit a URL for analysis, and get the ID.
	 * @param url URL to submit
	 * @return ID of URL, otherwise, -1.
	 */
	public int submitUrl(final String url){
		final String token=getToken();
		ConvenientPost post=new ConvenientPost(){{
			setUrl(domain+"1.2/submit/url");
			add("email",getEmail());
			add("url",url);
			add("signature",sign(url,token));
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
	public Result requestStatus(){
		final String token=getToken();
		ConvenientGet post=new ConvenientGet(){{
			setUrl(domain+"1.2/status/user");
			add("email",getEmail());
			add("date",getDate());
			add("signature",sign(getEmail() + ":" + getDate(),token));
		}};
		
		if(!post.execute()) return new Result(false,post);
		
        JSONObject json = post.getResultAsJson();
        return new Result(json.getBoolean("success"),post);
	}
	

}
