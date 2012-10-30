package com.ciotc.runmo.component.outlookbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.runmo.OutlookBar;
import com.ciotc.runmo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.runmo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.runmo.component.tabcomponent.impl.ViewModel;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.runmo.util.I18N;
import com.ciotc.runmo.util.Util;

import static com.ciotc.runmo.util.Constants.*;
public class ViewPanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.ViewPanel.";
	String title = "view";
	String[] buttons = { "view2D", "images/view2D.png", "viewContour", "images/viewContour.png", "view3D", "images/view3D.png", "play", "images/play.png", "stop", "images/stop.png", "first", "images/previous.png", "last", "images/next.png", "forward", "images/backward.png", "backward",
			"images/forward.png", "speed", "images/speed.png", "index", "images/index.png" };

	ViewComponent comp;
	//int maxLen = 0;

	private JButton view2DButton;
	private JButton view3DButton;
	private JButton viewContourButton;
	private JButton playButton;
	private JButton stopButton;
	private JButton firstButton;
	private JButton lastButton;
	private JButton forwardButton;
	private JButton backwardButton;
	private JButton speedButton;
	private JButton indexButton;

	ButtonGroup group;
	private JRadioButton[] speedButtons;
	private JPanel speedPanel;
	private JPanel framePanel;
	private JSlider slider;
	private JLabel frameIndexText;
	public ViewPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);

