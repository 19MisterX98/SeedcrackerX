package kaptainwutax.seedcracker.profile;

public abstract class CustomProfile extends FinderProfile {

	public CustomProfile(String author, boolean defaultState) {
		super(defaultState);
		this.author = author;
		this.locked = false;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
