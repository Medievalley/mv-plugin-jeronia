package org.shrigorevich.ml.domain.mob;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.common.BaseService;
import org.shrigorevich.ml.domain.mob.models.SkullModel;
import org.shrigorevich.ml.domain.mob.models.SkullModelImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MobServiceImpl extends BaseService implements MobService {

    private final Map<EntityType, SkullModel> skulls;
    private final PlayerProfile profile;

    public MobServiceImpl(Plugin plugin) {
        super(plugin);
        this.skulls = new HashMap<>();
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
    public void load() {
        SkullModelImpl skeletonSkull = new SkullModelImpl();
        skeletonSkull.setName("Skeleton");
        skeletonSkull.setSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ0NmViNjQyZGMzYTRkZmJiNWFkNTI5N2VkYWUyOTk2ZWE0Y2ZmZjkyYWMyZWI1NmRmYWU5ZWUxZDU4ZTQwOCJ9fX0=");
        skeletonSkull.setType(EntityType.SKELETON.toString());
        skulls.put(EntityType.SKELETON, skeletonSkull);

        SkullModelImpl zombieSkull = new SkullModelImpl();
        zombieSkull.setName("Zombie");
        zombieSkull.setSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVmYzQ2YzdhZWE0NjAwZGRhY2MwNmEzNGY4ZWYxN2VhNWM4MTFiNWRiNTRlNjk4NWUzYWU5MjZmOWRlODE0NyJ9fX0=");
        zombieSkull.setType(EntityType.ZOMBIE.toString());
        skulls.put(EntityType.ZOMBIE, zombieSkull);
    }
}
