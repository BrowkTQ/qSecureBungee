package me.Browk.qSecureBungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class ConfigHandler {

    static ConfigHandler instance = new ConfigHandler();
    Configuration config;

    public static ConfigHandler getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        File configFile = new File(p.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = p.getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(p.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ProxyServer.getInstance().getLogger().severe("A avut loc o eroare in timp ce se incarca configuratia.");
        }

        if(config.get("mysql.host") == null)
            config.set("mysql.host", "0.0.0.0");
        if(config.get("mysql.port") == null)
            config.set("mysql.port", "3306");
        if(config.get("mysql.database") == null)
            config.set("mysql.database", "numeBazaDeDate");
        if(config.get("mysql.user") == null)
            config.set("mysql.user", "userBazaDeDate");
        if(config.get("mysql.password") == null)
            config.set("mysql.password", "parolaUser");
        saveConfig(p);
    }

    public void saveConfig(Plugin p) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(p.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ProxyServer.getInstance().getLogger().severe("A avut loc o eroare in timp ce se salva configuratia.");
        }
    }

    public Configuration getConfig() {
        return config;
    }
}