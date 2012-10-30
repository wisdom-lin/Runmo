package com.ciotc.runmo.component.statusbar;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.ciotc.runmo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.runmo.util.Constants;
import com.ciotc.runmo.util.I18N;

public class FirstStatusBar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 显示的文本
	 */
	String text = I18N.getString("StatusBar.FirstStausBar.string2");

	public FirstStatusBar() {
//setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(text, 0+Constants.STATUS_BAR_LEFT_BASE_LINE, getHeight()-Constants.STATUS_BAR_BOTTOM_BASE_LINE);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			Boolean bool = (Boolean) evt.getNewValue();
			if (bool)
				text = I18N.getString("StatusBar.FirstStausBar.string1");
			else
				text = I18N.getString("StatusBar.FirstStausBar.string2");
			repaint();

		}else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case START:
				text = I18N.getString("StatusBar.FirstStausBar.string3");
				break;
			default:
				break;
			}
			repaint();
		} 
	}
}
