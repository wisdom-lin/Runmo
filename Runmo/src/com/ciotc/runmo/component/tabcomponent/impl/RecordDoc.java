package com.ciotc.runmo.component.tabcomponent.impl;

import static com.ciotc.runmo.util.Constants.SENSOR_NUM;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ciotc.runmo.component.tabcomponent.Doc;
import com.ciotc.runmo.util.Constants;

public class RecordDoc extends Doc {
	int powa;
	int gain;
	int period;
	int frameLen;
	Integer[][] data;

	/**
	 * 数据格式
	 * 0、标识{@link FEEMO_FILE_TAG}(用于标识文件类型)<br>
	 * 1、帧数<br>
	 * 2、录制周期<br>
	 * 3、数据内容<br>
	 * 4、powa<br>
	 * 5、gain <br>
	 */
	public boolean saveDataToFile(String path) {
		this.path = path;
		DataOutputStream ds = null;
//		BufferedOutputStream bos = new BufferedOutputStream(new Bytea);
		try {
			ds = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			ds.writeUTF(Constants.FEEMO_FILE_TAG);
			
			ds.writeInt(frameLen);
			ds.writeInt(period);

			//ByteArrayOutputStream bos = new ByteArrayOutputStream(new BufferedOutputStream());
			
			//byte[] bs = bos.toByteArray();
			
			for (int i = 0; i < frameLen; i++) {
				for (int j = 0; j < Constants.SENSOR_NUM; j++) {
					ds.writeInt(data[i][j]);
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
