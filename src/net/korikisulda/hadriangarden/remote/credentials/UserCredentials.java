package net.korikisulda.hadriangarden.remote.credentials;

import net.korikisulda.hadriangarden.http.ConvenientPost;
import net.korikisulda.hadriangarden.remote.results.AuthenticationResult;

import org.json.JSONObject;

public class UserCredentials {
		public static final String domain="https://api.blocked.org.uk/";

		private String token="";
		private String email="";
		private String probeToken="";
		private String password="";
				
		/**
		 * Constructs a user with an email address and password. This will cause the user to be registered
		 * With the backend. Use getToken() after this, and make sure you save the token.
		 * @param email Email address of user
		 * @param password Password of user
		 */
		public UserCredentials(String email,String password){
			this.password=password;
			this.setEmail(email);
		}
		/**
		 * Constructs a user with a secret provided by the server (I call it a token).
		 * You must provide the email address using setEmail() after this.
		 * @param token Shared secret issued by server
		 */
		public UserCredentials(String email,String token,String probeToken){
			this.email=email;
			this.token=token;
			this.probeToken=probeToken;
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
		 * Gets the probe token associated with this user. You need it to make probes.
		 */
		public String getProbeToken(){
			return probeToken;
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
		 * @return AuthenticationResult containing success status, and the key
		 */
		public AuthenticationResult registerUser(){
			ConvenientPost post=new ConvenientPost(){{
				setUrl(domain+"1.2/register/user");
				add("email",email);
				add("password",password);
			}};
			
			if(!post.execute()) return new AuthenticationResult("",false,post);

	        JSONObject json = post.getResultAsJson();
	        return new AuthenticationResult(json.getString("secret"),json.getBoolean("success"),post);
		}
		
		/**
		 * Requests a probe secret from the API
		 * @return AuthenticationResult representing success, and containing key if successful
		 */
		public AuthenticationResult requestProbeToken(){
			final String token=getToken();
			ConvenientPost post=new ConvenientPost(){{
				setUrl(domain+"1.2/prepare/probe");
				add("email",getEmail());
				add("signature",sign(getEmail() + ":" + getDate(),token));
				add("date",getDate());
			}};
			
			if(!post.execute()) return new AuthenticationResult("",false,post);
			
	        JSONObject json = post.getResultAsJson();
        	probeToken=json.getString("probe_hmac");
	        return new AuthenticationResult(json.getString("probe_hmac"),json.getBoolean("success"),post);
		}

}
