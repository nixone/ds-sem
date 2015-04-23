package sk.nixone.ds.agent.sem3.ui;

public class Position {
	
	public final double x;
	public final double y;
	
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "["+x+", "+y+"]";
	}
}
