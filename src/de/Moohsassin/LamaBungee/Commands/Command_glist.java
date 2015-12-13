package de.Moohsassin.LamaBungee.Commands;

import java.util.HashMap;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_glist extends Command {

	public Command_glist() {
		super("glist");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new ComponentBuilder("Es sind insgesamt " + ProxyServer.getInstance().getPlayers().size() + " Lamas online!").color(ChatColor.GREEN).create());
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		
		String rank = PlayerDatas.getRank(p.getName());
		if(!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Dev"))) {
			Methods.sendMessage(p, LamaBungee.noPerm);
			return;
		}
		
		HashMap<String, Integer> serverType = new HashMap<>();
		for(String server : ProxyServer.getInstance().getServers().keySet()) {
			String name = server.replaceAll("[0-9]", "");
			if(name.contains("-") && name.split("-").length == 2) name = name.split("-")[1];
			int i = 0;
			if(serverType.containsKey(name)) i = serverType.get(name);
			
			i += ProxyServer.getInstance().getServerInfo(server).getPlayers().size();
			
			serverType.put(name, i);
		}
		
		Methods.sendMessage(p, "  ");
		for(String server : serverType.keySet()) {
			Methods.sendMessage(p, "§6» §7Auf den §e" + server.replaceAll("-", "") + " Servern §7sind §e" + serverType.get(server) + " §7Lamas online");
		}
		Methods.sendMessage(p, "  ");
		Methods.sendMessage(p, "§6» §7Insgesamt sind §e" + ProxyServer.getInstance().getPlayers().size() + " Lamas §7online!");
		Methods.sendMessage(p, "  ");
		
		serverType.clear();
		
	}
	
}
