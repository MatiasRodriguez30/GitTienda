package com.example.TiendaInventario.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, bytes);
            return "/images/" + file.getOriginalFilename(); // Devuelve la URL de la imagen
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }
}
