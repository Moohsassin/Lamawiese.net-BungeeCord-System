package de.Moohsassin.LamaBungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.protocol.packet.Chat;

public class Methods {

	static ScheduledTask broadcaster;
	
	public static void reloadServer() {
		
		ProxyServer.getInstance().getServers().clear();
		
		for(String server : Methods.getStringListFromFile(new File("/minecraft", "server.yml"), "Server")) {
			
			String[] split = server.split(";");
			int port = Integer.valueOf(split[1]);
			String name = split[0];
			
			if(split.length == 3) {
				
				for(int i = 1; i <= Integer.valueOf(split[2]); i ++) {
					ServerInfo info = ProxyServer.getInstance().constructServerInfo(name + i, new InetSocketAddress("localhost", Integer.valueOf(port + "" + i)), "§e§lLamawiese.net - §6§l" + name + i, false);
					ProxyServer.getInstance().getServers().put(name + i, info);
				}
				
			} else {
				ServerInfo info = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress("localhost", port), "§e§lLamawiese.net - §6§l" + name, false);
				ProxyServer.getInstance().getServers().put(name, info);
			}
			
		}
	}
	
	public static void runBroadcaster() {
		File f = new File("/minecraft", "bungee.yml");
		final String prefix = getStringFromFile(f, "Broadcaster.Prefix");
		final List<String> messages = getStringListFromFile(f, "Broadcaster.Messages");
		final List<String> toPick = new ArrayList<>();
		toPick.addAll(messages);
		
		broadcaster = ProxyServer.getInstance().getScheduler().schedule(LamaBungee.instance, new Runnable() {
			
			public void run() {
				
				if(toPick.isEmpty()) toPick.addAll(messages);
				
				String text = toPick.get(0);
				toPick.remove(text);
				
				text = text.replaceAll("%n", "/n");
				
				for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					Methods.sendMessage(p, "  ");
					Methods.sendMessage(p, prefix + "§7 " + text.replaceAll("%playername%", p.getName()));
					Methods.sendMessage(p, "  ");
				}
				
			}
		}, 0, 10, TimeUnit.MINUTES);
		
	}
	
	public static void cancelBroadcaster() {
		broadcaster.cancel();
		broadcaster = null;
	}
	
	public static String getStringFromFile(File f, String toGet) {
		Configuration prov;
		try {
			prov = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			return prov.getString(toGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getStringListFromFile(File f, String path) {
		Configuration prov;
		try {
			prov = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			return prov.getStringList(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setStringToFile(File f, String path, String toSet) {
		Configuration prov;
		try {
			prov = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			prov.set(path, toSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(ProxiedPlayer p, String message) {
		p.unsafe().sendPacket(new Chat(JSONUtil.toJSON(ChatColor.translateAlternateColorCodes('&', message))));
	}
	
	
	
}
