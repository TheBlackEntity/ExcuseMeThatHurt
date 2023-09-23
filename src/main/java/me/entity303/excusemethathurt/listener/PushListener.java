package me.entity303.excusemethathurt.listener;

import me.entity303.excusemethathurt.ExcuseMeThatHurt;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.TimeUnit;

public class PushListener implements Listener {
    private final ExcuseMeThatHurt plugin;

    public PushListener(ExcuseMeThatHurt plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPush(PlayerMoveEvent e) {
        //We count every player in a radius of .1 x .1 x .1 as a pushing player
        for (Entity entity : e.getPlayer().getNearbyEntities(.1, .1, .1)) {
            if (!(entity instanceof Mob))
                continue; //Do nothing, a non-mob can't get angry

            long forgetTime = this.plugin.getConfiguration().getForgetPush();

            Mob mob = (Mob) entity;

            PersistentDataContainer container = mob.getPersistentDataContainer();

            container.set(this.plugin.getSuspiciousActionPersonKey(), PersistentDataType.STRING, e.getPlayer().getUniqueId().toString());
            container.set(this.plugin.getSuspiciousActionKey(), PersistentDataType.STRING, "push");
            container.set(this.plugin.getSuspiciousActionForgetKey(), PersistentDataType.LONG, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(forgetTime));
        }
    }
}
