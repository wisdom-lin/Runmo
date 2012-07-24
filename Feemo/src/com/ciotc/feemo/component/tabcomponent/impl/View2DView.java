package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.feemo.util.Constants.SENSOR_NUM;
import static com.ciotc.feemo.util.Constants.SENSOR_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputListener;

import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.ActionConstants;

public class View2DView extends ViewView implements MouseMotionListener,MouseInputListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	boolean isEntering = false;
	
	public View2DView() {
		addMouseMotionListener(this);
		addMouseListener(this);
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		float pixel = CalcPixelByWhichSide();
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		Point point = e.getPoint();
		if (!_rect.contains(point)){
			firePropertyChange(ActionConstants.VIEW_VIEW_MOUSE_MOVE, null, new int[]{Integer.MIN_VALUE,0,0});
			return;
		}
		int i1 = Math.round(e.getX() / pixel);
		int i2 = Math.round(e.getY() / pixel);

		int[] is = InWhichBox(i1, i2, e.getX(), e.getY(), pixel);
		firePropertyChange(ActionConstants.VIEW_VIEW_MOUSE_MOVE, null, is);
	}

	/**
	 * 
	 * @param x
	 *                初略的横坐标
	 * @param y
	 *                初略的纵坐标
	 * @param m
	 *                鼠标的横坐标
	 * @param n
	 *                鼠标的纵坐标
	 * @return 返回准确的横纵坐标
	 */
	private int[] InWhichBox(int x, int y, int m, int n, float pixel) {
		float[] f = new float[4];
		f[0] = (x - 1) * pixel;
		f[1] = x * pixel;
		f[2] = (x + 1) * pixel;
		f[3] = (x + 2) * pixel;
		float[] g = new float[4];
		g[0] = (y - 1) * pixel;
		g[1] = y * pixel;
		g[2] = (y + 1) * pixel;
		g[3] = (y + 2) * pixel;
		int i = 0, j = 0, k;
		for (k = 0; k < 3; k++) {
			if (m >= f[k] && m < f[k + 1]) {
				i = (x - 1) + k;
				break;
			}
		}
		for (k = 0; k < 3; k++) {
			if (n >= g[k] && n < g[k + 1]) {
				j = (y - 1) + k;
				break;
			}
		}
		return new int[] { i, j };
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		float pixel = CalcPixelByWhichSide();
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		Point point = e.getPoint();
		if (!_rect.contains(point)) {
			firePropertyChange(ActionConstants.VIEW_VIEW_MOUSE_MOVE, null, new int[]{Integer.MIN_VALUE,0,0});
		}
		
	}
}
