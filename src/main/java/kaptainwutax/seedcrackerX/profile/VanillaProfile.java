package kaptainwutax.seedcrackerX.profile;

import kaptainwutax.seedcrackerX.finder.Finder;

public class VanillaProfile extends FinderProfile {

	public VanillaProfile() {
		super(true);
		this.author = "KaptainWutax";
		this.setTypeState(Finder.Type.DUNGEON, false);
	}

}
