package gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MyPanel extends JPanel
{
	private int vmargin = 7;
	private int hmargin = 22;
    
    public MyPanel(JPanel parent)
    {
    	super();
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	parent.add(this);
    	
    	init();
    }
    
    public void init()
    { }
  
	public void addMyComp(String desc, JComponent panel)
	{
		JPanel pan = new JPanel(new BorderLayout());
		pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
		
        pan.add(new JLabel(desc), BorderLayout.LINE_START);
        pan.add(panel, BorderLayout.LINE_END);
        
        add(pan);
	}
}
