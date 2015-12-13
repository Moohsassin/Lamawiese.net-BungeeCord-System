package de.Moohsassin.LamaBungee.FriendManager;

import java.util.ArrayList;
import java.util.UUID;

import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_friend extends Command {

	public Command_friend() {
		super("friend");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(args.length == 0) {
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend §7Liste mit allen Befehlen");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend list §7Liste mit all deinen Freunden");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend invite <Spieler> §7Sende eine Anfrage");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend accept <Spieler> §7Nimm eine Anfrage an");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend deny <Spieler> §7Lehne eine Anfrage ab");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend remove <Spieler> §7Kündigt eine Freundschaft");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend find <Spieler> §7Server eines Freundes");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend jump <Spieler> §7Springe zu einem Freund");
				Methods.sendMessage(p, FriendMethods.pr + "§e/friend invites §7Liste mit unbeantworteten Anfragen");
				Methods.sendMessage(p, FriendMethods.pr + "§e/msg <Spieler> <Nachricht> §7Nachricht zum Freund");
				Methods.sendMessage(p, "  ");
				return;
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
				
				ArrayList<UUID> offline = FriendMethods.getFriends(p.getUniqueId());
				ArrayList<UUID> online = new ArrayList<>();
				
				if(offline.isEmpty()) {
					Methods.sendMessage(p, FriendMethods.pr + "Du hast keine Lama-Freunde!");
					return;
				}
				
				for(UUID uuids : offline) {
					if(ProxyServer.getInstance().getPlayer(uuids) != null) {
						online.add(uuids);
					}
				}
				
				offline.removeAll(online);
				
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, FriendMethods.pr + "Freunde-Informationen für §e" + p.getName());
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, "§6» §7Online:");
				Methods.sendMessage(p, "  ");
				for(UUID u : online) {
					ProxiedPlayer t = ProxyServer.getInstance().getPlayer(u);
					Methods.sendMessage(p, "§6 ● §7" + t.getName() + " §8(§7" + t.getServer().getInfo().getName() + "§8)");
				}
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, "§6» §7Offline:");
				String off = "";
				for(UUID u : offline) {
					off += ", " + PlayerDatas.getName(u + "");
				}
				if(!off.equalsIgnoreCase("")) Methods.sendMessage(p, "§c  " + off.substring(2));
				else Methods.sendMessage(p, "  §c§okeiner");
				Methods.sendMessage(p, "  ");
				
			}
			
			else if(args.length == 1 && args[0].equalsIgnoreCase("invites")) {
				
				ArrayList<UUID> requests = FriendMethods.getFriendRequests(p.getUniqueId());

				if(requests.isEmpty()) {
					Methods.sendMessage(p, FriendMethods.pr + "Du hast §ckeine §7unbeantwortete Anfrage!");
					return;
				}
				
				Methods.sendMessage(p, "  ");
				Methods.sendMessage(p, FriendMethods.pr + "Du hast noch §e" + requests.size() + " §7unbeantwortete Anfragen:");
				String toSend = "";
				for(UUID u : requests) {
					toSend += ", " + PlayerDatas.getName(u + "");
				}
				Methods.sendMessage(p, FriendMethods.pr + "§7" + toSend.substring(2));
				Methods.sendMessage(p, "  ");
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("invite")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler hat noch nie auf der Lamawiese gespielt!");
					return;
				}
				
				UUID u = UUID.fromString(PlayerDatas.getUUID(args[1]));
				
				if(u.equals(p.getUniqueId())) {
					Methods.sendMessage(p, FriendMethods.pr + "Du kannst dir keine Anfrage schicken!");
					return;
				}
				
				FriendMethods.sendRequest(p.getUniqueId(), UUID.fromString(PlayerDatas.getUUID(args[1])));
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("accept")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler hat noch nie auf der Lamawiese gespielt!");
					return;
				}
				
				FriendMethods.acceptRequest(p.getUniqueId(), UUID.fromString(PlayerDatas.getUUID(args[1])));
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("deny")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Du hast von diesem Spieler keine Anfrage!");
					return;
				}
				
				FriendMethods.denyRequest(p.getUniqueId(), UUID.fromString(PlayerDatas.getUUID(args[1])));
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("remove")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler ist nicht dein Freund!");
					return;
				}
				
				FriendMethods.removeFriend(p.getUniqueId(), UUID.fromString(PlayerDatas.getUUID(args[1])));
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("find")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler ist nicht dein Freund!");
					return;
				}
				
				if(!FriendMethods.getFriends(p.getUniqueId()).contains(UUID.fromString(PlayerDatas.getUUID(args[1])))) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler ist nicht dein Freund!");
					return;
				}
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);
				if(t == null) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Freund ist §coffline!");
					return;
				}
				
				Methods.sendMessage(p, FriendMethods.pr + "§e" + t.getName() + " §7befindet sich momentan auf §e" + t.getServer().getInfo().getName());
				
			}
			
			else if(args.length == 2 && args[0].equalsIgnoreCase("jump")) {
				
				if(!PlayerDatas.hasPlayerBefore(args[1])) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler ist nicht dein Freund!");
					return;
				}
				
				if(!FriendMethods.getFriends(p.getUniqueId()).contains(UUID.fromString(PlayerDatas.getUUID(args[1])))) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Spieler ist nicht dein Freund!");
					return;
				}
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);
				if(t == null) {
					Methods.sendMessage(p, FriendMethods.pr + "Dieser Freund ist §coffline!");
					return;
				}
				
				p.connect(t.getServer().getInfo());
				
			} else {
				Methods.sendMessage(p, FriendMethods.pr + "Diesen Befehl gibt es nicht! Mit §e/friend §7erhälst du eine Liste mit verfügbaren Befehlen!");
			}
			
		}
		
	}
}
