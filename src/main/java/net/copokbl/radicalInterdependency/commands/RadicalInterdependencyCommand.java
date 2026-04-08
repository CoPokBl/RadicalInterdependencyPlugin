package net.copokbl.radicalInterdependency.commands;

import net.copokbl.radicalInterdependency.Main;
import net.copokbl.radicalInterdependency.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RadicalInterdependencyCommand implements CommandExecutor {
    private static final String SUBCOMMANDS = "start, reset, roles, alerts";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.t("&aUsage: &6/radicalinterdependency <subcommand>"));
            sender.sendMessage(Utils.t("&aAvailable subcommands: &6" + SUBCOMMANDS));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start": {
                if (Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                    sender.sendMessage(Utils.t("&cNot enough players online to start Radical Interdependency!"));
                    return true;
                }

                Main.getInstance().start();
                sender.sendMessage(Utils.t("&aRadical Interdependency has been started!"));
                break;
            }

            case "reset": {
                Main.getInstance().reset();
                sender.sendMessage(Utils.t("&cRadical Interdependency has been reset!"));
                break;
            }

            case "roles": {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(Utils.t("&cYou're the console, you don't have any roles."));
                    return true;
                }

                sender.sendMessage(Utils.t("&aYour roles:"));
                for (String roleId : Main.getInstance().getRoles(p)) {
                    sender.sendMessage(Utils.t("&a- &6" + roleId));
                }
                break;
            }

            case "alerts": {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(Utils.t("&cYou're the console, you don't have alerts."));
                    return true;
                }

                boolean enabled = Main.getInstance().hasAlertsEnabled(p);
                Main.getInstance().setAlertsEnabled(p, !enabled);
                sender.sendMessage(Utils.t("&aAlerts have been &6" + (!enabled ? "enabled" : "disabled") + "&a!"));
                break;
            }

            default: {
                sender.sendMessage(Utils.t("&cUnknown subcommand. Available subcommands: &6" + SUBCOMMANDS));
                return true;
            }
        }
        return true;
    }
}
