package de.Moohsassin.LamaBungee;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MySQL {
	
	private final String host;
	private final int port;
	private final String user;
	private final String password;
	private final String database;
	private Connection conn;
	private ScheduledTask task;
	
	public MySQL(String database1) throws Exception {
		Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("/minecraft", "mysql.yml"));
		this.host = cfg.getString("ip");
		this.port = cfg.getInt("port");
		this.user = cfg.getString("username");
		this.password = cfg.getString("password");
		this.database = database1;
		try {
			openConnection();
		} catch (Exception e) {
			System.out.print("Fehlgeschlagen" + e.getMessage());
		}
	}

	public Connection openConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
		this.conn = conn;
		
		task = ProxyServer.getInstance().getScheduler().schedule(LamaBungee.instance, new Runnable() {
			public void run() {
				if(!hasConnection()) {
					task.cancel();
					try { openConnection(); }
					catch (Exception e) {	e.printStackTrace(); }
					
					return;
				}
				if(database.equalsIgnoreCase("Players")) queryUpdate("DELETE FROM Lamas WHERE Name='88'");
				if(database.equalsIgnoreCase("GeneralDatas")) queryUpdate("DELETE FROM Skins WHERE Name='A'");
				if(database.equalsIgnoreCase("Games")) queryUpdate("DELETE FROM LamaJump WHERE Uuid='-1'");
				if(database.equalsIgnoreCase("ClanDatas")) queryUpdate("DELETE FROM RegisteredClans WHERE Uuid='-1'");
				if(database.equalsIgnoreCase("FriendDatas")) queryUpdate("DELETE FROM Friends WHERE Uuid='-1'");
				if(database.equalsIgnoreCase("LamaPex")) queryUpdate("DELETE FROM Groups WHERE Name='Hitler'");

			}
		}, 20, 20, TimeUnit.MINUTES);
		
		return conn;
	}

	public Connection getConnection() {
		return this.conn;
	}

	public boolean hasConnection() {
		try {
			return (this.conn != null) || (this.conn.isValid(1));
		} catch (SQLException localSQLException) {
		}
		return false;
	}

	public ResultSet getQuery(String query) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = this.conn.prepareStatement(query);
		rs = stmt.executeQuery(query);
		return rs;
	}

	public void queryUpdate(String query) {
		Connection conn = this.conn;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Failed to send update '" + query + "'.");
		} finally {
			closeRessources(null, st);
		}
	}

	public void closeRessources(ResultSet rs, PreparedStatement st) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException localSQLException) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException localSQLException1) {
			}
		}
	}

	public long getLong(String table, String value, Object fromWhom, String toGet) {

		ResultSet rs = null;
		PreparedStatement st = null;
			
		try {
				
			st = this.conn.prepareStatement("SELECT * FROM " + table + " WHERE " + value + "='" + fromWhom + "'");
			rs = st.executeQuery();
			rs.last();
				
			if(rs.getRow() != 0) {
				rs.first();
				return rs.getLong(toGet);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRessources(rs, st);
		}
		
		return -1;
	}
	
	public int getInt(String table, String value, Object fromWhom, String toGet) {

		ResultSet rs = null;
		PreparedStatement st = null;
			
		try {
				
			st = this.conn.prepareStatement("SELECT * FROM " + table + " WHERE " + value + "='" + fromWhom + "'");
			rs = st.executeQuery();
			rs.last();
				
			if(rs.getRow() != 0) {
				rs.first();
				return rs.getInt(toGet);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRessources(rs, st);
		}
		
		return -1;
	}
	
	
	public String getString(String table, String value, Object fromWhom, String toGet) {
		
		ResultSet rs = null;
		PreparedStatement st = null;
			
		try {
				
			st = this.conn.prepareStatement("SELECT * FROM " + table + " WHERE " + value + "='" + fromWhom + "'");
			rs = st.executeQuery();
			rs.last();
				
			if(rs.getRow() != 0) {
				rs.first();
				return rs.getString(toGet);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeRessources(rs, st);
		}
		return null;
	}
	
	public ArrayList<Object> getList(String table, String value, String key, String toGet) {
		
		ArrayList<Object> list = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + value + "='" + key + "'");
			rs = st.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getObject(toGet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeRessources(rs, st);
		}
		return list;
	}
	
	public void closeConnection() {
		try {
			this.task.cancel();
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.task = null;
			this.conn = null;
		}
	}
	
}
