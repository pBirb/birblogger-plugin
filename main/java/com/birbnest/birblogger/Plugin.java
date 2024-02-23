package com.birbnest.birblogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;

public class Plugin extends JavaPlugin implements Listener {
    private static final Logger LOGGER = Logger.getLogger("birblogger");
    private static Plugin instance;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        if (getConfig().getBoolean("logging")) {
            LOGGER.info("Logging enabled");
        } else {
            LOGGER.info("Logging disabled");
        }
    }
private Properties advancementNames;

@Override
public void onEnable() {
    instance = this;
    advancementNames = new Properties();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("advancement_names.properties")) {
        advancementNames.load(in);
    } catch (IOException e) {
        getLogger().severe("Failed to load advancement names");
        e.printStackTrace();
    }

    if (getConfig().getBoolean("logging")) {
        LOGGER.info("Logging enabled");
    } else {
        LOGGER.info("Logging disabled");
    }

    // Register event handlers
    getServer().getPluginManager().registerEvents(this, this);
     // Start the broadcast
        Broadcast broadcast = new Broadcast(this);
        broadcast.startBroadcast();
}

    public static Plugin getInstance() {
        return instance;
    }

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("birblogger")) {
        if (!sender.hasPermission("birblogger.use")) {
            sender.sendMessage("You do not have permission to use this command");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /birblogger enable | disable");
            return false;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            LOGGER.info("Logging enabled");
            sender.sendMessage("Logging enabled");
            getConfig().set("logging", true);
            return true;
        } else if (args[0].equalsIgnoreCase("disable")) {
            LOGGER.info("Logging disabled");
            sender.sendMessage("Logging disabled");
            getConfig().set("logging", false);
            return true;
        } else {
            sender.sendMessage("Usage: /birblogger enable | disable");
            return false;
        }
} else if (command.getName().equalsIgnoreCase("broadcast")) {
    if (!sender.hasPermission("birblogger.broadcast")) {
        sender.sendMessage("You do not have permission to use this command");
        return false;
    }

    if (args.length == 0) {
        sender.sendMessage("Usage: /broadcast <message>");
        return false;
    }

    // Retrieve the prefix from the configuration
    String prefix = getConfig().getString("broadcast_prefix");
    if (prefix == null) {
        prefix = "";
    } else {
        prefix += " ";
    }

    // Join the arguments into a single string to form the message
    String message = String.join(" ", args);

    // Add the prefix to the message and translate color codes
    String broadcastMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);

    // Broadcast the message to all players
    getServer().broadcastMessage(broadcastMessage);

    return true;
}

return false;
}
@EventHandler
public void onPlayerChat(AsyncPlayerChatEvent event) {
    List<String> filteredWords = getConfig().getStringList("filteredwordlist");

    if (Filter.containsFilteredWord(event.getMessage(), filteredWords)) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "Your message contains a filtered word.");
    } else if (getConfig().getBoolean("logging")) {
        final String message = String.format("%s: %s", event.getPlayer().getName(), event.getMessage()).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                Discord.sendMessage(message);
                Discord.sendMessageAdmin(message);
            }
        }.runTaskAsynchronously(this);
    }
}
// Player command
@EventHandler
public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    if (getConfig().getBoolean("logging")) {
        final String message = String.format("%s: %s", event.getPlayer().getName(), event.getMessage()).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getMessage().startsWith("/")) {
                    Discord.sendMessageAdmin(message);
                } else {
                    Discord.sendMessage(message);
                }
            }
        }.runTaskAsynchronously(this);
    }
}
// Player death
@EventHandler
public void onPlayerDeath(PlayerDeathEvent event) {
    if (getConfig().getBoolean("logging")) {
        final String message = String.format("%s died: %s", event.getEntity().getName(), event.getDeathMessage()).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                Discord.sendMessage(message);
                Discord.sendMessageAdmin(message);
            }
        }.runTaskAsynchronously(this);
    }
}
// Player quit 
@EventHandler
public void onPlayerQuit(PlayerQuitEvent event) {
    if (getConfig().getBoolean("logging")) {
        final String message = String.format("%s left the game", event.getPlayer().getName()).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                Discord.sendMessage(message);
                Discord.sendMessageAdmin(message);
            }
        }.runTaskAsynchronously(this);
    }
}
// Player join
@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    if (getConfig().getBoolean("logging")) {
        final String message = String.format("%s joined the game", event.getPlayer().getName()).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                Discord.sendMessage(message);
                Discord.sendMessageAdmin(message);
            }
        }.runTaskAsynchronously(this);
    }
}
// Player achievement/adavancement
@EventHandler
public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
    if (getConfig().getBoolean("logging")) {
        String advancementKey = event.getAdvancement().getKey().getKey();

        // Ignore recipe unlocks
        if (advancementKey.startsWith("recipes/")) {
            return;
        }

        String advancementName = getAdvancementName(advancementKey);

        final String message = String.format("%s has made the advancement [%s]", event.getPlayer().getName(), advancementName).replace("@", "@\u200B");
        LOGGER.info(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                Discord.sendMessage(message);
                Discord.sendMessageAdmin(message);
            }
        }.runTaskAsynchronously(this);
    }
}

private String getAdvancementName(String key) {
    return advancementNames.getProperty(key, key);
}
}