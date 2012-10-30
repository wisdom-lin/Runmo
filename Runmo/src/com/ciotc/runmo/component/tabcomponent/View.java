package com.ciotc.runmo.component.tabcomponent;

import static com.ciotc.runmo.util.Constants.COMPONENT_GAP;
import static com.ciotc.runmo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.runmo.util.Constants.SENSOR_WIDTH;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int[] recvdata;

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 选择长为基准还是宽为基准
	 */
	protected float CalcPixelByWhichSide() {
		int width = getWidth() - COMPONENT_GAP;
		int height = getHeight() - COMPONENT_GAP;
		float w0 = width * 1.0f / SENSOR_WIDTH;
		float h0 = height * 1.0f / SENSOR_HEIGHT;
		if (Float.compare(w0, h0) > 0) { //w0>h0 选择高 
			return height * 1.0f / SENSOR_HEIGHT;
		} else { //选择宽
			return width * 1.0f / SENSOR_WIDTH;
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawFrame(g, recvdata);
	}

	protected void drawFrame(Graphics g, int[] recvdata2) {

	}
}
