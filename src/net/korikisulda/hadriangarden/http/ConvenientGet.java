package net.korikisulda.hadriangarden.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class ConvenientGet extends ConvenientRequest{

	@Override
    public boolean execute(){
    	try{
	        HttpGet httpGet = new HttpGet(URL);
	        httpGet.setHeader("Accept", "application/json");
	    	
	        HttpResponse response = httpclient.execute(httpGet);
	        result = EntityUtils.toString(response.getEntity());
	        
	        success=true;
    	}catch(Exception e){
    		success=false;
    	}
    	return success;
    }

}
