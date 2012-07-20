package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ciotc.feemo.OutlookBar;
import com.ciotc.feemo.component.outlookbar.BasicOutlookButtonUI;
import com.ciotc.feemo.component.outlookbar.PercentLayout;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;

public abstract class OutlookBarPanel extends JPanel implements ActionListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OutlookBar tabs;
	private String title;

	public OutlookBarPanel(OutlookBar tabs) {
		super();
		this.tabs = tabs;
	}

	/**
	 * 另外的初始化
	 */
	protected void init() {

	}

	protected void constructPanel(String className, String title, String[] buttons) {
		setName(title);
		setLayout(new PercentLayout(PercentLayout.VERTICAL, 0));
		setOpaque(false);

		for (int i = 0, c = buttons.length; i < c; i += 2) {
			JButton button = new JButton(I18N.getString(className + buttons[i]));
			button.setName(buttons[i]);
			button.setActionCommand(buttons[i]);
			button.addActionListener(this);
			try {
				button.setUI(new BasicOutlookButtonUI());
				button.setBorder(BorderFactory.createLineBorder(Color.black));
			} catch (Exception e) {
				e.printStackTrace();
			}
			button.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource(buttons[i + 1])));
			add(button);
		}
		this.title = I18N.getString(className + "title");
	}

	/**
	 *
	 * @param name
	 * @return  找不到时返回null
	 */
	protected JButton findButtonByName(String name) {
		for (Component comp : getComponents()) {
			if (comp.getName().equals(name)) {
				if (comp instanceof JButton)
					return (JButton) comp;
				else
					return null;
			}
		}
		return null;
	}

	protected void setAllButtonEnable(boolean bool) {
		for (Component comp : getComponents()) {
			if (comp instanceof JButton)
				comp.setEnabled(bool);
		}
	}

	public String getTitle() {
		return title;
	}

}