package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_hub extends Command {

	public Command_hub(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(p.getServer().getInfo().getName().contains("MAIN-HUB")) {
				Methods.sendMessage(p, LamaBungee.pr + "Du bist bereits in der Lobby!");
				return;
			}
			
			if(p.getServer().getInfo().getName().contains("HUB")) {
				p.connect(ProxyServer.getInstance().getServerInfo("MAIN-HUB-1"));
				return;
			}
			
			if(p.getServer().getInfo().getName().contains("-")) {
				String game = p.getServer().getInfo().getName().split("-")[0] + "-HUB-1";
				if(ProxyServer.getInstance().getServers().containsKey(game)) {
					p.connect(ProxyServer.getInstance().getServerInfo(game));
					return;
				}
			}
			
			p.connect(ProxyServer.getInstance().getServerInfo("MAIN-HUB-1"));
			
			return;
		}
	}
}
