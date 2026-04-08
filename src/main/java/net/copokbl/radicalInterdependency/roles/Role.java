package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface Role extends Listener {
    String getId();
    String getName();
    String getDescription();

    default void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    default void denyAlert(Player p, String action) {
        Main.getInstance().alert(p, "&cYou cannot " + action + " because you are not the " + getName() + "!");
    }
}
