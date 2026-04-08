package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BuilderRole implements Role {

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

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "place blocks");
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "empty buckets");
    }
}
