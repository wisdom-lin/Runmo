package com.citoc.feemo.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;

public class Test extends JRibbonFrame{

	public Test() {
		//getRibbon().putClientProperty(BasicRibbonUI.IS_USING_TITLE_PANE, true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RibbonApplicationMenu menu = new RibbonApplicationMenu();
		//menu.addMenuEntry(new RibbonApplicationMenuEntryPrimary(icon, text, mainActionListener, entryKind))
		
		//getRibbon().setApplicationMenu();
		setSize(500, 600);
		
//	JDialog dialog = new JDialog(this);
//	dialog.setModal(false);
//	dialog.setVisible(true);
	
	JToolBar toolbar = new JToolBar();
	toolbar.add(new JButton("ddd"));
	add(toolbar);
	JToolBar toolbar2 = new JToolBar();
	toolbar2.add(new JButton("aaa"));
	add(toolbar2);
	}
	
	public static void main(String[] args) {
		new Test().setVisible(true);
	}
}
