package com.ciotc.runmo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ciotc.runmo.config.Configurge;

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
		Configurge.open();
		
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
