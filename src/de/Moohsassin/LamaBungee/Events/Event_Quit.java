package de.Moohsassin.LamaBungee.Events;

import java.util.concurrent.TimeUnit;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.ServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_Quit implements Listener {

	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		
		final String server = e.getPlayer().getServer().getInfo().getName();
		
		ProxyServer.getInstance().getScheduler().schedule(LamaBungee.instance, new Runnable() {public void run() {
		
			ServerManager.updateTotalOnlineCount();
			if(server.contains("-")) ServerManager.updateCount(server.split("-")[0], null);
			
		}}, 50, TimeUnit.MILLISECONDS);
	}
	
}