//		if (dm.isSelect2D()) {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view2D_select.png"));
//		} else {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view2D.png"));
//		}
//		if (dm.isSelectContour()) {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/viewContour_select.png"));
//		} else {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/viewContour.png"));
//		}
//		if (dm.isSelect3D()) {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view3D_select.png"));
//		} else {
//			view2DButton.setIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view3D.png"));
//		}
		view2DButton = findButtonByName("view2D");
		view2DButton.setSelectedIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view2D_select.png"));
		view3DButton = findButtonByName("view3D");
		view3DButton.setSelectedIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/view3D_select.png"));
		viewContourButton = findButtonByName("viewContour");
		viewContourButton.setSelectedIcon(Util.getResizableIconFromResource(OutlookBar.class, "images/viewContour_select.png"));
		playButton = findButtonByName("play");
		stopButton = findButtonByName("stop");
		firstButton = findButtonByName("first");
		lastButton = findButtonByName("last");
		forwardButton = findButtonByName("forward");
		backwardButton = findButtonByName("backward");
		speedButton = findButtonByName("speed");
		indexButton = findButtonByName("index");
		group = new ButtonGroup();
		speedButtons = new JRadioButton[5];
		speedPanel = new JPanel();
		
		speedButtons[0] = new JRadioButton(String.format("%.1f%s/%s",TIME_UNIT*1.0f / SLOWEST_SPEED*1.0f,I18N.getString("OutlookBar.ViewPanel.speedtitle1"),I18N.getString("OutlookBar.ViewPanel.speedtitle2")));
		speedButtons[1] = new JRadioButton(String.format("%.1f%s/%s",TIME_UNIT*1.0f / MEDIUM_SLOW_SPEED*1.0f,I18N.getString("OutlookBar.ViewPanel.speedtitle1"),I18N.getString("OutlookBar.ViewPanel.speedtitle2")));
		speedButtons[2] = new JRadioButton(String.format("%.1f%s/%s",TIME_UNIT*1.0f / NOMAL_SPEED*1.0f,I18N.getString("OutlookBar.ViewPanel.speedtitle1"),I18N.getString("OutlookBar.ViewPanel.speedtitle2")));
		speedButtons[3] = new JRadioButton(String.format("%.1f%s/%s",TIME_UNIT*1.0f / MEDIUM_FAST_SPEED*1.0f,I18N.getString("OutlookBar.ViewPanel.speedtitle1"),I18N.getString("OutlookBar.ViewPanel.speedtitle2")));
		speedButtons[4] = new JRadioButton(String.format("%.1f%s/%s",TIME_UNIT*1.0f / FASTEST_SPEED*1.0f,I18N.getString("OutlookBar.ViewPanel.speedtitle1"),I18N.getString("OutlookBar.ViewPanel.speedtitle2")));
		
		for (int i = 0; i < speedButtons.length; i++) {
			final int index = i;
			
			group.add(speedButtons[i]);
			speedPanel.add(speedButtons[i]);
			speedButtons[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					comp.getViewModel().selectSpeed(index);

				}
			});
		}
		
		framePanel = new JPanel();
		slider = new JSlider(SwingConstants.HORIZONTAL);
		slider.setMinimum(0);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int value = slider.getValue();
				frameIndexText.setText(MessageFormat.format(I18N.getString("OutlookBar.ViewPanel.frameIndex"), value+1));
				comp.getViewModel().setaFrameByFrameIndex(value);
			}
		});
		frameIndexText = new JLabel();
		framePanel.setLayout(new BorderLayout());
		framePanel.add(slider,BorderLayout.CENTER);
		framePanel.add(frameIndexText,BorderLayout.EAST);
		init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("view2D")) {
			boolean bool = view2DButton.isSelected();
			view2DButton.setSelected(!bool);
			comp.getViewModel().select2D();

		} else if (e.getActionCommand().equals("view3D")) {
			boolean bool = view3DButton.isSelected();
			view3DButton.setSelected(!bool);
			comp.getViewModel().select3D();
		} else if (e.getActionCommand().equals("viewContour")) {
			boolean bool = viewContourButton.isSelected();
			viewContourButton.setSelected(!bool);
			comp.getViewModel().selectContour();
		} else if (e.getActionCommand().equals("play")) {
			comp.getViewModel().play();
		} else if (e.getActionCommand().equals("stop")) {
			comp.getViewModel().stop();
		} else if (e.getActionCommand().equals("first")) {
			comp.getViewModel().first();
		} else if (e.getActionCommand().equals("last")) {
			comp.getViewModel().last();
		} else if (e.getActionCommand().equals("forward")) {
			comp.getViewModel().forward();
		} else if (e.getActionCommand().equals("backward")) {
			comp.getViewModel().backward();
		} else if (e.getActionCommand().equals("speed")) {
			JPopupMenu pop = new JPopupMenu();
			pop.add(speedPanel);
			pop.show(this, getWidth(), speedButton.getY());
		} else if (e.getActionCommand().equals("index")) {
			//comp.getViewModel().play();
			JPopupMenu pop = new JPopupMenu();
			pop.add(framePanel);
			pop.show(this, getWidth(), indexButton.getY());
		}
		getParent().invalidate();
		getParent().repaint();
	}

	@Override
	protected void init() {
		view2DButton.setSelected(false);
		viewContourButton.setSelected(false);
		view3DButton.setSelected(false);
		view2DButton.setEnabled(false);
		viewContourButton.setEnabled(false);
		view3DButton.setEnabled(false);
		playButton.setEnabled(false);
		stopButton.setEnabled(false);
		firstButton.setEnabled(false);
		lastButton.setEnabled(false);
		forwardButton.setEnabled(false);
		backwardButton.setEnabled(false);
		for (int i = 0; i < 5; i++) {
			speedButtons[i].setEnabled(false);
		}
		slider.setEnabled(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//System.out.println(evt);
		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				init();
			} else if (temp instanceof ViewComponent) {
				//TODO set all button disable
				//System.out.println("change");
				comp = (ViewComponent) temp;
				stateChange(comp.getViewModel());
				//getParent().invalidate();
				//getParent().repaint();
			}
		} /*else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			Boolean exist = (Boolean) evt.getNewValue();
			System.out.println("exist:"+exist);
			if (!exist) {
				init();
			}
		}*/ else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			//Boolean exist = (Boolean) evt.getNewValue();
			//if (!exist) {
			init();
			getParent().invalidate();
			getParent().repaint();
			//}
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			//ChangeInfo info = (ChangeInfo) evt.getNewValue();
			ViewComponent temp = (ViewComponent) evt.getNewValue();
			if (comp == temp) {
				ViewModel model = temp.getViewModel();
				stateChange(model);
				getParent().invalidate();
				getParent().repaint();
			}
		}

	}

	private void stateChange(ViewModel dm) {
//		view2DButton.setEnabled(true);
//		view3DButton.setEnabled(true);
//		viewContourButton.setEnabled(true);
		view2DButton.setEnabled(true);
		viewContourButton.setEnabled(true);
		view3DButton.setEnabled(true);
		playButton.setEnabled(true);
		stopButton.setEnabled(true);
		firstButton.setEnabled(true);
		lastButton.setEnabled(true);
		forwardButton.setEnabled(true);
		backwardButton.setEnabled(true);
		slider.setEnabled(true);
		for (int i = 0; i < 5; i++) {
			speedButtons[i].setEnabled(true);
		}

		view2DButton.setSelected(dm.isSelect2D());
		viewContourButton.setSelected(dm.isSelectContour());
		view3DButton.setSelected(dm.isSelect3D());

		boolean isPlay = dm.isPlay();
		if (isPlay == true) // 不能播放
			playButton.setEnabled(false);
		if (isPlay == false) // 不能停止
			stopButton.setEnabled(false);

		//maxLen = dm.getFrameLen();
		int frameIndex = dm.getFrameIndex();
		int totalFrameNum = dm.getFrameLen();
		slider.setMaximum(totalFrameNum - 1);
		if (frameIndex == 0) // 不能向前播放
			forwardButton.setEnabled(false);
		if (frameIndex >= totalFrameNum - 1) // 不能向后播放
			backwardButton.setEnabled(false);
		slider.setValue(frameIndex);
		int selectedSpeed = dm.getSelectedSpeed();
		speedButtons[selectedSpeed].setSelected(true);

	}
}
