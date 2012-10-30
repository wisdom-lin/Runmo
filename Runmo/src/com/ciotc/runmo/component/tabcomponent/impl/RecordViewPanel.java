package com.ciotc.runmo.component.tabcomponent.impl;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.ciotc.runmo.util.I18N;

public class RecordViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JRadioButton button2D;
	private JRadioButton buttonContour;
	private JRadioButton button3D;
	private ButtonGroup group;

	public RecordViewPanel() {
		setLayout(new BorderLayout());
		add(createTopPanel());
	}

	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		button2D = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.2D"));
		buttonContour = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.contour"));
		button3D = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.3D"));
		panel.add(button2D);
		panel.add(buttonContour);
		panel.add(button3D);
		group = new ButtonGroup();
		group.add(button2D);
		group.add(buttonContour);
		group.add(button3D);
		return panel;
	}
}
