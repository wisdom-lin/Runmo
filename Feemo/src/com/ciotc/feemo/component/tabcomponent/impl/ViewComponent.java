package com.ciotc.feemo.component.tabcomponent.impl;

import static com.ciotc.feemo.util.Constants.COMPONENT_GAP;
import static com.ciotc.feemo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.feemo.util.Constants.SENSOR_WIDTH;
import static com.ciotc.feemo.util.I18N.getString;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import com.ciotc.feemo.component.tabcomponent.ChangeInfo;
import com.ciotc.feemo.component.tabcomponent.TabComponent;
import com.ciotc.feemo.util.ActionConstants;
import com.ciotc.feemo.util.Constants;

public class ViewComponent extends TabComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ViewView view2d;
	ViewView viewContour;
	ViewView view3d;

	//WaitLayerUI waitLayerUI;

	public ViewComponent(String path) {

		model = new ViewModel();
		model.addChangeListener(this);

		view2d = new View2DView();
		viewContour = new ViewContourView();
		view3d = new View3DView();

//		setLayout(new BorderLayout());
//		add(view);

		model.addChangeListener(view2d);
		//view2d.addMouseMotionListener((ViewModel)model);
		view2d.addPropertyChangeListener((ViewModel) model);
		model.addChangeListener(viewContour);
		model.addChangeListener(view3d);

		doc = new ViewDoc(path);

		this.title = getSimplePath();

	}

	public String getPath() {
		return doc.getPath();
	}

	public String getSimplePath() {
		return doc.getSimplePath();
	}

	@Override
	public void open() {
		super.open();
		final ViewModel vmodel = (ViewModel) model;
		ViewDoc vdoc = (ViewDoc) doc;
		if (!vdoc.readDataFromFile()) {
			JOptionPane.showMessageDialog(this, getString("ViewComponent.readFail"), getString("Feemo"), JOptionPane.PLAIN_MESSAGE);
			firePropertyChange(ActionConstants.VIEW_COMPONENT_OPEN_ERROR, null, this);
		} else {
			//doc-->model
			//System.out.println(System.currentTimeMillis());
			vmodel.setDoc(vdoc);
			//System.out.println(System.currentTimeMillis());
//			waitLayerUI = new WaitLayerUI();
//			JPanel jp = new JPanel();
//			JLayer<JPanel> waitLayer = new JLayer<JPanel>(jp, waitLayerUI);
////
//			setLayout(new BorderLayout());
//			getParent().add(waitLayer);
//			waitLayerUI.start();
			firePropertyChange(ActionConstants.VIEW_COMPONENT_OPEN, null, ViewComponent.this);
			//System.out.println(System.currentTimeMillis());
			vmodel.init();
			//System.out.println(System.currentTimeMillis());
//			new Thread() {
//				public void run() {
//
//					try {
//						Thread.sleep(1500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					SwingUtilities.invokeLater(new Runnable() {
//						
//						@Override
//						public void run() {
							
						
//							waitLayerUI.stop();
//						}
//					});
//				}
//			}.start();
//			firePropertyChange(ActionConstants.VIEW_COMPONENT_OPEN, null, ViewComponent.this);
//			vmodel.init();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		ChangeInfo info = (ChangeInfo) e.getSource();
		//System.out.println(info.getTag());
		if (info.getTag().equals(ActionConstants.VIEW_VIW_CONTROL)) {
			Integer value = (Integer) info.getObject();
			int count = Integer.bitCount(value);
			removeAll();
			if (count == 3) {
				GridLayout layout = new GridLayout(2, 2);
				setLayout(layout);
			} else if (count == 2) {
				setLayout(new GridLayout(1, 2));
			} else if (count == 1) {
				setLayout(new BorderLayout());
			}
			if ((value & Constants._2D) != 0) {
				add(view2d);
			}
			if ((value & Constants._CONTOUR) != 0) {
				add(viewContour);
			}
			if ((value & Constants._3D) != 0) {
				add(view3d);
			}
			getParent().invalidate();
			getParent().repaint();
		} else if (info.getTag().equals(ActionConstants.VIEW_FRAME_MODEL)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, null, this);
		} else if (info.getTag().equals(ActionConstants.VIEW_FRAME_MOUSE_MOVE)) {
			firePropertyChange(ActionConstants.VIEW_COMPONENT_MOUSE_MOVE, null, this);
		}

	}

	@Override
	public void close() {
		super.close();
		firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, null, this);
	}

	@Override
	public void triggerClose() {
		close();
	}

	/**
	 * 选择长为基准还是宽为基准
	 */
	private GridLayout CalcPixelByWhichSide() {
		int width = getWidth() - COMPONENT_GAP;
		int height = getHeight() - COMPONENT_GAP;
		float w0 = width * 1.0f / SENSOR_WIDTH;
		float h0 = height * 1.0f / SENSOR_HEIGHT;
		if (Float.compare(2 * w0, h0) <= 0) { //2*w0<h0 选择高 
			return new GridLayout(2, 1);
		} else { //选择宽
			return new GridLayout(1, 2);
		}

	}

	public ViewModel getViewModel() {
		return (ViewModel) model;
	}

}
