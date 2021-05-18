package com.example.csvfileprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CsvUtils csvUtils;

    @PostMapping(value = "/upload", consumes = "text/csv")
    public void uploadSimple(@RequestBody InputStream body) throws IOException {
        userRepository.saveAll(csvUtils.read(User.class, body));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        csvUtils.read(User.class, file.getInputStream());
    }

    @PostMapping(value = "/process-csv", consumes = "multipart/form-data")
    public Collection<User> processCSVFile(@RequestParam(value = "file", required = true) MultipartFile file)
            throws Exception {
        return csvUtils.processCSV(file);
    }
}
