package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public class ByteImageReader implements ImageReader {

    private final BufferedImage image;
    private final ImageProperties imageProperties;

    private final int pixelWidth;
    private final int pixelHeight;

    private int row = 0;
    private int col = 0;

    public ByteImageReader(BufferedImage image, ImageProperties imageProperties) {
        this.image = image;
        this.imageProperties = imageProperties;
        this.pixelWidth = imageProperties.getWidth() / imageProperties.getPixelPerRow();
        this.pixelHeight = imageProperties.getHeight() / imageProperties.getPixelPerCol();
    }

    @Override
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
        return row >= imageProperties.getPixelPerCol() || col >= imageProperties.getPixelPerRow();
    }

    private void nextCel() {
        this.col++;
        if (this.col == imageProperties.getPixelPerRow()) {
            this.row++;
            this.col = 0;
        }
    }

}
