package sk.nixone.util;

import javax.swing.UnsupportedLookAndFeelException;

public class AppearanceUtil {
	public static void setNiceSwingLookAndFeel() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if (javax.swing.UIManager.getSystemLookAndFeelClassName().equals(info.getClassName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				}
				
				if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {   
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
