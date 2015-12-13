package de.Moohsassin.LamaBungee;

import java.util.ArrayList;

import net.md_5.bungee.api.ProxyServer;

public class ServerManager {

	static MySQL sql;
	
	public static void openConnection() {
		try {
			sql = new MySQL("GeneralDatas");
			sql.openConnection();
			sql.queryUpdate("CREATE TABLE IF NOT EXISTS OnlineCounts (Name VARCHAR(100), Count INT)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<String> server = new ArrayList<>();
		
		for(String servers : ProxyServer.getInstance().getServers().keySet()) {
			if(servers.contains("-")) server.add(servers.split("-")[0]);
		}
		
		for(String scuts : server) {
			sql.queryUpdate("DELETE FROM OnlineCounts WHERE Name='" + scuts + "'");
			sql.queryUpdate("INSERT INTO OnlineCounts (Name,Count) VALUES ('" + scuts + "','0')");
		}
		
	}
	
	public static void updateTotalOnlineCount() {
		int now = ProxyServer.getInstance().getOnlineCount();
		sql.queryUpdate("DELETE FROM OnlineCounts WHERE Name='GLOBAL'");
		sql.queryUpdate("INSERT INTO OnlineCounts (Name,Count) VALUES ('GLOBAL','" + now + "')");
	}
	
	public static void updateCount(String from, String to) {
		
		if(from != null) {
			int count = -1;
			for(String servers : ProxyServer.getInstance().getServers().keySet()) {
				if(servers.contains(from)) {
					count += ProxyServer.getInstance().getServerInfo(servers).getPlayers().size();
				}
			}
			if(count < 0) count = 0;
			sql.queryUpdate("DELETE FROM OnlineCounts WHERE Name='" + from + "'");
			sql.queryUpdate("INSERT INTO OnlineCounts (Name,Count) VALUES ('" + from + "','" + count + "')");
		}
		
		if(to != null) {
			int count = 1;
			for(String servers : ProxyServer.getInstance().getServers().keySet()) {
				if(servers.contains(to)) {
					count += ProxyServer.getInstance().getServerInfo(servers).getPlayers().size();
				}
			}
			if(count < 0) count = 0;
			sql.queryUpdate("DELETE FROM OnlineCounts WHERE Name='" + to + "'");
			sql.queryUpdate("INSERT INTO OnlineCounts (Name,Count) VALUES ('" + to + "','" + count + "')");
		}
		
	}
	
	public static void closeConnection() {
		sql.closeConnection();
	}
	
}
