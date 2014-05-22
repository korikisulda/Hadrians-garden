package net.korikisulda.hadriangarden.remote.results;

import net.korikisulda.hadriangarden.http.ConvenientRequest;

public class Result {

	public Result(boolean success,ConvenientRequest originalRequest) {
		this.success=success;
		this.originalRequest=originalRequest;
	}
	private boolean success=false;
	private ConvenientRequest originalRequest;
	
	/**
	 * Because grammar
	 * @return Was this result successful? If so, then true! Otherwise, it's a logical cake.
	 */
	public boolean isSuccess(){
		return success;
	}
	
	/**
	 * Status code of request
	 * @return Status code. 0 if it failed for whatever reason, I think.
	 */
	public int getStatus(){
		return originalRequest.getStatus();
	}

}
