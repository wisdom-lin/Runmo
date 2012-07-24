package com.ciotc.feemo.setting;

import java.io.Serializable;
import static com.ciotc.feemo.setting.SettingConstants.*;

/**
 * 所有的配置信息
 * @author Linxiaozhi
 *
 */
public class Settings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Settings instance = null;
	/**
	 * 检测周期
	 */
	float recordPeriod = PEROID_DEFAULT;
	/**
	 * 检测的最大帧数
	 */
	int recordFrame = FRAME_DEFAULT;

	/**
	 * 驱动电压
	 */
	int powa = POWA_DEFAULT;

	/**
	 * 增益值
	 */
	int gain = GAIN_DEFAULT;

	public static Settings getInstance() {
		return instance;
	}

	public static void setInstance(Settings instance) {
		Settings.instance = instance;
	}

	public float getRecordPeriod() {
		return recordPeriod;
	}

	public void setRecordPeriod(float recordPeriod) {
		this.recordPeriod = recordPeriod;
	}

	public int getRecordFrame() {
		return recordFrame;
	}

	public void setRecordFrame(int recordFrame) {
		this.recordFrame = recordFrame;
	}

	public int getPowa() {
		return powa;
	}

	public void setPowa(int powa) {
		this.powa = powa;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	@Override
	public String toString() {
		return "Settings [recordPeriod=" + recordPeriod + ", recordFrame=" + recordFrame + ", powa=" + powa + ", gain=" + gain + "]";
	}

}
