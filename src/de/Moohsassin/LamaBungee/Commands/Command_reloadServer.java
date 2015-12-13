package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_reloadServer extends Command {

	public Command_reloadServer() {
		super("reloadServer");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			String rank = PlayerDatas.getRank(p.getName());
			
			if(rank.equalsIgnoreCase("Owner")) {
				
				Methods.reloadServer();
				Methods.sendMessage(p, LamaBungee.pr + "Server wurden neugeladen!");
				
			} else {
				Methods.sendMessage(p, LamaBungee.noPerm); 
			}
			
		}
		
	}
	
}
