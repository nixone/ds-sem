package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;

import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;

public class StationsCanvas extends HelperCanvas implements Emitter<Object> {

	private DelayedEmitter<Object> repainter = new DelayedEmitter<Object>(new Emitter<Object>() {

		@Override
		public void reset() {
			displaying = false;
		}

		@Override
		public void emit(Object value) {
			displaying = true;
			repaint();
		}
	
	}, 50);
	
	private StationsLayout layout;
	private	Model model;
	
	public StationsCanvas(Model model, StationsLayout layout) {
		this.model = model;
		this.layout = layout;
	}

	@Override
	public void paintDisplay() {
		resetPosition();
		resetDrawPosition();
		
		paintLines();
		paintStations();
	}
	
	private void paintLines() {
		g.setColor(Color.GRAY);
		for(Line line : model.getLines()) {
			Station previous = null;
			for(Station current : line) {
				if(previous != null) {
					Position p1 = layout.getPosition(previous);
					Position p2 = layout.getPosition(current);
					
					ln(p1.x, p1.y, p2.x, p2.y);
				}
				previous = current;
			}
		}
	}
	
	private void paintStations() {
		g.setColor(Color.BLACK);
		for(Station station : model.getStations()) {
			resetDrawPosition();
			Position p = layout.getPosition(station);
			
			point(p.x, p.y);
			moveDraw(17, 0);
			paintCount(p.x, p.y, station.getCurrentPeopleCount());		
			resetDrawPosition();
			moveDraw(0, -10);
			str(station.getName(), p.x, p.y);
		}
		resetDrawPosition();
	}

	@Override
	public void reset() {
		displaying = false;
	}

	@Override
	public void emit(Object value) {
		displaying = true;
		repaint();
	}
}
