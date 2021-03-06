package io.github.dsh105.echopet.config.options;

import io.github.dsh105.echopet.config.YAMLConfig;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.entity.living.data.PetType;
import org.bukkit.Bukkit;


public class ConfigOptions extends Options {

    public static ConfigOptions instance;

    public ConfigOptions(YAMLConfig config) {
        super(config);
        instance = this;
        this.setDefaults();
    }

    public boolean allowPetType(PetType petType) {
        return this.config.getBoolean("pets."
                + petType.toString().toLowerCase().replace("_", " ") + ".enable", true);
    }

    public boolean allowMounts(PetType petType) {
        if (petType == PetType.ENDERDRAGON) {
            return false;
        }
        return this.config.getBoolean("pets."
                + petType.toString().toLowerCase().replace("_", " ") + ".allow.mounts", true);
    }

    public boolean allowData(PetType type, PetData data) {
        return this.config.getBoolean("pets." + type.toString().toLowerCase().replace("_", " ")
                + ".allow." + data.getConfigOptionString(), true);
    }

    public boolean forceData(PetType type, PetData data) {
        return this.config.getBoolean("pets." + type.toString().toLowerCase().replace("_", " ")
                + ".force." + data.getConfigOptionString(), false);
    }

    public boolean canFly(PetType petType) {
        return this.config.getBoolean("pets." + petType.toString().toLowerCase().replace("_", " ")
                + ".canFly", false);
    }

    public Object getConfigOption(String s) {
        return this.config.get(s);
    }

    public Object getConfigOption(String s, Object def) {
        return this.config.get(s, def);
    }

    public String getCommandString() {
        return this.config.getString("commandString", "pet");
    }

    public float getRideSpeed(PetType petType) {
        return (float) this.config.getDouble("pets." + petType.toString().toLowerCase().replace("_", " ") + ".rideSpeed", 0.25D);
    }

    public double getRideJumpHeight(PetType petType) {
        return this.config.getDouble("pets." + petType.toString().toLowerCase().replace("_", " ") + ".rideJump", 0.6D);
    }

    public boolean useSql() {
        return this.config.getBoolean("sql.use", false);
    }

    public boolean sqlOverride() {
        if (useSql()) {
            return this.config.getBoolean("sql.overrideFile");
        }
        return false;
    }

    @Override
    public void setDefaults() {
        set("commandString", "pet");

        set("autoUpdate", false, "If set to true, EchoPet will automatically download and install", "new updates.");
        set("checkForUpdates", true, "If -autoUpdate- is set to false, EchoPet will notify certain", "players of new updates if they are available (if set to true).");

        set("sql.overrideFile", true, "If true, Pets saved to a MySQL Database will override", "those saved to a file (Default and AutoSave Pets)");
        set("sql.timeout", 30);
        set("sql.use", false);
        set("sql.host", "localhost");
        set("sql.port", 3306);
        set("sql.database", "EchoPet");
        set("sql.username", "none");
        set("sql.password", "none");

        set("petSelector.giveOnJoin.enable", false);
        set("petSelector.giveOnJoin.usePerm", false);
        set("petSelector.giveOnJoin.perm", "echopet.selector.join");
        set("petSelector.giveOnJoin.slot", 9);
        set("petSelector.clearInvOnJoin", false);

        set("autoSave", true, "If true, EchoPet will autosave all pet data to prevent data", "loss in the event of a server crash.");
        set("autoSaveTimer", 180, "Interval between autosave of pet data (in seconds).");
        set("loadSavedPets", true, "Auto-load pets from last session");
        set("multiworldLoadOverride", true, "When true, if -loadSavedPets-", "is set to false, Pets will", "still be loaded when", "players switch worlds");

        set("sendLoadMessage", true, "Send message that pet was loaded if -loadSavedPets- is true");
        set("sendForceMessage", true, "For all data values forced, EchoPet will notify the player", "(if set to true).");

        set("worlds." + Bukkit.getWorlds().get(0).getName(), true);
        set("worlds.enableByDefault", true);

        if (config.getConfigurationSection("worldguard.regions") == null) {
            set("worldguard.regions.echopet", true);
        }
        set("worldguard.regions.allowByDefault", true);
        set("worldguard.regionEnterCheck", true);

        for (PetType petType : PetType.values()) {
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".enable", true);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".tagVisible", true);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".defaultName", petType.getDefaultName());
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".interactMenu", true);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".startFollowDistance", 12);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".stopFollowDistance", 8);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".teleportDistance", 50);

            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.canDamagePlayers", false);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.lockRange", 10);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.ticksBetweenAttacks", 20);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.damage", petType.getAttackDamage());
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.loseHealth", false);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".attack.health", petType.getMaxHealth());

            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".rideSpeed", 0.25D);
            set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".jumpHeight", 0.6D);

            if (petType != PetType.ENDERDRAGON) {
                boolean canFly = (petType == PetType.BAT || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER);
                set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".canFly", canFly);
                set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow.mounts", canFly);
            }

            for (PetData pd : PetData.values()) {
                if (petType.isDataAllowed(pd)) {
                    set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".allow." + pd.getConfigOptionString(), true);
                    set("pets." + petType.toString().toLowerCase().replace("_", " ") + ".force." + pd.getConfigOptionString(), false);
                }
            }
        }

        config.saveConfig();
    }
}