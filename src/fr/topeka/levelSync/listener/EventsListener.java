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
		Player p = event.getPlayer();
		if(sql.checkConnection()) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				
				@Override
				public void run() {
					if(p != null) {
						if(p.isOnline()) {
							try {
								float[] values = sql.getPlayerLevel(p);
								p.setExp(values[1]);
								p.setLevel((int) values[0]);
							} catch (SQLException e) {
								e.printStackTrace();
								p.sendMessage("Couln't sync your experience");
							}
						}
					}
					
				}
			}, 20L);
		}else {
			p.sendMessage("Couln't sync your experience");
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				Player p = event.getPlayer();
				if(sql.checkConnection()) {
					try {
						sql.setPlayerLevel(p);
					}catch(SQLException e) {
						p.sendMessage("An error occured while trying to save your experience to database");
						e.printStackTrace();
					}
				}else {
					p.sendMessage("An error occured while trying to save your experience to database");
				}
				
			}
			
		});
	}
}
