package net.korikisulda.hadriangarden.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public abstract class ConvenientRequest {
    protected HttpClientBuilder httpBuilder = HttpClientBuilder.create();
    protected CloseableHttpClient httpclient= httpBuilder.build();
    
    protected String accept="application/json";
    protected String agent="Mozilla/5.0 (Java) Hadrian's Garden/1.2"; //Was claire perry. Damn you, Richard ;D
    
    protected String URL;
    protected String result;
    protected boolean success=false;
    protected int status=-1;
    
    public void setUrl(String URL){
    	this.URL=URL;
    }
    
    public String getUrl(){
    	return this.URL;
    }
    
    public abstract HttpRequestBase getMethod();
    
    public boolean execute(){
    	try{
	        HttpRequestBase httpRequest = getMethod();
	        
	        httpRequest.setHeader("Accept",accept);
	        httpRequest.setHeader("User-Agent",agent);
	        
	        HttpResponse response = httpclient.execute(httpRequest);
	        status=response.getStatusLine().getStatusCode();
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
    
    public int getStatus(){
    	return status;
    }
    
    public JSONObject getResultAsJson(){
        return new JSONObject(getResult());
    }
    
    /**
     * Sets the Accept header. application/json by default.
     * Sample sent by Firefox: "text/html,application/xhtml+xml,application/xml;q=0.9"
     * Missing off a bit at the end because it contains something that doesn't play nicely with comments.
     * @param accept New accept header
     */
    public void setAccept(String accept){
    	this.accept=accept;
    }
    
    /**
     * Sets the User-Agent header. Claire Perry by default (Sorry, couldn't resist..)
     * @param agent New user-agent
     */
    public void setAgent(String agent){
    	this.agent=agent;
    }
        
	/**
	 * HMAC signing
	 * @param value String to sign
	 * @param key Secret to sign with
	 * @return Signature hash
	 */
	protected String sign(String value,String key){
		//Code is from here, with the change to SHA512 from 1.
		//https://stackoverflow.com/questions/6312544/hmac-sha1-how-to-do-it-properly-in-java
	      try {
	            // Get an hmac_sha1 key from the raw key bytes
	            byte[] keyBytes = key.getBytes();           
	            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	            // Get an hmac_sha1 Mac instance and initialize with the signing key
	            Mac mac = Mac.getInstance("HmacSHA512");
	            mac.init(signingKey);

	            // Compute the hmac on input data bytes
	            byte[] rawHmac = mac.doFinal(value.getBytes());

	            // Convert raw bytes to Hex
	            byte[] hexBytes = new Hex().encode(rawHmac);

	            //  Covert array of Hex bytes to a String
	            return new String(hexBytes, "UTF-8");
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	}
	
	protected String md5sum(String string){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			return new String(new Hex().encode(md.digest(string.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = new Date();
		dateFormat.applyPattern("y-M-d H:m");
		return dateFormat.format(date);
	}
}
