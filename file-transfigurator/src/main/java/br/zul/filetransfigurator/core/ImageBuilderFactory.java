package br.zul.filetransfigurator.core;

import br.zul.filetransfigurator.properties.ImageProperties;

public interface ImageBuilderFactory {

    ImageBuilder create(ImageProperties imageProperties);

}
