package de.Moohsassin.LamaBungee.Events;

import java.io.File;

import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_Ping implements Listener {

	@EventHandler
	public void onProxyPing(ProxyPingEvent e) {
		
		ServerPing ping = e.getResponse();
		
		ping.setVersion(new Protocol("LamaCord 1.8", ping.getVersion().getProtocol()));
		
		File file = new File("/minecraft", "bungee.yml");
		
		ping.setDescription(Methods.getStringFromFile(file, "Motd.Header") + "\n" + Methods.getStringFromFile(file, "Motd.Footer"));
		
		if(Methods.getStringFromFile(file, "Settings.Wartung").equalsIgnoreCase("true")) {
			ping.setVersion(new Protocol(Methods.getStringFromFile(file, "Motd.Wartung.Protocol"), 2));
			ping.setDescription(Methods.getStringFromFile(file, "Motd.Wartung.Header") + "\n" + Methods.getStringFromFile(file, "Motd.Wartung.Footer"));
		}
		
		e.setResponse(ping);
		
	}
	
}
