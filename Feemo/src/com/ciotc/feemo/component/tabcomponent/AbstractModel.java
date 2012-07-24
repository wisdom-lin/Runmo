package com.ciotc.feemo.component.tabcomponent;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class AbstractModel {
	private Vector<ChangeListener> vectorChangeListeners = new Vector<ChangeListener>();

	public synchronized void addChangeListener(ChangeListener changelistener) {
		vectorChangeListeners.addElement(changelistener);
	}

	public synchronized void removeChangeListener(ChangeListener changelistener) {
		vectorChangeListeners.removeElement(changelistener);
	}

	public synchronized void removeAllChangeListeners() {
		Enumeration<ChangeListener> enumeration = vectorChangeListeners.elements();
		while (enumeration.hasMoreElements()) {
			ChangeListener changelistener = enumeration.nextElement();
			vectorChangeListeners.removeElement(changelistener);
		}
	}

	protected synchronized void fireChangeEvent(ChangeEvent changeevent) {
		Enumeration<ChangeListener> enumeration = vectorChangeListeners.elements();
		while (enumeration.hasMoreElements()) {
			ChangeListener changelistener = enumeration.nextElement();
			changelistener.stateChanged(changeevent);
		}
	}

}