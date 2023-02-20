package org.shrigorevich.ml.handlers;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;
import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.StructureService;
import org.shrigorevich.ml.domain.structure.events.StructsDamagedEvent;

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
