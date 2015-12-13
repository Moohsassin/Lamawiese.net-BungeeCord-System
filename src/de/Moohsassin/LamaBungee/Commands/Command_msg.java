package de.Moohsassin.LamaBungee.Commands;

import java.util.UUID;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import de.Moohsassin.LamaBungee.FriendManager.FriendMethods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_msg extends Command {

	public Command_msg(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(args.length <= 1) {
				Methods.sendMessage(p, LamaBungee.pr + "§cVerwendung: §7/msg <Spiername> <Nachricht>");
				return;
			}

			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			
			if(t == null) {
				Methods.sendMessage(p, FriendMethods.pr + "§e" + args[0] + " §7ist nicht online!");
				return;
			}
			
			String message = "";
			for(int i = 1; i < args.length; i ++) {
				message += " " + args[i];
			}
			
			String rank = PlayerDatas.getRank(p.getName());
			if(rank.equalsIgnoreCase("User") | rank.equalsIgnoreCase("Premium")) {
				FriendMethods.sendMessageToFriend(p.getUniqueId(), UUID.fromString(PlayerDatas.getUUID(args[0])), message.substring(0));
			}
			
			else {
				
				Methods.sendMessage(p, LamaBungee.pr.replace("§6", "§6§l") + "Du §8➠ §e" + t.getName() + " §8» §7" + message);
				Methods.sendMessage(t, LamaBungee.pr.replace("§6", "§6§l") + "§e" + p.getName() + " §8➠ §7Dir §8» §7" + message);
				
			}
			
			return;
			
		}
		
	}
	
}
