package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;

public interface ImageBuilder {

    boolean isEmpty();

    boolean isFull();

    void write(int b);

    void clear();

    BufferedImage buildBufferedImage();

}
