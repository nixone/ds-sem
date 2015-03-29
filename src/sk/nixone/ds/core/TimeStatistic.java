package sk.nixone.ds.core;

public class TimeStatistic extends Statistic {

	double lastTime = 0;
	
	@Override
	public void clear() {
		super.clear();
		lastTime = 0;
	}
	
	public void add(double currentTime, double amount) {
		double delta = currentTime - lastTime;
		if(delta <= 0) {
			return;
		}
		
		super.add(amount/delta);
		lastTime = currentTime;
	}
}
