package fr.topeka.levelSync.tasks;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.topeka.levelSync.Main;

public class SaveDataTask extends BukkitRunnable {
	
	private Main main;

	public SaveDataTask(Main main) {
		this.main = main;
	}

	@Override
	public void run() {
		int n = 0;
		try {
			main.getLogger().info("[LevelSync] Starting saving data task");
			for(Player p : main.getServer().getOnlinePlayers()) {
				main.getSql().setPlayerLevel(p);
				n++;
			}
			main.getLogger().info("[LevelSync] saving data task done for " + n + " players");
		}catch(SQLException e) {
			main.getLogger().warning("[LevelSync] " + main.msg_syncError);
			e.printStackTrace();
		}

	}

}
