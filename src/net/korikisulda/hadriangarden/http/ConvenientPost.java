package net.korikisulda.hadriangarden.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ConvenientPost {
    private HttpClientBuilder httpBuilder = HttpClientBuilder.create();
    private CloseableHttpClient httpclient= httpBuilder.build();
    private List<NameValuePair> postParams = new ArrayList<NameValuePair>();
    
    private String URL;
    private String result;
    private boolean success=false;
    
    public void setUrl(String URL){
    	this.URL=URL;
    }
    
    public String getUrl(){
    	return this.URL;
    }
    
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
    
    public boolean getSuccess(){
    	return success;
    }
    
    public String getResult(){
    	return result;
    }
    
    public JSONObject getResultAsJson(){
        return new JSONObject(getResult());
    }
    
    public void add(String key,String value){
    	postParams.add(new BasicNameValuePair(key,value));
    }
}
