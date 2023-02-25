package br.zul.filetransfigurator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.zul.filetransfigurator.core.BitsImageBuilder;
import br.zul.filetransfigurator.core.BitsImageReader;
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
        log.info("fileToVideo.size: {}", file.getSize());
        log.info("fileToVideo.name: {}", file.getOriginalFilename());
        File video = File.createTempFile("transfiguration_fileToVideo_", ".mp4");
        @Cleanup
        VideoWriter videoWriter = new VideoWriter(video, imageProperties, BitsImageBuilder::new);
        @Cleanup
        InputStream is = file.getInputStream();
        videoWriter.write(SIGNATURE);
        videoWriter.writeLong(file.getSize());
        videoWriter.writeString(file.getOriginalFilename());
        Transferer.builder()
                .inputStream(is)
                .outputStream(videoWriter)
                .build()
                .transfer();
        return video;
    }

    public VideoToFileResponse videoToFile(MultipartFile video) throws IOException {
        File videoFile = File.createTempFile("transfiguration_", video.getOriginalFilename());
        video.transferTo(videoFile);

        @Cleanup
        VideoReader videoReader = new VideoReader(videoFile, imageProperties, BitsImageReader::new);
        File file = File.createTempFile("transfiguration_videoToFile_", ".tmp");

        byte[] signature = new byte[SIGNATURE.length];
        videoReader.read(signature);
        log.info("videoToFile.signature={}", signature);
        long fileSize = videoReader.readLong();
        log.info("videoToFile.size={}", fileSize);
        String originalFilename = videoReader.readString();
        log.info("videoToFile.name={}", originalFilename);

        @Cleanup
        OutputStream outputStream = new FileOutputStream(file);

        Transferer.builder()
                .inputStream(videoReader)
                .outputStream(outputStream)
                .maxSize(fileSize)
                .build()
                .transfer();

        return VideoToFileResponse.builder()
                .file(file)
                .originalFilename(originalFilename)
                .build();
    }

}
