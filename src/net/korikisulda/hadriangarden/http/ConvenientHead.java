package net.korikisulda.hadriangarden.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.util.EntityUtils;

public class ConvenientHead extends ConvenientRequest{

	@Override
    public boolean execute(){
    	try{
	        HttpHead httpHead = new HttpHead(URL);
	        HttpResponse response = httpclient.execute(httpHead);
	        result = EntityUtils.toString(response.getEntity());
	        success=true;
    	}catch(Exception e){
    		success=false;
    	}
    	return success;
    }
}
