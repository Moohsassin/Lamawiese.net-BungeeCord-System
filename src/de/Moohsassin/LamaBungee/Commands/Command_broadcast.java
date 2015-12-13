package de.Moohsassin.LamaBungee.Commands;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import de.Moohsassin.LamaBungee.Scroller;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_broadcast extends Command {

	public Command_broadcast(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			String rank = PlayerDatas.getRank(p.getName());
			
			if(!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Dev"))) {
				Methods.sendMessage(p, LamaBungee.noPerm);
				return;
			}
			
			if(args.length == 0) {
				Methods.sendMessage(p, "§cVerwendung: /broadcast [-g] [-t] <Message>");
				return;
			}
			
			boolean isGlobal = false;
			boolean isTitle = false;
			
			int arguments = 0;
			if(args[0].equalsIgnoreCase("-g") | args[0].equalsIgnoreCase("-t")) {
				arguments ++;
				if(args[0].equalsIgnoreCase("-g")) {
					isGlobal = true;
				} else isTitle = true;
			}
			if(args.length >= 2 && args[1].equalsIgnoreCase("-t") && !args[0].equalsIgnoreCase("-t")) {
				arguments ++;
				isTitle = true;
			}
			if(args.length >= 2 && args[1].equalsIgnoreCase("-g") && !args[0].equalsIgnoreCase("-g")) {
				arguments ++;
				isGlobal = true;
			}
			
			String toSend = "";
			for(int i = arguments; i < args.length; i ++) toSend += " " + args[i].replaceAll("&", "§");
			toSend = toSend.substring(1);
			toSend = "§7" + toSend;
			
			if(!isTitle) {
				for(ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
					if(isGlobal) Methods.sendMessage(players, "§6Broadcast §8┃ §7" + toSend);
					else if(players.getServer().getInfo().getName().equalsIgnoreCase(((ProxiedPlayer) sender).getServer().getInfo().getName())) Methods.sendMessage(players, "§6Broadcast §8┃ §7" + toSend);
				}
			} else {
				
				for(int i = 0; i < 20; i ++) {
					toSend = " " + toSend;
				}
				Scroller scroller = new Scroller(toSend, 20, 40, '&');
				
				final Collection<ProxiedPlayer> atStart = ProxyServer.getInstance().getPlayers();
				
				for(int i = 0; i < (toSend.length() + 20); i ++) {
					
					final Title title = ProxyServer.getInstance().createTitle();
					title.fadeIn(0);
					title.title(new ComponentBuilder(scroller.next()).create());
					title.fadeOut(0);
					title.stay(30);
					
					BungeeCord.getInstance().getScheduler().schedule(LamaBungee.instance, new Runnable() {
						
						@Override
						public void run() {
							for(ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
								if(atStart.contains(players)) title.send(players);
							}
						}
					}, 150 * i, TimeUnit.MILLISECONDS);
					
				}
				
			}
				
			return;
			
		}
		
	}
	
}
