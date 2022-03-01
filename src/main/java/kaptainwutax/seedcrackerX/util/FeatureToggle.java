package kaptainwutax.seedcrackerX.util;

public class FeatureToggle {

    //This is for The featureToggles in the config object
    //It allows for the booleans to be passed around by reference
    //(I know that it's a hacky workaround)
    private boolean enabled;

    public FeatureToggle(boolean flag) {
        enabled = flag;
    }

    public void set(boolean flag) {
        enabled = flag;
    }

    public boolean get() {
        return enabled;
    }
}
