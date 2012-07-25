package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.ActionConstants.VIEW_FRAME_MODEL;
import static com.ciotc.feemo.util.ActionConstants.VIEW_FRAME_MOUSE_MOVE;
import static com.ciotc.feemo.util.ActionConstants.VIEW_VIW_CONTROL;
import static com.ciotc.feemo.util.Constants.NOMAL_SPEED;
import static com.ciotc.feemo.util.Constants.SENSOR_NUM;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.Model;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;

public class ViewModel extends Model implements PropertyChangeListener {

	class PaintTask extends TimerTask {

		@Override
		public void run() {
			try {
				if (frameIndex >= frameLen) {
					stop();
					frameIndex = frameLen - 1;
					setaFrameByFrameIndex(frameIndex);
					return;
				}
				synchronized (ViewModel.this) {
					while (isPlay == false)
						ViewModel.this.wait();
				}
				setaFrameByFrameIndex(frameIndex);
				//setaFrame(displayDoc.getSelectedFrame(frameIndex));
				frameIndex++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private int frameIndex = 0;
	private int playPeriod = NOMAL_SPEED;

	private int[] aFrame = new int[SENSOR_NUM];
	private int selectedSpeed = 2;
	private int viewControl = 0;
//	private boolean isFirst = false;
//	private boolean isLast = true;
	private boolean isPlay;
	private int frameLen = 0;
	private int x;
	private int y;
	private int pointValue;
	private boolean isEntering;
	Timer timer = new Timer();
	PaintTask paint;

	ViewDoc doc;

	ViewModel() {

	}

	public void init() {
		select2D();
		//select3D();
		//selectContour();
		//play();
		selectNowFrame();
	}

	/**
	 * 超快
	 */
	public void fastest() {
		selectedSpeed = 4;
		playPeriod = 200;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 第一帧
	 */
	public void first() {
		frameIndex = 0;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	/**
	 * 向前一帧
	 */
	public void forward() {
		frameIndex--;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	/**
	 * 最后一帧
	 */
	public void last() {
		frameIndex = frameLen - 1;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	/**
	 * 向后一帧
	 */
	public void backward() {
		frameIndex++;
		//setaFrame(displayDoc.getSelectedFrame(frameIndex));
		setaFrameByFrameIndex(frameIndex);
	}

	/**
	 * 较快
	 */
	public void mediumFast() {
		selectedSpeed = 3;
		playPeriod = 500;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 慢
	 */
	public void mediumSlow() {
		selectedSpeed = 1;
		playPeriod = 2000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 标准
	 */
	public void nomal() {
		selectedSpeed = 2;
		playPeriod = 1000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 播放
	 */
	public void play() {
		isPlay = true;
		timer.scheduleAtFixedRate(new PaintTask(), 0, playPeriod);
	}

	/**
	 * 超慢
	 */
	public void slowest() {
		selectedSpeed = 0;
		playPeriod = 5000;
		if (isPlay == true) {
			stop();
			play();
		} else
			selectNowFrame();
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		isPlay = false;
		synchronized (ViewModel.this) {
			ViewModel.this.notify();
		}

		timer.cancel();
		timer.purge();
		timer = new Timer();

		if (frameIndex < frameLen)
			setaFrameByFrameIndex(frameIndex);

	}

	public void selectSpeed(int index) {
		switch (index) {
		case 0:
			slowest();
			break;
		case 1:
			mediumSlow();
			break;
		case 2:
			nomal();
			break;
		case 3:
			mediumFast();
			break;
		case 4:
			fastest();
			break;
		default:
			break;
		}
	}

	boolean select2D;
	boolean selectContour;
	boolean select3D;

	public void select2D() {
		if (!select2D) {
			viewControl |= Constants._2D;
			select2D = true;
		} else {
			viewControl &= ~Constants._2D;
			select2D = false;
		}
		fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_VIW_CONTROL, viewControl)));
	}

	public void select3D() {
		if (!select3D) {
			viewControl |= Constants._3D;
			select3D = true;
		} else {
			viewControl &= ~Constants._3D;
			select3D = false;
		}
		fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_VIW_CONTROL, viewControl)));
	}

	public void selectContour() {
		if (!selectContour) {
			viewControl |= Constants._CONTOUR;
			selectContour = true;
		} else {
			viewControl &= ~Constants._CONTOUR;
			selectContour = false;
		}
		fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_VIW_CONTROL, viewControl)));
	}

	public void selectNowFrame() {
		if (frameIndex < frameLen)
			//setaFrame(displayDoc.getSelectedFrame(frameIndex));
			setaFrameByFrameIndex(frameIndex);
	}

	public void setaFrameByFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
		if (frameIndex < frameLen) {
			this.aFrame = doc.getSelectedFrame(frameIndex);
			fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_FRAME_MODEL, this)));
		}
	}

	public void setDoc(ViewDoc doc) {
		this.doc = doc;
		this.frameLen = doc.getFrameLen();
	}

	public int[] getaFrame() {
		return aFrame;
	}

	public int getSelectedSpeed() {
		return selectedSpeed;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public int getFrameLen() {
		return frameLen;
	}

	public boolean isSelect2D() {
		return select2D;
	}

	public boolean isSelectContour() {
		return selectContour;
	}

	public boolean isSelect3D() {
		return select3D;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void selectForceInXY(int x, int y) {
		this.x = x;
		this.y = y;
		if (frameIndex < frameLen) {
			pointValue = doc.getForceinXY(frameIndex, x, y);
			fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_FRAME_MOUSE_MOVE, this)));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.VIEW_VIEW_MOUSE_MOVE)) {
			int[] is = (int[]) evt.getNewValue();
			if (is[0] == Integer.MIN_VALUE) {
				isEntering = false;
				fireChangeEvent(new ChangeEvent(new ChangeInfo(VIEW_FRAME_MOUSE_MOVE, this)));
				return;
			}
			isEntering = true;
			selectForceInXY(is[0], is[1]);
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPointValue() {
		return pointValue;
	}

	public boolean isEntering() {
		return isEntering;
	}

}
