package de.Moohsassin.LamaBungee;

import java.io.File;

import de.Moohsassin.LamaBungee.BanManager.BanMethods;
import de.Moohsassin.LamaBungee.BanManager.Command_ban;
import de.Moohsassin.LamaBungee.BanManager.Command_checkban;
import de.Moohsassin.LamaBungee.BanManager.Command_tempban;
import de.Moohsassin.LamaBungee.BanManager.Command_unban;
import de.Moohsassin.LamaBungee.Commands.Command_blocked;
import de.Moohsassin.LamaBungee.Commands.Command_broadcast;
import de.Moohsassin.LamaBungee.Commands.Command_find;
import de.Moohsassin.LamaBungee.Commands.Command_glist;
import de.Moohsassin.LamaBungee.Commands.Command_hub;
import de.Moohsassin.LamaBungee.Commands.Command_kick;
import de.Moohsassin.LamaBungee.Commands.Command_list;
import de.Moohsassin.LamaBungee.Commands.Command_msg;
import de.Moohsassin.LamaBungee.Commands.Command_ping;
import de.Moohsassin.LamaBungee.Commands.Command_reloadServer;
import de.Moohsassin.LamaBungee.Commands.Command_server;
import de.Moohsassin.LamaBungee.Commands.Command_systemreload;
import de.Moohsassin.LamaBungee.Events.Event_Chat;
import de.Moohsassin.LamaBungee.Events.Event_Join;
import de.Moohsassin.LamaBungee.Events.Event_Login;
import de.Moohsassin.LamaBungee.Events.Event_Ping;
import de.Moohsassin.LamaBungee.Events.Event_Quit;
import de.Moohsassin.LamaBungee.Events.Event_ServerConnect;
import de.Moohsassin.LamaBungee.Events.Event_ServerSwitch;
import de.Moohsassin.LamaBungee.FriendManager.Command_friend;
import de.Moohsassin.LamaBungee.FriendManager.FriendMethods;
import de.Moohsassin.PartyManager.Command_party;
import de.Moohsassin.PartyManager.PartyEvents;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class LamaBungee extends Plugin {

	public static Plugin instance;
	public static String pr = "§6Lamawiese §8┃ §7";
	public static String noPerm = pr + "§cDu hast keine Berechtigung für diesen Befehl!";
	public static PluginManager pm;
	
	@Override
	public void onEnable() {
		
		instance = this;
		pm = getProxy().getPluginManager();
		
		BanMethods.openConnection();
		PlayerDatas.openConnection();
		FriendMethods.openConnection();
		ServerManager.openConnection();
		
		registerCommands();
		registerEvents();
		
		Methods.reloadServer();
		
		Methods.runBroadcaster();
		
	}
	
	@Override
	public void onDisable() {
		
		BanMethods.closeConnection();
		PlayerDatas.closeConnection();
		FriendMethods.closeConnection();
		ServerManager.closeConnection();
		
		Methods.cancelBroadcaster();
		
	}
	
	private void registerEvents() {
		pm.registerListener(this, new Event_Join());
		pm.registerListener(this, new Event_ServerSwitch());
		pm.registerListener(this, new Event_Ping());
		pm.registerListener(this, new Event_Login());
		pm.registerListener(this, new Event_Chat());
		pm.registerListener(this, new PartyEvents());
		pm.registerListener(this, new Event_Quit());
		pm.registerListener(this, new Event_ServerConnect());
	}
	
	private void registerCommands() {
		
		//\\ ===== BLOCKED COMMANDS ===== //\\
		for(String blockedCmds : Methods.getStringListFromFile(new File("/minecraft", "bungee.yml"), "Blocked_Commands")) {	
			pm.registerCommand(this, new Command_blocked(blockedCmds));
			pm.registerCommand(this, new Command_blocked("bukkit:" + blockedCmds));
		}
		
		//\\ ===== SOCIAL COMMANDS ===== //\\
		pm.registerCommand(this, new Command_friend());
		pm.registerCommand(this, new Command_party());
		
		pm.registerCommand(this, new Command_msg("bukkit:msg"));
		pm.registerCommand(this, new Command_msg("bukkit:tell"));
		pm.registerCommand(this, new Command_msg("tell"));
		pm.registerCommand(this, new Command_msg("msg"));
		
		//\\ ===== ADMIN COMMANDS ===== //\\
		pm.registerCommand(this, new Command_reloadServer());
		pm.registerCommand(this, new Command_systemreload("sreload"));
		pm.registerCommand(this, new Command_broadcast("broadcast"));
		pm.registerCommand(this, new Command_broadcast("alert"));
		pm.registerCommand(this, new Command_list());
		pm.registerCommand(this, new Command_glist());
		pm.registerCommand(this, new Command_find("find"));
		pm.registerCommand(this, new Command_find("info"));
		pm.registerCommand(this, new Command_find("ip"));
		
		//\\ ===== QUICK COMMANDS ===== //\\
		pm.registerCommand(this, new Command_ping());
		pm.registerCommand(this, new Command_hub("hub"));
		pm.registerCommand(this, new Command_hub("lobby"));
		pm.registerCommand(this, new Command_server("join"));
		pm.registerCommand(this, new Command_server("server"));
		
		//\\ ===== PLAYER MANAGEMENT COMMANDS ===== //\\
		pm.registerCommand(this, new Command_kick());
		pm.registerCommand(this, new Command_ban());
		pm.registerCommand(this, new Command_tempban());
		pm.registerCommand(this, new Command_unban());
		pm.registerCommand(this, new Command_checkban());
	}
}
