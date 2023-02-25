package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public class ImageReader {

    private final BufferedImage image;
    private final ImageProperties imageProperties;

    private final int pixelWidth;
    private final int pixelHeight;

    private int row = 0;
    private int col = 0;

    public ImageReader(BufferedImage image, ImageProperties imageProperties) {
        this.image = image;
        this.imageProperties = imageProperties;
        this.pixelWidth = imageProperties.getWidth() / imageProperties.getBytePerRow();
        this.pixelHeight = imageProperties.getHeight() / imageProperties.getBytePerCol();
    }

    public int read() {
        if (isFinished()) {
            return -1;
        }
        int pixelValue = image.getRGB(col * pixelWidth + pixelWidth / 2, row * pixelHeight + pixelHeight / 2);
        nextCel();
        int r = (pixelValue >> 16) & 0xff;
        int g = (pixelValue >> 8) & 0xff;
        int b = pixelValue & 0xff;
        return ColorUtils.getByte(r, g, b);
    }

    private boolean isFinished() {
        return row >= imageProperties.getWidth() || col >= imageProperties.getHeight();
    }

    private void nextCel() {
        this.col++;
        if (this.col == imageProperties.getWidth()) {
            this.row++;
            this.col = 0;
        }
    }

}
