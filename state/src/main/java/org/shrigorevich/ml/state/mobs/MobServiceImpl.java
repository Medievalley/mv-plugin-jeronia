package org.shrigorevich.ml.state.mobs;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.mobs.CustomMob;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.mobs.MobType;
import org.shrigorevich.ml.domain.mobs.PresetUnit;
import org.shrigorevich.ml.state.BaseService;
import org.shrigorevich.ml.state.mobs.models.SkullModel;

import java.util.*;

import static org.bukkit.entity.EntityType.*;

public class MobServiceImpl extends BaseService implements MobService {

    private final Map<EntityType, SkullModel> skulls;
    private final Map<UUID, CustomMob> mobs;
    private final List<EntityType> mobTypesForRegSpawn;
    private final PlayerProfile profile;

    public MobServiceImpl(Plugin plugin) {
        super(plugin, LogManager.getLogger());
        this.skulls = new HashMap<>();
        this.mobs = new HashMap<>();
        this.mobTypesForRegSpawn = new ArrayList<>(List.of(ZOMBIE, SKELETON, SPIDER));
        this.profile = Bukkit.getServer().createProfile(UUID.randomUUID());
    }

    @Override
    public ItemStack getSkull(EntityType type) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        if (skulls.containsKey(type)) {
            SkullModel skull = skulls.get(type);
            Set<ProfileProperty> properties = profile.getProperties();
            properties.add(new ProfileProperty("textures", skull.getSkin()));
            itemMeta.setPlayerProfile(profile);
            itemMeta.displayName(Component.text(skull.getName()));
            item.setItemMeta(itemMeta);
        }
        return item;
    }

    @Override
    public void setup() {
        //TODO: resolve skulls
//        SkullModelImpl skeletonSkull = new SkullModelImpl();
//        skeletonSkull.setName("Skeleton");
//        skeletonSkull.setSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ0NmViNjQyZGMzYTRkZmJiNWFkNTI5N2VkYWUyOTk2ZWE0Y2ZmZjkyYWMyZWI1NmRmYWU5ZWUxZDU4ZTQwOCJ9fX0=");
//        skeletonSkull.setType(SKELETON.toString());
//        skulls.put(SKELETON, skeletonSkull);
//
//        SkullModelImpl zombieSkull = new SkullModelImpl();
//        zombieSkull.setName("Zombie");
//        zombieSkull.setSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVmYzQ2YzdhZWE0NjAwZGRhY2MwNmEzNGY4ZWYxN2VhNWM4MTFiNWRiNTRlNjk4NWUzYWU5MjZmOWRlODE0NyJ9fX0=");
//        zombieSkull.setType(ZOMBIE.toString());
//        skulls.put(ZOMBIE, zombieSkull);
    }

    @Override
    public void addMob(CustomMob mob) {
        mobs.put(mob.getId(), mob);
    }

    @Override
    public Optional<CustomMob> getMob(UUID id) {
        return mobs.containsKey(id) ? Optional.of(mobs.get(id)) : Optional.empty();
    }

    @Override
    public void remove(UUID entityId) {
        mobs.remove(entityId);
    }

    @Override
    public int getCurrentPower() {
        int power = 0;
        for (CustomMob m : mobs.values()) power += m.getPower();
        return power;
    }

    @Override
    public Map<EntityType, PresetUnit> getPressurePreset() {
        Map<EntityType, PresetUnit> preset = new EnumMap<>(EntityType.class);
        preset.put(SKELETON, new PresetUnitImpl(0.5, 0.5));
        preset.put(ZOMBIE, new PresetUnitImpl(0.5, 0.5));
        return preset;
    }

    @Override
    public int getCurrentQuantity() {
        return mobs.size();
    }

    @Override
    public CustomMob createMob(Mob entity, MobType type, double power) {
        switch (type) {
            case PRESSURE_ZOMBIE -> {
                return new PressureZombie(entity, power);
            }
            case PRESSURE_SKELETON -> {
                return new PressureSkeleton(entity, power);
            }
            case PRESSURE_CREEPER -> {
                return new PressureCreeper(entity, power);
            }
            case PRESSURE_SPIDER -> {
                return new PressureSpider(entity, power);
            }
            default -> throw new IllegalArgumentException(String.format("Mob type: %s not implemented", type));
        }
    }
}
