package kaptainwutax.seedcracker.profile;

import kaptainwutax.seedcracker.finder.Finder;

import java.util.HashMap;

public abstract class FinderProfile {

	public final HashMap<Finder.Type, Boolean> typeStates = new HashMap<>();

	protected String author;
	protected boolean locked;

	public FinderProfile(boolean defaultState) {
		for(Finder.Type type: Finder.Type.values()) {
			this.typeStates.put(type, defaultState);
		}
	}

	public String getAuthor() {
		return this.author;
	}

	public boolean getLocked() {
		return this.locked;
	}

	public boolean setTypeState(Finder.Type type, boolean state) {
		if(this.getLocked())return false;
		this.typeStates.put(type, state);
		return true;
	}

}
