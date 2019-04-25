package fr.florentlife.cmwauth;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.command.CommandException;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLOperations {
		
	public static boolean verifExistenceJoueur(String joueur)
	{
		try {
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
			CMWAuth.getInstance().getLogger().severe(e.toString());
			return false;
		}
	}
	
	public static boolean verifCorrespondance(String joueur, String mdp)
	{
		try {
			Connection con = CMWAuth.getConnection();
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
}
