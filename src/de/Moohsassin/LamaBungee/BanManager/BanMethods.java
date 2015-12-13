package de.Moohsassin.LamaBungee.BanManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.Moohsassin.LamaBungee.MySQL;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanMethods {

	public static MySQL sql;
	
	public static void openConnection() {
		try {
			sql = new MySQL("Players");
			sql.openConnection();
			
			sql.queryUpdate("CREATE TABLE IF NOT EXISTS BannedLamas (Uuid VARCHAR(36), Reason VARCHAR(100), BannedBy VARCHAR(36), Time LONG)");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		sql.closeConnection();
	}
	
	public static boolean isBanned(String name) {
		return getReason(name) != null;
	}
	
	public static String getReason(String name) {
		return sql.getString("BannedLamas", "Uuid", PlayerDatas.getUUID(name), "Reason");
	}
	
	public static String getBanner(String bannedPlayer) {
		if(!isBanned(bannedPlayer)) return null;
		return PlayerDatas.getName(sql.getString("BannedLamas", "Uuid", PlayerDatas.getUUID(bannedPlayer), "BannedBy"));
	}
	
	public static String getBanEndDate(String name) {

		long end = getBanEnd(name);
		if(end == -2) return "§cBANRESET :)";
		
		DateFormat df = new SimpleDateFormat("dd.MM.YY hh:mm:ss aa");
		String format = df.format(new Date(end));
			
		String[] splited = format.split(" ");
		String[] timeSplited = splited[1].split(":");
		
		int i = Integer.valueOf(timeSplited[0]);
		if(splited[2].equalsIgnoreCase("pm") &&  i != 12) i += 12;
		if(splited[2].equalsIgnoreCase("am") && i == 12) i = 0;
		
		splited[1] = i + ":" + timeSplited[1] + ":" + timeSplited[2];
		
		return "§7" + splited[0] + " um " + splited[1];
	}
	
	public static long getTimeLeft(String name) {
		if(!isBanned(name)) return 0;
		
		long current = System.currentTimeMillis();
		long end = sql.getLong("BannedLamas", "Uuid", PlayerDatas.getUUID(name), "Time");
		
		if(end == -2) return -2;
		if(end < current) {
			unban(name);
			return 0;
		}
		
		return end - current;
	}
	
	public static long getBanEnd(String name) {
		if(!isBanned(name)) return 0;
		return sql.getLong("BannedLamas", "Uuid", PlayerDatas.getUUID(name), "Time");
	}
	
	public static void unban(String name) {
		sql.queryUpdate("DELETE FROM BannedLamas WHERE Uuid='" + sql.getString("Lamas", "Name", name, "Uuid") + "'");
	}
	
	public static void ban(String name, String reason, long time, TimeUnit unit, ProxiedPlayer banner) {
		unban(name);
		long bantime = -2;
		if(time != -2) bantime = System.currentTimeMillis() + (time * unit.getSeconds() * 1000);
		
		sql.queryUpdate("INSERT INTO BannedLamas (Uuid,Reason,BannedBy,Time) VALUES ('" + PlayerDatas.getUUID(name) + "','" + reason + "','" + banner.getUniqueId() + "','" + bantime + "')");
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
		if(p != null) p.disconnect(new ComponentBuilder(getBanScreen(p.getName(), bantime, reason)).create());
	}
	
	public static String getBanScreen(String name, long time, String reason) {
		if(time == -2) return "§e§lplay.Lamawiese.net\n\n§cDu wurdest §lPERMANENT §cvon unserem Netzwerk gebannt!\n§7Grund: §c" + reason;
		else return "§e§lplay.Lamawiese.net\n\n§cDu wurdest zeitlich von unserem Netzwerk gebannt!\n§7Grund: §c" + reason + "\n§7Dein Ban endet am: " + getBanEndDate(name);
	}
	
}
