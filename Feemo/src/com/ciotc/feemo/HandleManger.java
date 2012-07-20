package com.ciotc.feemo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import com.ciotc.feemo.util.Constants;
import com.ciotc.teemo.usbdll.USBDLL;
import static com.ciotc.feemo.util.USBLock.LOCK;

/**
 * 管理手柄的连接
 * @author Linxiaozhi
 *
 */
public class HandleManger {
	/**
	 * 检查手柄连接的任务
	 * @author Linxiaozhi
	 *
	 */
	class HandleChecking extends TimerTask {

		@Override
		public void run() {
			int deviceIndex = 0;
			synchronized (LOCK) {
				deviceIndex = USBDLL.getDevicesNum();
			}
			if (deviceIndex == 0) { //没有连接
				//statusBar.setText(getResourceString("MainFrame.noHandleTips"));
				if (isConnectHandle) { //由连接变过来的
					stopCheckButton();
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							context.disconnectHandle();
						}
					});

				}

				isConnectHandle = false;

				return;
			} else {//连接
				if (!isConnectHandle) { //由没连接变过来的
					boolean bool;
					synchronized (LOCK) {
						bool = USBDLL.open(deviceIndex - 1);
					}
					if (!bool) //打不开相当没有连接上
						return;
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							context.connectHandle();
						}
					});

					startCheckButton();
					//firePropertyChange(IS_OPEN_HANDLE, isOpenHandle, true);
				}

				isConnectHandle = true;

			}
		}

	}

	class ButtonChecking extends TimerTask {

		@Override
		public void run() {
			int info = 0;
			synchronized (LOCK) {
				info = USBDLL.getButton1Info();
			}
			if (info == 0)
				return;
			if (info == 1) { // 按键1 开
				if (!isButtonPress)
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							context.newMovie();
						}
					});
				isButtonPress = true;

			}

			if (info == 2) {// 按键1 关
				if (isButtonPress)
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							context.closeMovie();
						}
					});

				isButtonPress = false;

			}
		}
	}

	/**
	 * 检查手柄有无连接的状态
	 */
	public boolean isConnectHandle = false;
	/**
	 * 检查按键是有按下的状态
	 */
	boolean isButtonPress = false;

	public Timer timer = null;

	/**
	 * 检查手柄有无连接的任务
	 */
	HandleChecking handleTask;
	/**
	 *  检查按键是有按下的任务
	 */
	ButtonChecking buttonTask;

	Context context;

	public HandleManger(Context context) {
		timer = new Timer();
		//handleTimer = new Timer();
		//buttonTimer = new Timer();
		this.context = context;
	}

	/**
	 * 开始检测
	 */
	public void open() {
		startCheckHandle();
	}

	/**
	 * 如果手柄打开了，则关闭手柄，否则返回.
	 */
	public void close() {
		stopCheckButton();
		stopCheckHandle();
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
//		handleTimer.cancel();
//		handleTimer.purge();
//		handleTimer = null;
//
//		buttonTimer.cancel();
//		buttonTimer.purge();
//		buttonTimer = null;

		if (isConnectHandle) {
			synchronized (LOCK) {
				USBDLL.clearButton1Info();
			}
			synchronized (LOCK) {
				USBDLL.clearButton2Info();
			}
			synchronized (LOCK) {
				USBDLL.close();
			}
		} else
			return;
	}

	private void startCheckHandle() {
		handleTask = new HandleChecking();
		timer.schedule(handleTask, 0, Constants.HANDLE_CHECK_PERIOD);
	}

	private void stopCheckHandle() {
		if (buttonTask != null)
			handleTask.cancel();
	}

	private void stopCheckButton() {
		if (buttonTask != null)
			buttonTask.cancel();
	}

	private void startCheckButton() {
		buttonTask = new ButtonChecking();
		synchronized (LOCK) {
			USBDLL.clearButton1Info();
		}
		isButtonPress = false;
		timer.scheduleAtFixedRate(buttonTask, 0, Constants.BUTTON1_CHECK_PERIOD); // 200ms
	}
}
