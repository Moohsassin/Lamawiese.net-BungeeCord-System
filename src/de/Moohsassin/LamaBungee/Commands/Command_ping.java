package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_ping extends Command {

	public Command_ping() {
		super("ping");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(args.length == 0) {
				Methods.sendMessage(p, LamaBungee.pr + "Dein Ping: §e" + p.getPing());
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			if(t == null) {
				Methods.sendMessage(p, LamaBungee.pr + "§e" + args[0] + " §7ist nicht online!");
				return;
			}
			
			Methods.sendMessage(p, LamaBungee.pr + t.getName() + "s Ping: §e" + t.getPing());
			
		}
		
	}
}
