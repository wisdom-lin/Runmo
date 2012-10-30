package com.ciotc.feemo.component.lengendtoolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.feemo.component.tabcomponent.paint.MyColor;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.I18N;
import com.citoc.feemo.config.Configurge;
/**
 * 事件监听和ThirdStatusBar类似
 * @author Linxiaozhi
 *
 */
public class LengendPanel extends JPanel implements ChangeListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ViewComponent comp = null;
	int showWhich = 0;

	JSlider slider = null;

	final static int WIDTH = 50;
	final static int HEIGHT = 480;
	final static int UNIT = 16;
	final static int ICON_WIDTH = WIDTH / 2;
	final static int ICON_HEIGHT = HEIGHT / UNIT;
	final static int ICON_HALF_HEIGHT = ICON_HEIGHT / 2;
	final static int MIN = 0;
	final static int MAX = 255;
	//int lower = 0; // it means old value
	int newLower = 0;

	class MyIcon implements Icon {
		BufferedImage bi = null;
		int val;

		/**
		 * 构造自己的图标 该图标只有颜色 根据颜色索引值来得到相应的颜色
		 * @param in 颜色索引值
		 */
		public MyIcon(int in) {
			this.val = in;
			if (in != 256) {
				bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_BGR);
			} else {
				bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT / 8, BufferedImage.TYPE_INT_BGR);
			}

			Graphics g2 = bi.getGraphics();
			Color color = null;
			//if (in != 0) {
			color = MyColor.values()[in >> 4].getRgb();
			//} else {
			//	color = Color.WHITE;
			//}
			g2.setColor(color);

			g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
			//	g2.fillRect(0, 0, halfWidth/2, height);
			//其他部分将它抹掉
			//g2.setColor(bgColor);
			//	g2.fillRect(halfWidth/2, 0, halfWidth, height);

		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (val != 256)
				g.drawImage(bi, x, y - ICON_HALF_HEIGHT, c);
			else
				g.drawImage(bi, x, y - ICON_HALF_HEIGHT + bi.getWidth(), c);

		}

		@Override
		public int getIconWidth() {
			return ICON_WIDTH;
		}

		@Override
		public int getIconHeight() {
			return ICON_HEIGHT;
		}

	}

	public LengendPanel() {

		String string = Configurge.getProperty(Configurge.FILTER_VAULE);
		int val = 0;
		try {
			val = Integer.parseInt(string.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//int index = 1;//Arrays.binarySearch(lowers, val);
		slider = new JSlider(JSlider.VERTICAL, MIN, MAX, val);
		slider.setFocusable(false);
		//slider.setInverted(true);
		slider.addChangeListener(this);
		slider.setMinorTickSpacing(1);

		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		for (int i = 0; i < slider.getMaximum(); i += UNIT)
			table.put(Integer.valueOf(i), new JLabel(new MyIcon(i)));
		table.put(Integer.valueOf(255), new JLabel(new MyIcon(256)));
		//slider.setLabelTable(slider.createStandardLabels(1));
		slider.setLabelTable(table);
		//slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		setLayout(new BorderLayout());
		//JLabel title = new JLabel(getResourceString("lengendDialog.title"));
		//title.setHorizontalTextPosition(SwingConstants.CENTER);
		//add(title,BorderLayout.NORTH);
		add(slider, BorderLayout.CENTER);
		//slider.setBackground(bgColor);
		//slider.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		//slider.setSize(345, 100);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		slider.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public static void main(String[] args) {
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}
		JFrame frame = new JFrame();

		LengendPanel lengend = new LengendPanel();
		frame.add(lengend);
		//frame.setLocationRelativeTo(null);
		frame.setLocation(500, 200);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//new LengendFrame(null);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		newLower = slider.getValue();
//System.out.println(newLower+","+showWhich);
Configurge.setProperty(Configurge.FILTER_VAULE, newLower+"");

		//	preferences.putInt(LOWER, newLower);
		//	firePropertyChange(ActionConstants.ALL_COMPONENT_FILTER_VALUE, lower, newLower);

		if (showWhich == 2) {
			comp.fresh();
		}

		//lower = newLower;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.CURRENT_COMPONENT_CHANGE)) {
			Component temp = (Component) evt.getNewValue();
			if (temp instanceof RecordComponent) {
				showWhich = 1;
			} else if (temp instanceof ViewComponent) {
				showWhich = 2;
				comp = (ViewComponent) temp;
			}
		}else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_EXIST)) {
			showWhich = 0;
		}else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			showWhich = 0;
		} 
	}

}