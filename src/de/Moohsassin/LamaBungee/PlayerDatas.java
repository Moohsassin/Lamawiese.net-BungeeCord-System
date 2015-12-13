package de.Moohsassin.LamaBungee;

public class PlayerDatas {

	public static MySQL sql;
	
	public static void openConnection() {

		try {
			sql = new MySQL("Players");
			sql.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasPlayerBefore(String name) {
		return getRank(name) != null;
	}
	
	public static void closeConnection() {
		sql.closeConnection();
	}
	
	public static String getRank(String name) {
		return sql.getString("Lamas", "Name", name, "Rank");	
	}

	public static String getName(String UUID) {
		return sql.getString("Lamas", "Uuid", UUID, "Name");
	}
	
	public static String getUUID(String name) {
		return sql.getString("Lamas", "Name", name, "Uuid");
	}
	
}
