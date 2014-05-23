package net.korikisulda.hadriangarden.remote.credentials;

import org.json.JSONObject;

import net.korikisulda.hadriangarden.http.ConvenientPost;
import net.korikisulda.hadriangarden.remote.ProbeType;
import net.korikisulda.hadriangarden.remote.User;

public class ProbeCredentials {
	public static final String domain="https://api.blocked.org.uk/";
	
	private String uuid;
	private String token;
	private User user;
	
	/**
	 * Constructs a probe that has already been registered before
	 * @param userFor The user this is on behalf of
	 * @param uuid Probe's UUID
	 * @param probeSecret Probe's secret token
	 */
	public ProbeCredentials(User userFor,String uuid, String probeSecret){
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
	public ProbeCredentials(User userFor,String seed,String country,ProbeType probeType){
		this(userFor,"","");
		token=registerProbe(userFor.getEmail(),country,seed,probeType);
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
	 * Surprisingly enough, this function registers a this probe. Used internally
	 * @param email Email
	 * @param country Two digit country code
	 * @param seed Seed for UUID. Afaik, this can be pretty much anything
	 * @param probeType The type of probe
	 * @return Probe secret
	 */
	private String registerProbe(final String email,final String country,final String seed, final ProbeType probeType){		
		ConvenientPost post=new ConvenientPost(){{
			String probeUuid=md5sum(seed + "-" + user.getProbeToken());
			setUrl(domain+"1.2/register/probe");
			add("email",email);
			add("probe_seed",seed);
			add("probe_type",probeType.getName());
			add("cc",country);
			add("signature",sign(probeUuid,user.getToken()));
			add("probe_uuid",probeUuid);
			
			setUuid(probeUuid);
		}};
		
		if(!post.execute()) return null;
		System.out.println(post.getResult());
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success")) return json.getString("secret");
        else return null;
	}

	public User getUser() {
		return user;
	}

}
