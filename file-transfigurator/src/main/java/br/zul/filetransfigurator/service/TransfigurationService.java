package br.zul.filetransfigurator.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.zul.filetransfigurator.core.VideoReader;
import br.zul.filetransfigurator.core.VideoWriter;
import br.zul.filetransfigurator.dto.VideoToFileResponse;
import br.zul.filetransfigurator.properties.ImageProperties;
import br.zul.filetransfigurator.util.Transferer;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TransfigurationService {

    private static final byte[] SIGNATURE = { 0, 1, 2, 3 };

    @Autowired
    private ImageProperties imageProperties;

    public File fileToVideo(MultipartFile file) throws IOException {
        File video = File.createTempFile("transfiguration_fileToVideo_", ".mp4");
        @Cleanup
        VideoWriter videoWriter = new VideoWriter(video, imageProperties);
        @Cleanup
        InputStream is = file.getInputStream();
        videoWriter.write(SIGNATURE);
        videoWriter.writeLong(file.getSize());
        videoWriter.writeString(file.getName());
        Transferer.builder()
                .inputStream(is)
                .outputStream(videoWriter)
                .build()
                .transfer();
        return video;
    }

    public VideoToFileResponse videoToFile(MultipartFile video) throws IOException {
        byte[] signature = new byte[SIGNATURE.length];
        File videoFile = File.createTempFile("transfiguration_", video.getOriginalFilename());
        video.transferTo(videoFile);
        @Cleanup
        VideoReader videoReader = new VideoReader(videoFile, imageProperties);
        videoReader.read(signature);
        log.info("File.signature={}", signature);
        long fileSize = videoReader.readLong();
        log.info("File.size={}", fileSize);
        File file = File.createTempFile("transfiguration_videoToFile_", ".tmp");
        String originalFilename = videoReader.readString();
        log.info("File.name={}", originalFilename);
        return VideoToFileResponse.builder()
                .file(file)
                .originalFilename(originalFilename)
                .build();
    }

}
