package sk.nixone.ds.agent.sem3;

public class Bus {
	
	private String lineName;
	private Station lastStation;
	
	public Bus(String lineName, Station firstStation) {
		this.lineName = lineName;
		this.lastStation = firstStation;
	}
	
	public String getLineName() {
		return lineName;
	}
	
	public Station getLastStation() {
		return lastStation;
	}
}
