package br.zul.filetransfigurator.dto;

import java.io.File;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoToFileResponse {

    private File file;
    private String originalFilename;

}
