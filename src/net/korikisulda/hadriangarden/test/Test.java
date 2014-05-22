package net.korikisulda.hadriangarden.test;

import net.korikisulda.hadriangarden.remote.Probe;
import net.korikisulda.hadriangarden.remote.ProbeType;
import net.korikisulda.hadriangarden.remote.User;

public class Test {
	public static void main(String[] args){
		new Test().doThings();
	}
	
	public void doThings(){
		System.out.println("Things in square brackets are things you should keep a record of.");
		
		//Use this first,then comment it out and use the next, and so on. Make sure only one is not commented at a time..
		//Make sure you fill in everything correctly. You need to register to get the token:
		//register("testprobe0@albatross.korikisulda.net","thisisapassword");
		
		//And now, we can make our very own probe!
		//probeFun("testprobe0@albatross.korikisulda.net","7SOmnuvEFMLXZ5AqsQJWM5iIZ3DQgxioq6KN");
		//new User("testprobe0@albatross.korikisulda.net","7SOmnuvEFMLXZ5AqsQJWM5iIZ3DQgxioq6KN","").getStatus();
		//But would that be nearly so fun if we didn't get URLs and submit results?
		moreProbeFun(
			"testprobe0@albatross.korikisulda.net",
			"HrnPLrRJe5bmLPHRzRmXsYYxAB9sgUYX",		/*User token*/
			"1IFnMA5N7pF2zbv0FVR9P7G5B34xdq5e",		/*User probe token: `probe_hmac`*/
			"32V6TIHxLBCWvnugsECWbaisdzSF2st5vobp",	/*Probe token:	`secret`*/
			"e22d82a55c52946a16cdb38aa9810085"		/*Probe UUID*/
		).requestISPMeta();
	}
	
	public void register(String email,String password){
		System.out.println("Your token is [" + new User(email,password).getToken() + "].");
		System.out.println("Your account will be pending. It must be accepted before you can do fun things.");
	}
	
	public void probeFun(String email, String token){
		User user=new User(email,token,"");
		System.out.println("It seems that your account is " + (user.requestStatus()?"okay":"somehow wrong. Everything else will probably fail.") + ".");
		System.out.println("Your user probe token is [" + user.requestProbeToken() + "].");
		
		System.out.println("Let's make a probe! Country is gb, Seed 'ThisIsASeed' and we're pretending to be a Raspberry Pi.");
		Probe probe=new Probe(user,"gb","ThisIsASeed",ProbeType.RASPBERRY_PI);
		if(probe.getToken()==""){
			System.out.println("It seems we have failed to make our probe.");
		}else{
			System.out.println("We've made a probe!");
			System.out.println("UUID is [" + probe.getUuid() + "]");
			System.out.println("Probe token is [" + probe.getToken() + "]");
			System.out.println("And we're done!");
		}
	}
	
	public Probe moreProbeFun(String email,String userToken,String userProbeToken,String probeToken, String probeUuid){
		User user=new User(email,userToken,userProbeToken);
		
		Probe probe=new Probe(user,probeUuid,probeToken);
		probe.setNetwork("I don't know what to put here");
		System.out.println(probe.requestUrl());
		
		//probe.sendTestResult("http://korikisulda.net/null/", 200, "ok", "-1.0", "I don't know what to put here", "192.168.0");
		return probe;
	}
}
