package com.birbnest.birblogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.logging.Logger;
import java.util.List;
import org.bukkit.ChatColor;

public class Broadcast {
    private int currentIndex = 0;
    private JavaPlugin plugin;

    public Broadcast(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startBroadcast() {
        Logger logger = plugin.getLogger();
        logger.info("Broadcast started");
        
        int interval = plugin.getConfig().getInt("broadcast_interval") * 20; // Convert to ticks
        String prefix = plugin.getConfig().getString("broadcast_prefix") + " ";
        
        List<String> messages = plugin.getConfig().getStringList("broadcast_list");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (currentIndex >= messages.size()) {
                    currentIndex = 0; // Reset index if it's out of bounds
                }

            String message = ChatColor.translateAlternateColorCodes('&', prefix + messages.get(currentIndex));
                Bukkit.broadcastMessage(message);
                currentIndex++;
            }
        }.runTaskTimer(plugin, interval, interval);
    }

    // Broadcast command
    public void broadcast(String message) {
        String prefix = plugin.getConfig().getString("broadcast_prefix") + " ";
        String broadcastMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        Bukkit.broadcastMessage(broadcastMessage);
    }
}