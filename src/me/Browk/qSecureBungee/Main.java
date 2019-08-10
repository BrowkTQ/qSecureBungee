package me.Browk.qSecureBungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends Plugin implements Listener {

    public static Main instance;
    public static MySQL mysql;
    private ConfigHandler settings = ConfigHandler.getInstance();
    private ArrayList<String> listase = new ArrayList<String>();

    public void onEnable() {
        instance = this;
        settings.setup(this);
        getProxy().getPluginManager().registerListener(this, this);
        loadDatabase();
        try {
            PreparedStatement preparedStatement = mysql.getConnection().prepareStatement(mysql.UPDATE1);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        startMessage();
    }

    public void onDisable() {
        listase.clear();
    }

    @SuppressWarnings("deprecation")
    private void startMessage() {
        CommandSender c = getProxy().getConsole();
        c.sendMessage(ChatColor.YELLOW + "");
        c.sendMessage(ChatColor.YELLOW + "         _____");
        c.sendMessage(ChatColor.YELLOW + "        /  ___|");
        c.sendMessage(ChatColor.YELLOW + "   __ _ \\ `--.   ___   ___  _   _  _ __  ___");
        c.sendMessage(ChatColor.YELLOW + "  / _` | `--. \\ / _ \\ / __|| | | || '__|/ _ \\");
        c.sendMessage(ChatColor.YELLOW + " | (_| |/\\__/ /|  __/| (__ | |_| || |  |  __/");
        c.sendMessage(ChatColor.YELLOW + "  \\__, |\\____/  \\___| \\___| \\__,_||_|   \\___|");
        c.sendMessage(ChatColor.YELLOW + "     | |");
        c.sendMessage(ChatColor.YELLOW + "     |_|");
        c.sendMessage(ChatColor.YELLOW + "");
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        String playername = e.getPlayer().getName();
        if(!listase.contains(playername)) {
            return;
        }
        try {
            dbUpdate(playername);
            listase.remove(playername);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDatabase() {
        if (this.mysql != null) {
            this.mysql = null;
        }
        getLogger().info("Se realizeaza conexiunea la baza de date!");
        (this.mysql = new MySQL("qSecure", settings.getConfig().getString("mysql.host"), settings.getConfig().getString("mysql.port"), settings.getConfig().getString("mysql.database"), settings.getConfig().getString("mysql.user"), settings.getConfig().getString("mysql.password"))).setupTable();
    }

    private void dbUpdate(String playername) throws SQLException {
        PreparedStatement preparedStatement = mysql.getConnection().prepareStatement(mysql.UPDATE);
        preparedStatement.setString(1, "neconectat");
        preparedStatement.setString(2, playername);
        preparedStatement.execute();
        preparedStatement.close();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF();
                if(channel.equals("qSecure")){
                    String input = in.readUTF();
                    listase.add(input);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

}
