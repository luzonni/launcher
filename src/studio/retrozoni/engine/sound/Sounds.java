package studio.retrozoni.engine.sound;

public enum Sounds {
	
	Click("click");

	
	private final String ResourceName;
	
	Sounds(String resourceName) {
		this.ResourceName = resourceName;
	}
	
	public String resource() {
		return this.ResourceName;
	}

}
