package me.iangry.afk;

import com.earth2me.essentials.Essentials;
import java.util.ArrayList;
import java.util.logging.Logger;
import net.ess3.api.IUser;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static ArrayList<String> afk = new ArrayList<>();

    public static Main plugin;

    protected FileConfiguration config;

    static Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    public void onDisable() {
        ConsoleCommandSender clogger = getServer().getConsoleSender();
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lAFKCommand &7" + getDescription().getVersion()));
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDeveloper: &7iAngry"));
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lDisabled"));
        saveConfig();
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)this);
        ConsoleCommandSender clogger = getServer().getConsoleSender();
        int pluginId = 71723; // <-- Replace with the id of your plugin!
        MetricsLite metrics = new MetricsLite(this, pluginId);
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lAFKCommand &7" + getDescription().getVersion()));
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDeveloper: &7iAngry#0932"));
        clogger.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lEnabled"));
        if (getConfig().getBoolean("checkUpdates")) {
            UpdateChecker updater = new UpdateChecker(this, 53419);
            try {
                if (updater.checkForUpdates()) {
                    getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "You are using an older version of §bAFK§3§lCommand");
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Download the newest version here:");
                    getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "§n§ohttps://shorturl.at/ikpyA");
                    getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                    Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(operator -> operator.sendMessage("§3There is now a newer version of §bAFK§3§lCommand"));
                    Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(operator -> operator.sendMessage("§3Download the newest version here:"));
                    Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(operator -> operator.sendMessage("§b§o§nhttps://shorturl.at/ikpyA"));
                } else {
                    getServer().getConsoleSender().sendMessage("AFKCommands is up to date: " +
                            getDescription().getVersion());
                }
            } catch (Exception e) {
                getLogger().info("Could not check for updates! Stacktrace:");
                e.printStackTrace();
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (alias.equalsIgnoreCase("AFKCommand"));
        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&aAFKCommand&2]"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDeveloper: &7iAngry#0932"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aVersion: &7: "+ getDescription().getVersion()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCommands: &7/AFKCommand Reload"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload"));
        if (sender.hasPermission("afkcommand.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&aAFKCommand&2]"));
                    reloadConfig();
            saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReload Complete"));
        }
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAFKStatusChange(AfkStatusChangeEvent e) {
        IUser iUser = e.getAffected();
            if (e.getValue()) {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), getConfig().getString("command-on-afk").replaceAll("%player%", iUser.getName()));
                afk.add(iUser.getName());
                if (getConfig().getBoolean("disable-message")) {
                    return;
                }else {
                    iUser.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("message-to-player-on-afk").replaceAll("%player%", iUser.getName())));
                }
            }
        }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (afk.contains(player.getName()) &&
                event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), getConfig().getString("command-on-unafk").replaceAll("%player%", player.getPlayer().getName()));
            afk.remove(player.getName());
            if (getConfig().getBoolean("disable-message")) {
                return;
            }else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("message-to-player-on-unafk").replaceAll("%player%", player.getPlayer().getName())));
            }
        }
    }
}
