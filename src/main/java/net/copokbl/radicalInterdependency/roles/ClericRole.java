package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ClericRole implements Role {

    @Override
    public String getId() {
        return "cleric";
    }

    @Override
    public String getName() {
        return "Cleric";
    }

    @Override
    public String getDescription() {
        return "You are the only player who can use inventories.";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (Main.getInstance().hasRole(p, getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(p, "interact with inventories");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "drop items");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (Main.getInstance().hasRole(e.getPlayer(), getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(e.getPlayer(), "swap items");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) {
            return;
        }

        if (Main.getInstance().hasRole(p, getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(p, "pick up items");
    }
}
