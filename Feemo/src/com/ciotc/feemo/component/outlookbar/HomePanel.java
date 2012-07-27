package com.ciotc.feemo.component.outlookbar;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;

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
	String[] buttons = { "new", "images/newMovie.png", "open", "images/openMovie.png", "option", "images/option.png", "feedback", "images/feedback.png"/*, "help", "images/help.png"*/};

	Context context;

	JButton buttonNew;
	boolean bool1;
	boolean bool2;

	public HomePanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
		buttonNew = findButtonByName("new");
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
		buttonNew.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("new")) {
//			if (context != null)
//				context.newMovie();

			synchronized (USBLock.LOCK) {
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
		} else if (e.getActionCommand().equals("feedback")) {
			String receiver = "linxiaozhi@ciotc.org";
			Desktop desktop = null;
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
			}
			if (desktop != null && desktop.isSupported(Desktop.Action.MAIL)) {
				try {
					desktop.mail(new URI("mailto:" + receiver));
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (URISyntaxException ex) {
					ex.printStackTrace();
				}
			}

			//FeedbackClient client = new FeedbackClient(null,true);
			//client.setVisible(true);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			bool1 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				buttonNew.setEnabled(true);
			else
				buttonNew.setEnabled(false);

		} else if (evt.getPropertyName().equals(ActionConstants.HANDLE_CONNECT)) {
			bool2 = (Boolean) evt.getNewValue();
			if (!bool1 && bool2)
				buttonNew.setEnabled(true);
			else
				buttonNew.setEnabled(false);
		}

	}
}
