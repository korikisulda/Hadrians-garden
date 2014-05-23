package net.korikisulda.hadriangarden.remote;

import net.korikisulda.hadriangarden.http.ConvenientGet;
import net.korikisulda.hadriangarden.http.ConvenientPost;
import net.korikisulda.hadriangarden.remote.credentials.ProbeCredentials;
import net.korikisulda.hadriangarden.remote.results.IspResult;

import org.json.JSONObject;
/**
 * Represents a probe.
 * You'll need a User to initialise this.
 */
public class Probe {
	//private User user;
	private ProbeCredentials probeCredentials;
	public Probe(ProbeCredentials probeCredentials){
		//this.user=probeCredentials.getUser();
		this.probeCredentials=probeCredentials;
	}
	
	public static final String domain="https://api.blocked.org.uk/";
	
	public ProbeCredentials getCredentials(){
		return probeCredentials;
	}
	
	/**
	 * Gets the UUID of our probe
	 * @return Probe UUID
	 */
	public String getUuid(){
		return getCredentials().getUuid();
	}
	
	/**
	 * Gets the token for this probe
	 * @return Secret token
	 */
	public String getToken(){
		return getCredentials().getToken();
	}
	
	/**
	 * Gets a URL the backend wants us to take a look at
	 * @return URL returned by backend
	 */
	public String requestUrl(){
		ConvenientGet get=new ConvenientGet(){{
			setUrl(domain+"1.2/request/httpt");
			add("signature",sign(getUuid(),getToken()));
			add("probe_uuid",getUuid());
			add("network_name",lastIspRequest.getIsp());
		}};
		
		if(!get.execute()) return null;
		
		JSONObject json=get.getResultAsJson();

        if(json.getBoolean("success"))
            return json.getString("url");
        else return null;
	}
	
	private IspResult lastIspRequest;
	
	/**
	 * Request the ISP meta from the middleware
	 * @return IspResult representing success state, and IP/ISP
	 */
	public IspResult requestISPMeta(){
		ConvenientGet get=new ConvenientGet(){{
			setUrl(domain+"1.2/status/ip");
			add("date",getDate());
			add("signature",sign(getDate(),getToken()));
			add("probe_uuid",getUuid());
		}};
		
		if(!get.execute()) return new IspResult(false,get);
		
		if(get.getResultAsJson().getBoolean("success")||lastIspRequest==null) lastIspRequest=new IspResult(get.getResultAsJson().getBoolean("success"),get);
        return new IspResult(get.getResultAsJson().getBoolean("success"),get);
	}
	
	/**
	 * Submits a URL test result to the remote backend.
	 * We might need a prettier version at some point (after all, we can combine URL and status using
	 * a ConvenientRequest, and IP/Network can be handled internally too)
	 * @param url URL that was tested
	 * @param status HTTP status code returned
	 * @param determinedSimpleStatus Do we think it's blocked? It can be 'timeout', 'ok', or 'blocked'
	 * @param config Config version. I don't think we actually have one yet, so I just say -1.0
	 * @param networkName Network name! We need to work out standardised names for this
	 * @param ip IP address. We could get from either a remote host, or through
	 * 		  A router using UPNP. The server should probably make efforts to check if this is correct.
	 * @return Success: true if our transmission succeeded, false if it failed for absolutely any reason.
	 */
	public boolean sendTestResult(final String url,final int status,final String determinedSimpleStatus,final String config,final String networkName, final String ip){
		ConvenientPost post=new ConvenientPost(){{
			String date=getDate();
			
			setUrl(domain+"1.2/response/httpt");
			
			add("probe_uuid",getUuid());
			add("url",url);
			add("config",config);
			
			add("ip_network",ip);
			add("network_name",networkName);
			
			add("status",determinedSimpleStatus);
			add("http_status",String.valueOf(status));
			add("date",date);
			
			
			add("signature",sign(
				getUuid() + ":" +
				url + ":" +
				determinedSimpleStatus + ":" +
				date + ":" +
				config
				
				,getToken()
				));
		}};
		
		if(!post.execute()) return false;

		JSONObject json=post.getResultAsJson();

        if(json.getBoolean("success"))
            return true;
        else return false;
        
	}
}
