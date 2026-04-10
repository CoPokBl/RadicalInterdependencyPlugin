package net.copokbl.radicalInterdependency;

import net.copokbl.radicalInterdependency.commands.RadicalInterdependencyCommand;
import net.copokbl.radicalInterdependency.roles.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class Main extends JavaPlugin {
    private static Main instance;
    private static final Role[] ROLES = {
            new ClericRole(),
            new MinerRole(),
            new BuilderRole(),
            new MercenaryRole()
    };

    private YamlConfiguration data;
    private YamlConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        RadicalInterdependencyCommand cmdHandler = new RadicalInterdependencyCommand();
        Objects.requireNonNull(getCommand("radicalinterdependency")).setExecutor(cmdHandler);
        Objects.requireNonNull(getCommand("radicalinterdependency")).setTabCompleter(cmdHandler);

        File dataFile = new File(getDataFolder(), "data.yml");
        File configFile = new File(getDataFolder(), "config.yml");
        if (!dataFile.exists()) {
            boolean ignored = dataFile.getParentFile().mkdirs();
            try {
                boolean ignored2 = dataFile.createNewFile();
                boolean ignored3 = configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        data = new YamlConfiguration();
        config = new YamlConfiguration();
        try {
            data.load(dataFile);
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        if (!config.contains("share-health")) {
//            config.set("share-health", true);
            saveConfig();
        }

        if (config.getBoolean("share-health", false)) {
            Bukkit.getPluginManager().registerEvents(new ShareStatsHandler(), this);
        }

        for (Role role : ROLES) {
            role.registerEvents();
        }

        getLogger().info("Radical Interdependency has been enabled!");
    }

    public void saveData() {
        try {
            data.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getRoles(Player p) {
        return data.getStringList("players." + p.getUniqueId() + ".roles").toArray(String[]::new);
    }

    public boolean hasRole(Player p, String role) {
        String[] roles = getRoles(p);
        return roles.length > 0 && Arrays.asList(roles).contains(role);
    }

    public void assignRole(Player p, Role role) {
        List<String> roles = new ArrayList<>(Arrays.stream(getRoles(p)).toList());
        roles.add(role.getId());
        data.set("players." + p.getUniqueId() + ".roles", roles);
        saveData();

        p.sendMessage(Utils.t("&aYou have been assigned: &6" + role.getName()));
        p.sendMessage(Utils.t("&a" + role.getDescription()));
    }

    public boolean hasAlertsEnabled(Player p) {
        return data.getBoolean("players." + p.getUniqueId() + ".alerts", true);
    }

    public void setAlertsEnabled(Player p, boolean enabled) {
        data.set("players." + p.getUniqueId() + ".alerts", enabled);
        saveData();
    }

    public void alert(Player p, String message) {
        if (hasAlertsEnabled(p)) {
            p.sendMessage(Utils.t(message));
        }
    }

    public void start(List<Player> players) {
        Random random = ThreadLocalRandom.current();

        List<Role> roleList = new ArrayList<>(Arrays.asList(ROLES));
        roleList.sort(Comparator.comparingInt(r -> random.nextInt()));

        List<Player> shuffledPlayers = new ArrayList<>(players);
        shuffledPlayers.sort(Comparator.comparingInt(r -> random.nextInt()));

        int max = Math.max(roleList.size(), shuffledPlayers.size());
        for (int i = 0; i < max; i++) {
            Player p = shuffledPlayers.get(i % shuffledPlayers.size());
            Role r = roleList.get(i % roleList.size());
            assignRole(p, r);
        }
    }

    public void reset() {
        data.set("players", null);
        saveData();
    }

    public Role getRole(String id) {
        for (Role role : ROLES) {
            if (role.getId().equalsIgnoreCase(id)) {
                return role;
            }
        }
        return null;
    }

    public Role[] getAllRoles() {
        return ROLES;
    }

    public int getRoleCount() {
        return ROLES.length;
    }

    public static Main getInstance() {
        return instance;
    }
}
