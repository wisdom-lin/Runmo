package com.ciotc.feemo;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.ciotc.feemo.component.outlookbar.JOutlookBar;
import com.ciotc.feemo.component.outlookbar.panel.HomePanel;
import com.ciotc.feemo.component.outlookbar.panel.RecordPanel;
import com.ciotc.feemo.component.outlookbar.panel.ViewPanel;
import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.feemo.util.ActionConstants;

public class OutlookBar extends JOutlookBar implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Context context;

	public OutlookBar() {
		this(null);
	}

	public OutlookBar(Context context) {
		setName("OutlookBar");
		this.context = context;
		setTabPlacement(JTabbedPane.LEFT);

		HomePanel home = new HomePanel(this, context);
		addTab(home.getTitle(), makeScrollPane(home));
		addPropertyChangeListener(home);

		RecordPanel record = new RecordPanel(this);
		addTab(record.getTitle(), makeScrollPane(record));
		addPropertyChangeListener(record);

		ViewPanel view = new ViewPanel(this);
		addTab(view.getTitle(), makeScrollPane(view));
		addPropertyChangeListener(record);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component comp = (Component) evt.getNewValue();
			if (comp instanceof RecordComponent) {
				setSelectedIndex(1);
			} else if (comp instanceof ViewComponent) {
				setSelectedIndex(2);
			}
		} else if (evt.getPropertyName().equals(ActionConstants.ALL_COMPONENT_CLOSE)) {
			Boolean bool = (Boolean) evt.getNewValue();
			if (bool)
				setSelectedIndex(0);
		} else if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			firePropertyChange(ActionConstants.HANDLE_CONNECT, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, evt.getOldValue(), evt.getNewValue());
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new OutlookBar());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
