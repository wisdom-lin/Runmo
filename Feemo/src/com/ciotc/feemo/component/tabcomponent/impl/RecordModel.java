package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.USBLock.LOCK;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.TriggerCloser;
import com.ciotc.feemo.component.TriggerOpener;
import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.Model;
import com.ciotc.feemo.setting.Settings;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.USBLock;
import com.ciotc.teemo.usbdll.USBDLL;
import com.citoc.feemo.config.Configurge;

public class RecordModel extends Model implements TriggerCloser, TriggerOpener, PropertyChangeListener {
	/**
	 * 记录当前所在的状态
	 * @author LinXiaozhi
	 *
	 */
	public enum Status {
		/**
		 * 起始
		 */
		START,
		/**
		 * 正在录制
		 */
		RECORDING,
		/**
		 * 录制暂停
		 */
		PAUSE,
		/**
		 * 录制停止
		 */
		STOP,
		/**
		 * 已保存
		 */
		SAVED,
		/**
		 * 结束
		 */
		END

	}

	Timer timer = null;
	TimerTask buttonCheckTask;
	TimerTask dataCollectTask;

	Status status = Status.START;// 默认状态
	Settings set = Settings.getInstance();
	int powa = set.getPowa();
	int gain = set.getGain();
	int period = Math.round(set.getRecordPeriod() * 1000);
	int frame = set.getRecordFrame();
	List<Integer[]> datas = new ArrayList<Integer[]>(); //存储的数据
	volatile boolean pause = false; //暂停
	boolean isSave = false;
	//boolean isSaving = false;
	/**
	 * //1表示2D，2表示轮廓，3表示3D
	 */
	int selectWhichView = 1;

	public RecordModel() {

	}

	private void constructTimer() {
		timer = new Timer();
		buttonCheckTask = new ButtonChecking();
		timer.schedule(buttonCheckTask, 0, Constants.BUTTON2_CHECK_PERIOD);

		dataCollectTask = new DataCollectingOutRecord();
		timer.schedule(dataCollectTask, 0, period);
	}

	/**
	 * 检查黄色按键的状态
	 * @author Linxiaozhi
	 *
	 */
	class ButtonChecking extends TimerTask {

		boolean isPresson = false;
		{
			synchronized (LOCK) {
				USBDLL.clearButton2Info();
			}
		}

		@Override
		public void run() {
			int info = 0;

			synchronized (LOCK) {
				info = USBDLL.getButton2Info();
			}
			if (info == 0)
				return;

			if (info == 1) { //按键开
				if (!isPresson) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							play();

						}
					});
				}
				isPresson = true;
			}
			if (info == 2) { //按键关
				if (isPresson) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							stop();

						}
					});
				}
				isPresson = false;
			}
		}
	}

	class DataCollectingOutRecord extends TimerTask {

		@Override
		public void run() {
			byte[] recvdata;
			int _powa = set.getPowa();//pref.getInt("powa", 10);
			int _gain = set.getGain();//pref.getInt("gain", 0);
			synchronized (LOCK) {
				USBDLL.setPowA(_powa);
				USBDLL.setGain(_gain);
				recvdata = USBDLL.collectFrame();
			}
			final int[] temp = doFilter(recvdata);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_DATA, temp)));
				}
			});
		}
	}

	class DataCollectingInRecord extends TimerTask {

		int counter = 0;

		@Override
		public void run() {
			byte[] recvdata;
			synchronized (LOCK) {
				USBDLL.setPowA(powa);
				USBDLL.setGain(gain);
				recvdata = USBDLL.collectFrame();
			}
			if (!pause) {
				counter++;
				//save data
				int size = recvdata.length;
				Integer[] bData = new Integer[size];

				for (int i = 0; i < size; i++) {
					bData[i] = recvdata[i] & 0xff;
				}
				datas.add(bData);
			}

			final int[] temp = doFilter(recvdata);

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_DATA, temp)));
					fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_LEN, counter)));
				}
			});
			if (counter >= frame) {
				//cancel();
				stop();
			}
		}

	}

	public void start() {
		constructTimer();

		status = Status.START;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.START)));
	}

	public void play() {
		/**
		 * 开始录制的时候 将powa和gain保存到doc中，至此之后录制过程中都按这个来进行
		 */
		powa = set.getPowa();//pref.getInt("powa", 10);
		gain = set.getGain();//pref.getInt("gain", 0);

		//rdoc.setPowa(powa);
		//rdoc.setGain(gain);
		//rdoc.startRecord();
		//r2dStatusBar.setPowaGain(powa, gain);
		//gainInFrame.setVisible(false);
		//firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.RECORDING);
		//status = Status.RECORDING;
		//status.set(Status.RECORDING);
		dataCollectTask.cancel();
		dataCollectTask = new DataCollectingInRecord();
		timer.schedule(dataCollectTask, 0, period);

		status = Status.RECORDING;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.RECORDING)));

	}

	public void pause() {
		//firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.PAUSE);
		//status = Status.PAUSE;
		//status.set(Status.PAUSE);
		pause = true;

		status = Status.PAUSE;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.PAUSE)));
	}

	public void restore() {

		pause = false;

		status = Status.RECORDING;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.RECORDING)));
		//firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.RECORDING);
		//status = Status.RECORDING;
		//status.set(Status.RECORDING);
	}

	public void stop() {
//		rdoc.stopRecord();
//		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.STOP);
//		//status = Status.STOP;
//		status.set(Status.STOP);
//		System.out.println("triggerstop");
		dataCollectTask.cancel();
//		dataCollectTask = new DataCollectingOutRecord();
//		timer.schedule(dataCollectTask, 0, period);
		status = Status.STOP;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.STOP)));
	}

	/**
	 * 保存<br>
	 * <b>注意：保存成功之后不能再录制了</b>
	 */
	public void save() {
		status = Status.SAVED;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.SAVED)));
