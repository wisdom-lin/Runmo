package com.ciotc.feemo.component.tabcomponent.impl;

import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.View;
import com.ciotc.feemo.util.ActionConstants;

public abstract class ViewView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		drawFrame(g, recvdata);
//	}
//
//	public abstract void drawFrame(Graphics g2, int[] dd);
//
//	/**
//	 * 选择长为基准还是宽为基准
//	 */
//	protected float CalcPixelByWhichSide() {
//		int width = getWidth() - COMPONENT_GAP;
//		int height = getHeight() - COMPONENT_GAP;
//		float w0 = width * 1.0f / SENSOR_WIDTH;
//		float h0 = height * 1.0f / SENSOR_HEIGHT;
//		if (Float.compare(w0, h0) > 0) { //w0>h0 选择高 
//			return height * 1.0f / SENSOR_HEIGHT;
//		} else { //选择宽
//			return width * 1.0f / SENSOR_WIDTH;
//		}
//
//	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ChangeInfo info = (ChangeInfo) e.getSource();
		//System.out.println(info.getTag());
		if (info.getTag().equals(ActionConstants.VIEW_FRAME_MODEL)) {
			ViewModel model = (ViewModel) info.getObject();
			recvdata = model.getaFrame();
			repaint();
		}

	}
}
