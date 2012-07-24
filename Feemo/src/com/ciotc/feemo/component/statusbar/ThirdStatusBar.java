package com.ciotc.feemo.component.statusbar;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.JPanel;

import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.I18N;

public class ThirdStatusBar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String text1 = "";
	String text2 = "";
	int showWhich = 0;
	ViewComponent comp;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (showWhich == 1)
			g.drawString(text1, 0, getHeight());
		else if (showWhich == 2)
			g.drawString(text2, 0, getHeight());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				showWhich = 1;

			} else if (temp instanceof ViewComponent) {
				showWhich = 2;
				comp = (ViewComponent) temp;

				repaint();
			}
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			//Boolean exist = (Boolean) evt.getNewValue();
			//if (!exist) {
			text1 = "";//清空
			//}
			showWhich = 0;
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			//Boolean exist = (Boolean) evt.getNewValue();
			//if (!exist) {
			text2 = "";//清空
			//}
			showWhich = 0;
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE)) {
			ViewComponent comp = (ViewComponent) evt.getNewValue();
			if (!comp.getViewModel().isEntering()) {
				text2 = "";
				showWhich = 0;
				repaint();
				return;
			}
			int x = comp.getViewModel().getX();
			int y = comp.getViewModel().getY();
			int value = comp.getViewModel().getPointValue();
			text2 = MessageFormat.format(I18N.getString("StatusBar.ThirdStatusBar.string1"), x, y, value);
			showWhich = 2;
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE)) {
			int[] ist = (int[]) evt.getNewValue();
			if (ist[0] == Integer.MIN_VALUE) {
				text1 = "";
				showWhich = 0;
				repaint();
				return;
			}
			int x = ist[0];
			int y = ist[1];
			int value = ist[2];
			text1 = MessageFormat.format(I18N.getString("StatusBar.ThirdStatusBar.string1"), x, y, value);
			showWhich = 1;
			repaint();
		}
	}

}
