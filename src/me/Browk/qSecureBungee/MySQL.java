package me.Browk.qSecureBungee;

import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class MySQL {
    private Connection connection;
    private String url;
    private String username;
    private String password;
    private String TABLE;
    public String UPDATE;
    public String UPDATE1;

    public MySQL(final String tableprefix, final String host, final String port, final String database, final String username, final String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
        this.TABLE = "CREATE TABLE IF NOT EXISTS " + tableprefix + "_sesiune (nume VARCHAR(255), statut_sesiune VARCHAR(255), pin VARCHAR(255), discordID VARCHAR(255))";
        this.UPDATE = "UPDATE " + tableprefix + "_sesiune SET statut_sesiune=? WHERE nume=?";
        this.UPDATE1 = "UPDATE " + tableprefix + "_sesiune SET statut_sesiune='neconectat'";
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(this.url, this.username, this.password);
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null || !this.connection.isValid(5)) {
            this.connect();
        }
        return this.connection;
    }

    public void setupTable() {
        try {
            this.getConnection().createStatement().executeUpdate(this.TABLE);
        } catch (SQLException e) {
            ProxyServer.getInstance().getLogger().severe("Nu s-a putut realiza conexiunea la baza de date! Se va reincerca realizarea conexiunii in 30 de secunde!");
            ProxyServer.getInstance().getScheduler().schedule(Main.instance, (Runnable)new Runnable() {
                @Override
                public void run() {
                    setupTable();
                }
            }, 30, TimeUnit.SECONDS);
        }
        ProxyServer.getInstance().getLogger().info("Baza de date a fost incarcata cu succes!");
    }
}
