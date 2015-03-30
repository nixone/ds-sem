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
	boolean displaying = false;
	
	public SimulationCanvas(Simulation simulation) {
		super();
		this.simulation = simulation;
		setPreferredSize(new Dimension(800, 600));
		
		simulation.getSimulationUpdated().add(new Emitter<Object>() {
			
			@Override
			public void reset() {
				displaying = false;
				repaint();
			}
			
			@Override
			public void emit(Object value) {
				displaying = true;
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
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, w-1, h-1);
		
		if(displaying) {
			paintDisplay();
		} else {
			str("We are not observing simulation right now...", 0.5, 0.5);
		}
	}
	
	private void paintDisplay() {
		resetPosition();
		ln(0.5, 0.1, 0.25, 0.2);
		ln(0.5, 0.1, 0.75, 0.2);
		ln(0.15, 0.6, 0.5, 0.8);
		ln(0.65, 0.6, 0.5, 0.8);
		paintProgress(0.5, 0.1, simulation.arrivalEvent, "Arrival");
		move(0.25, 0);
		paintLine(0);
		move(0.5, 0);
		paintLine(1);
		resetPosition();
		paintCount(0.5, 0.8, simulation.finishedTravelers);
	}
	
	private void ln(double fx, double fy, double tx, double ty) {
		g.setColor(Color.GRAY);
		g.drawLine(tx(fx), ty(fy), tx(tx), ty(ty));
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
		int width = 50;
		int height = 30;
		
		g.setColor(Color.WHITE);
		g.fillRect(tx(x)-width/2, ty(y)-height/2, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(tx(x)-width/2, ty(y)-height/2, width, height);
		
		str(String.valueOf(count), x, y);
	}
	
	private void paintProgress(double x, double y, PlannedEvent event, String dsc) {
		paintProgress(x, y, event == null ? 0 : event.getProgress(), dsc);
	}
	
	private void paintProgress(double x, double y, PlannedEvent event) {
		paintProgress(x, y, event == null ? 0 : event.getProgress(), null);
	}
	
	private void paintProgress(double x, double y, double progress, String description) {
		int width = 140;
		int height = 30;
		g.setColor(Color.WHITE);
		g.fillRect(tx(x)-width/2, ty(y)-height/2, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(tx(x)-width/2, ty(y)-height/2, width, height);
		g.setColor(Color.GREEN);
		g.fillRect(tx(x)+1-width/2, ty(y)+1-height/2, (int)((width-1)*progress), height-1);
		
		if(description != null) {
			str(description, x, y);
		}
	}
	
	private void str(String text, double x, double y) {
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getHeight();
		g.setColor(Color.BLACK);
		g.drawString(text, tx(x)-width/2, ty(y)+height/2);
	}
	
	private void paintLine(int line) {
		ln(0, 0.2, -0.1, 0.3);
		ln(0, 0.2, 0.1, 0.3);
		
		ln(-0.1, 0.3, -0.1, 0.4);
		ln(-0.1, 0.4, -0.1, 0.6);
		
		ln(0.1, 0.3, 0.1, 0.4);
		ln(0.1, 0.4, 0.1, 0.5);
		ln(0.1, 0.5, -0.1, 0.6);
		
		paintCount(0, 0.2, simulation.travellersWithLuggage.get(line).size());
		paintCount(-0.1, 0.3, simulation.beforeLuggageQueues.get(line).size());
		paintCount(0.1, 0.3, simulation.beforeCheckupQueues.get(line).size());
		paintProgress(-0.1, 0.4, simulation.processingEvents[line], "Scan");
		paintProgress(0.1, 0.4, simulation.detectorEvents[line], "Detector");
		paintProgress(0.1, 0.5, simulation.checkupEvents[line], "Personal checkup");
		paintCount(-0.1, 0.6, simulation.afterLuggageQueues.get(line).size());
	}
}
