package com.spring.cloud.azureblob.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobService.class);

    private final BlobContainerClient blobContainerClient;

    public BlobService(BlobContainerClient blobContainerClient) {
        this.blobContainerClient = blobContainerClient;
    }

    public byte[] getFileAsBytes(String fileName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        long blobSize = blobClient.getProperties().getBlobSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) blobSize);
        blobClient.downloadStream(outputStream);
        return outputStream.toByteArray();
    }

    public String listFiles() {
        LOGGER.info("Listing files");
        List<BlobItem> blobItems = blobContainerClient.listBlobs().stream().toList();
        return blobItems.stream().map(BlobItem::getName).collect(Collectors.joining(", "));
    }
}
