package br.zul.filetransfigurator.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public class ByteImageBuilder implements ImageBuilder {

    private final ImageProperties imageProperties;
    private final int pixelWidth;
    private final int pixelHeight;
    private final BufferedImage image;
    private final Graphics2D g2d;

    private boolean empty;
    private int row;
    private int col;

    public ByteImageBuilder(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
        this.pixelWidth = imageProperties.getWidth() / imageProperties.getPixelPerRow();
        this.pixelHeight = imageProperties.getHeight() / imageProperties.getPixelPerCol();
        this.image = new BufferedImage(imageProperties.getWidth(), imageProperties.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        this.g2d = image.createGraphics();
        this.clear();
    }

    @Override
    public boolean isFull() {
        return col >= imageProperties.getPixelPerRow() || row >= imageProperties.getPixelPerCol();
    }

    @Override
    public void write(int b) {
        this.empty = false;
        Color color = ColorUtils.getColor((byte) b);
        g2d.setColor(color);
        g2d.fillRect(col * pixelWidth, row * pixelHeight, pixelWidth, pixelHeight);
        nextCel();
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void clear() {
        this.empty = true;
        this.row = 0;
        this.col = 0;
        this.g2d.setColor(Color.BLACK);
        this.g2d.fillRect(0, 0, imageProperties.getWidth(), imageProperties.getHeight());
    }

    @Override
    public BufferedImage buildBufferedImage() {
        BufferedImage image = new BufferedImage(imageProperties.getWidth(), imageProperties.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(this.image, 0, 0, null);
        return image;
    }

    private void nextCel() {
        this.col++;
        if (this.col == this.imageProperties.getPixelPerRow()) {
            this.row++;
            this.col = 0;
        }
    }

}
