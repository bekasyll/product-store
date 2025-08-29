package com.bekassyl.productstoreapp.services;

import com.bekassyl.productstoreapp.dto.ProductRequestDTO;
import com.bekassyl.productstoreapp.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
@Slf4j
public class FileStorageService {
    private final ProductService productService;

    @Autowired
    public FileStorageService(ProductService productService) {
        this.productService = productService;
    }

    public void deleteProductImage(int id) {
        Product product = productService.getProductById(id);

        String uploadDir = "src/main/resources/static/images/products";
        Path imagePath = Paths.get(uploadDir, product.getImageFileName());

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            log.info("Exception: " + e.getMessage());
        }
    }

    public String saveProductImage(ProductRequestDTO productRequestDTO) {
        MultipartFile image = productRequestDTO.getImageFileName();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "src/main/resources/static/images/products";

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
        }

        return storageFileName;
    }
}
