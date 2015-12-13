package de.Moohsassin.LamaBungee.Events;

import java.io.File;

import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_ServerSwitch implements Listener {

	@EventHandler
	public void onSwitch(ServerSwitchEvent e) {
		
		ProxiedPlayer p = e.getPlayer();
		
		File f = new File("/minecraft", "bungee.yml");
		String[] tab = new String[]{
			Methods.getStringFromFile(f, "Tablist.Header").replaceAll("%n", "\n").replaceAll("%currentServer%", p.getServer().getInfo().getName()),
			Methods.getStringFromFile(f, "Tablist.Footer").replaceAll("%n", "\n").replaceAll("%currentServer%", p.getServer().getInfo().getName())
		};
		
		p.setTabHeader(
			new ComponentBuilder(tab[0]).create(), 
			new ComponentBuilder(tab[1]).create()
		);
		
	}
}
