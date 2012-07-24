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
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;

import com.ciotc.contour.Point1;
import com.ciotc.contour.RenderContour;
import com.ciotc.contour.Triangle;
import com.ciotc.contour.Triangulate;
import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.View;
import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;

public class RecordContourView extends View implements MouseMotionListener, MouseInputListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int rowNum = Constants.SENSOR_HEIGHT;
	public final static int colNum = Constants.SENSOR_WIDTH;

	boolean isEntering = false;

	public RecordContourView() {
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void drawFrame(Graphics g, int[] dd) {

		Graphics2D g2 = (Graphics2D) g;
		Color oldColor = g2.getColor();
		float pixel = CalcPixelByWhichSide();
		//画边框
		g2.setColor(Color.BLACK);
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		g2.draw(_rect);

		// 改造 只需简单改变 就可以解决边界的问题!
		double[][] data = new double[colNum + 2][rowNum + 2];
		int len = dd.length;
		for (int i = 0; i < len; i++) {
			int x = i / rowNum;
			int y = i % rowNum;
			int t = dd[i];//dd[i] < 0 ? dd[i] + 256 : dd[i];

			// data[colNum - x][rowNum - y] = t; // 其余的都是0
			// 牙齿方位改变 上面公式不适合
			data[x + 1][y + 1] = t; // 其余的都是0
		}

		Triangulate t = new Triangulate(data);
		Set<java.lang.Double> vals = t.getValues();
		List<Triangle> traingles = t.getTriangles();

		List<List<List<Point1>>> lists = new ArrayList<List<List<Point1>>>();
		RenderContour rc = new RenderContour(traingles);

		// long l1 = System.currentTimeMillis();
		for (double val : vals) {
			List<List<Point1>> points = rc.getContour(val);
			lists.add(points);
		}
		// long l2 = System.currentTimeMillis();
		// System.out.println(l2-l1);

		g.setColor(Color.BLACK);
		Stroke s = g2.getStroke();
		for (List<List<Point1>> points : lists) {
			for (List<Point1> pos : points) {
				Path2D path = new Path2D.Double();
				int k = 15;
				// List<Point1> ps = B2Spline(pos,k);
				List<Point1> ps = B3Spline(pos, k);
				// int piexel = 15;
				path.moveTo(ps.get(0).x * pixel, ps.get(0).y * pixel);
				for (int i = 1; i < ps.size(); i++) {
					Point1 p = ps.get(i);
					path.lineTo(p.x * pixel, p.y * pixel);
				}
				path.closePath();

				int val = (int) Math.round(pos.get(0).z);

				Color color;
				/*
				 * if (val == 0) color = MyColor.q.getRgb();
				 * else if(val == 255) color =
				 * MyColor.q.getRgb(); else {
				 */
				int v = val >> 4;
				color = MyColor.values()[v].getRgb();

				// }
				g2.setColor(color);
				g2.fill(path);
				// g.setColor(Color.BLACK);
				// g.draw(path);

			}
		}
		g2.setStroke(s);
		g2.setColor(oldColor);

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
		//System.out.println(e);
		if (info.getTag().equals(ActionConstants.DATA_COLLECT_DATA)) {
			//System.out.println(recvdata);
			recvdata = (int[]) info.getObject();
			repaint();
			if (isEntering) {
				float pixel = CalcPixelByWhichSide();
				Point point = getMousePosition();

				int i1 = (int) Math.round(point.getX() / pixel);
				int i2 = (int) Math.round(point.getY() / pixel);
				int[] ist = new int[3];
				int[] is = InWhichBox(i1, i2, point.getX(), point.getY(), pixel);
				ist[0] = is[0];
				ist[1] = is[1];
				int i = Constants.SENSOR_HEIGHT * ist[0] + ist[1];
				ist[2] = recvdata[i];
				firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, ist);
			}
		}

	}

	private List<Point1> B3Spline(List<Point1> pos, int k) {
		if (pos.size() <= 4)
			return pos;
		List<Point1> points = new ArrayList<Point1>();
		Point1 _p1 = pos.get(0);
		Point1 _p2 = pos.get(1);
		Point1 _p3 = pos.get(2);
		points.add(new Point1((_p1.x + 4 * _p2.x + _p3.x) / 6.0, (_p1.y + 4 * _p2.y + _p3.y) / 6.0, 0));
		for (int i = 1; i < pos.size() - 2; i++) {
			for (int j = 0; j < k; j++) {
				double t = j * 1.0 / k;
				Point1 p1 = pos.get(i - 1);
				Point1 p2 = pos.get(i);
				Point1 p3 = pos.get(i + 1);
				Point1 p4 = pos.get(i + 2);

				double x = ((-p1.x + 3 * p2.x - 3 * p3.x + p4.x) * t * t * t + (3 * p1.x - 6 * p2.x + 3 * p3.x) * t * t + (-3 * p1.x + 3 * p3.x) * t + (p1.x + 4 * p2.x + p3.x)) / 6.0;
				double y = ((-p1.y + 3 * p2.y - 3 * p3.y + p4.y) * t * t * t + (3 * p1.y - 6 * p2.y + 3 * p3.y) * t * t + (-3 * p1.y + 3 * p3.y) * t + (p1.y + 4 * p2.y + p3.y)) / 6.0;

				Point1 pp = new Point1(x, y, 0);
				points.add(pp);
			}
		}
		return points;
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
		if (!_rect.contains(point)) {
			isEntering = false;
			//用来判断离开
			firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
			return;
		}
if(recvdata==null) return;
		int i1 = Math.round(e.getX() / pixel);
		int i2 = Math.round(e.getY() / pixel);
		int[] ist = new int[3];
		int[] is = InWhichBox(i1, i2, e.getX(), e.getY(), pixel);
		ist[0] = is[0];
		ist[1] = is[1];
		int i = Constants.SENSOR_HEIGHT * ist[0] + ist[1];
		ist[2] = recvdata[i];
		firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, ist);
		isEntering = true;
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
	private int[] InWhichBox(int x, int y, double m, double n, float pixel) {
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
		float pixel = CalcPixelByWhichSide();
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		Point point = e.getPoint();
		if (_rect.contains(point)) {
			isEntering = true;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		float pixel = CalcPixelByWhichSide();
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		Point point = e.getPoint();
		if (!_rect.contains(point)) {
			firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
			isEntering = false;
		}
	}

}
