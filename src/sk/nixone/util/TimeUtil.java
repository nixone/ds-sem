package sk.nixone.util;

public class TimeUtil {
	
	
	public static String toString(long total) {
		if(total < 0) {
			total = 0;
		}
		
		long seconds = total % 60; total /= 60;
		long minutes = total % 60; total /= 60;
		long hours = total % 24; total /= 24;
		long days = total % 365; total /= 365;
		long years = total;
		
		String output = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		
		if (days > 0) {
			output = String.format("%dd ", days)+output;
			
			if (years > 0) {
				output = String.format("%dy ", years)+output;
			}
		}
		
		return output;
	}
	
	public static String toString(double total) {
		long decimal = (long)total;
		double fractional = total-decimal;
		
		return toString(decimal)+String.format("%.3f", fractional).substring(1);
	}
}
