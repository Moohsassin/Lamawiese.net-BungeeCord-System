package de.Moohsassin.LamaBungee.Events;

import java.io.File;

import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import de.Moohsassin.LamaBungee.BanManager.BanMethods;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event_Login implements Listener {

	@EventHandler
	public void onLogin(PreLoginEvent e) {
		String playerName = e.getConnection().getName();
		
		if(BanMethods.isBanned(playerName) && BanMethods.getTimeLeft(playerName) != 0) {
			e.setCancelled(true);
			e.setCancelReason(BanMethods.getBanScreen(playerName, BanMethods.getBanEnd(playerName), BanMethods.getReason(playerName)));
			return;
		}
		
		String rank = PlayerDatas.getRank(playerName);
		if(Methods.getStringFromFile(new File("/minecraft", "bungee.yml"), "Settings.Wartung").equalsIgnoreCase("true") && 
		!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Dev") | rank.equalsIgnoreCase("BauTeam"))) {
			e.setCancelled(true);
			e.setCancelReason("§6§lWir führen eine längere Wartung durch und sind bald wieder zurück!");
		}
		
	}
}
