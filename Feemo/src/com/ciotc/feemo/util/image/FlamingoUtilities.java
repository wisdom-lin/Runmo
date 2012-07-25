package com.ciotc.feemo.util.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class FlamingoUtilities {
	/**
	 * Creates a thumbnail of the specified width.
	 * 
	 * @param image
	 *            The original image.
	 * @param requestedThumbWidth
	 *            The width of the resulting thumbnail.
	 * @return Thumbnail of the specified width.
	 * @author Romain Guy
	 */
	public static BufferedImage createThumbnail(BufferedImage image,
			int requestedThumbWidth) {
		float ratio = (float) image.getWidth() / (float) image.getHeight();
		int width = image.getWidth();
		BufferedImage thumb = image;

		do {
			width /= 2;
			if (width < requestedThumbWidth) {
				width = requestedThumbWidth;
			}

			BufferedImage temp = new BufferedImage(width,
					(int) (width / ratio), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = temp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
			g2.dispose();

			thumb = temp;
		} while (width != requestedThumbWidth);

		return thumb;
	}
	
	/**
	 * Retrieves transparent image of specified dimension.
	 * 
	 * @param width
	 *            Image width.
	 * @param height
	 *            Image height.
	 * @return Transparent image of specified dimension.
	 */
	public static BufferedImage getBlankImage(int width, int height) {
		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice d = e.getDefaultScreenDevice();
		GraphicsConfiguration c = d.getDefaultConfiguration();
		BufferedImage compatibleImage = c.createCompatibleImage(width, height,
				Transparency.TRANSLUCENT);
		return compatibleImage;
	}
}
