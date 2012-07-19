package com.ciotc.feemo.component.outlookbar.panel;

import com.ciotc.feemo.OutlookBar;

public class RecordPanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.RecordPanel.";
	String title = "record";
	String[] buttons = { "record", "images/play.png", 
			"pause", "images/pause.png", 
			"save", "images/save_as.png",
			"option", "images/option.png" };

	public RecordPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
	}
}
