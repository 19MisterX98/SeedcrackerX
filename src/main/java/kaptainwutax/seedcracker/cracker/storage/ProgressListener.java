package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.util.Log;

public class ProgressListener {

	protected float progress;
	protected int count = 0;

	public ProgressListener() {
		this(0.0F);
	}

	public ProgressListener(float progress) {
		this.progress = progress;
	}

	public synchronized void addPercent(float percent, boolean debug) {
		if((this.count & 3) == 0 && debug) {
			Log.debug("Progress: " + this.progress +  "%");
		}

		this.count++;
		this.progress += percent;
	}

}
