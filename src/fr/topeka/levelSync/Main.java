package fr.topeka.levelSync;

import java.sql.SQLException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.topeka.levelSync.listener.EventsListener;
import fr.topeka.levelSync.sql.Sql;

/**
 * 
 * @author Topeka_
 */
public class Main extends JavaPlugin{

	Sql sql;
	
	@Override
	public void onEnable() {
		try {
			this.saveDefaultConfig();
			sql = new Sql(this);
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvents(new EventsListener(this, sql), this);
			this.getLogger().info("[levelSync] Plugin enabled");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			this.getLogger().warning("An error occured during loading, disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		sql.closeConnection();
		this.getLogger().info("Plugin disabled");
	}
}
