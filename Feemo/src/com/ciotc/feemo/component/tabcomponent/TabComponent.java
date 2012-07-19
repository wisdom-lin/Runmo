package com.ciotc.feemo.component.tabcomponent;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 继承JPanel是为了让它有{link JComponent#addPropertyChangeListener(java.beans.PropertyChangeListener)}方法
 * @author Linxiaozhi
 *
 */
public class TabComponent extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Doc doc;
	protected Model model;
	protected View view;

	protected String title;
	protected String name;

	public TabComponent() {
		model = new Model();
		model.addChangeListener(this);

		view = new View();
		setLayout(new BorderLayout());
		add(view);

		model.addChangeListener(view);

	}

	public Doc getDoc() {
		return doc;
	}

	public Model getModel() {
		return model;
	}

	public View getView() {
		return view;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

}
