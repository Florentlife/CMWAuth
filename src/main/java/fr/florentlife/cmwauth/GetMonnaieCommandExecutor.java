package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetMonnaieCommandExecutor implements CommandExecutor{
	
	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"] "+ChatColor.RESET;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(MySQLOperations.isConnected(p.getName()))
				p.sendMessage(prefix+ ChatColor.DARK_GREEN+ "Vous avez actuellement "+ChatColor.GRAY+MySQLOperations.getMonnaie(p.getName())+" points boutique");
			else
			{
				String[] message = {prefix + ChatColor.RED+"Vous devez être connecté pour effectuer cette commande.", prefix + ChatColor.RED+"Pour vous connecter utilisez /connectsite [mot de passe]."};
				p.sendMessage(message);
			}
		}
		else
			CMWAuth.getInstance().getLogger().warning("Cette commande est reserve aux joueurs !");
		return true;
	}
}
