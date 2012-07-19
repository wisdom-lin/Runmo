package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ciotc.feemo.OutlookBar;
import com.ciotc.feemo.component.outlookbar.BasicOutlookButtonUI;
import com.ciotc.feemo.component.outlookbar.PercentLayout;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;

public class OutlookBarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OutlookBar tabs;
	private String title;

	public OutlookBarPanel(OutlookBar tabs) {
		super();
		this.tabs = tabs;
	}

	protected void constructPanel(String className, String title, String[] buttons) {
		setName(title);
		setLayout(new PercentLayout(PercentLayout.VERTICAL, 0));
		setOpaque(false);

		for (int i = 0, c = buttons.length; i < c; i += 2) {
			JButton button = new JButton(I18N.getString(className + buttons[i]));
			button.setName(buttons[i]);
			try {
				button.setUI(new BasicOutlookButtonUI());
				button.setBorder(BorderFactory.createLineBorder(Color.black));
			} catch (Exception e) {
				e.printStackTrace();
			}
			button.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource(buttons[i + 1])));
			add(button);
		}
		this.title = I18N.getString(className + "title");
	}

	public String getTitle() {
		return title;
	}

}