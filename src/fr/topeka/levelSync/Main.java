package fr.topeka.levelSync;

import java.sql.SQLException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.topeka.levelSync.listener.EventsListener;
import fr.topeka.levelSync.sql.Sql;
//import fr.topeka.levelSync.tasks.SaveDataTask;

/**
 * 
 * @author Topeka_
 */
public class Main extends JavaPlugin{

	private Sql _sql;
	public String msg_syncError, msg_dbError;
	
	
	@Override
	public void onEnable() {
		try {
			this.saveDefaultConfig();
			_sql = new Sql(this);
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvents(new EventsListener(this, _sql), this);
			this.getLogger().info("[levelSync] Plugin enabled");
			msg_syncError = getConfig().getString("message.syncError");
			msg_dbError = getConfig().getString("message.dbError");
//			SaveDataTask task = new SaveDataTask(this);
//			task.runTaskTimerAsynchronously(this, 6000, 3600);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			this.getLogger().warning("An error occured during loading, disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		_sql.closeConnection();
		this.getLogger().info("Plugin disabled");
	}

	public Sql getSql() {
		return _sql;
	}
}
