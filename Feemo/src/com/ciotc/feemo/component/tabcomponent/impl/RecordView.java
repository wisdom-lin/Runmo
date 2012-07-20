package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.COMPONENT_GAP;
import static com.ciotc.feemo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.feemo.util.Constants.SENSOR_NUM;
import static com.ciotc.feemo.util.Constants.SENSOR_WIDTH;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.View;
import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.ActionConstants;

public class RecordView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int[] recvdata;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawFrame(g, recvdata);
	}

	public void drawFrame(Graphics g2, int[] dd) {
		Color oldColor;

		Graphics2D g = (Graphics2D) g2;
		oldColor = g.getColor();
		// 背景为灰色
//		g.setColor(Color.LIGHT_GRAY);

//		g.fillRect(0, 0, getWidth(), getHeight());

		if (dd == null)
			return;
		float pixel = CalcPixelByWhichSide();

		//画边框
		g.setColor(Color.BLACK);
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		g.draw(_rect);
			
		g.setColor(new Color(0, 0, 0));

		//文本方式打印
//		if(dd!=null)
//		for (int i = 0; i < SENSOR_WIDTH; i++) {
//			//System.out.println("J"+i);
//			for (int j = 0; j < SENSOR_HEIGHT; j++) {
//				//System.out.print("J"+j+"="+tempData[i*44+j]+" ");
//				String s = String.format("%4d", dd[i * SENSOR_HEIGHT + j]);
//				g.drawString(s, i * pixel, j * pixel);
//			}
//		}

		int x, y;
		List<Integer> values = new ArrayList<Integer>();
		//中心点
		Point point = null;
		float sum = 0;
		float productfx = 0, productfy = 0;
		/**
		 * 画矩阵 
		 * 同时还求出中心点
		 */
		for (int i = 0; i < SENSOR_NUM; i++) {
			Color color = null;

			x = i / SENSOR_HEIGHT; // rowNum == 44; x
			y = i % SENSOR_HEIGHT; // y

			int f = dd[i];

			if (f == 255)
				color = MyColor.values()[16].getRgb();
			else if (f == 0) {
				continue;
			} else {
				int in = f >>> 4;
				color = MyColor.values()[in].getRgb();
			}
			g.setColor(color);

			Rectangle2D rect = new Rectangle2D.Float(x * pixel, y * pixel, pixel, pixel);

			g.fill(rect);

			//@date 2012.6.15
			if (f != 0)
				values.add(f);
		}

		if (sum == 0) {
			point = new Point(0, 0);

		} else {
			x = (int) (productfx / sum);
			y = (int) (productfy / sum);
			point = new Point(x, y);
		}

		//drawCOF(g, point, pixel);
		g.setColor(oldColor);
	}

	/**
	 * 选择长为基准还是宽为基准
	 */
	private float CalcPixelByWhichSide() {
		int width = getWidth()-COMPONENT_GAP;
		int height = getHeight()-COMPONENT_GAP;
		float w0 = width * 1.0f / SENSOR_WIDTH;
		float h0 = height * 1.0f / SENSOR_HEIGHT;
		if (Float.compare(w0, h0) > 0) { //w0>h0 选择高 
			return height * 1.0f / SENSOR_HEIGHT;
		} else { //选择宽
			return width * 1.0f / SENSOR_WIDTH;
		}

	}

	/**
	 * 画中心点
	 * 
	 * @param g
	 */
	private void drawCOF(Graphics2D g, Point p, float pixel) {
		int x;
		int y;
		if (p == null)
			return;
		/**
		 * 画中心点
		 */
		x = p.x;
		y = p.y;
		// 画菱形的算法
		// 菱形内有两条线
		float m = 0.5f;

		// 第一种算法对应的公式
		if (x != 0 || y != 0) {
			Path2D centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y - m) * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, y * pixel);
			centerPoint.closePath();
			g.setColor(Color.RED);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo((x + 3 * m) * pixel, (y + m) * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.closePath();
			g.setColor(Color.WHITE);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + 3 * m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.closePath();
			g.setColor(Color.RED);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.lineTo((x - m) * pixel, (y + m) * pixel);
			centerPoint.lineTo(x * pixel, y * pixel);
			centerPoint.closePath();
			g.setColor(Color.WHITE);
			g.fill(centerPoint);

			centerPoint = new Path2D.Double();
			centerPoint.moveTo((x + m) * pixel, (y - m) * pixel);
			centerPoint.lineTo((x + 3 * m) * pixel, (y + m) * pixel);
			centerPoint.lineTo((x + m) * pixel, (y + 3 * m) * pixel);
			centerPoint.lineTo((x - m) * pixel, (y + m) * pixel);
			centerPoint.closePath();
			centerPoint.moveTo((x + 2 * m) * pixel, y * pixel);
			centerPoint.lineTo(x * pixel, (y + 2 * m) * pixel);
			centerPoint.moveTo(x * pixel, y * pixel);
			centerPoint.lineTo((x + 2 * m) * pixel, (y + 2 * m) * pixel);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1.0f));
			g.draw(centerPoint);
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ChangeInfo info = (ChangeInfo) e.getSource();
		if (info.getTag().equals(ActionConstants.DATA_COLLECT_DATA)) {
			recvdata = (int[]) info.getObject();

			repaint();
		}

	}
}
