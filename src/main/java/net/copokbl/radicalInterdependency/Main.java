package net.copokbl.radicalInterdependency;

import net.copokbl.radicalInterdependency.commands.RadicalInterdependencyCommand;
import net.copokbl.radicalInterdependency.roles.*;
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
            new MinerRole(),
            new ClericRole(),
            new MercenaryRole(),
            new BuilderRole()
    };

    private YamlConfiguration data;

    @Override
    public void onEnable() {
        instance = this;
        Objects.requireNonNull(getCommand("radicalinterdependency")).setExecutor(new RadicalInterdependencyCommand());

        File dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
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

    public void start() {
        Player[] players = getServer().getOnlinePlayers().toArray(new Player[0]);
        Random random = ThreadLocalRandom.current();

        List<Role> roleList = Arrays.asList(ROLES);
        roleList.sort(Comparator.comparingInt(r -> random.nextInt()));

        for (int i = 0; i < roleList.size(); i++) {
            Player p = players[i % players.length];
            Role r = roleList.get(i);
            assignRole(p, r);
        }
    }

    public void reset() {
        data.set("players", null);
        saveData();
    }

    public int getRoleCount() {
        return ROLES.length;
    }

    public static Main getInstance() {
        return instance;
    }
}
