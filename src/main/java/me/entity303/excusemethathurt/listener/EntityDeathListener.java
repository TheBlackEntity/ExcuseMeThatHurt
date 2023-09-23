package me.entity303.excusemethathurt.listener;

import me.entity303.excusemethathurt.ExcuseMeThatHurt;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class EntityDeathListener implements Listener {
    private final ExcuseMeThatHurt plugin;

    public EntityDeathListener(ExcuseMeThatHurt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Pillager)
            this.handlePillager((Pillager) e.getEntity());
    }

    private void handlePillager(Pillager pillager) {
        assert pillager != null;

        PersistentDataContainer container = pillager.getPersistentDataContainer();

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

        if (attacker.getLocation().distance(pillager.getLocation()) > this.plugin.getConfiguration().getMaxAttackDistance())
            return; //Do nothing, player is too far away

        String action = container.get(this.plugin.getSuspiciousActionKey(), PersistentDataType.STRING);

        if (action == null)
            return; //Do nothing, something went wrong

        if (!pillager.isPatrolLeader())
            return; //Do nothing, pillager is not captain

        if (pillager.getRaid() != null)
            return; //Do nothing, you don't get Bad Omen inside a raid

        int badOmenLevel = 0;

        if (attacker.hasPotionEffect(PotionEffectType.BAD_OMEN))
            //Bad Omen max level is 5, so we're capping at amplifier 4
            badOmenLevel = Math.min(4, attacker.getPotionEffect(PotionEffectType.BAD_OMEN).getAmplifier() + 1);

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, (20 * 60) * 60 /*1 hour Bad Omen*/, badOmenLevel, false, false, true), true /*Let's force this effect, not sure if it might fail otherwise*/);

        NamespacedKey badOmenKey = NamespacedKey.minecraft("adventure/voluntary_exile");

        Advancement advancement = Bukkit.getAdvancement(badOmenKey);

        if (advancement == null)
            return; //Do nothing, something deleted the advancement

        AdvancementProgress progress = attacker.getAdvancementProgress(advancement);

        for (String criteria : progress.getRemainingCriteria())
            progress.awardCriteria(criteria);
    }
}
