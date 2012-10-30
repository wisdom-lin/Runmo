package com.ciotc.feemo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.ciotc.feemo.component.lengendtoolbar.LengendPanel;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;
import com.citoc.feemo.config.Configurge;
import com.l2fprod.common.swing.PercentLayout;

public class MainFrame extends JFrame implements Context, PropertyChangeListener {

	class MainFrameAdapter extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			Configurge.close();
			if (isExitRecording) {
				closeMovie();
			}
			handleManger.close();
			Util.writeSetting();
		}

		@Override
		public void windowOpened(WindowEvent e) {
			//Configurge.open();
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
	JPanel lengendToolbar;
	HandleManger handleManger;
	boolean isExitRecording;

	public MainFrame() {

		constructOutlookBar();
		constructStatusBar();
		constructTabPane();
		constructHanleManger();
		constructLengendToolbar();

		JPanel panel = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, Constants.COMPONENT_GAP));
		panel.add(outlookBar, "100");
		panel.add(tabpane, "*");
		add(panel, BorderLayout.CENTER);

		addWindowListener(new MainFrameAdapter());

		setName("MainFrame");
		setTitle(I18N.getString("MainFrame.title"));
		setSize(Util.getSizeDependOnScreen(Constants.WIDTH_RATE, Constants.HEIGHT_RATE));
		setMinimumSize(new Dimension(Constants.MAINFRAME_MIN_WIDTH, Constants.MAINFRAME_MIN_HEIGHT));
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
		//add(outlookBar, BorderLayout.WEST);
		addPropertyChangeListener(outlookBar);
	}

	void constructStatusBar() {
		statusBar = new StatusBar();
		add(statusBar, BorderLayout.SOUTH);
		addPropertyChangeListener(statusBar);
	}

	void constructTabPane() {
		tabpane = new MainTabPane();
		//add(tabpane, BorderLayout.CENTER);
		tabpane.addPropertyChangeListener(this);
	}

	private void constructLengendToolbar() {
		lengendToolbar = new JPanel();
		lengendToolbar.setLayout(new BorderLayout());
		LengendPanel lengend = new LengendPanel();
		//lengend.addPropertyChangeListener(this);
		addPropertyChangeListener(lengend);
		lengendToolbar.add(lengend, BorderLayout.NORTH);
		//lengendToolbar.add(lengend, BorderLayout.CENTER);
		
		//lengend.addPropertyChangeListener(tabs);
		add(lengendToolbar, BorderLayout.EAST);
		
		//lengendToolbar.setVisible(false);
		//content.remove(lengendToolbar);
	}

	public void controlLengend(){
		if(lengendToolbar.isVisible())
		lengendToolbar.setVisible(false);
		else
			lengendToolbar.setVisible(true);
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
		String userDir = System.getProperty("user.home") + "/Desktop";
		//弹出文件对话框
		String path = Util.chooseOpenFile(this, userDir);
		if (path == null)
			return;
		tabpane.openRecordComponent(path);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			Boolean bool = (Boolean) evt.getNewValue();
			isExitRecording = bool;
			if (!bool) {
				//restart
				handleManger.stopCheckButton();
				handleManger.startCheckButton();
			}
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			firePropertyChange(ActionConstants.CURRENT_COMPONENT_CHANGE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.ALL_COMPONENT_CLOSE)) {
			firePropertyChange(ActionConstants.ALL_COMPONENT_CLOSE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_DATA_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE)) {
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
