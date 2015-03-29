package sk.nixone.ds.sem2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.time.PlannedEvent;

public class SimulationCanvas extends JPanel {
	
	int w = 100;
	int h = 100;
	double ox = 0;
	double oy = 0;
	Graphics2D g;
	Simulation simulation;
	
	public SimulationCanvas(Simulation simulation) {
		super();
		this.simulation = simulation;
		setPreferredSize(new Dimension(800, 600));
		
		simulation.getSimulationUpdated().add(new Emitter<Object>() {
			
			@Override
			public void reset() {
				repaint();
			}
			
			@Override
			public void emit(Object value) {
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		this.g = (Graphics2D)g;
		w = getWidth(); h = getHeight();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		
		resetPosition();
		paintProgress(0.5, 0.1, simulation.arrivalEvent);
		move(0.25, 0);
		paintLine(0);
		move(0.5, 0);
		paintLine(1);
		resetPosition();
		paintCount(0.5, 0.8, simulation.finishedTravelers);
	}
	
	private void move(double x, double y) {
		ox += x;
		oy += y;
	}
	
	private void resetPosition() {
		oy = ox = 0;
	}
	
	private int tx(double x) {
		return (int)((ox+x)*w);
	}
	
	private int ty(double y) {
		return (int)((oy+y)*h);
	}
	
	private void paintCount(double x, double y, int count) {
		g.setColor(Color.BLACK);
		g.drawRect(tx(x)-20, ty(y)-20, 40, 40);
		g.drawString(String.valueOf(count), tx(x)-10, ty(y)+10);
	}
	
	private void paintProgress(double x, double y, PlannedEvent event) {
		paintProgress(x, y, event == null ? 0 : event.getProgress());
	}
	
	private void paintProgress(double x, double y, double progress) {
		int width = 100;
		int height = 40;
		g.setColor(Color.BLACK);
		g.drawRect(tx(x)-width/2, ty(y)-height/2, width, height);
		g.setColor(Color.GREEN);
		g.fillRect(tx(x)+1-width/2, ty(y)+1-height/2, (int)((width-1)*progress), height-1);
	}
	
	private void paintLine(int line) {
		paintCount(0, 0.2, simulation.travellersWithLuggage.get(line).size());
		paintCount(-0.1, 0.3, simulation.beforeLuggageQueues.get(line).size());
		paintCount(0.1, 0.3, simulation.beforeCheckupQueues.get(line).size());
		paintProgress(-0.1, 0.4, simulation.processingEvents[line]);
		paintProgress(0.1, 0.4, simulation.detectorEvents[line]);
		paintProgress(0.1, 0.5, simulation.checkupEvents[line]);
		paintCount(-0.1, 0.6, simulation.afterLuggageQueues.get(line).size());
	}
}
