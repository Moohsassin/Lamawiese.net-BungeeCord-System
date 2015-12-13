package de.Moohsassin.LamaBungee.BanManager;

public enum TimeUnit {

	SECONDS("s", "sekunde", 1),
	MINUTES("m", "minute", 60),
	HOURS("h", "stunde", 60 * 60),
	DAYS("d", "tag", 60 * 60 * 24),
	WEEKS("w", "woche", 60 * 60 * 24 * 7);
	
	String sC;
	String g;
	long iS;
	
	private TimeUnit(String shortCut, String german, long inSeconds) {
		this.sC = shortCut;
		this.g = german;
		this.iS = inSeconds;
	}
	
	public long getSeconds() {
		return iS;
	}
	
	public static TimeUnit getFromShortCut(String value) {
		for(TimeUnit unit : TimeUnit.values()) if(unit.sC.equalsIgnoreCase(value)) return unit;
		return null;
	}
	
	public static TimeUnit getFromGerman(String value) {
		for(TimeUnit unit : TimeUnit.values()) if(unit.g.equalsIgnoreCase(value)) return unit;
		return null;
	}
}
