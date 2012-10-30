package com.ciotc.runmo.component.tabcomponent.impl;

import static com.ciotc.runmo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.runmo.util.Constants.SENSOR_NUM;
import static com.ciotc.runmo.util.Constants.SENSOR_WIDTH;

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
import com.ciotc.runmo.component.tabcomponent.ChangeInfo;
import com.ciotc.runmo.component.tabcomponent.View;
import com.ciotc.runmo.component.tabcomponent.paint.MyColor;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.runmo.util.Constants;

public class RecordView extends View implements MouseMotionListener, MouseInputListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int rowNum = Constants.SENSOR_HEIGHT;
	public final static int colNum = Constants.SENSOR_WIDTH;
	/**
	 * 1表示2D
	 * 2表示轮廓
	 * 3表示3D
	 */
	volatile int index = 1;

	boolean isEntering = false;

	//绘图的常数变量
	int zoomx;
	int zoomy;
	int shiftx;
	int shifty;
	int dir; //方向
	//缩放的比例
	float unit; //10个像素
	float zoomh;
	float lean;

	Point points[][] = new Point[Constants.SENSOR_HEIGHT + 1][Constants.SENSOR_WIDTH + 1];

	public RecordView() {
		constructPointsAndParameter();
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	/**
	 * 构造坐标
	 * @param data
	 */
	public void constructPointsAndParameter() {
		zoomx = 6;//缩放比例
		zoomy = 5;

		shiftx = 50;
		shifty = 200; //300

		dir = 0;

		unit = 1; //10个像素
		zoomh = 0.7f; //1

		lean = 0.5f; //0.5773;

		int i, j;

		for (i = 0; i <= rowNum; i++) {
			for (j = 0; j <= colNum; j++) {
				points[i][j] = new Point(j, i);
				points[i][j].x = (int) ((points[i][j].x * 1.0 + points[i][j].y * lean * 1.0) * zoomx * unit * 1.0);

				points[i][j].y *= zoomy * unit;
				points[i][j].x += shiftx;
				points[i][j].y += shifty;

			}

		}
	}

	public void drawFrame3D(Graphics g, int[] dd) {
		if (dd == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		Color oldColor = g.getColor();

		//统一用宽度改变
		float pixel = CalcPixelByWhichSide();

		//画边框
		g2.setColor(Color.BLACK);
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		g2.draw(_rect);

		if (dd == null) {
			g2.setColor(oldColor);
			return;
		}

		int width = (int) (pixel * SENSOR_WIDTH);

		unit = (float) (width * 0.1 / colNum);
		zoomh = (float) (width * 0.07 / colNum);
		shifty = (int) (width * 20.0 / colNum);
		if (dir == 0 || dir == 2) {
			shiftx = (int) (width * 5.0 / colNum);
		} else if (dir == 1 || dir == 3) {
			shiftx = (int) (width * 15.0 / colNum);
		}

		//reconstruct points
		int i, j;
		if (dir == 0 || dir == 2)
			for (i = 0; i <= rowNum; i++)
				for (j = 0; j <= colNum; j++) {
					points[i][j].x = j;
					points[i][j].y = i;

					points[i][j].x = (int) ((points[i][j].x * 1.0 + points[i][j].y * lean * 1.0) * zoomx * unit * 1.0);

					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;
				}
		else if (dir == 1 || dir == 3)
			for (i = 0; i <= rowNum; i++)
				for (j = 0; j <= colNum; j++) {
					points[i][j].x = j;
					points[i][j].y = i;

					points[i][j].x = (int) ((points[i][j].x * 1.0 - points[i][j].y * lean * 1.0) * zoomx * unit * 1.0);

					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;

				}

		//int i,j;
		if (dir == 0) {
			int totalNum = rowNum * colNum;
			for (i = 0; i < rowNum; i++)
				for (j = colNum - 1; j >= 0; j--)
					//for(j = 0;j<colNum;j++)
					//drawCube(i,j,dd[i*colNum+j],g);
					drawCube(i, j, dd[totalNum - 1 - (j * rowNum + i)], g);
		}
		if (dir == 1) {
			int totalNum = rowNum * colNum;
			for (i = 0; i < rowNum; i++)
				//for(j = 0;j<colNum;j++)
				for (j = 0; j < colNum; j++)
					drawCube(i, j, dd[totalNum - 1 - (j * rowNum + i)], g);
		}

		if (dir == 2) {
			for (i = 0; i < rowNum; i++)
				for (j = colNum - 1; j >= 0; j--)
					drawCube(i, j, dd[j * rowNum + i], g);
		}

		if (dir == 3) {
			for (i = 0; i < rowNum; i++)
				for (j = 0; j < colNum; j++)
					drawCube(i, j, dd[j * rowNum + i], g);
		}
		g.setColor(oldColor);
	}

	/**
	 * 画一个立方体
	 * @param i x坐标
	 * @param j y坐标
	 * @param b 数值
	 * @param g 画笔
	 */
	private void drawCube(int i, int j, int b, Graphics g) {
		// TODO Auto-generated method stub
		int dataColor = b;

		if (dataColor == 0) //48 before
			return;
//		if (dataColor < 0)   
//			dataColor += 256;

		int x_1, y_1, x_2, y_2, x_3, y_3, x_4, y_4, x_5, y_5, x_6, y_6, x_7, y_7, x_8, y_8;

		x_2 = points[i][j].x;
		y_2 = points[i][j].y;
		x_1 = points[i][j + 1].x;
		y_1 = points[i][j + 1].y;
		x_3 = points[i + 1][j].x;
		y_3 = points[i + 1][j].y;
		x_4 = points[i + 1][j + 1].x;
		y_4 = points[i + 1][j + 1].y;

		x_5 = x_1;
		y_5 = (int) (y_1 - dataColor * zoomh);
		x_6 = x_2;
		y_6 = (int) (y_2 - dataColor * zoomh);
		x_7 = x_3;
		y_7 = (int) (y_3 - dataColor * zoomh);
		x_8 = x_4;
		y_8 = (int) (y_4 - dataColor * zoomh);

		//开始绘图

		Color color = null;
		if (dataColor == Constants.SENSOR_MAX_VALUE)
			color = MyColor.values()[Constants.SENSOR_COLOR_NUM].getRgb();
		else {
			int in = dataColor >>> Constants.SENSOR_NUM_PER_COLOR;
			color = MyColor.values()[in].getRgb();
		}

		//改进画法
		int ptx1[] = new int[6];
		int pty1[] = new int[6];
		Color oldColor = g.getColor();
		g.setColor(color);
		if (dir == 1 || dir == 3) {

			ptx1[0] = x_1;
			pty1[0] = y_1;
			ptx1[1] = x_5;
			pty1[1] = y_5;
			ptx1[2] = x_6;
			pty1[2] = y_6;
			ptx1[3] = x_7;
			pty1[3] = y_7;
			ptx1[4] = x_3;
			pty1[4] = y_3;
			ptx1[5] = x_4;
			pty1[5] = y_4;

			g.fillPolygon(ptx1, pty1, 6);

			g.setColor(new Color(0, 0, 0));
			g.drawPolygon(ptx1, pty1, 6);
			g.drawLine(x_8, y_8, x_7, y_7);
			g.drawLine(x_8, y_8, x_5, y_5);
			g.drawLine(x_8, y_8, x_4, y_4);

		} else if (dir == 0 || dir == 2) {

			ptx1[0] = x_2;
			pty1[0] = y_2;
			ptx1[1] = x_3;
			pty1[1] = y_3;
			ptx1[2] = x_4;
			pty1[2] = y_4;
			ptx1[3] = x_8;
			pty1[3] = y_8;
			ptx1[4] = x_5;
			pty1[4] = y_5;
			ptx1[5] = x_6;
			pty1[5] = y_6;

			g.fillPolygon(ptx1, pty1, 6);

			g.setColor(new Color(0, 0, 0));
			g.drawPolygon(ptx1, pty1, 6);
			g.drawLine(x_7, y_7, x_6, y_6);
			g.drawLine(x_7, y_7, x_8, y_8);
			g.drawLine(x_7, y_7, x_3, y_3);

		}
		g.setColor(oldColor);
	}

	public void drawFrame2D(Graphics g2, int[] dd) {
		if (dd == null)
			return;
		Color oldColor;

		Graphics2D g = (Graphics2D) g2;
		oldColor = g.getColor();
		// 背景为灰色
//		g.setColor(Color.LIGHT_GRAY);

//		g.fillRect(0, 0, getWidth(), getHeight());

		
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

			if (f == Constants.SENSOR_MAX_VALUE)
				color = MyColor.values()[Constants.SENSOR_COLOR_NUM].getRgb();
			else if (f == Constants.SENSOR_MIN_VALUE) {
				continue;
			} else {
				int in = f >>> Constants.SENSOR_NUM_PER_COLOR;
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

	public void drawFrameContour(Graphics g, int[] dd) {
		if (dd == null)
			return;
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
				int v = val >> Constants.SENSOR_NUM_PER_COLOR;
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

	public void drawFrame(Graphics g2, int[] dd) {
		switch (index) {
		case 1:
			drawFrame2D(g2, dd);
			break;
		case 2:
			drawFrameContour(g2, dd);
			break;
		case 3:
			drawFrame3D(g2, dd);
			break;
		default:
			break;
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
			if (index == 1 || index == 2) {
				if (isEntering) {
					float pixel = CalcPixelByWhichSide();
					Point point = getMousePosition();
					if (point == null) {
						//System.out.println("point null");
						return;
					}
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
		} else if (info.getTag().equals(ActionConstants.RECORD_VIEW_INDEX)) {
			index = (int) info.getObject();
			repaint();
		}

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
		if (recvdata == null)
			return;
		if (index == 1 || index == 2) {
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
		}else if(index==3){
			firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
		}
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
		if (index == 3) {

			float pixel = CalcPixelByWhichSide();
			Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
			Point point = e.getPoint();
			if (!_rect.contains(point))
				return;

			dir++;
			dir = dir % 4;

			switch (dir) {
			case 0:
				shiftx /= 3.0; //原来是4
				break;
			case 1:
				shiftx *= 3.0;
				break;
			case 2:
				shiftx /= 3.0;
				break;
			case 3:
				shiftx *= 3.0;
				break;
			default:
				break;
			}
			int i, j;
			if (dir == 0 || dir == 2)
				for (i = 0; i <= rowNum; i++)
					for (j = 0; j <= colNum; j++) {
						points[i][j].x = j;
						points[i][j].y = i;

						points[i][j].x = (int) ((points[i][j].x * 1.0f + points[i][j].y * lean * 1.0f) * zoomx * unit * 1.0f);

						points[i][j].y *= zoomy * unit;
						points[i][j].x += shiftx;
						points[i][j].y += shifty;
					}

			else if (dir == 1 || dir == 3)
				for (i = 0; i <= rowNum; i++)
					for (j = 0; j <= colNum; j++) {
						points[i][j].x = j;
						points[i][j].y = i;

						points[i][j].x = (int) ((points[i][j].x * 1.0f - points[i][j].y * lean * 1.0f) * zoomx * unit * 1.0f);

						points[i][j].y *= zoomy * unit;
						points[i][j].x += shiftx;
						points[i][j].y += shifty;

					}
			repaint();

		}
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
		if (index == 1 || index == 2) {
			float pixel = CalcPixelByWhichSide();
			Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
			Point point = e.getPoint();
			if (_rect.contains(point)) {
				isEntering = true;
			}
		} else if (index == 3) {
			firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (index == 1 || index == 2) {
			float pixel = CalcPixelByWhichSide();
			Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
			Point point = e.getPoint();
			if (!_rect.contains(point)) {
				firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
				isEntering = false;
			}
		} else if (index == 3) {
			firePropertyChange(ActionConstants.RECORD_VIEW_MOUSE_MOVE, null, new int[] { Integer.MIN_VALUE, 0, 0 });
		}
	}

}
