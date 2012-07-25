package com.ciotc.feemo.component.outlookbar;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;

public class TestOutlookBar extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestOutlookBar() {
		add(makeOutlookPanel(SwingConstants.LEFT));
		setSize(500, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		new TestOutlookBar();
	}

	JPanel makeOutlookPanel(int alignment) {
		JOutlookBar outlook = new JOutlookBar();
		outlook.setTabPlacement(JTabbedPane.LEFT);
		addTab(outlook, "Folders");
		addTab(outlook, "Backup");

		// show it is possible to add any component to the bar
		JTree tree = new JTree();
		outlook.addTab("A JTree", outlook.makeScrollPane(tree));

		outlook.addTab("Disabled", new JButton());
		//outlook.setEnabledAt(3, false);
		outlook.setAllTabsAlignment(alignment);

		JPanel panel = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 3));
		panel.add(outlook, "100");
		return panel;
	}

	void addTab(JOutlookBar tabs, String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new PercentLayout(PercentLayout.VERTICAL, 0));
		panel.setOpaque(false);

		String[] buttons = new String[] { "Inbox", "icons/outlook-inbox.gif", "Outbox", "icons/outlook-outbox.gif", "Drafts", "icons/outlook-inbox.gif", "Templates", "icons/outlook-inbox.gif", "Deleted Items", "icons/outlook-trash.gif", };

		for (int i = 0, c = buttons.length; i < c; i += 2) {
			JButton button = new JButton(buttons[i]);
			
			try {
				//button.setUI((ButtonUI) Class.forName((String) UIManager.get("BasicOutlookButtonUI")).newInstance());
				button.setUI(new BasicOutlookButtonUI());
				button.setBorder(BorderFactory.createLineBorder(Color.black));
			} catch (Exception e) {
				e.printStackTrace();
			}
//		      button.setIcon(new ImageIcon(OutlookBarMain.class
//		        .getResource(buttons[i + 1])));
			panel.add(button);
		}
		tabs.add(panel,title);
	}
}
