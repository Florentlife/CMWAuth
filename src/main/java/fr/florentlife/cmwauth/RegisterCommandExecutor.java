package fr.florentlife.cmwauth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class RegisterCommandExecutor implements CommandExecutor, ConversationAbandonedListener {
	
	final String prefix = ChatColor.GREEN + "["+ChatColor.BLUE+"CMWAuth"+ChatColor.GREEN+"] "+ChatColor.RESET;
	final String prefixErreur = prefix+ChatColor.RED+"Erreur: ";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(!MySQLOperations.verifExistenceJoueur(p.getName()))
			{
				conversationFactory.buildConversation((Conversable)p).begin();
				/*try {
					
				}
				catch(Exception e)
				{
					p.sendMessage(prefixErreur+"La commande s'utilise comme ça : /registersite [email] [mot de passe] [confirmation mot de passe]");
				}*/
			}
			else
				p.sendMessage(prefixErreur+"Vous êtes déjà enregistré sur le site !!");
		}
		else
			CMWAuth.getInstance().getLogger().info("Cette commande est reserve aux joueurs !");
		return true;
	}
	
	private ConversationFactory conversationFactory;
	
	public RegisterCommandExecutor() {
		this.conversationFactory = new ConversationFactory(CMWAuth.getInstance())
				.withModality(true)
				.withPrefix(new RegisterPrefix())
				.withFirstPrompt(new EmailPrompt())
				.withEscapeSequence("/end")
				.thatExcludesNonPlayersWithMessage("Commande reserve aux joueurs !!!")
				.addConversationAbandonedListener(this);
	}

	@Override
	public void conversationAbandoned(ConversationAbandonedEvent arg0) {
		if(arg0.gracefulExit())
			arg0.getContext().getForWhom().sendRawMessage("Fin de la conversation :)");
		else
			arg0.getContext().getForWhom().sendRawMessage("Échec de l'enregistrement");		
	}
	
	private class EmailPrompt extends StringPrompt {
		
		@Override
		public String getPromptText(ConversationContext context) {
			if(context.getSessionData("erreurEmail") == null)
				return "Quel est votre adresse mail ? Pour terminer l'enregistrement taper stop";
			else
				return "Erreur, adresse mail déjà utilisé ! Quel est votre adresse mail ? Pour terminer l'enregistrement tapez /end";
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String mail) {
			if(mail.equals("stop"))
			{
				context.setSessionData("end", "0");
				return new EndPrompt();
			}
			if(!MySQLOperations.mailExist(mail))
			{
				context.setSessionData("mail", mail);
				return new MdpPrompt();
			}
			context.setSessionData("erreurEmail", "Adresse mail déjà utilisé");
			return new EmailPrompt();
		}
		
	}
	
	private class MdpPrompt extends StringPrompt {

		@Override
		public Prompt acceptInput(ConversationContext context, String mdp) {
			if(mdp.equals("stop"))
			{
				context.setSessionData("end", "0");
				return new EndPrompt();
			}
			context.setSessionData("mdp", mdp);
			return new MdpConfPrompt();
		}

		@Override
		public String getPromptText(ConversationContext arg0) {
			return "Entrez votre mot de passe, utilisé pour vous connecter au site";
		}
		
	}
	
	private class MdpConfPrompt extends StringPrompt {

		@Override
		public Prompt acceptInput(ConversationContext context, String mdpConf) {
			if(mdpConf.equals("stop"))
			{
				context.setSessionData("end", "0");
				return new EndPrompt();
			}
			if(mdpConf.equals(context.getSessionData("mdp")))
			{
				context.setSessionData("mdpConf", mdpConf);
				return new ConfirmPrompt();
			}
			else
			{
				context.setSessionData("erreurMDP", 1);
				return new MdpConfPrompt();
			}
		}

		@Override
		public String getPromptText(ConversationContext context) {
			if(context.getSessionData("erreurMDP") != null)
				return "Vos mots de passes ne correspondent pas. Confirmer votre mot de passe :";
			return "Confirmer votre mot de passe";
		}
	}
	
	private class ConfirmPrompt extends StringPrompt
	{

		@Override
		public String getPromptText(ConversationContext context) {
			return "Confirmez vous l'inscription, telle que : Email = "+context.getSessionData("mail")+" et mot de passe :" +context.getSessionData("mdp")+" taper 'oui' pour confirmer.";
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String arg1) {
			if(arg1.toLowerCase().equals("oui"))
			{
				MySQLOperations.register(((Player)(context.getForWhom())), (String)context.getSessionData("mail"), (String)context.getSessionData("mdp"));
				context.setSessionData("end", "1");
				return new EndPrompt();
			}
			context.setSessionData("end", "0");
			return new EndPrompt(); 
		}
		
	}
	
	private class EndPrompt extends MessagePrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			if((String)context.getSessionData("end") == "1")
			{
				if(MySQLOperations.verifExistenceJoueur(((Player) (context.getForWhom())).getName()))
				{
					return "Enregistrement réussis :D !!!! ";
				}
				else
					return "Erreur d'enreistrement :(";
			}
			if((String)context.getSessionData("end") == "0")
				return "Vous avez mis fin a l'enregistrement ! Aucune donnée sauvegardé";
			return "Enregistrement fermé !";
		}

		@Override
		protected Prompt getNextPrompt(ConversationContext arg0) {
			return Prompt.END_OF_CONVERSATION;
		}
		
	}
	
	private class RegisterPrefix implements ConversationPrefix {

		@Override
		public String getPrefix(ConversationContext context) {
			String mail = (String)context.getSessionData("mail");
			String mdp = (String)context.getSessionData("mdp");
			String mdpConf = (String)context.getSessionData("mdpConf");
			String end = (String)context.getSessionData("end");
			
			if(mail != null && mdp == null && mdpConf == null && end == null)
			{
				return prefix+ChatColor.GREEN+"Étape 2/4 " +ChatColor.RESET;
			}
			if(mail != null && mdp != null && mdpConf == null && end == null)
			{
				return prefix+ChatColor.GREEN+"Étape 3/4 "+ChatColor.RESET;
			}
			if(mail != null && mdp != null && mdpConf != null && end == null)
			{
				return prefix+ChatColor.GREEN+"Étape 4/4 "+ChatColor.RESET;
			}
			if(end == "1")
				return prefix+ChatColor.GREEN+"Fin enregistrement :"+ChatColor.RESET;
			return prefix+ChatColor.GREEN+"Étape 1/4 "+ChatColor.RESET;
		}
		
	}

}
