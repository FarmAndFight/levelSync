package fr.topeka.levelSync.sql;

import java.sql.*;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.topeka.levelSync.Main;


public class Sql {

	public Connection connection;
	private Statement statement;
	
	private Main plugin;
	
	private String host, database, table, username, password;
	private int port;
	
	public Sql(Main plugin) throws ClassNotFoundException, SQLException{
		this.plugin = plugin;
		this.loadConfig();
		Class.forName("com.mysql.jdbc.Driver");
		this.connect();
	}
	
	private void connect() throws SQLException{
		this.connection = DriverManager.getConnection("jdbc:mysql://"+ host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
		this.statement = connection.createStatement();
		this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + this.table + "`("
				+ "`uuid` VARCHAR(36) NOT NULL,"
				+ "`experience` FLOAT(2,1),"
				+ "`level` INT(6)"
				+ ");");
	}
	
	private void loadConfig() {
		this.host = plugin.getConfig().getString("mysql.host");
		this.port = plugin.getConfig().getInt("mysql.port");
		this.username = plugin.getConfig().getString("mysql.username");
		this.password = plugin.getConfig().getString("mysql.password");
		this.database = plugin.getConfig().getString("mysql.database");
		this.table = plugin.getConfig().getString("mysql.table");
	}
	
	public float[] getPlayerLevel(Player player) throws SQLException{
		
		UUID uuid = player.getUniqueId();
		ResultSet result = statement.executeQuery("SELECT level, experience FROM " + this.table + " WHERE uuid=\"" + uuid.toString() + "\"");
		while(result.next()) {
			// level, exp
			float [] values = {result.getInt(1), result.getFloat(2)};
			return values;
		}
		float[] values = {0, 0};
		return values;
	}
	
	public void setPlayerLevel(Player player) throws SQLException{
		UUID uuid = player.getUniqueId();
		int level = player.getLevel();
		float xp = player.getExp();
		ResultSet result = statement.executeQuery("SELECT level, experience FROM " + this.table + " WHERE uuid=\"" + uuid.toString() + "\"");
		if(!result.next()) {
			// no record need insert
			statement.executeUpdate("INSERT INTO " + this.table + "(uuid, experience, level) VALUES (\"" + uuid.toString() + "\", " + xp + ", " + level + ")");

		}else {
			// record need update
			statement.executeUpdate("UPDATE " + this.table + " SET experience=\"" + xp + "\", level=" + level + " WHERE uuid=\"" + uuid.toString() + "\"");

		}
	}
	
	public boolean checkConnection() {
		try {
			if(connection == null || connection.isClosed() || !connection.isValid(3)) {
				return reConnect();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.getLogger().warning("Could not reconnect to database ! error: " + e.getMessage());
			return false;
		}
	}
	
	public boolean reConnect() {
		try {
			this.connect();
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void closeConnection() {
		try {
			if(!connection.isClosed()) {
				connection.close();
				connection = null;
			}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	}
}
