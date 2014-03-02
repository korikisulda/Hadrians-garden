package net.korikisulda.hadriangarden.remote;
/**
 * Type of probe. Used to identify to the server what exactly we are running on.
 * I personally want to petition for the inclusion of Sun Ultras.
 */
public enum ProbeType {
	ANDROID		("android"),
	RASPBERRY_PI("raspi"),
	ATLAS		("atlas"),
	WEB			("web");
	
	private String name;
	private ProbeType(String name){
		this.name=name;
	}
	
	/**
	 * Gets the server readable name of this probe-type
	 * @return Server-readable type name
	 */
	public String getName(){
		return name;
	}
}
