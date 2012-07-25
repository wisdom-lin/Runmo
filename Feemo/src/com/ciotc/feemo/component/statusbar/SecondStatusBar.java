package com.ciotc.feemo.component.statusbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.feemo.component.tabcomponent.impl.ViewModel;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;

public class SecondStatusBar extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 显示的文本
	 */
	String text1 = I18N.getString("StatusBar.SecondStatusBar.string1");
	String text2 = "";
	int counter = 1;
	int showWhich = 0; //1表示record，2表示view

	Status status;
	ViewComponent comp;

	public SecondStatusBar() {
		setBorder(BorderFactory.createMatteBorder(0, Constants.COMPONENT_GAP, 0, Constants.COMPONENT_GAP, Color.LIGHT_GRAY));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (showWhich == 1)
			g.drawString(text1, 0 + Constants.STATUS_BAR_LEFT_BASE_LINE, getHeight() - Constants.STATUS_BAR_BOTTOM_BASE_LINE);
		else if (showWhich == 2)
			g.drawString(text2, 0 + Constants.STATUS_BAR_LEFT_BASE_LINE, getHeight() - Constants.STATUS_BAR_BOTTOM_BASE_LINE);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				//System.out.println("dd");
				showWhich = 1;

			} else if (temp instanceof ViewComponent) {
				showWhich = 2;
				comp = (ViewComponent) temp;
				stateChange(((ViewComponent) temp).getViewModel());
				repaint();
			}
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			Boolean exist = (Boolean) evt.getNewValue();
			if (!exist) {
				text1 = "";//清空
				showWhich = 0;
			}
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			Boolean exist = (Boolean) evt.getNewValue();
			if (!exist) {
				text2 = "";//清空
			}
			showWhich = 0;
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			//System.out.println("dd1");
			status = (Status) evt.getNewValue();
			stateChange(status);
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_DATA_LEN)) {
			//System.out.println("dd2");
			counter = (Integer) evt.getNewValue();
			switch (status) {
			case RECORDING:
				text1 = MessageFormat.format(I18N.getString("StatusBar.SecondStatusBar.string2"), counter);
				break;
			default:
				break;
			}
			repaint();
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			ViewComponent temp = (ViewComponent) evt.getNewValue();
			if (temp == comp) {
				ViewModel model = temp.getViewModel();
				stateChange(model);
				repaint();
			}
		}

	}

	void stateChange(Status status) {
		switch (status) {
		case START:
			text1 = I18N.getString("StatusBar.SecondStatusBar.string1");
			break;
		case RECORDING:
			text1 = MessageFormat.format(I18N.getString("StatusBar.SecondStatusBar.string2"), counter);
			break;
		case PAUSE:
			text1 = I18N.getString("StatusBar.SecondStatusBar.string3");
			break;
		case STOP:
			text1 = I18N.getString("StatusBar.SecondStatusBar.string4");
			break;
		case SAVED:
			text1 = I18N.getString("StatusBar.SecondStatusBar.string5");
			break;
		case END:
			text1 = I18N.getString("StatusBar.SecondStatusBar.string6");
			break;
		default:
			break;
		}
	}

	void stateChange(ViewModel model) {
		int frameIndex = model.getFrameIndex();
		int totalFrameNum = model.getFrameLen();
		text2 = MessageFormat.format(I18N.getString("StatusBar.SecondStatusBar.string7"), totalFrameNum, frameIndex + 1);

	}
}
