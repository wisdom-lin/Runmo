package com.ciotc.feemo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;

public class MainFrame extends JFrame implements Context, PropertyChangeListener {

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
	public void propertyChange(PropertyChangeEvent evt) {

	}

	@Override
	public void openMovie() {
		//弹出文件对话框
		String path = Util.chooseFile(this, ".");
		if (path == null)
			return;
		tabpane.openRecordComponent(path);
	}

	@Override
	public void newMovie() {
		tabpane.newRecordComponent();
	}

	@Override
	public void closeMovie() {
		// TODO Auto-generated method stub
		System.out.println("closeMovie not complement");
	}

	class MainFrameAdapter extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			handleManger.close();
		}

		@Override
		public void windowOpened(WindowEvent e) {
			handleManger.open();
		}
	}

}
