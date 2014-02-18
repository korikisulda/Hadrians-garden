package net.korikisulda.hadriangarden.remote;

import net.korikisulda.hadriangarden.http.ConvenientPost;

import org.json.JSONObject;
/**
 * Represents a probe.
 * You'll need a User to initialise this.
 */
public class Probe {
	private String uuid;
	
	public Probe(User userFor,String uuid){
		this.uuid=uuid;
	}
	
	public Probe(User userFor,String seed,String country,ProbeType probeType){
		this(userFor,"");
		uuid=registerProbe(userFor.getEmail(),country,seed,probeType,userFor);
	}
	
	private String registerProbe(final String email,final String country,final String seed, final ProbeType probeType,final User user){
		ConvenientPost post=new ConvenientPost(){{
			add("email",email);
			add("probe_seed",seed);
			add("probe_type",probeType.getName());
			add("cc",country);
			setUrl("http://korikisulda.net/api/1.2/register/probe");
			add("signature",sign(email,user.getToken()));
			add("probe_uuid",md5sum(seed + "-" + user.getToken()));
		}};
		
		if(!post.execute()) return null;
		System.out.println(post.getResult());
        JSONObject json = post.getResultAsJson();

        if(json.getBoolean("success")) return json.getString("secret");
        else return null;
	}
}
