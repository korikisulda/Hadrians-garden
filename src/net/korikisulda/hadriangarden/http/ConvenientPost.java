package net.korikisulda.hadriangarden.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ConvenientPost extends ConvenientRequest{
    private List<NameValuePair> postParams = new ArrayList<NameValuePair>();
    
    @Override
    public boolean execute(){
    	try{
	        HttpPost httpPost = new HttpPost(URL);
	        httpPost.setHeader("Accept", "application/json");
	        
	        httpPost.setEntity(new UrlEncodedFormEntity(postParams));
	    	
	        HttpResponse response = httpclient.execute(httpPost);
	        result = EntityUtils.toString(response.getEntity());
	        
	        success=true;
    	}catch(Exception e){
    		success=false;
    	}
    	return success;
    }
    
    public void add(String key,String value){
    	postParams.add(new BasicNameValuePair(key,value));
    }
}
