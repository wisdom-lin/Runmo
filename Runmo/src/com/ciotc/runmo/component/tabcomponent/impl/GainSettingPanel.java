package com.ciotc.runmo.component.tabcomponent.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.runmo.setting.Settings;
import com.ciotc.runmo.util.I18N;
import com.ciotc.runmo.util.Util;
import com.ciotc.teemo.usbdll.USBDLL;

public class GainSettingPanel extends JPanel /*implements ChangeListener, ItemListener */{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	//private JComboBox jComboBox;
	private JSlider jSlider1;
	private JSlider jSlider2;
	private javax.swing.JLabel powaTitle;
	private javax.swing.JLabel gainTitle;
	private javax.swing.JLabel powaText;
	private javax.swing.JLabel gainText;
	private javax.swing.JLabel valueText;
	private javax.swing.JLabel valueTitle;

	/**value**/
	int powa;
	int gain;

	Settings set = Settings.getInstance();
	{
		System.out.println(set);
	}

	public GainSettingPanel() {
		setLayout(new BorderLayout());
		add(createPanel());
		load();
	}

	//@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createPanel() {
		JPanel panel = new JPanel();

		//jComboBox = new JComboBox();
		jSlider2 = new JSlider(0, 7);
		jSlider1 = new JSlider(0, 199);
		powaTitle = new javax.swing.JLabel();
		gainTitle = new javax.swing.JLabel();
		powaText = new javax.swing.JLabel();
		gainText = new javax.swing.JLabel();
		valueText = new javax.swing.JLabel();
		valueTitle = new javax.swing.JLabel();

		//jComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "4", "5", "8", "10", "16", "32" }));
		jSlider1.addChangeListener(new StateChanger1());
		jSlider2.addChangeListener(new StateChanger2());
		jSlider1.setMinorTickSpacing(1);
		jSlider2.setMinorTickSpacing(1);
		//jComboBox.addItemListener(this);

		powaTitle.setText(I18N.getString("TabComponent.GainSettingPanel.powA"));

		gainTitle.setText(I18N.getString("TabComponent.GainSettingPanel.gain"));

		powaText.setText("");

		gainText.setText("");

		valueText.setText("");

		valueTitle.setText(I18N.getString("TabComponent.GainSettingPanel.value"));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
		panel.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addGap(21, 21, 21)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(powaTitle).addComponent(gainTitle).addComponent(valueTitle))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jSlider1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(valueText).addComponent(gainText).addComponent(powaText))
						.addContainerGap(24, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addComponent(powaText).addGap(18, 18, 18).addComponent(gainText))
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(powaTitle))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(gainTitle))))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(valueText).addComponent(valueTitle))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		return panel;
	}

	void load() {
		powa = set.getPowa();//pref.getInt("powa", 100);
		gain = set.getGain();//pref.getInt("gain", 0);
		System.out.println("powa:" + powa + ";" + "gain:" + gain);
		jSlider1.setValue(powa);
		jSlider2.setValue(gain);
		//jComboBox.setSelectedIndex(gain);
		//gainText.setText(USBDLL.GAINS[gain] + "");
	}

	void apply1() {
		set.setPowa(powa);
	}

	void apply2() {
		set.setGain(gain);
	}

	class StateChanger1 implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			powa = jSlider1.getValue();
			powaText.setText(powa + "");
			valueText.setText(powa * USBDLL.GAINS[gain] + "");
			apply1();
		}

	}

	class StateChanger2 implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			gain = jSlider2.getValue();
			gainText.setText(USBDLL.GAINS[gain] + "");
			valueText.setText(powa * USBDLL.GAINS[gain] + "");
			apply2();
		}
	}

	public static void main(String[] args) {
		new JFrame() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{

				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						Util.writeSetting();
					}
				});

				Util.readSetting();
				GainSettingPanel gif = new GainSettingPanel();

				add(gif);

				setSize(600, 600);
				setVisible(true);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		};
	}

}
