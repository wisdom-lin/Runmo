package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.SENSOR_NUM;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ciotc.feemo.component.tabcomponent.Doc;
import com.ciotc.feemo.util.Constants;

public class RecordDoc extends Doc {
	int powa;
	int gain;
	int period;
	int frameLen;
	Integer[][] data;

	/**
	 * 数据格式
	 * 1、帧数
	 * 2、录制周期
	 * 3、数据内容
	 * 4、powa
	 * 5、gain 
	 */
	public boolean saveDataToFile(String path) {
		DataOutputStream ds = null;
		try {
			ds = new DataOutputStream(new FileOutputStream(path));

			ds.writeInt(frameLen);
			ds.writeFloat(period);

			for (int i = 0; i < frameLen; i++) {
				for (int j = 0; j < Constants.SENSOR_NUM; j++) {
					ds.write(data[i][j]);
				}
			}
			ds.writeInt(powa);
			ds.writeInt(gain);
			ds.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (ds != null) {
				try {
					ds.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
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

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void setData(List<Integer[]> datas) {
		/**
		 * 把dataList赋值给data 为了兼容以前的函数
		 */
		frameLen = datas.size();
		data = new Integer[frameLen][SENSOR_NUM];
		for (int i = 0; i < frameLen; i++) {
			Integer[] aData = new Integer[SENSOR_NUM];
			Integer[] bData = datas.get(i);
			for (int j = 0; j < SENSOR_NUM; j++)
				aData[j] = bData[j];
			data[i] = aData;
		}
	}

//	public List<Short[]> getDatas() {
//		return datas;
//	}
//
//	public void setDatas(List<Short[]> datas) {
//		this.datas = datas;
//	}

}
