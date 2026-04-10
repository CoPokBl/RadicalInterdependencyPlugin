package net.copokbl.radicalInterdependency.commands;

import net.copokbl.radicalInterdependency.Main;
import net.copokbl.radicalInterdependency.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RadicalInterdependencyCommand implements CommandExecutor, TabCompleter {
    private static final String SUBCOMMANDS = "start, reset, roles, alerts, assign";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String sub : new String[]{"start", "reset", "roles", "alerts", "assign"}) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("start")) {
            String partial = args[args.length - 1].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(partial)) {
                    completions.add(p.getName());
                }
            }
        } else if (args[0].equalsIgnoreCase("assign")) {
            if (args.length == 2) {
                String partial = args[1].toLowerCase();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(partial)) {
                        completions.add(p.getName());
                    }
                }
            } else if (args.length == 3) {
                String partial = args[2].toLowerCase();
                for (net.copokbl.radicalInterdependency.roles.Role role : Main.getInstance().getAllRoles()) {
                    if (role.getId().toLowerCase().startsWith(partial)) {
                        completions.add(role.getId());
                    }
                }
            }
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.t("&aUsage: &6/radicalinterdependency <subcommand>"));
            sender.sendMessage(Utils.t("&aAvailable subcommands: &6" + SUBCOMMANDS));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start": {
                List<Player> players;

                if (args.length > 1) {
                    players = new ArrayList<>();
                    for (int i = 1; i < args.length; i++) {
                        Player p = Bukkit.getPlayer(args[i]);
                        if (p == null || !p.isOnline()) {
                            sender.sendMessage(Utils.t("&cPlayer not found: &6" + args[i]));
                            return true;
                        }
                        players.add(p);
                    }
                } else {
                    players = new ArrayList<>(Bukkit.getOnlinePlayers());
                }

                if (players.isEmpty()) {
                    sender.sendMessage(Utils.t("&cNot enough players to start Radical Interdependency!"));
                    return true;
                }

                Main.getInstance().start(players);
                sender.sendMessage(Utils.t("&aRadical Interdependency has been started for &6" + players.size() + "&a player(s)!"));
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

            case "assign": {
                if (args.length < 3) {
                    sender.sendMessage(Utils.t("&aUsage: &6/ri assign <player> <role>"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    sender.sendMessage(Utils.t("&cPlayer not found: &6" + args[1]));
                    return true;
                }

                net.copokbl.radicalInterdependency.roles.Role role = Main.getInstance().getRole(args[2]);
                if (role == null) {
                    sender.sendMessage(Utils.t("&cUnknown role: &6" + args[2]));
                    return true;
                }

                Main.getInstance().assignRole(target, role);
                sender.sendMessage(Utils.t("&aAssigned &6" + role.getName() + "&a to &6" + target.getName() + "&a!"));
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
