package fr.florentlife.cmwauth;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.bukkit.command.CommandException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLOperations {
	
	/**
	 * Permet de vérifier l'existence du joueur dans la base de donnée de CMW
	 * @param joueur String pseudo du joueur
	 * @return true si le joueur est reconnu, false sinon
	 */
	public static boolean verifExistenceJoueur(String joueur)
	{
		try {
			if(CMWAuth.getConnection().isClosed())
			{
				Connection con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			Connection con = CMWAuth.getConnection();
			String query = "SELECT * FROM cmw_users WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur);
			ResultSet result = prepare.executeQuery();
			if(result.next())
				return true;
			else
				return false;
			
		}
		catch(Exception e)
		{
			CMWAuth.getInstance().getLogger().severe("ERREURE FATALE :");
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean verifCorrespondance(String joueur, String mdp)
	{
		try {
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT mdp FROM cmw_users WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur);
			ResultSet result = prepare.executeQuery();
			result.next();
			if(BCrypt.checkpw(mdp, result.getString("mdp")))
				return true;
			else
				return false;
		}
		catch(CommandException e)
		{
			e.printStackTrace();
			return false;
		}
		catch(Exception e)
		{
			CMWAuth.getInstance().getLogger().severe("ERREURE FATALE");
			e.printStackTrace();
			return false;
		}
	}
	
	public static void setConnection(String joueur)
	{
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "UPDATE cmw_users SET cmwauth = 1 WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur);
			prepare.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isConnected(String joueur) {
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT * FROM cmw_users WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur);
			ResultSet result = prepare.executeQuery();
			result.next();
			if(result.getInt("cmwauth") == 1)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean disConnect(String name) {
		try {
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "UPDATE cmw_users SET cmwauth = 0 WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, name);
			prepare.executeUpdate();
			query = "SELECT cmwauth FROM cmw_users WHERE pseudo = ?";
			prepare = con.prepareStatement(query);
			prepare.setString(1, name);
			ResultSet result = prepare.executeQuery();
			result.next();
			if(result.getInt("cmwauth") == 0)
				return true;
			CMWAuth.getInstance().getLogger().severe("Erreur indetermine, la modification n'a pas ete faite ! fr.florentlife.cmwauth.MySQLOperations");
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean setMdp(String joueur, String mdp)
	{
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "UPDATE cmw_users SET mdp = ? WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			String hash = BCrypt.hashpw(mdp, BCrypt.gensalt());
			prepare.setString(1, hash);
			prepare.setString(2, joueur);
			if(prepare.executeUpdate() == 1)
				return true;
			CMWAuth.getInstance().getLogger().severe("Erreur indetermine, la modification n'a pas ete faite ! fr.florentlife.cmwauth.MySQLOperations");
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean setMail(String joueur, String mail)
	{
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "UPDATE cmw_users SET email = ? WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, mail);
			prepare.setString(2, joueur);
			if(prepare.executeUpdate() == 1)
				return true;
			CMWAuth.getInstance().getLogger().severe("Erreur indetermine, la modification n'a pas ete faite ! fr.florentlife.cmwauth.MySQLOperations");
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static int getMonnaie(String joueur) {
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT tokens FROM cmw_users WHERE pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur);
			ResultSet result = prepare.executeQuery();
			result.next();
			return result.getInt("tokens");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void giveMonnaie(String donneur, String receveur, int montant) {
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT * FROM cmw_users WHERE pseudo = ? OR pseudo = ?";
			PreparedStatement prepare = con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			prepare.setString(1, donneur);
			prepare.setString(2, receveur);
			ResultSet result = prepare.executeQuery();
			result.next();
			if(result.getString("pseudo").toLowerCase().contains(donneur.toLowerCase()))
			{
				result.updateInt("tokens", result.getInt("tokens")-montant);
				result.updateRow();
				result.next();
				result.updateInt("tokens", result.getInt("tokens") +montant);
				result.updateRow();
				result.close();
			}
			else
			{
				result.updateInt("tokens", result.getInt("tokens") +montant);
				result.updateRow();
				result.next();
				result.updateInt("tokens", result.getInt("tokens")-montant);
				result.updateRow();
				result.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void giveMonnaie(String receveur, int montant)
	{
		try {
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT * FROM cmw_users WHERE pseudo = ?";
			PreparedStatement prepare= con.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			prepare.setString(1, receveur);
			ResultSet result = prepare.executeQuery();
			result.next();
			result.updateInt("tokens", result.getInt("tokens")+montant);
			result.updateRow();
			result.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void disconnectAll()
	{
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "UPDATE cmw_users SET cmwauth = 0 WHERE cmwauth = 1";
			con.createStatement().executeUpdate(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean mailExist(String mail)
	{
		try
		{
			Connection con =CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			String query = "SELECT * FROM cmw_users WHERE email = ?";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, mail);
			ResultSet result = prepare.executeQuery();
			if(result.next())
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static void register(Player joueur, String mail, String mdp)
	{
		try
		{
			Connection con = CMWAuth.getConnection();
			if(con.isClosed())
			{
				con = openConnection(CMWAuth.getInstance().getConfig());
				CMWAuth.getInstance().setConnection(con);
			}
			mdp = BCrypt.hashpw(mdp, BCrypt.gensalt());
			Date date = new Date();
			String dateStr = String.valueOf(date.getTime()/1000);
			String query = "INSERT INTO cmw_users (pseudo, mdp, email, anciennete, newsletter, rang, tokens, age, resettoken, ip, CleUnique, ValidationMail, img_extension, show_email, cmwauth) VALUES (?, ?, ?, ?, 1, 0, 0, 0, 0, ?, 0, 1, '', 0, 0) ";
			PreparedStatement prepare = con.prepareStatement(query);
			prepare.setString(1, joueur.getName());
			prepare.setString(2, mdp);
			prepare.setString(3, mail);
			prepare.setString(4, dateStr);
			prepare.setString(5, joueur.getAddress().getHostString());
			prepare.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection openConnection(FileConfiguration config)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+config.getString("CMWAuth.adresse")+":"+config.getString("CMWAuth.port")+"/"+config.getString("CMWAuth.base");
			
			return  DriverManager.getConnection(url, config.getString("CMWAuth.utilisateur"), config.getString("CMWAuth.mdp"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
