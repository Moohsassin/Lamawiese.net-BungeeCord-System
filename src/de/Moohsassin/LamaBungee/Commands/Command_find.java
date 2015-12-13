package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_find extends Command {

	String name;
	
	public Command_find(String name) {
		super(name);
		this.name = name;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {

			ProxiedPlayer p = (ProxiedPlayer) sender;
			String rank = PlayerDatas.getRank(p.getName());
			
			if(!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Dev") | rank.contains("Mod"))) {
				Methods.sendMessage(p, LamaBungee.noPerm);
				return;
			}
			
			if(args.length == 0) {
				Methods.sendMessage(p, "§cVerwendung: /" + name + " <Spieler>");
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			
			if(t == null) {
				Methods.sendMessage(p, "§e" + args[0] + " §7ist nicht online!");
				return;
			}
			
			Methods.sendMessage(p, "  ");
			Methods.sendMessage(p, LamaBungee.pr + "Spieler-Informationen für §e" + t.getName());
			Methods.sendMessage(p, "  ");
			Methods.sendMessage(p, "§6» §7UUID: §e" + t.getUniqueId());
			Methods.sendMessage(p, "§6» §7Rang: §e" + PlayerDatas.getRank(t.getName()));
			Methods.sendMessage(p, "§6» §7Server: §e" + t.getServer().getInfo().getName());
			Methods.sendMessage(p, "§6» §7Ip: §e" + t.getAddress());
			Methods.sendMessage(p, "§6» §7Ping: §e" + t.getPing());
			Methods.sendMessage(p, "  ");
			
		}
	}
}