package br.zul.filetransfigurator.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.zul.filetransfigurator.dto.VideoToFileResponse;
import br.zul.filetransfigurator.service.TransfigurationService;

@RestController
@RequestMapping("transfiguration")
public class TransfigurationController {

    @Autowired
    private TransfigurationService transfigurationService;

    @PostMapping("fileToVideo")
    public ResponseEntity<Resource> fileToVideo(@RequestParam MultipartFile file) throws IOException {
        File video = transfigurationService.fileToVideo(file);
        Resource resource = new FileSystemResource(video);
        String filename = file.getOriginalFilename().replace(".", "_") + "_transfiguration.mp4";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping("videoToFile")
    public ResponseEntity<Resource> videoToFile(@RequestParam MultipartFile video) throws IOException {
        VideoToFileResponse response = transfigurationService.videoToFile(video);
        Resource resource = new FileSystemResource(response.getFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + response.getOriginalFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
