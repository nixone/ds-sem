package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Circle extends JComponent {

	private Color color;
	private int diameter;
	
	public Circle(Color c, int diameter) {
		super();
		color = c;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(color);
		g.fillOval(0, 0, diameter, diameter);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(diameter, diameter);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
}
