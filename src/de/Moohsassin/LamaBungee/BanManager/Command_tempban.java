package de.Moohsassin.LamaBungee.BanManager;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_tempban extends Command {

	public Command_tempban() {
		super("tempban");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof ProxiedPlayer) {
			
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			String rank = PlayerDatas.getRank(p.getName());
			if(!(rank.equalsIgnoreCase("Owner") | rank.equalsIgnoreCase("Admin") | rank.contains("Mod") | rank.contains("Dev"))) {
				Methods.sendMessage(p, LamaBungee.noPerm);
				return;
			}
			
			if(args.length < 4) {
				Methods.sendMessage(p, "§cVerwendung: §7/tempban <Spielername> <Zeit> <Einheit> <Grund>");
				return;
			}
			
			String target = args[0];
			if(PlayerDatas.getRank(target) == null) {
				Methods.sendMessage(p, "§e" + target + " §7hat noch nie auf der Lamawiese gespielt!");
				return;
			}
			
			int time;
			try {
				time = Integer.valueOf(args[1]);
			} catch (Exception e) {
				Methods.sendMessage(p, "§cDeine Zeit ist keine Zahl!");
				return;
			}
			
			TimeUnit u = null;
			if(args[2].length() == 1) u = TimeUnit.getFromShortCut(args[2]);
			else u = TimeUnit.getFromGerman(args[2]);
			
			if(u == null) {
				Methods.sendMessage(p, "Du musst eine gültige Einheit angeben!");
				return;
			}
			
			String reason = "~###~*+";
			for(int i = 3; i < args.length; i ++) {
				reason += " " + args[i];
			}
			reason = reason.replace("~###~*+ ", "");
			
			target = PlayerDatas.getName(PlayerDatas.getUUID(target));
			
			if(target.equalsIgnoreCase("Moohsassin")) {
				Methods.sendMessage(p, "§cDieser Spieler ist zu cool um gebannt zu werden! §cMooooh mein Freund!");
				return;
			}
			
			BanMethods.ban(target, reason, time, u, p);
			Methods.sendMessage(p, LamaBungee.pr + "§e" + target + " §7wurde bis zum " + BanMethods.getBanEndDate(target) + " §7vom Netzwerk gebannt!");
		}	
	}
}
