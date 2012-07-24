package com.ciotc.feemo.component.statusbar;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.ciotc.feemo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.I18N;

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

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(text, 0, getHeight());
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
