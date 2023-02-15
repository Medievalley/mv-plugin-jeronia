package org.shrigorevich.ml.domain.mob.handlers;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import java.util.Objects;

class AttrHelper {


    public static void boostDamage(Attributable entity, double factor) {
        getDamageAttr(entity).setBaseValue(getDamageAttr(entity).getBaseValue() * factor);
    }

    public static void boostMaxHealth(Attributable entity, double factor) {
        getHealthAttr(entity).setBaseValue(getHealthAttr(entity).getBaseValue() * factor);
    }

    public static void boostSpeed(Attributable entity, double factor) {
        getSpeedAttr(entity).setBaseValue(getSpeedAttr(entity).getBaseValue() * factor);
    }

    public static boolean hasDamageAttr(Attributable entity) {
        return entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null;
    }

    public static boolean hasHealthAttr(Attributable entity) {
        return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null;
    }

    public static boolean hasSpeedAttr(Attributable entity) {
        return entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null;
    }

    private static AttributeInstance getDamageAttr(Attributable entity) {
        return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE));
    }

    private static AttributeInstance getSpeedAttr(Attributable entity) {
        return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED));
    }

    private static AttributeInstance getHealthAttr(Attributable entity) {
        return Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH));
    }
}
