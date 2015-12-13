package de.Moohsassin.LamaBungee.Events;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_Chat implements Listener {

	@EventHandler
	public void onChat(ChatEvent e) {
		
		String message = e.getMessage();
		
		message = message.replaceAll("<3", "❤");
		
		e.setMessage(message);
		
	}
	
}
