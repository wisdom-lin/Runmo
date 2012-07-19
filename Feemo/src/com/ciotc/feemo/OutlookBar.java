package com.ciotc.feemo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.ciotc.feemo.component.outlookbar.JOutlookBar;
import com.ciotc.feemo.component.outlookbar.panel.HomePanel;
import com.ciotc.feemo.component.outlookbar.panel.RecordPanel;
import com.ciotc.feemo.component.outlookbar.panel.ViewPanel;

public class OutlookBar extends JOutlookBar implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Context context;

	public OutlookBar() {
		this(null);
	}

	public OutlookBar(Context context) {
		this.context = context;
		setTabPlacement(JTabbedPane.LEFT);

		HomePanel home = new HomePanel(this, context);
		addTab(home.getTitle(), makeScrollPane(home));

		RecordPanel record = new RecordPanel(this);
		addTab(record.getTitle(), makeScrollPane(record));

		ViewPanel view = new ViewPanel(this);
		addTab(view.getTitle(), makeScrollPane(view));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new OutlookBar());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
