package com.ciotc.feemo;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.feemo.component.tabcomponent.TabComponent;
import com.ciotc.feemo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.feemo.component.tabcomponent.impl.ViewComponent;

public class MainTabPane extends JTabbedPane implements ChangeListener, PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * tab栏的图标
	 */
	public static final Icon TITLE_ICON = new ImageIcon(TabComponent.class.getResource("images/view_s.png"));
	public static final Icon CLOSE_ICON = new ImageIcon(TabComponent.class.getResource("images/close_s.png"));

	//List<TabComponent> components = new ArrayList<TabComponent>();

	MainTabPane() {
		addChangeListener(this);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * 创建新影片
	 */
	public void newRecordComponent() {
		TabComponent recordComp = new RecordComponent();
		recordComp.addPropertyChangeListener(this);

		//components.add(recordComp);
		addTab(recordComp.getTitle(), recordComp);
		JPanel panel = createTitlePanel(recordComp.getTitle(),recordComp);
		setTabComponentAt(indexOfComponent(recordComp), panel);		
		setSelectedIndex(indexOfComponent(recordComp));
	}

	/**
	 * 打开一个新影片
	 * @param path 影片的路径
	 */
	public void openRecordComponent(String path) {
		TabComponent viewComp = new ViewComponent(path);
		viewComp.addPropertyChangeListener(this);

		//components.add(viewComp);
		addTab(viewComp.getTitle(), viewComp);
		JPanel panel = createTitlePanel(viewComp.getTitle(),viewComp);
		setTabComponentAt(indexOfComponent(viewComp), panel);		
		setSelectedIndex(indexOfComponent(viewComp));

	}

	/**
	 * 创建TabComponent的抬头
	 * @param title
	 * @param comp
	 * @return
	 */
	private JPanel createTitlePanel(String title, final TabComponent comp) {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel imageLabel = new JLabel(TITLE_ICON);
		JLabel titleLabel = new JLabel(title);
		JLabel closeLabel = new JLabel(CLOSE_ICON);
		closeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		panel.add(imageLabel, BorderLayout.WEST);
		panel.add(titleLabel, BorderLayout.CENTER);
		panel.add(closeLabel, BorderLayout.EAST);
		return panel;
	}

	public void closeRecordComponent(TabComponent recordComp) {
		int index = indexOfComponent(recordComp);
		if (index != -1) {
			removeTabAt(index);
		}
	}

	public void closeViewComponent(ViewComponent viewComp) {
		int index = indexOfComponent(viewComp);
		if (index != -1) {
			removeTabAt(index);
		}
	}

	public void closeAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}

}
