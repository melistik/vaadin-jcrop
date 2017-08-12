package org.vaadin.jcrop.demo;

import com.vaadin.server.StreamResource.StreamSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class SampleStreamResource implements StreamSource {
    ByteArrayOutputStream imagebuffer = null;

    int reloads = 0;

    /*
     * We need to implement this method that returns the resource as a stream.
     */
    @Override
    public InputStream getStream() {
        /* Create an image and draw something on it. */
        BufferedImage image = new BufferedImage(750, 500, BufferedImage.TYPE_INT_RGB);
        Graphics drawable = image.getGraphics();
        drawable.setColor(Color.lightGray);
        drawable.fillRect(0, 0, 750, 500);
        drawable.setColor(Color.yellow);
        drawable.fillOval(150, 25, 450, 450);
        drawable.setColor(Color.black);
        drawable.drawString("drawn=" + new Date().toString(), 280, 240);

        try {
			/* Write the image to a buffer. */
            this.imagebuffer = new ByteArrayOutputStream();
            ImageIO.write(image, "png", this.imagebuffer);

			/* Return a stream from the buffer. */
            return new ByteArrayInputStream(this.imagebuffer.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }
}