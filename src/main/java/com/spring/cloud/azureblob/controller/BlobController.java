package com.spring.cloud.azureblob.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping("api")
public class BlobController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobController.class);

    private final BlobService blobService;

    public BlobController(BlobService blobService) {
        this.blobService = blobService;
    }

    @GetMapping("files/{fileName}")
    public ResponseEntity<InputStreamResource> getFileAsBytes(@PathVariable String fileName) {
        LOGGER.info("retrieving file: {}", fileName);
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        MediaType contentType = new MediaType(MimeTypeUtils.parseMimeType(mimeType));
        InputStream inputStream;
        try {
            inputStream = new ByteArrayInputStream(blobService.getFileAsBytes(fileName));
        } catch (Exception e) {
            LOGGER.error("Error occurred retrieving file {}: {}", fileName, e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=%s".formatted(fileName))
                .contentType(contentType)
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("files")
    public String getFiles() {
        LOGGER.info("Listing files");
        return blobService.listFiles();
    }
}