package com.ciotc.runmo.setting;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ciotc.runmo.util.Util;

import static com.ciotc.runmo.util.I18N.*;
import static com.ciotc.runmo.util.Util.*;

public class SettingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton cancelButton;
	private JButton okButton;
	private JButton recordButton;
	private JButton UserButton;
	private JButton languageButton;
	private JButton defaultButton;
	private JButton applyButton;
	private Component currentComponent;
	private MainPanel mainPanel;
	private RecordSettingPanel recordPanel;
//	private NetworkPanel networkPanel;
	private ViewSettingPanel viewPanel;
	private LanguagePanel languagePanel;

	public SettingDialog() {
		setName("SettingDialog");
		setTitle(getString("SettingDialog.title"));
		JPanel panel = null;
		mainPanel = new MainPanel();
		add(mainPanel, BorderLayout.CENTER);
		panel = createButtonPanel();
		add(panel, BorderLayout.WEST);
		panel = createBottomPanel();
		add(panel, BorderLayout.SOUTH);

		mainPanel.show(recordPanel);
		//pack();
		setSize(500,350);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void addListener(PropertyChangeListener listener){
		viewPanel.addPropertyChangeListener(listener);
		languagePanel.addPropertyChangeListener(listener);
	}
	
	public void removeListener(PropertyChangeListener listener){
		viewPanel.removePropertyChangeListener(listener);
		languagePanel.removePropertyChangeListener(listener);
	}
	
	class MainPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		void show(Component component) {
			if (currentComponent != null) {
				remove(currentComponent);
			}
			add("Center", currentComponent = component);
			revalidate();
			repaint();
		}

		public MainPanel() {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			add(createMainSouthPanel(), BorderLayout.SOUTH);
		}
		
		private JPanel createMainSouthPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.add(Box.createHorizontalGlue());
			defaultButton = new JButton(getString("SettingDialog.default"));
			applyButton = new JButton(getString("SettingDialog.apply"));
						
			defaultButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(currentComponent == recordPanel){
						recordPanel.setDefault();
					}else if(currentComponent == viewPanel){
						viewPanel.setDefault();
					}else if(currentComponent == languagePanel){
						languagePanel.setDefault();
					}
				}
			});
			applyButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(currentComponent == recordPanel){
						recordPanel.apply();
					}else if(currentComponent == viewPanel){
						viewPanel.apply();
					}else if(currentComponent == languagePanel){
						languagePanel.apply();
					}
				}
			});
			
			
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			panel.add(applyButton);
			panel.add(Box.createRigidArea(new Dimension(10, 0)));
			panel.add(defaultButton);

			return panel;

		}
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		recordButton = new JButton(getString("SettingDialog.RecordSettingPanel.title"),getResizableIconFromResource(SettingDialog.class, "images/setting1.png"));
		UserButton = new JButton(getString("SettingDialog.ViewSettingPanel.title"),getResizableIconFromResource(SettingDialog.class, "images/setting3.png"));
		languageButton = new JButton(getString("SettingDialog.LanguagePanel.title"),getResizableIconFromResource(SettingDialog.class, "images/language.png"));
		
		recordButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		UserButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		languageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		recordButton.setHorizontalTextPosition(SwingConstants.CENTER);
		UserButton.setHorizontalTextPosition(SwingConstants.CENTER);
		languageButton.setHorizontalTextPosition(SwingConstants.CENTER);
		recordButton.setFocusPainted(false);
		UserButton.setFocusPainted(false);
		languageButton.setFocusPainted(false);
		
		recordPanel = new RecordSettingPanel();
		viewPanel = new ViewSettingPanel();
		languagePanel = new LanguagePanel();
		recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(recordPanel);
			}
		});
		UserButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(viewPanel);
			}
		});
		languageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.show(languagePanel);
			}
		});
		
		panel.add(recordButton);
		//panel.add(UserButton);
		//panel.add(languageButton);

		return panel;
	}

	private JPanel createBottomPanel() {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPane.add(Box.createHorizontalGlue());
		cancelButton = new JButton(getString("SettingDialog.cancel"));
		okButton = new JButton(getString("SettingDialog.ok"));

		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recordPanel.apply();
				viewPanel.apply();
				languagePanel.apply();
				dispose();
				
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(okButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		return buttonPane;
	}

	public static void main(String[] args) {
		Util.readSetting();
		
		SettingDialog sd = new SettingDialog();
		sd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Util.writeSetting();
			}
		});
		sd.setVisible(true);
		sd.addListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getPropertyName());
			}
		});
		sd.setLocationRelativeTo(null);
		sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		sd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}
}
