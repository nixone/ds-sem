package sk.nixone.ds.agent.sem3.model;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Schedule extends LinkedList<Double> {
	
	private static final double ESTIMATION_TIME_RESOLUTION = 15;
	
	private static final double ESTIMATION_CORRECT_TIME = 0;
	private static final double ESTIMATION_CORRECT_FACTOR_GLOBAL = 1;
	private static final double ESTIMATION_CORRECT_FACTOR_LOCAL = 0.7575;
	
	private Model model;
	
	private Line line;
	
	private VehicleType vehicleType;
	
	private String expression = "";
	
	public Schedule(Model model, Line line, VehicleType vehicleType) {
		super();
		this.model = model;
		this.line = line;
		this.vehicleType = vehicleType;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void fromExpression(String expression) {
		clear();
		try {
			String [] parts = expression.split(Pattern.quote(":"));
			if(parts[0].contentEquals("list")) {
				fromListExpression(parts[1]);
			} else if(parts[0].contentEquals("distribute")) {
				fromDistributeExpression(parts[1]);
			} else if(parts[0].contentEquals("static")) {
				fromStaticExpression(parts[1]);
			}
			this.expression = expression;
		} catch(Throwable t) {
			// do nothing...
		}
	}
	
	private void fromStaticExpression(String staticPart) {
		Scanner scanner = new Scanner(staticPart);
		double start = scanner.nextDouble()*60;
		double offset = scanner.nextDouble()*60;
		int count = scanner.nextInt();
		scanner.close();
		
		for(int i=0; i<count; i++) {
			add(start);
			start += offset;
		}
	}
	
	private void fromListExpression(String listPart) {
		Scanner scanner = new Scanner(listPart);
		while(scanner.hasNextDouble()) {
			double minute = scanner.nextDouble();
			double seconds = minute * 60;
			add(seconds);
		}
		scanner.close();
	}
	
	private void fromDistributeExpression(String distributePart) {
		int availableVehicleCount = Integer.parseInt(distributePart);
		
		double time = 0;
		int servedCapacity = 0;
		while(availableVehicleCount > 0 && time < model.getMatchStartTime()) {
			double estimatedPeople = line.estimateCumulativePeople(time, vehicleType.getWaitingTimeForArrivals());
			estimatedPeople *= ESTIMATION_CORRECT_FACTOR_GLOBAL;
			estimatedPeople -= servedCapacity;
			estimatedPeople *= ESTIMATION_CORRECT_FACTOR_LOCAL;
			
			if(estimatedPeople >= vehicleType.getCapacity()) {
				availableVehicleCount--;
				servedCapacity += vehicleType.getCapacity();
				add(Math.max(0, time + ESTIMATION_CORRECT_TIME));
			}
			
			time += ESTIMATION_TIME_RESOLUTION;
		}
	}
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	
	public Line getLine() {
		return line;
	}
}
