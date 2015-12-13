package de.Moohsassin.PartyManager;

import de.Moohsassin.LamaBungee.Methods;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PartyEvents implements Listener {

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		if(PartyMethods.isPartyLeader(p)) {
			
			for(String names : PartyMethods.parties.get(p.getName())) {
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(names);
				
				t.connect(p.getServer().getInfo());
				Methods.sendMessage(t, PartyMethods.pr + "Die Party hat den Server gewechselt!");
				
			}
			
		}
	}
	
	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		
		ProxiedPlayer p = e.getPlayer();
		
		if(PartyMethods.isPartyLeader(p)) PartyMethods.deleteParty(p);
		if(PartyMethods.isInParty(p)) PartyMethods.leaveParty(p);
		
	}
	
}
