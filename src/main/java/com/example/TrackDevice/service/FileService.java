package com.example.TrackDevice.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final Path fileStorageLocation;



    public FileService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    public void saveFile(MultipartFile file, String subDir) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path subDirectory =this.fileStorageLocation.resolve(subDir).resolve("pic");
            Files.createDirectories(subDirectory);
            Path targetLocation = subDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory 'subDir'", ex);
        }
    }
}
