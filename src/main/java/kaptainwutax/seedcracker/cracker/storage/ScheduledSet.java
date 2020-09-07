package kaptainwutax.seedcracker.cracker.storage;

import java.util.*;

public class ScheduledSet<T> implements Iterable<T> {

	protected final Set<T> baseSet;
	protected final Set<T> scheduledSet;

	public ScheduledSet(Comparator<T> comparator) {
		if(comparator != null) {
			this.baseSet = new TreeSet<>(comparator);
		} else {
			this.baseSet = new HashSet<>();
		}

		this.scheduledSet = new HashSet<>();
	}

	public synchronized void scheduleAdd(T e) {
		this.scheduledSet.add(e);
	}

	public synchronized void dump() {
		synchronized(this.baseSet) {
			this.baseSet.addAll(this.scheduledSet);
			this.scheduledSet.clear();
		}
	}

	public synchronized boolean contains(T e) {
		return this.baseSet.contains(e) || this.scheduledSet.contains(e);
	}

	public Set<T> getBaseSet() {
		return this.baseSet;
	}

	@Override
	public synchronized Iterator<T> iterator() {
		return this.baseSet.iterator();
	}

	public synchronized int size() {
		return this.baseSet.size();
	}

}
