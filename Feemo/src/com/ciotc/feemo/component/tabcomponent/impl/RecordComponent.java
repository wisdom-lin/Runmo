package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.I18N.getString;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.TabComponent;
import com.ciotc.feemo.component.tabcomponent.impl.RecordModel.Status;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;
import com.ciotc.feemo.util.USBLock;
import com.ciotc.feemo.util.Util;
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

	Record2DView view2d;
	RecordContourView viewContour;
	Record3DView view3d;
	/**
	 * //1表示2D，2表示轮廓，3表示3D
	 */
	int lastIndex = 1;//初始化为1，即2D图

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
		firePropertyChange(ActionConstants.RECORD_COMPONENT_CLOSE, null, this);
	}

	@Override
	public void triggerClose() {
		synchronized (USBLock.LOCK) {
			USBDLL.clearButton1Info();
		}
//		close();
	}

	public void triggerOpen() {
		((RecordModel) model).triggerOpen();
	}

	public void triggerSave() {
		RecordDoc rdoc = (RecordDoc) doc;
		String path = Util.chooseSaveFile(this, ".", title + Constants.FEEMO_FILE_SUFFIX);
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

	public void select2D() {
		((RecordModel)model).selectWhichView(1);
	}

	public void selectContour() {
		((RecordModel)model).selectWhichView(2);
	}

	public void select3D() {
		((RecordModel)model).selectWhichView(3);
	}

	public int getSelectWhichView(){
		return ((RecordModel)model).getSelectWhichView();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		ChangeInfo info = (ChangeInfo) e.getSource();
		if (info.getTag().equals(ActionConstants.DATA_COLLECT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, null, info.getObject());

			Status status = (Status) info.getObject();

			if (status == Status.STOP) {
				Object[] options = { getString("RecordComponent.save"), getString("RecordComponent.notSave") };
				int response = JOptionPane.showOptionDialog(this, getString("RecordComponent.isSaveMoive"), getString("Feemo"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (response == 0) {

					RecordDoc rdoc = (RecordDoc) doc;
					String path = Util.chooseSaveFile(this, ".", title + Constants.FEEMO_FILE_SUFFIX);
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

		} else if (info.getTag().equals(ActionConstants.DATA_COLLECT_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, null, info.getObject());
		} else if (info.getTag().equals(ActionConstants.RECORD_VIEW_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE, null, info.getObject());
		} else if (info.getTag().equals(ActionConstants.DATA_COLLECT_DATA)) {

		}

	}

}
