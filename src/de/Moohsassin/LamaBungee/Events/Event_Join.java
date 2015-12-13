package de.Moohsassin.LamaBungee.Events;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.ServerManager;
import de.Moohsassin.LamaBungee.FriendManager.FriendMethods;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_Join implements Listener {

	@EventHandler
	public void onJoin(PostLoginEvent e) {
		
		final ProxiedPlayer p = e.getPlayer();
		
		if(!FriendMethods.getFriendRequests(p.getUniqueId()).isEmpty()) {
			LamaBungee.pm.dispatchCommand(p, "friend invites");
		}
		
		File f = new File("/minecraft", "bungee.yml");
		List<String> updates = Methods.getStringListFromFile(f, "Updates");
		int i = 1;
		for(String update : updates) {

			final Title title = ProxyServer.getInstance().createTitle();
			
			title.title(new ComponentBuilder(Methods.getStringFromFile(f, "Updates_Header")).create());
			title.subTitle(new ComponentBuilder(update).create());
			title.stay(20 * 4);
			title.fadeIn(0);
			title.fadeOut(0);
			
			ProxyServer.getInstance().getScheduler().schedule(LamaBungee.instance, new Runnable() {@Override public void run() {
					
				title.send(p);
					
			}}, i, TimeUnit.SECONDS);
			
			i += 3;
		}
		
		ServerManager.updateTotalOnlineCount();
		
	}
}
