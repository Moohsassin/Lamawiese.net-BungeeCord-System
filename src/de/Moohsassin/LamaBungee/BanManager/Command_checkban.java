package de.Moohsassin.LamaBungee.BanManager;

import de.Moohsassin.LamaBungee.LamaBungee;
import de.Moohsassin.LamaBungee.Methods;
import de.Moohsassin.LamaBungee.PlayerDatas;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_checkban extends Command {
	
	public Command_checkban() {
		super("checkban");
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
				Methods.sendMessage(p, "§cVerwendung: §7/checkban <Spielername>");
				return;
			}
			
			String target = args[0];
			if(PlayerDatas.getRank(target) == null) {
				Methods.sendMessage(p, "§e" + target + " §7ist nicht gebannt!");
				return;
			}
			
			if(!BanMethods.isBanned(target)) {
				Methods.sendMessage(p, "§e" + target + " §7ist nicht gebannt!");
				return;
			}
			
			if(BanMethods.getTimeLeft(target) == 0) {
				Methods.sendMessage(p, "§e" + target + " §7ist nicht gebannt!");
				return;
			}
			
			target = PlayerDatas.getName(PlayerDatas.getUUID(target));
			
			Methods.sendMessage(p, "  ");
			Methods.sendMessage(p, LamaBungee.pr + "Baninformationen für §e" + target);
			Methods.sendMessage(p, "  ");
			Methods.sendMessage(p, "§6» §7Gebannt von: §e" + BanMethods.getBanner(target));
			Methods.sendMessage(p, "§6» §7Grund: §e" + BanMethods.getReason(target));
			Methods.sendMessage(p, "§6» §7Ban endet am: §e" + BanMethods.getBanEndDate(target));
			Methods.sendMessage(p, "  ");
			
		}
		
	}

}
