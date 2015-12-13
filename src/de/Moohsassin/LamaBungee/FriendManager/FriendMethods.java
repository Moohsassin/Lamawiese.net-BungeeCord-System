package de.Moohsassin.LamaBungee.FriendManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.MySQL;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendMethods {
	
	public static String pr = "§6Freunde §8┃ §7";
	static MySQL sql;
	
	public static void openConnection() {
		try {
			sql = new MySQL("FriendDatas");
			
			sql.queryUpdate("CREATE TABLE IF NOT EXISTS Friends (Uuid VARCHAR(36), Friend VARCHAR(36))");
			sql.queryUpdate("CREATE TABLE IF NOT EXISTS Invites (Uuid VARCHAR(36), ToLama VARCHAR(36))");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		sql.closeConnection();
	}
	
	public static int getMaxFriendsCount(UUID player) {
		int amount = PlayerDatas.sql.getInt("Gadgets", "Gadget='FriendSlots' AND Uuid", player, "Amount");
		if(amount == -1) return 10;
		else return 10 + amount;
	}
	
	public static boolean isFriendWith(UUID sender, UUID player) {
		return getFriends(sender).contains(player);
	}
	
	public static void deleteAllFrom(UUID player, UUID where) {
		sql.queryUpdate("DELETE FROM Friends WHERE Uuid='" + player + "' AND Friend='" + where + "'");
		sql.queryUpdate("DELETE FROM Invites WHERE Uuid='" + player + "' AND ToLama='" + where + "'");
	}
	
	public static void sendRequest(UUID sender, UUID toPlayer) {
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		if(isFriendWith(sender, toPlayer)) {
			Methods.sendMessage(p, pr + "Du bist mit diesem Spieler bereits befreundet!");
			return;
		}
		if(getSendedRequests(sender).contains(toPlayer)) {
			Methods.sendMessage(p, pr + "Du hast diesem Spieler bereits eine Anfrage geschickt!");
			return;
		}
		
		if(getFriends(sender).size() == getMaxFriendsCount(sender)) {
			Methods.sendMessage(p, pr + "Du hast die maximale Anzahl an Freunden erreicht!");
			Methods.sendMessage(p, pr + "Wenn du mehr Freunde gleichzeitig haben willst, kannst du dir");
			Methods.sendMessage(p, pr + "weitere Slots in unserem Shop kaufen: §e§lstore.lamawiese.net");
			return;
		}
		
		if(getFriendRequests(sender).contains(toPlayer)) {
			acceptRequest(sender, toPlayer);
			return;
		}
		
		deleteAllFrom(sender, toPlayer);
		sql.queryUpdate("INSERT INTO Invites (Uuid,ToLama) VALUES ('" + sender + "','" + toPlayer + "')");
		
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(toPlayer);
		if(t != null) {
			Methods.sendMessage(t, pr + "Du hast eine neue Freundschaftsanfrage von §e" + p.getName() + " §7erhalten!");
			Methods.sendMessage(t, pr + "Du kannst sie mit §e/friend accept " + p.getName() + " §7annehmen!");
		}
		Methods.sendMessage(p, pr + "§e" + PlayerDatas.getName(toPlayer + "") + "§7 hat deine Anfrage erhalten!");
		
	}
	
	public static void acceptRequest(UUID sender, UUID fromWhom) {
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		if(!getFriendRequests(sender).contains(fromWhom)) {
			Methods.sendMessage(p, pr + "Du hast von diesem Spieler keine Anfrage!");
			return;
		}
		
		if(getFriends(sender).size() == getMaxFriendsCount(sender)) {
			Methods.sendMessage(p, pr + "Du hast die maximale Anzahl an Freunden erreicht!");
			Methods.sendMessage(p, pr + "Wenn du mehr Freunde gleichzeitig haben willst, kannst du dir");
			Methods.sendMessage(p, pr + "weitere Slots in unserem Shop kaufen: §estore.lamawiese.net");
			return;
		}
		
		deleteAllFrom(fromWhom, sender);
		
		sql.queryUpdate("INSERT INTO Friends (Uuid, Friend) VALUES ('" + sender + "','" + fromWhom + "')");
		sql.queryUpdate("INSERT INTO Friends (Uuid, Friend) VALUES ('" + fromWhom + "','" + sender + "')");
		
		String targetName = null;
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(fromWhom);
		if(t != null) {
			targetName = t.getName();
			Methods.sendMessage(t, pr + "§e" + p.getName() + "§7 hat deine Anfrage angenommen!");
		} 
		else targetName = PlayerDatas.getName(fromWhom + "");
		
		Methods.sendMessage(p, pr + "Du bist nun mit §e" + targetName + " §7befreundet!");
		
	}		
	
	
	public static void denyRequest(UUID sender, UUID fromWhom) {
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		if(!getFriendRequests(sender).contains(fromWhom)) {
			Methods.sendMessage(p, pr + "Du hast von diesem Spieler keine Anfrage!");
			return;
		}
		
		deleteAllFrom(fromWhom, sender);
		
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(fromWhom);
		if(t != null) Methods.sendMessage(t, pr + "§e" + p.getName() + " §7hat deine Anfrage abgelehnt!");
		
		Methods.sendMessage(p, pr + "Du hast die Anfrage abgelehnt!");
		
	}
	
	public static void removeFriend(UUID sender, UUID friend) {
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		
		if(getFriendRequests(sender).contains(friend)) {
			Methods.sendMessage(p, pr + "Um eine Anfrage abzulehnen ist folgender Befehl notwendig:");
			Methods.sendMessage(p, pr + "§c/friend deny <Spielername>"); // Vl den Spielernamen angeben ?
			return;
		}
		
		if(!getFriends(sender).contains(friend)) {
			Methods.sendMessage(p, pr + "Dieser Spieler ist nicht in deiner Freundeliste!");
			return;
		}
		
		deleteAllFrom(sender, friend);
		deleteAllFrom(friend, sender);
		
		String targetName = null;
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(friend);
		if(t != null) {
			targetName = t.getName();
		    Methods.sendMessage(t, pr + "§e" + p.getName() + " §7hat eure Freundschaft gekündigt!");
		}
		else targetName = PlayerDatas.getName(friend + "");
		
		Methods.sendMessage(p, pr + "Du hast §e" + targetName + " §7von deinen Freunden entfernt!");
		
	}
	
	public static void sendMessageToFriend(UUID sender, UUID friend, String message) {
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		String targetName = PlayerDatas.getName(friend + "");
		
		boolean isUser = false;
		String rank = PlayerDatas.getRank(p.getName());
		if(rank.equalsIgnoreCase("User") | rank.equalsIgnoreCase("Premium")) isUser = true;
		
		if(!getFriends(sender).contains(friend) && isUser) {
			Methods.sendMessage(p, pr + "§e" + targetName + " §7ist nicht in deiner Freundeliste!");
			return;
		}
		
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(targetName);
		if(t == null) {
			Methods.sendMessage(t, pr + "§e" + targetName + " §7ist nicht online!");
			return;
		}
		
		Methods.sendMessage(p, LamaBungee.pr.replace("§6", "§6§l") + "Du §8➠ §e" + t.getName() + " §8» §7" + message);
		Methods.sendMessage(t, LamaBungee.pr.replace("§6", "§6§l") + "§e" + p.getName() + " §8➠ §7Dir §8» §7" + message);
		
	}
	
	public static void joinFriend(UUID sender, UUID friend) {
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
		String targetName = PlayerDatas.getName(friend + "");
		if(!getFriends(sender).contains(friend)) {
			Methods.sendMessage(p, pr + "§e" + targetName + " §7ist nicht in deiner Freundeliste!");
			return;
		}
		
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(targetName);
		if(t == null) {
			Methods.sendMessage(t, pr + "§e" + targetName + " §7ist nicht online!");
			return;
		}
		
	}
	
	public static ArrayList<UUID> getFriends(UUID player) {
		
		ArrayList<UUID> friends = new ArrayList<>();
		
		Connection conn = sql.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Friends WHERE Uuid='" + player + "'");
			rs = st.executeQuery();
			
			while (rs.next()) {
				friends.add(UUID.fromString(rs.getString("Friend")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sql.closeRessources(rs, st);
		}
		
		return friends;
		
	}
	
	public static ArrayList<UUID> getFriendRequests(UUID player) {
		
		ArrayList<UUID> invites = new ArrayList<>();
		
		Connection conn = sql.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Invites WHERE ToLama='" + player + "'");
			rs = st.executeQuery();
			
			while (rs.next()) {
				invites.add(UUID.fromString(rs.getString("Uuid")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sql.closeRessources(rs, st);
		}
		
		return invites;
		
	}
	
	public static ArrayList<UUID> getSendedRequests(UUID player) {
		ArrayList<UUID> invites = new ArrayList<>();
		
		Connection conn = sql.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Invites WHERE Uuid='" + player + "'");
			rs = st.executeQuery();
			
			while (rs.next()) {
				invites.add(UUID.fromString(rs.getString("ToLama")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sql.closeRessources(rs, st);
		}
		
		return invites;
	}
	
}
