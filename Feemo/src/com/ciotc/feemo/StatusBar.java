package com.ciotc.feemo;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.ciotc.feemo.component.outlookbar.PercentLayout;
import com.ciotc.feemo.component.statusbar.FirstStatusBar;
import com.ciotc.feemo.component.statusbar.SecondStatusBar;
import com.ciotc.feemo.component.statusbar.ThirdStatusBar;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;

public class StatusBar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatusBar() {
		setLayout(new PercentLayout(PercentLayout.HORIZONTAL, 1));
		setPreferredSize(new Dimension(getWidth(), Constants.STATUS_BAR_HEIGHT));
		FirstStatusBar first = new FirstStatusBar();
		addPropertyChangeListener(first);
		add(first, "50%");
		SecondStatusBar second = new SecondStatusBar();
		add(second, "20%");
		ThirdStatusBar third = new ThirdStatusBar();
		add(third, "*");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			firePropertyChange(ActionConstants.HANDLE_CONNECT, evt.getOldValue(), evt.getNewValue());
		}

	}

}
