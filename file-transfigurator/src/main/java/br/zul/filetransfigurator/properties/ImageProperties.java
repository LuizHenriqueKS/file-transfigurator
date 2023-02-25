package br.zul.filetransfigurator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@ConfigurationProperties(prefix = "video")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageProperties {

    private int width;
    private int height;
    private int pixelPerRow;
    private int pixelPerCol;
    private int frameRate;
    private int durationPerImage;

}
