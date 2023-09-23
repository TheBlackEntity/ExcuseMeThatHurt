package me.entity303.excusemethathurt.listener;

import me.entity303.excusemethathurt.ExcuseMeThatHurt;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

public class EntityDamageListener implements Listener {
    private final ExcuseMeThatHurt plugin;

    public EntityDamageListener(ExcuseMeThatHurt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Mob))
            return; //Do nothing, Non-Mobs can't get angry

        Mob mob = (Mob) e.getEntity();

        PersistentDataContainer container = mob.getPersistentDataContainer();

        if (!container.has(this.plugin.getSuspiciousActionPersonKey(), PersistentDataType.STRING))
            return; //Do nothing, nothing bad happened to the mob

        if (container.has(this.plugin.getSuspiciousActionForgetKey(), PersistentDataType.LONG)) {
            long timeTillForget = container.get(this.plugin.getSuspiciousActionForgetKey(), PersistentDataType.LONG);

            if (timeTillForget < System.currentTimeMillis())
                return; //Do nothing, mob already forgot
        }

        String playerUuidString = container.get(this.plugin.getSuspiciousActionPersonKey(), PersistentDataType.STRING);

        if (playerUuidString == null)
            return; //Do nothing, something went wrong

        Player attacker = Bukkit.getPlayer(UUID.fromString(playerUuidString));

        if (attacker == null)
            return; //Do nothing, player went offline

        if (attacker.getLocation().distance(mob.getLocation()) > this.plugin.getConfiguration().getMaxAttackDistance())
            return; //Do nothing, player is too far away

        String action = container.get(this.plugin.getSuspiciousActionKey(), PersistentDataType.STRING);

        if (action == null)
            return; //Do nothing, something went wrong

        //Let's see what type of action we have and how we handle it

        if (action.equalsIgnoreCase("lava"))
            if (!this.isLavaDamage(e.getCause()))
                return; //Do nothing, since the DamageCause does not count as lava damage

        if (action.equalsIgnoreCase("fire"))
            if (!this.isFireDamage(e.getCause()))
                return; //Do nothing, since the DamageCause does not count as fire damage

        if (action.equalsIgnoreCase("push"))
            if (!this.isPushDamage(e.getCause()))
                return; //Do nothing, since the DamageCause does not count as push damage

        //Call event so plugins can handle the outcome
        EntityTargetLivingEntityEvent targetEvent = new EntityTargetLivingEntityEvent(mob, attacker, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);

        Bukkit.getPluginManager().callEvent(targetEvent);

        if (targetEvent.isCancelled())
            return; //Do nothing, some plugin cancelled the event

        //Attack!
        mob.setTarget(targetEvent.getTarget());

        //Golem! Golem! This player hurt me :(
        if (mob instanceof Villager)
            this.makeGolemsAngry(mob, attacker);

        //Zombified piglins! Unite!
        if (mob.getType() == EntityType.ZOMBIFIED_PIGLIN)
            this.makePigmenAngry(mob, attacker);
    }

    private void makeGolemsAngry(Mob mob, Player attacker) {
        if (!this.plugin.getConfiguration().isAlertGolems())
            return; //Do nothing, feature is disabled via config

        long range = this.plugin.getConfiguration().getAlertGolemsRange();

        for (Entity entity : mob.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof IronGolem))
                continue; //Do nothing, not a golem

            IronGolem golem = (IronGolem) entity;

            if (golem.isPlayerCreated())
                continue; //Do nothing, player created golems never attack players

            //Call event so plugins can handle the outcome
            EntityTargetLivingEntityEvent targetEvent = new EntityTargetLivingEntityEvent(golem, attacker, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);

            Bukkit.getPluginManager().callEvent(targetEvent);

            if (targetEvent.isCancelled())
                continue; //Do nothing, some plugin cancelled the event

            //Attack!
            golem.setTarget(attacker);
        }
    }

    private void makePigmenAngry(Mob mob, Player attacker) {
        if (!this.plugin.getConfiguration().isAlertPigmen())
            return; //Do nothing, feature is disabled via config

        long range = this.plugin.getConfiguration().getAlertPigmenRange();

        for (Entity entity : mob.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Mob))
                continue;

            if (entity.getType() != EntityType.ZOMBIFIED_PIGLIN)
                continue; //Do nothing, not a zombified piglin

            Mob foundMob = (Mob) entity;

            //Call event so plugins can handle the outcome
            EntityTargetLivingEntityEvent targetEvent = new EntityTargetLivingEntityEvent(foundMob, attacker, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);

            Bukkit.getPluginManager().callEvent(targetEvent);

            if (targetEvent.isCancelled())
                continue; //Do nothing, some plugin cancelled the event

            //Attack!
            foundMob.setTarget(attacker);
        }
    }

    private boolean isPushDamage(EntityDamageEvent.DamageCause cause) {
        EntityDamageEvent.DamageCause[] allowedCauses = {
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.CONTACT,
                EntityDamageEvent.DamageCause.HOT_FLOOR
        };

        return Arrays.stream(allowedCauses).anyMatch(allowedCause -> cause == allowedCause);
    }

    private boolean isFireDamage(EntityDamageEvent.DamageCause cause) {
        EntityDamageEvent.DamageCause[] allowedCauses = {
                EntityDamageEvent.DamageCause.FIRE
        };

        return Arrays.stream(allowedCauses).anyMatch(allowedCause -> cause == allowedCause);
    }

    private boolean isLavaDamage(EntityDamageEvent.DamageCause cause) {
        EntityDamageEvent.DamageCause[] allowedCauses = {
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.LAVA
        };

        return Arrays.stream(allowedCauses).anyMatch(allowedCause -> cause == allowedCause);
    }
}
