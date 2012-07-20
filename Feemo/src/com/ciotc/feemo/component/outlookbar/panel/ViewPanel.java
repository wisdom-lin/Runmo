package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import com.ciotc.feemo.OutlookBar;

public class ViewPanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.ViewPanel.";
	String title = "view";
	String[] buttons = { "view2D", "images/view2D.png", "viewContour", "images/viewContour.png", "view3D", "images/view3D.png", "play", "images/play.png", "stop", "images/stop.png", "first", "images/previous.png", "last", "images/next.png", "forward", "images/forward.png", "backward",
			"images/backward.png", "speed", "images/speed.png" };

	public ViewPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

}
