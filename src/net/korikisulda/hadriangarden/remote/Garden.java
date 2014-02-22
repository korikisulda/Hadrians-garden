package net.korikisulda.hadriangarden.remote;

import net.korikisulda.hadriangarden.http.ConvenientGet;

import org.json.JSONObject;

/**
 * Main 'instance' for Hadrian's Garden.
 * Also contains calls to the backend that don't require authentication.
 * Afaik, that only includes getting URLs to be checked.
 */
public class Garden {

	/**
	 * Gets a URL the backend wants us to take a look at
	 * @return URL returned by backend
	 */
	public String getUrl(){
		ConvenientGet get=new ConvenientGet(){{
			setUrl("http://korikisulda.net/api/1.2/request/httpt");
		}};
		
		if(!get.execute()) return null;
		
		JSONObject json=get.getResultAsJson();
		
        if(json.getBoolean("success"))
            return json.getString("url");
        else return null;
      }

	
}
