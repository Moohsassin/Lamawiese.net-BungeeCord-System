package de.Moohsassin.LamaBungee.Events;

import de.Moohsassin.LamaBungee.ServerManager;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_ServerConnect implements Listener {

	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		
		String from = (e.getPlayer().getServer() != null ? e.getPlayer().getServer().getInfo().getName() : null);
		String to = e.getTarget().getName();
		
		if(!to.contains("-")) to = null;
		else {
			to = to.split("-")[0];
		}
		
		if(from != null && !from.contains("-")) from = null;
		else if(from != null) {
			from = from.split("-")[0];
		}
		
		ServerManager.updateCount(from, to);
		
	}
	
}
