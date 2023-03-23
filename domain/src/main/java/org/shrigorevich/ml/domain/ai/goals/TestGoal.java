package org.shrigorevich.ml.domain.ai.goals;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MemoryKey;
import org.shrigorevich.ml.domain.mobs.MemoryUnit;
import org.shrigorevich.ml.domain.mobs.PointMemoryUnit;

import java.util.EnumSet;
import java.util.List;

public class TestGoal implements Goal<Mob> {

    private Pathfinder.PathResult path;
    private final CustomMob mob;
    private int timer;
    private int lifeTimer;
    private boolean isAchieved;
    private final List<MemoryUnit> points;
    private final String name;
    private final int lifeTime;
    private boolean stopped;
    private final GoalKey<Mob> key;
    private final GoalType type;
    public TestGoal(CustomMob mob, String name, int lifeTime, GoalType type) {
        this.type = type;
        this.lifeTime = lifeTime;
        this.name = name;
        this.mob = mob;
        this.points = mob.getMemory(MemoryKey.INTEREST_POINT);
        this.stopped = false;
        this.key = GoalKey.of(Mob.class, new NamespacedKey("ml", name));
    }

    @Override
    public boolean shouldActivate() {
        return !stopped;
    }

//    @Override
//    public boolean shouldStayActive() {
//        return !stopped;
//    }

    @Override
    public void start() {
        System.out.println(this.name + " goal started");
    }

    @Override
    public void stop() {
        lifeTimer = 0;
        System.out.println(this.name + " goal stopped");
    }

    @Override
    public void tick() {
        lifeTimer++;
        timer++;
        if (timer >= 20) {
            System.out.println(this.name + " goal tick");
            timer = 0;
        }
        if (lifeTimer >= lifeTime) {
            stopped = true;
        }

    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(this.type);
    }
}
