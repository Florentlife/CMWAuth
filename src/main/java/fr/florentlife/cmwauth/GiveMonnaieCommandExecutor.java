package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveMonnaieCommandExecutor implements CommandExecutor {

	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"] "+ChatColor.RESET;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(MySQLOperations.isConnected(p.getName()))
			{
				try {
					if(args[0] != "" && args[1] != "")
					{
						if(MySQLOperations.verifExistenceJoueur(args[0]))
						{
							Integer integer = new Integer(args[1]);
							if(MySQLOperations.getMonnaie(p.getName()) >= integer.intValue() )
							{
								MySQLOperations.giveMonnaie(p.getName(), args[0], integer.intValue());
								p.sendMessage(prefix+ChatColor.GREEN+"Vous avez transmit "+ChatColor.GRAY+integer.intValue()+ChatColor.GREEN+" points boutique à "+ChatColor.GRAY+args[0]);
								p.sendMessage(prefix+ChatColor.GREEN+"Si le joueur est actuellement connecté il recevra un message !");
								Player j = isConnected(args[0]);
								if(j != null)
									j.sendMessage(prefix+ChatColor.GRAY+p.getName()+ChatColor.GREEN+" Vous a offert "+ChatColor.GRAY+integer.intValue()+ChatColor.GREEN+" points boutique !!!! :D");
								
							}
						}
						else
						{
							p.sendMessage(prefix+ChatColor.RED+"Le pseudo rentré n'est pas reconnu par nos bases de données :/ ");
						}
					}
					else
						p.sendMessage(prefix+ChatColor.RED+"La commande s'utilise comme ça : /givemonnaiesite [joueur] [montant]");
				}
				catch(Exception e)
				{
					p.sendMessage(prefix+ChatColor.RED+"La commande s'utilise comme ça : /givemonnaiesite [joueur] [montant]");
				}				
			}
			else
			{
				String[] message = {prefix + ChatColor.RED+"Vous devez être connecté pour effectuer cette commande.", prefix + ChatColor.RED+"Pour vous connecter utilisez /connectsite [mot de passe]."};
				p.sendMessage(message);
			}
		}
		else
		{
			try {
				if(args[0] != "" && args[1] != "")
				{
					if(MySQLOperations.verifExistenceJoueur(args[0]))
					{
						Integer integer = new Integer(args[1]);
						MySQLOperations.giveMonnaie(args[0], integer.intValue());
						CMWAuth.getInstance().getLogger().info(integer.intValue()+" ont ete ajoute a "+args[0]);
						CMWAuth.getInstance().getLogger().info("S'il est connecte il recevra un message pour le prevenir de cet ajout !");
						Player j = isConnected(args[0]);
						if(j != null)
							j.sendMessage(prefix+"Le serveur vous a offert "+ChatColor.GRAY+integer.intValue()+ChatColor.GREEN+" points boutique !!!! :D");
					}
				}
			}
			catch(Exception e)
			{
				CMWAuth.getInstance().getLogger().info("La commande s'utilise comme ça : /givemonnaiesite [joueur] [montant]");
			}				
		}
		return true;
	}
	
	private static Player isConnected(String joueur)
	{
		for(Player p : CMWAuth.getInstance().getServer().getOnlinePlayers())
		{
			if(p.getName().toLowerCase().contains(joueur.toLowerCase()))
				return p;
		}
		return null;
	}

}
