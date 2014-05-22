package net.korikisulda.hadriangarden.remote.results;

public abstract class Result {

	public Result(boolean success) {
		this.success=success;
	}
	private boolean success=false;
	
	/**
	 * Because grammar
	 * @return Was this result successful?
	 */
	public boolean isSuccess(){
		return success;
	}

}
