package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BuilderRole implements Role, Listener {

    @Override
    public String getId() {
        return "builder";
    }

    @Override
    public String getName() {
        return "Builder";
    }

    @Override
    public String getDescription() {
        return "You are the only player who can place blocks.";
    }

    @Override
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        Main.getInstance().alert(e.getPlayer(), "&cYou cannot place blocks because you are not the " + getName() + "!");
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        Main.getInstance().alert(e.getPlayer(), "&cYou cannot place liquids because you are not the " + getName() + "!");
    }
}
