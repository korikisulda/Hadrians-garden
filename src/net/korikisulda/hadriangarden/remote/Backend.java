package net.korikisulda.hadriangarden.remote;

public class Backend {
	private User user;
	public Backend(User user){
		this.user=user;
	}
	/**
	 * Submit a URL to be tortured by the backend in the land of Mordor
	 * @param url URL to be submitted
	 * @return True if submission succeeded
	 */
	public boolean submitURL(String url){
		return false;
	}
	
	/**
	 * Gets a URL the backend wants us to take a look at
	 * @return URL returned by backend
	 */
	public String getUrl(){
		return "";
	}
}
