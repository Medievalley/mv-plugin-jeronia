package org.shrigorevich.ml.domain.handlers;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class TestHandler implements Listener {

    public TestHandler() {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ShootBow(EntityShootBowEvent event) {
        Entity arrow = event.getProjectile(); //the projectile
        Vector traj = arrow.getLocation().getDirection().clone(); // we iterate over each block in the arrow's path.
        Entity target; // the entity we need to shoot at.

        for (int o = 0; o < 50; o++){
            traj = arrow.getLocation().getDirection().add(traj);

        }

        //arrow.setVelocity(arrow.getLocation().getDirection().subtract(target.getLocation());
        // set the arrow to go towards the target (may want to put this in a repeating task to keep the arrow on target
    }
}
