package com.pertunia.lostfound.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "File is required");
        }

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String filePath = UPLOAD_DIR + Objects.requireNonNull(file.getOriginalFilename());
        file.transferTo(new File(filePath));

        return filePath;
    }
}
