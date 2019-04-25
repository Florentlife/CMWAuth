package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConnectSiteCommandExecutor implements CommandExecutor {
	
	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"]"+ChatColor.RESET;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(MySQLOperations.verifExistenceJoueur(p.getName()) && !MySQLOperations.isConnected(p.getName()))
			{
				try {
					if(args[0] != "")
					{
						if(MySQLOperations.verifCorrespondance(p.getName(), args[0]))
						{
							MySQLOperations.setConnection(p.getName());
							p.sendMessage(prefix+" Vous �tes maintenant connect� :) Pour modifier vos donn�es utilisez /updatesite");
							CMWAuth.getInstance().getLogger().info(p.getName()+" s'est connecte");
						}
						else
						{
							p.sendMessage(prefix+ ChatColor.RED+" ERREUR: les mots de passes ne correspondent pas");
						}
					}
					else
					{
						p.sendMessage(prefix+ " La commande s'utilise comme �a : /connectsite mot_de_passe");
					}
				}
				catch(Exception e)
				{
					p.sendMessage(prefix+ " La commande s'utilise comme �a : /connectsite mot_de_passe");
				}
			}
			else if(MySQLOperations.isConnected(p.getName()))
			{
				p.sendMessage(prefix+ChatColor.RED + " Vous �tes d�j� connect� !");
				p.sendMessage(prefix+ " Pour modifier vos donnees utiliser /updatesite");
			}
			else
			{
				p.sendMessage(prefix+ ChatColor.RED + " D�sol� mais vous n'�tes pas enregistrer sur le site, utilisez /registerSite");
			}
			
		}
		else
		{
			CMWAuth.getInstance().getLogger().warning("Desole mais cette commande est reserve aux Joueurs, elle ne peut donc pas etre realise");
		}
		return true;
	}
}
