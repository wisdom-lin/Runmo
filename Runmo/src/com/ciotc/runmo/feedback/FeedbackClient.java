package com.ciotc.runmo.feedback;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ciotc.runmo.util.I18N;

public class FeedbackClient extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel label = new JLabel(I18N.getString("FeedbackClient.tips"));
	JTextArea message = new JTextArea(10, 40);
	JScrollPane jsp = new JScrollPane(message);
	JButton sendButton = new JButton(I18N.getString("FeedbackClient.send"));
	JButton clearButton = new JButton(I18N.getString("FeedbackClient.clear"));

	public FeedbackClient(Frame frame, boolean bool) {
		super(frame, bool);
		setTitle("FeedbackClient.title");
		add(label, BorderLayout.NORTH);

		add(jsp, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.add(sendButton);
		panel.add(clearButton);
		add(panel, BorderLayout.SOUTH);
		setLocationRelativeTo(frame);
		pack();
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void clear() {
		message.setText("");
	}

	public void send() {
		

	}

	public static void main(String[] args) {
		FeedbackClient client = new FeedbackClient(null, false);
		client.setVisible(true);
	}
}
