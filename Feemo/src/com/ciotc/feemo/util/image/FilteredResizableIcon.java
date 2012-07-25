package com.ciotc.feemo.util.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilteredResizableIcon implements ResizableIcon {
	/**
	 * Image cache to speed up rendering.
	 */
	protected Map<String, BufferedImage> cachedImages;

	/**
	 * The main (pre-filtered) icon.
	 */
	protected ResizableIcon delegate;

	/**
	 * Filter operation.
	 */
	protected BufferedImageOp operation;

	/**
	 * Creates a new filtered icon.
	 * 
	 * @param delegate
	 *            The main (pre-filtered) icon.
	 * @param operation
	 *            Filter operation.
	 */
	public FilteredResizableIcon(ResizableIcon delegate,
			BufferedImageOp operation) {
		super();
		this.delegate = delegate;
		this.operation = operation;
		this.cachedImages = new LinkedHashMap<String, BufferedImage>() {
			@Override
			protected boolean removeEldestEntry(
					java.util.Map.Entry<String, BufferedImage> eldest) {
				return size() > 5;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return delegate.getIconHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return delegate.getIconWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.icon.ResizableIcon#setDimension(java.awt.Dimension
	 * )
	 */
	public void setDimension(Dimension newDimension) {
		delegate.setDimension(newDimension);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// check cache
		String key = this.getIconWidth() + ":" + this.getIconHeight();
		if (!this.cachedImages.containsKey(key)) {
			// check if loading
			if (this.delegate instanceof AsynchronousLoading) {
				AsynchronousLoading asyncDelegate = (AsynchronousLoading) this.delegate;
				// if the delegate is still loading - do nothing
				if (asyncDelegate.isLoading())
					return;
			}
			BufferedImage offscreen = FlamingoUtilities.getBlankImage(this
					.getIconWidth(), this.getIconHeight());
			Graphics2D g2d = offscreen.createGraphics();
			this.delegate.paintIcon(c, g2d, 0, 0);
			g2d.dispose();
			BufferedImage filtered = this.operation.filter(offscreen, null);
			this.cachedImages.put(key, filtered);
		}
		g.drawImage(this.cachedImages.get(key), x, y, null);
	}
}