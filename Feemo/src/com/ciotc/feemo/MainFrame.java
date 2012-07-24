package com.ciotc.feemo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;

public class MainFrame extends JFrame implements Context, PropertyChangeListener {

	class MainFrameAdapter extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			handleManger.close();
			Util.writeSetting();
		}

		@Override
		public void windowOpened(WindowEvent e) {
			handleManger.open();
			Util.readSetting();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OutlookBar outlookBar;
	StatusBar statusBar;
	MainTabPane tabpane;

	HandleManger handleManger;

	public MainFrame() {

		constructOutlookBar();
		constructStatusBar();
		constructTabPane();
		constructHanleManger();

		addWindowListener(new MainFrameAdapter());

		setName("MainFrame");
		setTitle(I18N.getString("MainFrame.title"));
		setSize(Util.getSizeDependOnScreen(Constants.WIDTH_RATE, Constants.HEIGHT_RATE));
		//setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void closeMovie() {
		tabpane.closeRecordComponent();
	}

	void constructHanleManger() {
		handleManger = new HandleManger(this);
	}

	void constructOutlookBar() {
		outlookBar = new OutlookBar(this);
		add(outlookBar, BorderLayout.WEST);
		addPropertyChangeListener(outlookBar);
	}

	void constructStatusBar() {
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.SOUTH);
		addPropertyChangeListener(statusBar);
	}

	void constructTabPane() {
		tabpane = new MainTabPane();
		add(tabpane, BorderLayout.CENTER);
		tabpane.addPropertyChangeListener(this);
	}

	@Override
	public void fresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void newMovie() {
		tabpane.newRecordComponent();
	}

	@Override
	public void openMovie() {
		//弹出文件对话框
		String path = Util.chooseOpenFile(this, ".");
		if (path == null)
			return;
		tabpane.openRecordComponent(path);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			firePropertyChange(ActionConstants.CURRENT_COMPONENT_CHANGE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, evt.getOldValue(), evt.getNewValue());
		}  else if (evt.getPropertyName().equals(ActionConstants.ALL_COMPONENT_CLOSE)) {
			firePropertyChange(ActionConstants.ALL_COMPONENT_CLOSE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_DATA_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, evt.getOldValue(), evt.getNewValue());
		}else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, evt.getOldValue(), evt.getNewValue());
		}else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		}
	}

	@Override
	public void disconnectHandle() {
		firePropertyChange(ActionConstants.HANDLE_CONNECT, true, false);
	}

	@Override
	public void connectHandle() {
		firePropertyChange(ActionConstants.HANDLE_CONNECT, false, true);
	}

}
