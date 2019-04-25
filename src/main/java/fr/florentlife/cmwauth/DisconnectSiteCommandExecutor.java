package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisconnectSiteCommandExecutor implements CommandExecutor {
	
	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"]"+ChatColor.RESET;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(MySQLOperations.disConnect(p.getName()))
			{
				p.sendMessage(prefix+ ChatColor.GREEN+ " Vous avez bien été déconnecté du service :)");
				CMWAuth.getInstance().getLogger().info(p.getName()+ "  s'est deconnecte");
			}
			else
			{
				CMWAuth.getInstance().getLogger().severe("ERREUR lors de la deconnection d'un joueur, consulter la stackTrace");
				p.sendMessage(prefix+ ChatColor.RED+ " Erreur lors de la déconection !! Consulter immédiatement un opérateur !");
			}
		}
		else
		{
			CMWAuth.getInstance().getLogger().warning("Desole mais cette fonction est reservee aux Joueurs !");
		}
		return true;
	}
}
