package kaptainwutax.seedcrackerX.profile;

import kaptainwutax.seedcrackerX.finder.Finder;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class FinderConfig {

    protected FinderProfile finderProfile = new ModmenuProfile();
    protected Map<Finder.Type, ConcurrentLinkedQueue<Finder>> activeFinders = new ConcurrentHashMap<>();

    public FinderConfig() {

    }

    public List<Finder.Type> getActiveFinderTypes() {
        return this.finderProfile.typeStates.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Finder> getActiveFinders() {
        this.activeFinders.values().forEach(finders -> {
            finders.removeIf(Finder::isUseless);
        });

        return this.activeFinders.values().stream()
                .flatMap(Queue::stream).collect(Collectors.toList());
    }

    public void addFinder(Finder.Type type, Finder finder) {
        if(finder.isUseless())return;

        if(!this.activeFinders.containsKey(type)) {
            this.activeFinders.put(type, new ConcurrentLinkedQueue<>());
        }

        this.activeFinders.get(type).add(finder);
    }

    public boolean getActive(Finder.Type type) {
        return this.finderProfile.typeStates.get(type);
    }

    public boolean setActive(Finder.Type type, boolean flag) {
        return this.finderProfile.setTypeState(type, flag);
    }

}
