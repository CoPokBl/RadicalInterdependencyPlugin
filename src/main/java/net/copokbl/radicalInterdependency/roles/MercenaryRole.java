package net.copokbl.radicalInterdependency.roles;

import net.copokbl.radicalInterdependency.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class MercenaryRole implements Role {

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

    private void onDamage(Cancellable e, Entity dmger) {
        Player p;

        if (dmger instanceof Player pl) {
            p = pl;
        } else if (dmger instanceof Projectile pr && pr.getShooter() != null && pr.getShooter() instanceof Player pl2) {
            p = pl2;
        } else {
            return;
        }

        if (Main.getInstance().hasRole(p, getId())) {
            return;
        }

        e.setCancelled(true);
        denyAlert(p, "deal damage");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        onDamage(e, e.getDamager());
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent e) {
        onDamage(e, e.getAttacker());
    }
}
