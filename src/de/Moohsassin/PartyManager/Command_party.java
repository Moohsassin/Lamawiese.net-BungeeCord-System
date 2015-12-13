package de.Moohsassin.PartyManager;

import java.util.ArrayList;
import java.util.UUID;

import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import de.Moohsassin.LamaBungee.FriendManager.FriendMethods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_party extends Command {

	public Command_party() {
		super("party");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			String rank = PlayerDatas.getRank(p.getName());
			if(rank.equalsIgnoreCase("User") | rank.equalsIgnoreCase("Premium")) {
				UUID u = UUID.fromString(PlayerDatas.getUUID("JosephGoebbels"));
				if(FriendMethods.getFriends(u).contains(u)) {
					Methods.sendMessage(p, PartyMethods.pr + "Diese Funktion ist bald verfügbar!");
					return;
				}
			}
			
			if(args.length == 0) {
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, PartyMethods.pr + "§e/party §7Liste mit allen Befehlen"); // 0
				Methods.sendMessage(p, PartyMethods.pr + "§e/party info §7Informationen zur Party"); // 1
				Methods.sendMessage(p, PartyMethods.pr + "§e/party invite <Spieler> §7Sende eine Anfrage"); // 2 
				Methods.sendMessage(p, PartyMethods.pr + "§e/party accept <Spieler> §7Nimm eine Anfrage an"); // 2
				Methods.sendMessage(p, PartyMethods.pr + "§e/party deny <Spieler> §7Lehne eine Anfrage ab"); // 2
				Methods.sendMessage(p, PartyMethods.pr + "§e/party leave §7Verlasse eine Party"); // 1
				Methods.sendMessage(p, PartyMethods.pr + "§e/party kick <Spieler> §7Kicke einen Spieler"); // 2
				Methods.sendMessage(p, PartyMethods.pr + "§e/party delete §7Lösche deine Party"); // 1
				Methods.sendMessage(p, PartyMethods.pr + "§e/party chat <Nachricht> §7Nachricht zur Party"); // >= 2
				Methods.sendMessage(p, "  ");
				return;
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {
				
				PartyMethods.leaveParty(p);
				
			} else if(args.length == 1 && args[0].equalsIgnoreCase("info")) {
				
				if(!PartyMethods.isInParty(p)) {
					Methods.sendMessage(p, PartyMethods.pr + "Du bist in keiner Party!");
					return;
				}
				
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, PartyMethods.pr + "Party-Informationen für §e" + p.getName());
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, "§6» §7Partyleader: §e" + PartyMethods.getPartyLeader(p));
				Methods.sendMessage(p, "§6» §7Spielt auf: §e" + p.getServer().getInfo().getName());
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, "§6» §7Mitglieder:");
				Methods.sendMessage(p, "  ");
				for(String s : PartyMethods.parties.get(PartyMethods.getPartyLeader(p))) {
					Methods.sendMessage(p, "§6 ● §7" + s);
				}
				Methods.sendMessage(p, "  ");
				
			} else if(args.length == 2 && args[0].equalsIgnoreCase("invite")) {
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);
				if(t == null) {
					Methods.sendMessage(p, PartyMethods.pr + "§e" + args[1] + " §7ist nicht online!");
					return;
				}
				
				PartyMethods.invitePlayer(t, p);
				
			} else if(args.length == 2 && args[0].equalsIgnoreCase("accept")) {

				PartyMethods.acceptInvitation(p, args[1]);
				
			} else if(args.length == 2 && args[0].equalsIgnoreCase("deny")) {

				PartyMethods.denyInvitation(p, args[1]);
				
			} else if(args.length == 2 && args[0].equalsIgnoreCase("kick")) {

				if(!PartyMethods.isInParty(p)) {
					Methods.sendMessage(p, PartyMethods.pr + "Du bist in keiner Party!");
					return;
				}

				if(!PartyMethods.isPartyLeader(p)) {
					Methods.sendMessage(p, PartyMethods.pr + "Du bist nicht der Party-Leader!");
					return;
				}
				
				if(args[1].equalsIgnoreCase(p.getName())) {
					Methods.sendMessage(p, PartyMethods.pr + "Du kannst dich nicht selbst kicken!");
					return;
				}
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);
				if(t == null) {
					Methods.sendMessage(p, PartyMethods.pr + "§e" + args[1] + " §7ist nicht in deiner Party!");
					return;
				}
				
				if(!PartyMethods.isInParty(t)) {
					Methods.sendMessage(p, PartyMethods.pr + "§e" + t.getName() + " §7ist nicht in deiner Party!");
					return;
				}
				
				if(!PartyMethods.getPartyLeader(t).equals(p.getName())) {
					Methods.sendMessage(p, PartyMethods.pr + "§e" + t.getName() + " §7ist nicht in deiner Party!");
					return;
				}
				
				ArrayList<String> member = PartyMethods.parties.get(p.getName());
				member.remove(t.getName());
				PartyMethods.parties.put(p.getName(), member);
				
				PartyMethods.sendPartyMessage(p.getName(), PartyMethods.pr + "§e" + t.getName() + " §7wurde aus der Party gekickt!");
				Methods.sendMessage(p, PartyMethods.pr + "Du hast §e" + t.getName() + " §7aus deiner Party gekickt!");
				Methods.sendMessage(t, PartyMethods.pr + "Du wurdest aus der Party gekickt!");
				
				if(member.isEmpty()) {
					PartyMethods.deleteParty(p);
				}
				
			} else if(args.length == 1 && args[0].equalsIgnoreCase("delete")) {
				
				PartyMethods.deleteParty(p);
				
			} else if(args.length >= 2 && args[0].equalsIgnoreCase("chat")) {
				
				String msg = "";
				for(int i = 1; i < args.length; i ++) {
					msg += " " + args[i];
				}
				
				msg = msg.substring(1);
				
				if(!PartyMethods.sendPartyMessage(p.getName(), PartyMethods.pr + "§e" + p.getName() + "§8 » §7" + msg)) {
					Methods.sendMessage(p, PartyMethods.pr + "Du bist in keiner Party!");
					return;
				}
				
			} else {
				Methods.sendMessage(p, PartyMethods.pr + "Diesen Befehl gibt es nicht! Mit §e/party §7erhälst du eine Liste mit verfügbaren Befehlen!");
			}
		}
	}
	
}
