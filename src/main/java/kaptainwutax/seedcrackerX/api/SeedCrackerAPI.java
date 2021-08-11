package kaptainwutax.seedcrackerX.api;

import java.util.List;

public interface SeedCrackerAPI {

    /*

    implement this class in your mod

    add an entrypoint in the fabric.mod.json.
    for example:
    "seedcrackerx": [
        "me.zeroX150.atomic.feature.module.impl.render.OreSim.SeedCrackerEP"
    ]
     */

    void seedCollector(List<Long> seeds);

}
