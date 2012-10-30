package com.ciotc.runmo;

import static com.ciotc.runmo.util.I18N.getString;
import static com.ciotc.runmo.util.USBLock.LOCK;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.runmo.component.tabcomponent.TabComponent;
import com.ciotc.runmo.component.tabcomponent.impl.RecordComponent;
import com.ciotc.runmo.component.tabcomponent.impl.ViewComponent;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.teemo.usbdll.USBDLL;

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

	boolean record_component_exist = false;

	MainTabPane() {
		setName("MainTabPane");
		addChangeListener(this);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * 创建新影片
	 */
	public void newRecordComponent() {
		TabComponent recordComp = new RecordComponent();
		recordComp.addPropertyChangeListener(this);
		recordComp.open();
	}

	/**
	 * 打开一个新影片
	 * @param path 影片的路径
	 */
	public void openRecordComponent(String path) {
		ViewComponent viewComp = new ViewComponent(path);
		final Cursor cursor = getCursor();
		if (!viewComp.readDataFromFile()) {
			JOptionPane.showMessageDialog(this, getString("ViewComponent.readFail"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);
			setCursor(cursor);
			return;
		}
		addTabComponent(viewComp);
		viewComp.addPropertyChangeListener(this);
		viewComp.open();
		setCursor(cursor);
	}

	/**
	 * 关闭录制影片
	 */
	public void closeRecordComponent() {
		RecordComponent comp = findRecordComponent();
		if (comp != null)
			comp.close();
	}

	private RecordComponent findRecordComponent() {
		int count = getTabCount();
		for (int i = count - 1; i >= 0; i--) {
			Component comp = getComponentAt(i);
			if (comp != null) {
				if (comp instanceof RecordComponent) {
					return (RecordComponent) comp;
				}
			}
		}
		return null;
	}

	public void removeTabComponent(TabComponent comp) {
		int index = indexOfComponent(comp);
		if (index != -1) {
			removeTabAt(index);
		}
	}

	public void addTabComponent(final TabComponent comp) {
		addTab(comp.getTitle(), comp);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel imageLabel = new JLabel(TITLE_ICON);
		JLabel titleLabel = new JLabel(comp.getTitle());
		JLabel closeLabel = new JLabel(CLOSE_ICON);
		closeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				comp.triggerClose();
			}
		});
		panel.add(imageLabel, BorderLayout.WEST);
		panel.add(titleLabel, BorderLayout.CENTER);
		panel.add(closeLabel, BorderLayout.EAST);

		setTabComponentAt(indexOfComponent(comp), panel);
		setSelectedIndex(indexOfComponent(comp));
	}

	public void closeAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Component comp = getSelectedComponent();
		firePropertyChange(ActionConstants.CURRENT_COMPONENT_CHANGE, null, comp);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_OPEN)) {
			TabComponent comp = (TabComponent) evt.getNewValue();
			addTabComponent(comp);
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, record_component_exist, true);
			record_component_exist = true;
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_CLOSE)) {
			TabComponent comp = (TabComponent) evt.getNewValue();
			removeTabComponent(comp);
			firePropertyChange(ActionConstants.RECORD_COMPONENT_EXIST, record_component_exist, false);
			record_component_exist = false;
			int count = getTabCount();
			if (count <= 0)
				firePropertyChange(ActionConstants.ALL_COMPONENT_CLOSE, false, true);

		} /*else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_OPEN)) {
			TabComponent comp = (TabComponent) evt.getNewValue();
			addTabComponent(comp);
		} */else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_CLOSE)) {
			TabComponent comp = (TabComponent) evt.getNewValue();
			firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, false, true);
			removeTabComponent(comp);
			int count = getTabCount();
			if (count <= 0)
				firePropertyChange(ActionConstants.ALL_COMPONENT_CLOSE, false, true);

		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_STATUS)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_STATUS, evt.getOldValue(), evt.getNewValue());

		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_DATA_LEN)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_DATA_LEN, evt.getOldValue(), evt.getNewValue());

		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MODEL)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		} else if (evt.getPropertyName().equals(ActionConstants.VIEW_COMPONENT_OPEN_ERROR)) {
			ViewComponent comp = (ViewComponent) evt.getNewValue();
			comp.removePropertyChangeListener(this);
		} else if (evt.getPropertyName().equals(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.RECORD_COMPONENT_MOUSE_MOVE, evt.getOldValue(), evt.getNewValue());
		}
	}

}
