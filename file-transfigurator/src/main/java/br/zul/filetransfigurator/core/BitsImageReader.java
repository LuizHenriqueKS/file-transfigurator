package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public class BitsImageReader implements ImageReader {

    private final BufferedImage image;
    private final ImageProperties imageProperties;

    private final int pixelWidth;
    private final int pixelHeight;

    private int row = 0;
    private int col = 0;
    private final int[] bits = new int[8];

    public BitsImageReader(BufferedImage image, ImageProperties imageProperties) {
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
        int[] bits = readBits();
        return convertToInt(bits);
    }

    private int convertToInt(int[] bits) {
        int b = 0;
        for (int i = 0; i < 8; i++) {
            b |= (bits[7 - i] == 1 ? 1 : 0) << (7 - i);
        }
        return b;
    }

    private int[] readBits() {
        for (int i = 0; i < 8; i++) {
            bits[i] = readBit();
        }
        return bits;
    }

    private int readBit() {
        try {
            if (isFinished()) {
                return 0;
            }
            int pixelValue = image.getRGB(col * pixelWidth + pixelWidth / 2, row * pixelHeight + pixelHeight / 2);
            int red = (pixelValue >> 16) & 0xFF;
            return red < 128 ? 1 : 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            nextCel();
        }
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
