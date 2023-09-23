package me.entity303.excusemethathurt.listener;

import me.entity303.excusemethathurt.ExcuseMeThatHurt;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.TimeUnit;

public class SubstancePlaceListener implements Listener {
    private final ExcuseMeThatHurt plugin;

    public SubstancePlaceListener(ExcuseMeThatHurt plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSuspiciousSubstancePlace(PlayerInteractEvent e) {
        if (e.getHand() == null)
            return; //Not a click

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return; //Not placing anything

        ItemStack itemInHand = e.getPlayer().getInventory().getItem(e.getHand());

        if (itemInHand == null)
            return; //Not item in hand

        String action = null;
        long forgetTime = 0;

        if (itemInHand.getType() == Material.LAVA_BUCKET) {
            if (!this.plugin.getConfiguration().isCheckForLavaDamage())
                return;

            action = "lava";
            forgetTime = this.plugin.getConfiguration().getForgetLava();
        }

        if (itemInHand.getType() == Material.FLINT_AND_STEEL) {
            if (!this.plugin.getConfiguration().isCheckForFireDamage())
                return;

            action = "fire";
            forgetTime = this.plugin.getConfiguration().getForgetFire();
        }

        if (itemInHand.getType() == Material.FIRE_CHARGE) {
            if (!this.plugin.getConfiguration().isCheckForFireDamage())
                return;

            action = "fire";
            forgetTime = this.plugin.getConfiguration().getForgetFire();
        }

        if (action == null)
            return; //Do nothing, this item does not cause an active threat :)

        long range = this.plugin.getConfiguration().getMobNotificationRange();
        for (Entity entity : e.getPlayer().getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Mob))
                continue; //Do nothing, Non-Mobs can't get angry

            Mob mob = (Mob) entity;

            if (!mob.hasLineOfSight(e.getPlayer()))
                continue; //Probably behind a wall or something

            PersistentDataContainer container = mob.getPersistentDataContainer();

            container.set(this.plugin.getSuspiciousActionPersonKey(), PersistentDataType.STRING, e.getPlayer().getUniqueId().toString());
            container.set(this.plugin.getSuspiciousActionKey(), PersistentDataType.STRING, action);
            container.set(this.plugin.getSuspiciousActionForgetKey(), PersistentDataType.LONG, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(forgetTime));
        }
    }
}
