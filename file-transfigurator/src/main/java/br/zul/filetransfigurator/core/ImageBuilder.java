package br.zul.filetransfigurator.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public class ImageBuilder {

    private final ImageProperties imageProperties;
    private final int pixelWidth;
    private final int pixelHeight;
    private final BufferedImage image;
    private final Graphics2D g2d;

    private boolean empty;
    private int row;
    private int col;

    public ImageBuilder(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
        this.pixelWidth = imageProperties.getWidth() / imageProperties.getBytePerRow();
        this.pixelHeight = imageProperties.getHeight() / imageProperties.getBytePerCol();
        this.image = new BufferedImage(imageProperties.getWidth(), imageProperties.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        this.g2d = image.createGraphics();
        this.clear();
    }

    public boolean isFull() {
        return col >= imageProperties.getWidth() || row >= imageProperties.getHeight();
    }

    public void write(int b) {
        this.empty = false;
        nextCel();
        Color color = ColorUtils.getColor((byte) b);
        g2d.setColor(color);
        g2d.fillRect(col * pixelWidth, row * pixelHeight, pixelWidth, pixelHeight);
        // test();
    }

    // private void test() {
    // int pixelValue = image.getRGB(col * pixelWidth + pixelWidth / 2, row *
    // pixelHeight + pixelHeight / 2);
    // int r = (pixelValue >> 16) & 0xff;
    // int g = (pixelValue >> 8) & 0xff;
    // int b = pixelValue & 0xff;
    // System.out.println(ColorUtils.getByte(r, g, b));
    // }

    public boolean isEmpty() {
        return empty;
    }

    public void clear() {
        this.empty = true;
        this.row = 0;
        this.col = 0;
        this.g2d.setColor(Color.BLACK);
        this.g2d.fillRect(0, 0, imageProperties.getWidth(), imageProperties.getHeight());
    }

    public BufferedImage buildBufferedImage() {
        BufferedImage image = new BufferedImage(imageProperties.getWidth(), imageProperties.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        // g2d.setColor(Color.BLACK);
        // g2d.fillRect(0, 0, imageProperties.getWidth(), imageProperties.getHeight());
        g2d.drawImage(this.image, 0, 0, null);
        return image;
    }

    private void nextCel() {
        this.col++;
        if (this.col == this.imageProperties.getWidth()) {
            this.row++;
            this.col = 0;
        }
    }

}
