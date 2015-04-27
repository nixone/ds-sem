package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Circle extends JPanel {

	private Color color;
	private int diameter;
	
	public Circle(Color c, int diameter) {
		color = c;
		setPreferredSize(new Dimension(diameter, diameter));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(color);
		g2d.fillOval(0, 0, diameter, diameter);
	}
}
