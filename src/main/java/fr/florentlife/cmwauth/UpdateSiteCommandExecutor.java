package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateSiteCommandExecutor implements CommandExecutor {
	
	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"] "+ChatColor.RESET;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(MySQLOperations.isConnected(p.getName()))
			{
				try {
					if(args[0].equals("mdp"))
					{
						try {
							if(args[1] != "")
							{
								if(MySQLOperations.setMdp(p.getName(), args[1]))
								{
									p.sendMessage(prefix+ ChatColor.GREEN+"Votre mot de passe site à bien été changé pour :" + ChatColor.GRAY +args[1]);
								}
								else
								{
									p.sendMessage(prefix+ ChatColor.RED+ "Impossible de modifier votre mot de passe :( !");
								}
								
							}
							else
								p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mdp nouveauMDP");
						}
						catch(Exception e)
						{
							p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mdp nouveauMDP");
						}
					}
					else if(args[0].equals("mail"))
					{
						try {
							if(args[1] != "")
							{
								if(MySQLOperations.setMail(p.getName(), args[1]))
								{
									p.sendMessage(prefix+ ChatColor.GREEN+"Votre mail site à bien été changé pour :" + ChatColor.GRAY +args[1]);
								}
								else
									p.sendMessage(prefix+ ChatColor.RED+ "Impossible de modifier votre mail :( !");
							}
							else
								p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mail nouveauMail");
						}
						catch(Exception e1)
						{
							p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mail nouveauMail");
						}
					}
					else
						p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mail nouveauMail ou /updatesite mdp nouveauMdp");
				}
				catch(Exception e2)
				{
					p.sendMessage(prefix + ChatColor.RED+"La commande s'utilise comme ça : /updatesite mail nouveauMail ou /updatesite mdp nouveauMdp");
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
			CMWAuth.getInstance().getLogger().warning("Cette commande est reserve aux joueurs !");
		}
		return true;
	}

}
