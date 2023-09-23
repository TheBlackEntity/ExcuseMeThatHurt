package me.entity303.excusemethathurt.config;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final long mobNotificationRange;

    private final long maxAttackDistance;

    private final boolean checkForPushDamage;
    private final long forgetPush;

    private final boolean checkForLavaDamage;
    private final long forgetLava;

    private final boolean checkForFireDamage;
    private final long forgetFire;

    private final boolean alertGolems;
    private final long alertGolemsRange;

    private final boolean alertPigmen;
    private final long alertPigmenRange;

    private final boolean badOmen;

    public Config(FileConfiguration configuration) {
        this.mobNotificationRange = configuration.getLong("WarnMobsRange");

        this.maxAttackDistance = configuration.getLong("MaxAttackDistance");

        this.checkForPushDamage = configuration.getBoolean("Threats.Push.Enabled");
        this.forgetPush = configuration.getLong("Threats.Push.Forget");

        this.checkForLavaDamage = configuration.getBoolean("Threats.Lava.Enabled");
        this.forgetLava = configuration.getLong("Threats.Lava.Forget");

        this.checkForFireDamage = configuration.getBoolean("Threats.Fire.Enabled");
        this.forgetFire = configuration.getLong("Threats.Fire.Forget");

        this.alertGolems = configuration.getBoolean("SpecialCases.Villagers.AlertGolems");
        this.alertGolemsRange = configuration.getLong("SpecialCases.Villagers.Range");

        this.alertPigmen = configuration.getBoolean("SpecialCases.ZombifiedPiglin.AlertPigmen");
        this.alertPigmenRange = configuration.getLong("SpecialCases.ZombifiedPiglin.Range");

        this.badOmen = configuration.getBoolean("SpecialCases.Pillager.BadOmen");
    }

    public boolean isCheckForFireDamage() {
        return this.checkForFireDamage;
    }

    public boolean isCheckForLavaDamage() {
        return this.checkForLavaDamage;
    }

    public boolean isCheckForPushDamage() {
        return this.checkForPushDamage;
    }

    public long getMobNotificationRange() {
        return this.mobNotificationRange;
    }

    public long getMaxAttackDistance() {
        return this.maxAttackDistance;
    }

    public long getForgetFire() {
        return this.forgetFire;
    }

    public long getForgetLava() {
        return this.forgetLava;
    }

    public long getForgetPush() {
        return this.forgetPush;
    }

    public boolean isAlertGolems() {
        return this.alertGolems;
    }

    public long getAlertGolemsRange() {
        return this.alertGolemsRange;
    }

    public boolean isAlertPigmen() {
        return this.alertPigmen;
    }

    public long getAlertPigmenRange() {
        return this.alertPigmenRange;
    }

    public boolean isBadOmen() {
        return this.badOmen;
    }
}
