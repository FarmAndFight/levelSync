package fr.topeka.levelSync.listener;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.topeka.levelSync.Main;
import fr.topeka.levelSync.sql.Sql;

public class EventsListener implements Listener {
	
	private Main plugin;
	private Sql sql;
	
	public EventsListener(Main main, Sql sql) {
		this.plugin = main;
		this.sql = sql;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					Player p = event.getPlayer();
					try {
						sql.checkConnection();
						float[] values = sql.getPlayerLevel(p);
						p.setExp(values[1]);
						p.setLevel((int) values[0]);
					} catch (SQLException e) {
						p.sendMessage("[LevelSync] " + plugin.msg_syncError);
						plugin.getLogger().warning("[LevelSync] " + plugin.msg_syncError);
						e.printStackTrace();
					}
				}
			}, 20L);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					Player p = event.getPlayer();
					sql.checkConnection();
					sql.sendPlayerLevel(p);
				}catch(SQLException e) {
					plugin.getLogger().warning("[LevelSync] " + plugin.msg_dbError);
					e.printStackTrace();
				}
			}		
		});
	}
}
