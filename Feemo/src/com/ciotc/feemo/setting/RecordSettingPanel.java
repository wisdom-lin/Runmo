package com.ciotc.feemo.setting;

import static com.ciotc.feemo.util.I18N.getString;
import static com.ciotc.feemo.setting.SettingConstants.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RecordSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;

	Settings set = Settings.getInstance();

	protected RecordSettingPanel() {

		initComponents();
	}

	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField(6);
		jTextField2 = new javax.swing.JTextField(6);
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();

		//ok = new javax.swing.JButton();
		//cancel = new javax.swing.JButton();
		//advance = new javax.swing.JButton();

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(getString("RecordSettingDialog.border1")));

		jLabel1.setText(getString("SettingDialog.RecordSettingPanel.title1"));

		jLabel2.setText(getString("SettingDialog.RecordSettingPanel.title2"));

		jLabel3.setText(getString("SettingDialog.RecordSettingPanel.explain1"));

		jLabel4.setText(getString("SettingDialog.RecordSettingPanel.explain2"));

		jPanel1.setLayout(new GridLayout(2, 1));
		JPanel jp = new JPanel(new BorderLayout());
		JPanel jp2 = new JPanel();
		jp2.add(jLabel1);
		jp2.add(jTextField1);
		jp2.add(jLabel3);
		jp.add(jp2, BorderLayout.WEST);
		jPanel1.add(jp);
		jp = new JPanel(new BorderLayout());
		jp2 = new JPanel();
		jp2.add(jLabel2);
		jp2.add(jTextField2);
		jp2.add(jLabel4);
		jp.add(jp2, BorderLayout.WEST);
		jPanel1.add(jp);

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(getString("RecordSettingDialog.border2")));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel2Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGap(54, 54, 54)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)).addContainerGap()));

		setLayout(null);
		add(jPanel1);
		jPanel1.setBounds(10, 10, 320, 104);
//		add(jPanel2);
//		jPanel2.setBounds(10, 124, 320, 60);

		//addActionListener();

		addInputVerifier();
		load();

	}

	/**
	 * 将model中的数据显示在view上
	 */
	public void load() {
		int frames = set.getRecordFrame();///pref.getInt("recordFrame", FRAMES);
		float period = set.getRecordPeriod();//pref.getFloat("recordPeriod", PERIOD);

		jTextField1.setText(frames + "");
		jTextField2.setText(period + "");
	}

	/**
	 * 应用 即将view中的数据得到model中去
	 */
	public void apply() {
		int frames = Integer.parseInt(jTextField1.getText());
		float period = Float.parseFloat(jTextField2.getText());

		set.setRecordFrame(frames);
		set.setRecordPeriod(period);
	}

	/**
	 * 还原最原始的model值 并显示在view上
	 */
	public void setDefault() {
		int frames = FRAME_DEFAULT;
		float period = PEROID_DEFAULT;

		jTextField1.setText(frames + "");
		jTextField2.setText(period + "");

	}

	class MyInputVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
			JTextField jtf = (JTextField) input;
			try {

				if (jtf.getName().equals("jtf1")) {
					int val = Integer.parseInt(jtf.getText().trim());
					if (val < FRAME_MIN || val > FRAME_MAX) {
						showTipInformation(FRAME_MIN, FRAME_MAX);
						return false;
					}
				} else if (jtf.getName().equals("jtf2")) {
					float val = Float.parseFloat(jtf.getText().trim());
					if (val < PEROID_MIN || val > PEROID_MAX) {
						showTipInformation(PEROID_MIN, PEROID_MAX);
						return false;
					}
				}
			} catch (Exception e) {
				showTipInformation();
				return false;
			}
			return true;
		}

	}

	private void addInputVerifier() {
		MyInputVerifier miv = new MyInputVerifier();
		jTextField1.setInputVerifier(miv);
		jTextField1.setName("jtf1");
		jTextField2.setInputVerifier(miv);
		jTextField2.setName("jtf2");
	}

	/**
	 * 提示输入的数据不在最大和最小范围之内
	 * 
	 * @param maxValue
	 *                最大值
	 * @param minValue
	 *                最小值
	 */
	private void showTipInformation(int minValue, int maxValue) {
		JOptionPane.showMessageDialog(this, getString("SettingDialog.tipforErrorInputing2") + "  " + minValue + "~" + maxValue, getString("Feemo"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 提示输入的数据不在最大和最小范围之内
	 * 
	 * @param maxValue
	 *                最大值
	 * @param minValue
	 *                最小值
	 */
	private void showTipInformation(float minValue, float maxValue) {
		JOptionPane.showMessageDialog(this, getString("SettingDialog.tipforErrorInputing2") + "  " + minValue + "~" + maxValue, getString("Feemo"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 提示输入的数据格式不正确
	 */
	private void showTipInformation() {
		JOptionPane.showMessageDialog(this, getString("SettingDialog.tipforErrorInputing1"), getString("Feemo"), JOptionPane.ERROR_MESSAGE);
	}

}
