package com.ciotc.runmo.util.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.plaf.UIResource;

public class ResizableIconUIResource implements ResizableIcon, UIResource {
    private ResizableIcon delegate;

	public ResizableIconUIResource(ResizableIcon delegate) {
		this.delegate = delegate;
	}

	public int getIconHeight() {
		return delegate.getIconHeight();
	}

	public int getIconWidth() {
		return delegate.getIconWidth();
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		delegate.paintIcon(c, g, x, y);
	}

	public void setDimension(Dimension newDimension) {
		delegate.setDimension(newDimension);
	}

	
    
}