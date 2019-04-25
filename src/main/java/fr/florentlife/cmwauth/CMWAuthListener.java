package fr.florentlife.cmwauth;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CMWAuthListener implements Listener{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(MySQLOperations.verifExistenceJoueur(p.getName()))
			MySQLOperations.disConnect(p.getName());
	}
}
