package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.ciotc.feemo.Context;
import com.ciotc.feemo.OutlookBar;
import com.ciotc.feemo.setting.SettingDialog;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.USBLock;
import com.ciotc.teemo.usbdll.USBDLL;

public class HomePanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.HomePanel.";
	String title = "home";
	String[] buttons = { "new", "images/newMovie.png", "open", "images/openMovie.png", "option", "images/option.png", "help", "images/help.png" };

	Context context;

	public HomePanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
		init();
	}

	public HomePanel(OutlookBar tabs, Context context) {
		this(tabs);
		this.context = context;
		//constructActionsListeners();
	}

	@Override
	protected void init() {
		super.init();
		JButton button = findButtonByName("new");
		button.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("new")) {
//			if (context != null)
//				context.newMovie();
			
			synchronized(USBLock.LOCK){
				USBDLL.setButton1On();
			}
		} else if (e.getActionCommand().equals("open")) {
			if (context != null)
				context.openMovie();
		} else if (e.getActionCommand().equals("option")) {
			final SettingDialog sd = new SettingDialog();
			sd.addListener(tabs);
			sd.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					sd.removeListener(tabs);
				}
			});
			sd.setLocationRelativeTo(null);
			sd.setModal(true);
			sd.setVisible(true);
			
		} else if (e.getActionCommand().equals("help")) {
			//TODO task
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			Boolean bool = (Boolean) evt.getNewValue();
			JButton button = findButtonByName("new");
			button.setEnabled(!bool);
		}else if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			Boolean bool = (Boolean) evt.getNewValue();
			JButton button = findButtonByName("new");
			button.setEnabled(bool);
		}

	}
}