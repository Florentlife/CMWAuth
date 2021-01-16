package fr.florentlife.cmwauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import fr.keke142.consolefilter.ConsoleFilter;

public class CMWAuth extends JavaPlugin{
	private static CMWAuth instance;
	private static Connection connection;
	public boolean initialize;
	@Override
	public void onEnable() {
		instance = this;
		initialize = false;
		getLogger().info("Demarrage de CMWAuth ...");
		saveDefaultConfig();
		if(getConfig().getBoolean("CMWAuth.mysql") && getConfig().getString("CMWAuth.adresse") != "" && getConfig().getString("CMWAuth.utilisateur") != "" && getConfig().getString("CMWAuth.mdp") != "" && getConfig().getString("CMWAuth.base") != "")
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				getLogger().info("Driver => OK");
				String url = "jdbc:mysql://"+getConfig().getString("CMWAuth.adresse")+":"+getConfig().getString("CMWAuth.port")+"/"+getConfig().getString("CMWAuth.base");
				
				Connection con = DriverManager.getConnection(url, getConfig().getString("CMWAuth.utilisateur"), getConfig().getString("CMWAuth.mdp"));
				getLogger().info("Connexion a la base de donnee reussis !");
				
				String query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'cmw_users' AND TABLE_SCHEMA = ?";
				
				PreparedStatement prepare = con.prepareStatement(query);
				prepare.setString(1, getConfig().getString("CMWAuth.base"));
				
				ResultSet result = prepare.executeQuery();
				//ResultSetMetaData resultMeta = result.getMetaData();
				
				if(result.next())
				{
					getLogger().info("Table de CraftMyWebsite reconnu");
					query = "SHOW COLUMNS FROM cmw_users LIKE 'cmwauth'";
					prepare = con.prepareStatement(query);
					ResultSet resultSet = prepare.executeQuery();
				    if(resultSet.next())
				    {
				    	getLogger().info("CMWAuth est bien configure !");
				    	//Ajout des commandes et listener
						initialize();
				    	connection = con;
				    }
			    	else {
			    		getLogger().warning("Mauvaise configuration de la table, ou premiere installation ?");
					    getLogger().info("Tentative de reconfiguration de la table");
					    try {
					    	Statement state = con.createStatement();
					    	state.executeUpdate("ALTER TABLE cmw_users ADD cmwauth INT UNSIGNED NOT NULL");
					    	getLogger().info("Configuration reussis !");
					    	//Ajout des commandes et listener
							initialize();
							connection = con;
					    }
					    catch(SQLException e)
					    {
					    	getLogger().severe("Echec de la configuration");
					    	getLogger().severe("Rapport d'erreur : ");
					    	getLogger().severe(e.toString());
					    }
				    }
				}
				else
				{
					getLogger().severe("Impossible de detecter les tables de CraftMyWebsite");
					getLogger().severe("Echec du demarrage");
				}
			}
			catch(SQLException e)
			{
				getLogger().severe(e.toString());
			}
			catch(Exception e)
			{
				getLogger().severe("Echec de la connexion");
				getLogger().severe("Rapport d'erreur de connexion :");
				getLogger().severe(e.toString());
				//getConfig().set("CMWAuth.mysql", false);
				//saveConfig();
			}
		}
		else
		{
			getLogger().warning("La base de donnee n'a pas ete chargee");
			getConfig().set("CMWAuth.mysql", false);
			saveConfig();
		}
        getLogger().info("Remerciement special a Keke142");
		if(initialize)
			getLogger().info("Plugin fonctionnel");
		else
			getLogger().info("Erreur lors du lancement, verifier les logs, events et commandes desactives");
		getLogger().info("Fin du demarrage");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Arret de CMWAuth ...");
		getLogger().info("Deconnexion de tout les joueurs ...");
		MySQLOperations.disconnectAll();
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getLogger().info("Plugin arrete");
	}
	
	public void initialize() {
		getCommand("connectSite").setExecutor(new ConnectSiteCommandExecutor());
		getCommand("disconnectSite").setExecutor(new DisconnectSiteCommandExecutor());
		getCommand("updateSite").setExecutor(new UpdateSiteCommandExecutor());
		getCommand("getmonnaiesite").setExecutor(new GetMonnaieCommandExecutor());
		getCommand("givemonnaiesite").setExecutor(new GiveMonnaieCommandExecutor());
		getCommand("registersite").setExecutor(new RegisterCommandExecutor());
		getServer().getPluginManager().registerEvents(new CMWAuthListener(), this);
		getLogger().info("Enclenchement des filtres");
		ConsoleFilter filter = new ConsoleFilter();
        Logger rootLogger = LogManager.getRootLogger();
        org.apache.logging.log4j.core.Logger rootLoggerAsApache = (org.apache.logging.log4j.core.Logger) rootLogger;
        rootLoggerAsApache.addFilter(filter);
        this.initialize = true;
	}
	
	public static CMWAuth getInstance() {
		return instance;
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	public void setConnection(Connection con)
	{
		connection = con;
	}
}
