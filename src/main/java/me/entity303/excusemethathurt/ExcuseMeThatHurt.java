package me.entity303.excusemethathurt;

import me.entity303.excusemethathurt.config.Config;
import me.entity303.excusemethathurt.listener.EntityDamageListener;
import me.entity303.excusemethathurt.listener.EntityDeathListener;
import me.entity303.excusemethathurt.listener.PushListener;
import me.entity303.excusemethathurt.listener.SubstancePlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExcuseMeThatHurt extends JavaPlugin implements Listener {
    private final NamespacedKey suspiciousActionPersonKey = new NamespacedKey(this, "suspiciousperson");
    private final NamespacedKey suspiciousActionKey = new NamespacedKey(this, "suspiciousaction");
    private final NamespacedKey suspiciousActionForgetKey = new NamespacedKey(this, "suspiciousforget");
    private Config config;


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();

        this.config = new Config(this.getConfig());

        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);

        //Don't even register listener, if feature is disabled
        if (this.getConfiguration().isCheckForPushDamage())
            Bukkit.getPluginManager().registerEvents(new PushListener(this), this);

        Bukkit.getPluginManager().registerEvents(new SubstancePlaceListener(this), this);

        //Don't even register listener, if feature is disabled
        if (this.getConfiguration().isBadOmen())
            Bukkit.getPluginManager().registerEvents(new EntityDeathListener(this), this);
    }

    public Config getConfiguration() {
        return this.config;
    }

    public NamespacedKey getSuspiciousActionPersonKey() {
        return this.suspiciousActionPersonKey;
    }

    public NamespacedKey getSuspiciousActionKey() {
        return this.suspiciousActionKey;
    }

    public NamespacedKey getSuspiciousActionForgetKey() {
        return this.suspiciousActionForgetKey;
    }
}
