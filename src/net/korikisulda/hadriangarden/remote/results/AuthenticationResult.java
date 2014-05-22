package net.korikisulda.hadriangarden.remote.results;

public class AuthenticationResult extends Result{

	private String key;
	
	public AuthenticationResult(String key,boolean success) {
		super(success);
		this.key=key;
	}
	
	public String getKey(){
		return key;
	}

}
