package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class MinerRole implements Role, Listener {

    @Override
    public String getId() {
        return "miner";
    }

    @Override
    public String getName() {
        return "Miner";
    }

    @Override
    public String getDescription() {
        return "You are the only player who can break blocks.";
    }

    @Override
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        Main.getInstance().alert(e.getPlayer(), "&cYou cannot break blocks because you are not the " + getName() + "!");
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        Main.getInstance().alert(e.getPlayer(), "&cYou cannot pickup liquids because you are not the " + getName() + "!");
    }
}
