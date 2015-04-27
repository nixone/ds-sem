package sk.nixone.ds.core.ui.property;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.JPanel;

public class PropertyPanel extends JPanel {
	
	public PropertyPanel(List<Property> properties) {
		super();
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		Group verticalGroup = layout.createSequentialGroup();
		Group horizontalGroup = layout.createParallelGroup();
		
		for(Property property : properties) {
			verticalGroup.addGroup(
				layout.createParallelGroup(Alignment.CENTER)
				.addComponent(property.getLabel())
				.addComponent(property.getEditor())
			);
			horizontalGroup.addGroup(
				layout.createSequentialGroup()
				.addComponent(property.getLabel())
				.addComponent(property.getEditor())
			);
		}
		
		layout.setHorizontalGroup(horizontalGroup);
		layout.setVerticalGroup(verticalGroup);
	}
}
