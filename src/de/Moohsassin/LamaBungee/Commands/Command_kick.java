package de.Moohsassin.LamaBungee.Commands;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_kick extends Command {

	public Command_kick() {
		super("kick");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			String rank = PlayerDatas.getRank(p.getName());
			if(!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Mod") | rank.contains("Dev"))) {
				Methods.sendMessage(p, LamaBungee.noPerm);
				return;
			}
			
			if(args.length == 0) {
				Methods.sendMessage(p, "§cVerwendung: /kick <Spieler> [Grund]");
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			if(t == null) {
				Methods.sendMessage(p, "§e" + args[0] + " §7ist nicht online!");
				return;
			}
			
			String reason = null;
			if(args.length >= 2) {
				reason = "";
				for(int i = 1; i < args.length; i ++) reason += " " + args[i];
				reason = reason.substring(1);
			}
			
			String kickmsg = "§cDu wurdest von der Lamawiese gekickt!";
			
			if(reason != null) kickmsg += "\n\n§cGrund: §7" + reason;
			
			t.disconnect(new ComponentBuilder(kickmsg).create());
			
		}
	}
}