//		Object[] options = { getResourceString("save"), getResourceString("DisplayFileTemplate.notSave") };
//		int response = JOptionPane.showOptionDialog(r2DInFrame, getResourceString("DisplayFileTemplate.isSaveMoive"), getResourceString("Teemo"), JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//		if (response == 0) {
//			if (rdoc.saveData2File() && rdoc.saveData2Database()) {
//				String id = String.valueOf(rdoc.getPatientID());
//				mainFrame.pntFrm.validateOpenFrm(id);
//				JOptionPane.showMessageDialog(r2DInFrame, getResourceString("saveSuccess"), getResourceString("Teemo"), JOptionPane.PLAIN_MESSAGE);
//				rdoc.putPeferGain();
//				if (timer != null) {
//					timer.cancel();
//					timer.purge();
//					timer = null;
//				}
//				firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.SAVED);
//				//status = Status.SAVED;
//				status.set(Status.SAVED);
//			} else
//				JOptionPane.showMessageDialog(r2DInFrame, getResourceString("saveFail"), getResourceString("Teemo"), JOptionPane.ERROR_MESSAGE);
//		}

	}

	public void close() {
//		if (timer != null) {
//			timer.cancel();
//			timer.purge();
//			timer = null;
//		}
//		removeAll();
//		firePropertyChange(MainTabbedPane.RECORD_STSTUS, status.get(), Status.END);
		//status = Status.END;
//		status.set(Status.END);
//		firePropertyChange(MainTabbedPane.CLOSE_REALTIME_FILE_TEMPLATE, null, this);
		if (buttonCheckTask != null) {
			buttonCheckTask.cancel();
		}
		if (dataCollectTask != null) {
			dataCollectTask.cancel();
		}
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		status = Status.END;
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_STATUS, Status.END)));

	}

	public void triggerSave() {
		fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.DATA_COLLECT_SAVE, null)));
	}

	/**
	 * 直接关闭 根据现在的状态调用停止 保存 和关闭
	 */
	public void closeDirectly() {
		System.out.println(status);
		switch (status) {
		case START:
			close();
			break;
		case RECORDING:
			stop();
			triggerSave();
			close();
			break;
		case PAUSE:
			restore();
			stop();
			triggerSave();
			close();
			break;
		case STOP:
			if (!isSave) {
				triggerSave();
			}
			close();
			break;
		case SAVED:
			close();
		default:
			break;
		}
	}

	public void saveToDoc(RecordDoc doc) {
		doc.setPowa(powa);
		doc.setGain(gain);
		doc.setPeriod(period);
		doc.setData(datas);
		isSave = true;
	}

	public void selectWhichView(int index) {
		if (selectWhichView != index) {
			selectWhichView = index;
			fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.RECORD_VIEW_INDEX, selectWhichView)));
		}
	}

	public int getSelectWhichView() {
		return selectWhichView;
	}

	@Override
	public void triggerClose() {
		synchronized (USBLock.LOCK) {
			USBDLL.clearButton2Info();
		}
	}

	@Override
	public void triggerOpen() {
		synchronized (USBLock.LOCK) {
			USBDLL.setButton2On();
		}
	}

	/**
	 * 默认设置一种
	 * @param src
	 * @return
	 */
	int[] doFilter(byte[] src){
		return doFilter2(src);
	}
	
	/**
	 * 滤值,将数值调整正确
	 * @param src 原数组
	 * @return 滤值后的数组
	 */
	int[] doFilter1(byte[] src) {
		int[] dest = new int[src.length];
		try {
			for (int i = 0; i < src.length; i++) {
				dest[i] = src[i] & 0xff;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;

	}

	/**
	 * 如果低于最低值时，就需要滤掉.用于数值
	 * @param src 原数组
	 * @return 滤值后的数组
	 */
	int[] doFilter2(byte[] src) {
		String string = Configurge.getProperty(Configurge.FILTER_VAULE);
		int[] dest = new int[src.length];
		try {
			int value = Integer.parseInt(string.trim());

			for (int i = 0; i < dest.length; i++) {
				dest[i] = src[i] & 0xff;
				if (dest[i] <= value)
					dest[i] = 0;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_VIEW_MOUSE_MOVE)) {
			fireChangeEvent(new ChangeEvent(new ChangeInfo(ActionConstants.RECORD_VIEW_MOUSE_MOVE, evt.getNewValue())));
		}
	}

}
