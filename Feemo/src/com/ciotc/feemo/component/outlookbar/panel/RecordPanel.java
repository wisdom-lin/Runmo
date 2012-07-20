package com.ciotc.feemo.component.outlookbar.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

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
	String[] buttons = { "record", "images/play.png", "stop", "images/stop.png", "save", "images/save_as.png", "option", "images/option.png" };
	String[] add_button = { "pause", "images/pause.png" };

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
	private JButton optionButton;

	public RecordPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
		init();
	}

	@Override
	protected void init() {
		recordButton = findButtonByName("record");
		stopButton = findButtonByName("stop");
		saveButton = findButtonByName("save");
		optionButton = findButtonByName("option");

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
			comp.save();
		} else if (e.getActionCommand().equals("option")) {
			JPopupMenu popup = new JPopupMenu();
			popup.add(new GainSettingPanel());
			popup.show(this, 200, 300);
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			System.out.println("change");
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				comp = (RecordComponent) temp;
			} else if (temp instanceof ViewComponent) {
				//TODO set all button disable	
			}
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			Status status = (Status) evt.getNewValue();
			switch (status) {
			case START:
				recordIndex = 0;
				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));
				recordButton.setEnabled(true);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				optionButton.setEnabled(true);
				break;
			case RECORDING:
				recordIndex = 1;
				recordButton.setText(I18N.getString((className + "pause")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/stop.png")));
				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case PAUSE:
				recordIndex = 2;
				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));
				recordButton.setEnabled(true);
				stopButton.setEnabled(true);
				saveButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case STOP:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(true);
				optionButton.setEnabled(false);
				break;
			case SAVED:
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			case END:
				recordButton.setText(I18N.getString((className + "record")));
				recordButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class.getResource("images/play.png")));
				recordButton.setEnabled(false);
				stopButton.setEnabled(false);
				saveButton.setEnabled(false);
				optionButton.setEnabled(false);
				break;
			default:
				break;
			}
		}

	}
}
