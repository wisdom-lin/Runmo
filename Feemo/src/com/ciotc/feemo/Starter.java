package com.ciotc.feemo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//一些加载
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.setVisible(true);
			}
		});

	}

}
