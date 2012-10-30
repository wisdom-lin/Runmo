package com.ciotc.feemo;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.ciotc.feemo.component.statusbar.FirstStatusBar;
import com.ciotc.feemo.component.statusbar.SecondStatusBar;
import com.ciotc.feemo.component.statusbar.ThirdStatusBar;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.l2fprod.common.swing.PercentLayout;

public class StatusBar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatusBar() {
		setLayout(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		setPreferredSize(new Dimension(getWidth(), Constants.STATUS_BAR_HEIGHT));
		FirstStatusBar first = new FirstStatusBar();
		addPropertyChangeListener(first);
		add(first, "65%");
		SecondStatusBar second = new SecondStatusBar();
		addPropertyChangeListener(second);
		add(second, "20%");
		ThirdStatusBar third = new ThirdStatusBar();
		addPropertyChangeListener(third);
		add(third, "*");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			firePropertyChange(ActionConstants.CURRENT_COMPONENT_CHANGE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_DATA_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, evt.getOldValue(), evt.getNewValue());

		} else if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			firePropertyChange(ActionConstants.HANDLE_CONNECT, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		}

	}

}
