package net.korikisulda.hadriangarden.http;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

public class ConvenientGet extends ConvenientRequest{
    private List<NameValuePair> getParams = new ArrayList<NameValuePair>();
	@Override
	public HttpRequestBase getMethod() {
	    try {
		URIBuilder builder = new URIBuilder(URL).setParameters(getParams);
		HttpGet get=new HttpGet(builder.build());
		return get;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		    return null;
		}
	}
	
	protected void add(String key, String value) {
    	getParams.add(new BasicNameValuePair(key,value));
	}
	
}
