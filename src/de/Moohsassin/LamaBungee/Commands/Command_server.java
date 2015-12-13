package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_server extends Command {

	public Command_server(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new ComponentBuilder("§cNur für Spieler!").create());
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) sender;

		String rank = PlayerDatas.getRank(p.getName());
		
		if(rank.equalsIgnoreCase("Owner")
		 | rank.equalsIgnoreCase("Admin")
		 | rank.contains("Dev")
		 | rank.contains("Mod")
		 | rank.equalsIgnoreCase("BauTeam")) {

			if(args.length == 0) {
				Methods.sendMessage(p, LamaBungee.pr + "Du musst einen Server angeben!");
				return;
			}

			ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
			
			if(info == null) {
				Methods.sendMessage(p, LamaBungee.pr + "§cDieser Server existiert nicht!");
				return;
			}

			p.connect(info);
			
			return;
		}
		
		Methods.sendMessage(p, LamaBungee.noPerm);
		return;
		
	}
	
}
