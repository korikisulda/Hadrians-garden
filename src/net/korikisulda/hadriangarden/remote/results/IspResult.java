package net.korikisulda.hadriangarden.remote.results;

import net.korikisulda.hadriangarden.http.ConvenientRequest;

public class IspResult extends Result{
	private String isp="";
	private String ip="";
	public IspResult(boolean success,ConvenientRequest originalRequest) {
		super(success,originalRequest);
		if(success){
			isp=originalRequest.getResultAsJson().getString("isp");
			ip=originalRequest.getResultAsJson().getString("ip");
		}
	}
	
	public String getIsp(){
		return isp;
	}
	
	public String getIp(){
		return ip;
	}

}
