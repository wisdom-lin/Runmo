package com.ciotc.runmo.component.tabcomponent.impl;

import static com.ciotc.runmo.util.I18N.getString;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import com.ciotc.runmo.component.tabcomponent.ChangeInfo;
import com.ciotc.runmo.component.tabcomponent.TabComponent;
import com.ciotc.runmo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.runmo.util.Constants;
import com.ciotc.runmo.util.I18N;
import com.ciotc.runmo.util.USBLock;
import com.ciotc.runmo.util.Util;
import com.ciotc.teemo.usbdll.USBDLL;

public class RecordComponent extends TabComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 记录新建的录制影片数
	 */
	private static int counter = 0;

	/**
	 * //1表示2D，2表示轮廓，3表示3D
	 */
	//int lastIndex = 2;//初始化为1，即2D图

	public RecordComponent() {
		model = new RecordModel();
		model.addChangeListener(this);

		view = new RecordView();
		//viewContour = new RecordContourView();
		//view3d = new Record3DView();
		setLayout(new BorderLayout());
		//add(view);

		add(view);
		view.addPropertyChangeListener((RecordModel) model);
		model.addChangeListener(view);

		doc = new RecordDoc();

		this.title = I18N.getString("RecordComponent.title") + (++counter);
	}

	public void start() {
		//选择轮廓图作为录制初始图
		selectContour();
		((RecordModel) model).start();
	}

	public void play() {
		((RecordModel) model).play();
	}

	public void pause() {
		((RecordModel) model).pause();
	}

	public void stop() {
		((RecordModel) model).stop();
	}

	public void restore() {
		((RecordModel) model).restore();
	}

	public void save() {
		((RecordModel) model).save();
	}

	@Override
	public void open() {
		super.open();
		firePropertyChange(ActionConstants.RECORD_COMPONENT_OPEN, null, this);
		start();//insert in here?
	}

	@Override
	public void close() {
		super.close();
		RecordModel rm = (RecordModel) model;
		rm.closeDirectly();
		//firePropertyChange(ActionConstants.RECORD_COMPONENT_CLOSE, null, this);
	}

	@Override
	public void triggerClose() {
//		synchronized (USBLock.LOCK) {
//			USBDLL.clearButton1Info();
//		}
		close();
	}

	public void triggerOpen() {
		((RecordModel) model).triggerOpen();
	}

	public void triggerSave() {
		Object[] options = { getString("RecordComponent.save"), getString("RecordComponent.notSave") };
		int response = JOptionPane.showOptionDialog(this, getString("RecordComponent.isSaveMoive"), getString("Feemo"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (response == 0) {
			RecordDoc rdoc = (RecordDoc) doc;
			String userDir = System.getProperty("user.home")+"/Desktop";
			String path = Util.chooseSaveFile(this, userDir, title + Constants.FEEMO_FILE_SUFFIX);
			if (path != null) {
				((RecordModel) model).saveToDoc(rdoc);
				if (rdoc.saveDataToFile(path)) {
					save();
					JOptionPane.showMessageDialog(this, getString("RecordComponent.saveSuccess"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, getString("RecordComponent.saveFail"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);

				}
			}
		}
	}

	public void select2D() {
		((RecordModel) model).selectWhichView(1);
	}

	public void selectContour() {
		((RecordModel) model).selectWhichView(2);
	}

	public void select3D() {
		((RecordModel) model).selectWhichView(3);
	}

	public int getSelectWhichView() {
		return ((RecordModel) model).getSelectWhichView();
	}

	public Status getStatus() {
		return ((RecordModel) model).status;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ChangeInfo info = (ChangeInfo) e.getSource();
		if (info.getTag().equals(ActionConstants.DATA_COLLECT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, null, info.getObject());

			Status status = (Status) info.getObject();
//
//			if (status == Status.STOP) {
//				((RecordModel)model).setSaving(true);
//				Object[] options = { getString("RecordComponent.save"), getString("RecordComponent.notSave") };
//				int response = JOptionPane.showOptionDialog(this, getString("RecordComponent.isSaveMoive"), getString("Feemo"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//				if (response == 0) {
//
//					RecordDoc rdoc = (RecordDoc) doc;
//					String path = Util.chooseSaveFile(this, ".", title + Constants.FEEMO_FILE_SUFFIX);
//					if (path != null) {
//						((RecordModel) model).saveToDoc(rdoc);
//						if (rdoc.saveDataToFile(path)) {
//							JOptionPane.showMessageDialog(this, getString("RecordComponent.saveSuccess"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);
//							save();
//						} else {
//							JOptionPane.showMessageDialog(this, getString("RecordComponent.saveFail"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);
//
//						}
//					}
//				}
//			}
			if (status == Status.END)
				firePropertyChange(ActionConstants.RECORD_COMPONENT_CLOSE, null, this);
		} else if (info.getTag().equals(ActionConstants.DATA_COLLECT_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, null, info.getObject());
		} else if (info.getTag().equals(ActionConstants.RECORD_VIEW_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE, null, info.getObject());
		} else if (info.getTag().equals(ActionConstants.DATA_COLLECT_SAVE)) {
			triggerSave();
		}

	}

}
