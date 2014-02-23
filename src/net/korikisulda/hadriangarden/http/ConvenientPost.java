package net.korikisulda.hadriangarden.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

public class ConvenientPost extends ConvenientRequest{
    private List<NameValuePair> postParams = new ArrayList<NameValuePair>();
    
    public void add(String key,String value){
    	postParams.add(new BasicNameValuePair(key,value));
    }

	@Override
	public HttpRequestBase getMethod() {
		try{
			HttpPost httpPost = new HttpPost(URL);
	        httpPost.setEntity(new UrlEncodedFormEntity(postParams));
			return httpPost;
		}catch(Exception e){}
		
		return null;
	}
}
