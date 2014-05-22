package net.korikisulda.hadriangarden.remote.results;

import net.korikisulda.hadriangarden.http.ConvenientRequest;

public class AuthenticationResult extends Result{
	private String key;
	
	public AuthenticationResult(String key,boolean success,ConvenientRequest originalRequest) {
		super(success,originalRequest);
		this.key=key;
	}
	
	public String getKey(){
		return key;
	}
}
