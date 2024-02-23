package com.birbnest.birblogger;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.logging.Logger;

public class Discord {
       private static final Logger LOGGER = Logger.getLogger("birblogger");

    public static String getWebhookURL() {
        Plugin plugin = Plugin.getInstance();
        if (plugin != null) {
            return plugin.getConfig().getString("discord.webhook");
        } else {
            return null;
        }
    }
    public static String getAdminWebhookURL() {
        Plugin plugin = Plugin.getInstance();
        if (plugin != null) {
            return plugin.getConfig().getString("discord.adminwebhook");
        } else {
            return null;
        }
    }
   
     
    
public static void sendMessage(String message) {
    String webhookURL = getWebhookURL();
    if (webhookURL != null) {
        try {
            URI uri = new URI(webhookURL);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-Bot");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String json = String.format("{\"content\": \"%s\"}", message);
            OutputStream stream = connection.getOutputStream();
            stream.write(json.getBytes());
            stream.flush();
            stream.close();
            connection.getInputStream().close(); 
            connection.disconnect();
        } catch (IOException e) {
            LOGGER.severe("Failed to send to webhook, double check the config that you are using a valid webhook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
public static void sendMessageAdmin (String message) {
    String webhookURL = getWebhookURL();
    if (webhookURL != null) {
        try {
            URI uri = new URI(webhookURL);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-Bot");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String json = String.format("{\"content\": \"%s\", \"username\": \"Admin\"}", message);
            OutputStream stream = connection.getOutputStream();
            stream.write(json.getBytes());
            stream.flush();
            stream.close();
            connection.getInputStream().close(); 
            connection.disconnect();
        } catch (IOException e) {
            LOGGER.severe("Failed to send to webhook, double check the config that you are using a valid webhook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}
