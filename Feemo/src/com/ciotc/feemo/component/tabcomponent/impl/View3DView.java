package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.feemo.util.Constants.SENSOR_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.Constants;

public class View3DView extends ViewView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static int rowNum = Constants.SENSOR_HEIGHT;
	public final static int colNum = Constants.SENSOR_WIDTH;

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

	View3DView() {
		zoomx = 6;//缩放比例
		zoomy = 5;

		shiftx = 50;
		shifty = 200; //300

		dir = 0;

		unit = 1; //10个像素
		zoomh = 0.7f; //1

		lean = 0.5f; //0.5773;

		addMouseListener(new MyMouseAdapter());
		constructPoints();
	}

	/**
	 * 构造坐标
	 * @param data
	 */
	public void constructPoints() {
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

	public void drawFrame(Graphics g, int[] dd) {
		Graphics2D g2 = (Graphics2D) g;
		Color oldColor = g.getColor();

		//统一用宽度改变
		float pixel = CalcPixelByWhichSide();

		//画边框
		g2.setColor(Color.BLACK);
		Rectangle2D _rect = new Rectangle2D.Float(0, 0, pixel * SENSOR_WIDTH, pixel * SENSOR_HEIGHT);
		g2.draw(_rect);

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
		if (dataColor == 255)
			color = MyColor.values()[16].getRgb();
		else {
			int in = dataColor >>> 4;
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

	//响应鼠标事件
	class MyMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

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
}
