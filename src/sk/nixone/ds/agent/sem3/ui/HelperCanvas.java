package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.LineMetrics;

import javax.swing.JPanel;
import javax.swing.text.StyledEditorKit.BoldAction;

public abstract class HelperCanvas extends JPanel {

	int w = 100;
	int h = 100;
	double ox = 0;
	double oy = 0;
	int dox = 0;
	int doy = 0;
	protected Graphics2D g;
	protected boolean displaying = false;
	
	public HelperCanvas() {
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				double x = ix(e.getX());
				double y = iy(e.getY());
				onClick(x, y);
			}
		});
	}
	
	public boolean isAt(double x, double y, double px, double py) {
		double dx = x-px;
		double dy = y-py;
		return Math.sqrt(dx*dx + dy*dy) <= 0.025;
	}
	
	public void onClick(double x, double y) {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		this.g = (Graphics2D)g;
		w = getWidth(); h = getHeight();
		
		g.setFont(g.getFont().deriveFont(12f));
		this.g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB );
		this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
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
	public abstract void paintDisplay();
	
	public void ln(double fx, double fy, double tx, double ty) {
		g.drawLine(tx(fx), ty(fy), tx(tx), ty(ty));
	}
	
	public void move(double x, double y) {
		ox += x;
		oy += y;
	}
	
	public void moveDraw(int x, int y) {
		dox += x;
		doy += y;
	}
	
	public void resetPosition() {
		oy = ox = 0;
	}
	
	public void resetDrawPosition() {
		dox = doy = 0;
	}
	
	public double ix(int x) {
		return ((double)(x-dox)/w)-ox;
	}
	
	public double iy(int y) {
		return ((double)(y-doy)/h)-oy;
	}
	
	public int tx(double x) {
		return (int)((ox+x)*w)+dox;
	}
	
	public int ty(double y) {
		return (int)((oy+y)*h)+doy;
	}
	
	public void point(double x, double y) {
		point(x, y, 2);
	}
	
	public void point(double x, double y, int radius) {
		g.fillOval(tx(x)-radius, ty(y)-radius, radius*2, radius*2);
	}
	
	public void progress(double x, double y, int w, int h, double progress) {
		int tx = tx(x);
		int ty = ty(y);
		
		g.setColor(Color.WHITE);
		g.fillRect(tx-w/2, ty-h/2, w, h);
		g.setColor(Color.BLACK);
		g.drawRect(tx-w/2, ty-h/2, w, h);
		g.setColor(Color.GREEN);
		g.fillRect(tx+1-w/2, ty+1-h/2, (int)((w-1)*progress), h-1);
	}
	
	public void count(double x, double y, int count) {
		int width = 30;
		int height = 17;
		
		g.setColor(Color.WHITE);
		g.fillRect(tx(x)-width/2, ty(y)-height/2, width, height);
		g.setColor(Color.GRAY);
		g.drawRect(tx(x)-width/2, ty(y)-height/2, width, height);
		
		str(String.valueOf(count), x, y);
	}
	
	public void str(String text, double x, double y) {
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getHeight();
		g.setColor(Color.BLACK);
		g.drawString(text, tx(x)-width/2, ty(y)+height/2);
	}
	
	public void strBig(String text, double x, double y) {
		Font originalFont = g.getFont();
		Font newFont = originalFont.deriveFont(20f);
		g.setFont(newFont);
		str(text, x, y);
		g.setFont(originalFont);
	}
	
	public void strBox(String text, double x, double y) {
		int width = g.getFontMetrics().stringWidth(text);
		int height = g.getFontMetrics().getAscent()+g.getFontMetrics().getDescent();
		
		g.setColor(new Color(255, 255, 255, 200));
		g.fillRoundRect(tx(x)-width/2 - 2, ty(y)-height/2 - 2, width + 4, height + 4, 3, 3);
		
		g.setColor(Color.BLACK);
		g.drawString(text, tx(x)-width/2, ty(y)+height/2);
	}
	
	public void strToRight(String text, double x, double y) {
		int height = g.getFontMetrics().getHeight();
		g.setColor(Color.BLACK);
		g.drawString(text, tx(x), ty(y)+height/2);
	}
	
	public Position i(Position from, Position to, double progress) {
		return new Position(
			from.x*(1-progress) + (progress)*to.x,
			from.y*(1-progress) + (progress)*to.y
		);
	}
}
