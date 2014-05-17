package net.korikisulda.hadriangarden.remote;

import net.korikisulda.hadriangarden.http.ConvenientGet;
import net.korikisulda.hadriangarden.http.ConvenientPost;

import org.json.JSONObject;
/**
 * Represents a probe.
 * You'll need a User to initialise this.
 */
public class Probe {
	public static final String domain="http://korikisulda.net/";
	
	private String uuid;
	private String token;
	private User user;
	
	/**
	 * Constructs a probe that has already been registered before
	 * @param userFor The user this is on behalf of
	 * @param uuid Probe's UUID
	 * @param probeSecret Probe's secret token
	 */
	public Probe(User userFor,String uuid, String probeSecret){
		this.uuid=uuid;
		this.token=probeSecret;
		this.user=userFor;
	}
	
	/**
	 * Constructs and registers a probe for the first time
	 * @param userFor User the probe is being constructed for. Ensure the user is fully registered and authorised
	 * @param seed Probe seed. This can, afaik, be anything
	 * @param country Two digit country code. ISO-something-or-other. We're in "gb"
	 * @param probeType Type of probe
	 */
	public Probe(User userFor,String seed,String country,ProbeType probeType){
		this(userFor,"","");
		token=registerProbe(userFor.getEmail(),country,seed,probeType);
	}
	
	/**
	 * Surprisingly enough, this function registers a this probe. Used internally
	 * @param email Email
	 * @param country Two digit country code
	 * @param seed Seed for UUID. Afaik, this can be pretty much anything
	 * @param probeType The type of probe
	 * @return Probe secret
	 */
	private String registerProbe(final String email,final String country,final String seed, final ProbeType probeType){
		final Probe probe=this;
		
		ConvenientPost post=new ConvenientPost(){{
			String probeUuid=md5sum(seed + "-" + user.getProbeToken());
			setUrl(domain+"api/1.2/register/probe");
			add("email",email);
			add("probe_seed",seed);
			add("probe_type",probeType.getName());
			add("cc",country);
			add("signature",sign(probeUuid,user.getToken()));
			add("probe_uuid",probeUuid);
			
			probe.setUuid(probeUuid);
		}};
		
		if(!post.execute()) return null;
		System.out.println(post.getResult());
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success")) return json.getString("secret");
        else return null;
	}
	
	/**
	 * Sets the probe UUID. Used internally.
	 * @param uuid New probe UUID
	 */
	private void setUuid(String uuid){
		this.uuid=uuid;
	}
	
	/**
	 * Gets the UUID of our probe
	 * @return Probe UUID
	 */
	public String getUuid(){
		return uuid;
	}
	
	/**
	 * Gets the token for this probe
	 * @return Secret token
	 */
	public String getToken(){
		return token;
	}
	
	/**
	 * Gets a URL the backend wants us to take a look at
	 * @return URL returned by backend
	 */
	public String getUrl(){
		ConvenientGet get=new ConvenientGet(){{
			setUrl(domain+"api/1.2/request/httpt");
			add("signature",sign(getUuid(),getToken()));
			add("probe_uuid",getUuid());
		}};
		
		if(!get.execute()) return null;
		
		JSONObject json=get.getResultAsJson();

        if(json.getBoolean("success"))
            return json.getString("url");
        else return null;
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
			
			setUrl(domain+"api/1.2/response/httpt");
			
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
