package de.Moohsassin.PartyManager;

import java.util.ArrayList;
import java.util.HashMap;

import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyMethods {

	public static HashMap<String, ArrayList<String>> parties = new HashMap<>();
	public static HashMap<String, ArrayList<String>> invites = new HashMap<>();
	public static String pr = "§6Party §8┃ §7";
	
	public static boolean hasInvitationFrom(ProxiedPlayer p, String leader) {
		if(invites.get(p.getName()) == null) return false;
		return invites.get(p.getName()).contains(leader);
	}
	
	public static void acceptInvitation(ProxiedPlayer p, String leader) {
		
		if(!hasInvitationFrom(p, leader)) {
			Methods.sendMessage(p, pr + "Du hast keine Einladung von §e" + leader);
			return;
		}
		
		ProxiedPlayer l = ProxyServer.getInstance().getPlayer(leader);
		
		Methods.sendMessage(p, pr + "Du hast die Anfrage von §e" + leader + "§7 angenommen!");
		Methods.sendMessage(l, pr + "§e" + p.getName() + " §7hat deine Anfrage angenommen!");
		
		addPlayerToParty(l, p);
	}
	
	public static void denyInvitation(ProxiedPlayer p, String leader) {
		if(!hasInvitationFrom(p, leader)) {
			Methods.sendMessage(p, pr + "Du hast keine Einladung von §e" + leader);
			return;
		}
		
		ArrayList<String> invs = invites.get(p.getName());
		invs.remove(leader);
		
		if(invs.isEmpty()) invites.remove(p.getName());
		else invites.put(p.getName(), invs);
		
		Methods.sendMessage(p, pr + "Du hast die Einladung abgelehnt!");
		Methods.sendMessage(ProxyServer.getInstance().getPlayer(leader), pr + "§e" + p.getName() + " §7hat deine Einladung abgelehnt!");
	}
	
	public static void invitePlayer(ProxiedPlayer p, ProxiedPlayer leader) {
		
		if(p.getName().equalsIgnoreCase(leader.getName())) {
			Methods.sendMessage(leader, pr + "Du kannst dich nicht selbst einladen!");
			return;
		}
		
		if(isInParty(leader) && !isPartyLeader(leader)) {
			Methods.sendMessage(leader, pr + "Du bist nicht der Party leader!");
			return;
		}
		
		if(isInParty(p)) {
			Methods.sendMessage(leader, pr + "Dieser Spieler ist bereits in einer Party!");
			return;
		}
		
		if(hasInvitationFrom(p, leader.getName())) {
			Methods.sendMessage(leader, pr + "Du hast diesem Spieler bereits eine Anfrage gesendet!");
			return;
		}
		
		ArrayList<String> invs = invites.get(p.getName());
		if(invs == null) invs = new ArrayList<>();
		
		invs.add(leader.getName());
		
		invites.put(p.getName(), invs);
			
		Methods.sendMessage(p, pr + "Du hast eine neue Party Einladung von §e" + leader.getName() + " §7erhalten!");
		Methods.sendMessage(p, pr + "Mit §e/party accept " + leader.getName() + " §7kannst du diese annehmen!");
		
		Methods.sendMessage(leader, pr + "§e" + p.getName() + "§7 hat deine Anfrage erhalten!");
		
	}
	
	public static boolean isInParty(ProxiedPlayer p) {
		if(isPartyLeader(p)) return true;
		for(ArrayList<String> values : parties.values()) {
			if(values.contains(p.getName())) return true;
		}
		return false;
	}
	
	public static boolean isPartyLeader(ProxiedPlayer p) {
		return parties.containsKey(p.getName());
	}
	
	public static String getPartyLeader(ProxiedPlayer p) {
		if(isPartyLeader(p)) return p.getName();
		else {
			for(String keys : parties.keySet()) {
				if(parties.get(keys).contains(p.getName())) return keys;
			}
		}
		return null;
	}
	
	public static void addPlayerToParty(ProxiedPlayer leader, ProxiedPlayer p) {

		invites.remove(p.getName());
		
		ArrayList<String> member = parties.get(leader.getName());
		if(member == null) member = new ArrayList<>();
		
		PartyMethods.sendPartyMessage(leader.getName(), pr + "§e" + p.getName() + " §7hat die Party betreten!");
		
		member.add(p.getName());
		parties.put(leader.getName(), member);
		
	}
	
	public static void deleteParty(ProxiedPlayer p) {
		if(!isInParty(p)) {
			Methods.sendMessage(p, pr + "Du bist in keiner Party!");
			return;
		}
		
		if(!isPartyLeader(p)) {
			Methods.sendMessage(p, pr + "Du bist nicht der Party-Leader!");
			return;
		}
		
		for(String playerNames : parties.get(p.getName())) {
			Methods.sendMessage(ProxyServer.getInstance().getPlayer(playerNames), pr + "§cDie Party wurde aufgelöst!");
		}
		
		Methods.sendMessage(p, pr + "§cDeine Party wurde aufgelöst!");
		
		parties.remove(p.getName());
	}
	
	public static boolean sendPartyMessage(String sender, String message) {
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender); 
		
		if(!isInParty(p)) return false;
		
		for(String playerNames : parties.get(getPartyLeader(p))) {
			Methods.sendMessage(ProxyServer.getInstance().getPlayer(playerNames), message);
		}
		Methods.sendMessage(ProxyServer.getInstance().getPlayer(getPartyLeader(p)), message);
		
		return true;
	}
	
	public static void leaveParty(ProxiedPlayer p) {
		
		if(!isInParty(p)) {
			Methods.sendMessage(p, pr + "Du bist in keiner Party!");
			return;
		}
		
		if(isPartyLeader(p)) {
			Methods.sendMessage(p, PartyMethods.pr + "Als Partyleader kannst du die Party nicht verlassen!");
			Methods.sendMessage(p, PartyMethods.pr + "Du kannst sie mit §e/party delete §7auflösen");
			return;
		}
		
		String leader = getPartyLeader(p);
		ArrayList<String> member = parties.get(leader);
		member.remove(p.getName());
		parties.put(leader, member);
		
		Methods.sendMessage(p, pr + "Du hast die Party verlassen!");
		sendPartyMessage(leader, pr + "§e" + p.getName() + " §7hat die Party verlassen!");
		
		if(member.isEmpty()) {
			deleteParty(ProxyServer.getInstance().getPlayer(leader));
		}
		
	}
	
}
