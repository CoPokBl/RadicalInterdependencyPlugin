package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MercenaryRole implements Role, Listener {

    @Override
    public String getId() {
        return "mercenary";
    }

    @Override
    public String getName() {
        return "Mercenary";
    }

    @Override
    public String getDescription() {
        return "You are the only player who can deal damage to entities.";
    }

    @Override
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlace(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) {
            return;
        }

        if (Main.getInstance().hasRole(p, getId())) {
            return;
        }

        e.setCancelled(true);
        Main.getInstance().alert(p, "&cYou cannot deal damage because you are not the " + getName() + "!");
    }
}
