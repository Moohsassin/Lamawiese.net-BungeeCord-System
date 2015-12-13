package de.Moohsassin.LamaBungee.BanManager;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_ban extends Command {

	public Command_ban() {
		super("ban");
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
			
			if(args.length == 0) {
				Methods.sendMessage(p, "§cVerwendung: §7/ban <Spielername> <Grund>");
				return;
			}
			
			if(args.length <= 1) {
				Methods.sendMessage(p, "§cDu musst einen Grund angeben!");
				return;
			}
			
			String target = args[0];
			if(PlayerDatas.getRank(target) == null) {
				Methods.sendMessage(p, "§e" + target + " §7hat noch nie auf der Lamawiese gespielt!");
				return;
			}
			
			String reason = "~###~*+";
			for(int i = 1; i < args.length; i ++) {
				reason += " " + args[i];
			}
			reason = reason.replace("~###~*+ ", "");
			
			target = PlayerDatas.getName(PlayerDatas.getUUID(target));
			
			if(target.equalsIgnoreCase("Moohsassin")) {
				Methods.sendMessage(p, "§cDieser Spieler ist zu cool um gebannt zu werden! §cMooooh mein Freund!");
				return;
			}
			
			BanMethods.ban(target, reason, -2, null, p);
			Methods.sendMessage(p, "§e" + target + " §7wurde §cPERMANENT §7vom Netzwerk gebannt!");
			
		}
		
	}
	
}
