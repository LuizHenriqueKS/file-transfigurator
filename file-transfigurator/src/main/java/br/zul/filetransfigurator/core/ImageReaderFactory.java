package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;

import br.zul.filetransfigurator.properties.ImageProperties;

public interface ImageReaderFactory {

    ImageReader create(BufferedImage image, ImageProperties imageProperties);

}
