package com.example.csvfileprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/upload", consumes = "text/csv")
    public void uploadSimple(@RequestBody InputStream body) throws IOException {
        userRepository.saveAll(CsvUtils.read(User.class, body));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        userRepository.saveAll(CsvUtils.read(User.class, file.getInputStream()));
    }
}
