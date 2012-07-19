package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.ciotc.feemo.Context;
import com.ciotc.feemo.OutlookBar;

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
	}

	public HomePanel(OutlookBar tabs, Context context) {
		this(tabs);
		this.context = context;
		constructActionsListeners();
	}

	void constructActionsListeners() {
		for (int i = 0; i < buttons.length; i += 2) {
			String name = buttons[i];
			for (Component comp : getComponents()) {
				if (comp.getName().equals(name)) {
					if (comp instanceof JButton) {
						JButton button = (JButton) comp;
						try {
							//将name的第一个字母改为大写;
							String temp = name.toUpperCase().substring(0, 1) + name.substring(1);

							Class<?> clazz = Class.forName(getClass().getName() + "$" + temp + "Listener");
							Constructor<?> constrcutor = clazz.getDeclaredConstructor(new Class[] { HomePanel.class });
							ActionListener listener = (ActionListener) constrcutor.newInstance(HomePanel.this);
							button.addActionListener(listener);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					break;
				}
			}
		}
	}

	class NewListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (context != null)
				context.newMovie();
		}

	}

	class OpenListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (context != null)
				context.openMovie();
		}

	}

	class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("uncomplement");
		}

	}

	class HelpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("uncomplement");
		}

	}
}
