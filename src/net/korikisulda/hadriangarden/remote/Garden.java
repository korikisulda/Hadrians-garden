package net.korikisulda.hadriangarden.remote;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Main 'instance' for Hadrian's Garden.
 * Also contains calls to the backend that don't require authentication.
 * Afaik, that only includes getting URLs to be checked.
 */
public class Garden {
	public Garden(){
		
	}
	
	public static void main(String[] args){
		System.out.println(new Garden().getUrl());
	}

	/**
	 * Gets a URL the backend wants us to take a look at
	 * @return URL returned by backend
	 */
	public String getUrl(){
        HttpClientBuilder httpBuilder = HttpClientBuilder.create(); //No, I don't know either..
        CloseableHttpClient httpclient= httpBuilder.build();
        JSONObject json;

        HttpGet httpGet = new HttpGet("https://bowdlerize.co.uk/api/1.1/request/httpt");
        httpGet.setHeader("Accept", "application/json");

        try
        {
            HttpResponse response = httpclient.execute(httpGet);
            String rawJSON = EntityUtils.toString(response.getEntity());

            json = new JSONObject(rawJSON);

            if(json.getBoolean("success"))
            {
                return json.getString("url");
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
	}
}
