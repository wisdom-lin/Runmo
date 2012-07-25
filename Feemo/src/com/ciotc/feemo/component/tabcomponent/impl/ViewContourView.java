package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.feemo.util.Constants.SENSOR_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ciotc.contour.Point1;
import com.ciotc.contour.RenderContour;
import com.ciotc.contour.Triangle;
import com.ciotc.contour.Triangulate;
import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.Constants;
import com.ciotc.feemo.util.I18N;

public class ViewContourView extends ViewView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int rowNum = Constants.SENSOR_HEIGHT;
	public final static int colNum = Constants.SENSOR_WIDTH;

	public ViewContourView() {
		title = I18N.getString("ViewComponent.ViewContourView.title");
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
				int v = val >>Constants.SENSOR_NUM_PER_COLOR;
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
}
