package com.ciotc.runmo.component.tabcomponent.impl;

import static com.ciotc.runmo.util.Constants.COMPONENT_GAP;
import static com.ciotc.runmo.util.Constants.SENSOR_HEIGHT;
import static com.ciotc.runmo.util.Constants.SENSOR_WIDTH;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.event.ChangeEvent;

import com.ciotc.runmo.component.tabcomponent.ChangeInfo;
import com.ciotc.runmo.component.tabcomponent.TabComponent;
import com.ciotc.runmo.util.ActionConstants;
import com.ciotc.runmo.util.Constants;

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

		doc = new ViewFilterDoc(new ViewDoc(path));
		//doc = new ViewDoc(path);

		this.title = getSimplePath();

	}

	public String getPath() {
		return doc.getPath();
	}

	public String getSimplePath() {
		return doc.getSimplePath();
	}

	public boolean readDataFromFile() {
		ViewModel vmodel = (ViewModel) model;
		ViewDocImpl vdoc = (ViewDocImpl) doc;
		//System.out.println("read" + System.currentTimeMillis());
		boolean bool = vdoc.readDataFromFile();
		//System.out.println("read complete" + System.currentTimeMillis());
		if (bool)
			vmodel.setDoc(vdoc);
		return bool;
	}

	@Override
	public void open() {
		super.open();
		final ViewModel vmodel = (ViewModel) model;
		vmodel.init();

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

	public void fresh(){
		//firePropertyChange(ActionConstants.VIEW_COMPONENT_MODEL, null, this);
		final ViewModel vmodel = (ViewModel) model;
		vmodel.selectNowFrame();
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
