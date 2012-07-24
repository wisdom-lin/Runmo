package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import com.ciotc.feemo.OutlookBar;
import com.ciotc.feemo.component.tabcomponent.impl.GainSettingPanel;
import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.Util;

public class RecordPanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.RecordPanel.";
	String title = "record";
	String[] buttons = { "record", "images/play.png", "stop", "images/pause.png", "save", "images/save_as.png", "recordDisplayOption", "images/option.png", "option", "images/option.png" };
	String[] add_button = { "pause", "images/stop.png" };

	RecordComponent comp;
	/**
	 * 用来表示“录制”按钮当前的功能
	 * 0 表示录制
	 * 1 表示暂停
	 * 2表示继续录制
	 */
	int recordIndex = 0;

	private JButton recordButton; // 录制、暂停、继续录制共用
	private JButton stopButton;
	private JButton saveButton;
	private JButton recordDisplayOptionButton;
	private JButton optionButton;

	private JRadioButton button2D;
	private JRadioButton buttonContour;
	private JRadioButton button3D;
	private ButtonGroup group;
	private JPanel panel;

	public RecordPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
		recordButton = findButtonByName("record");
		stopButton = findButtonByName("stop");
		saveButton = findButtonByName("save");
		optionButton = findButtonByName("option");
		recordDisplayOptionButton = findButtonByName("recordDisplayOption");
		createViewOptionPanel();
		init();
	}

	@Override
	protected void init() {
		recordButton.setEnabled(false);
		stopButton.setEnabled(false);
		saveButton.setEnabled(false);
		recordDisplayOptionButton.setEnabled(false);
		optionButton.setEnabled(false);

	}

	private void createViewOptionPanel() {
		panel = new JPanel();
		button2D = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.2D"));
		button2D.setActionCommand("button2D");
		button2D.addActionListener(this);
		buttonContour = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.contour"));
		buttonContour.setActionCommand("buttonContour");
		buttonContour.addActionListener(this);
		button3D = new JRadioButton(I18N.getString("TabComponent.RecordViewPanel.3D"));
		button3D.setActionCommand("button3D");
		button3D.addActionListener(this);
		panel.add(button2D);
		panel.add(buttonContour);
		panel.add(button3D);
		group = new ButtonGroup();
		group.add(button2D);
		group.add(buttonContour);
		group.add(button3D);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("record")) {
			if (recordIndex == 0)
				comp.triggerOpen();
			else if (recordIndex == 1)
				comp.pause();
			else if (recordIndex == 2)
				comp.restore();
		} else if (e.getActionCommand().equals("stop")) {
			comp.stop();
		} else if (e.getActionCommand().equals("save")) {
			comp.triggerSave();
		} else if (e.getActionCommand().equals("option")) {
			JPopupMenu popup = new JPopupMenu();
			popup.add(new GainSettingPanel());
			popup.show(this, getWidth(), optionButton.getY());
		} else if (e.getActionCommand().equals("recordDisplayOption")) {
			JPopupMenu popup = new JPopupMenu();
			popup.add(panel);
			popup.show(this, getWidth(), recordDisplayOptionButton.getY());
		} else if (e.getActionCommand().equals("button2D")) {
			comp.select2D();
		} else if (e.getActionCommand().equals("buttonContour")) {
			comp.selectContour();
		} else if (e.getActionCommand().equals("button3D")) {
			comp.select3D();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				comp = (RecordComponent) temp;
//				int index = comp.getSelectWhichView();
//				switch (index) {
//				case 1:
//					button2D.setSelected(true);
//					break;
//				case 2:
//					buttonContour.setSelected(true);
//					break;
//				case 3:
//					button3D.setSelected(true);
//					break;
//				default:
//					break;
//				}
			} else if (temp instanceof ViewComponent) {
				init();
			}
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			Boolean exist = (Boolean) evt.getNewValue();
			if (!exist) {
				init();
			}
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case START:System.out.println("start");
				recordIndex = 0;

				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));

				recordButton.setEnabled(true);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				int index = comp.getSelectWhichView();
				switch (index) {
				case 1:
					button2D.setSelected(true);
					break;
				case 2:
					buttonContour.setSelected(true);
					break;
				case 3:
					button3D.setSelected(true);
					break;
				default:
					break;
				}
				recordDisplayOptionButton.setEnabled(true);
				optionButton.setEnabled(true);
				break;
			case RECORDING:
				recordIndex = 1;

				recordButton.setText(I18N.getString((className + "pause")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/stop.png")));

				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				recordDisplayOptionButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case PAUSE:
				recordIndex = 2;

				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));

				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				recordDisplayOptionButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case STOP:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(true);
				recordDisplayOptionButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case SAVED:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				recordDisplayOptionButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case END:

				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));

				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				recordDisplayOptionButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			default:
				break;
			}
			getParent().invalidate();
			getParent().repaint();
		}

	}
}
