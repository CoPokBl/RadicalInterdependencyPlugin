package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class MinerRole implements Role {

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

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "break blocks");
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "pickup liquids");
    }
}
