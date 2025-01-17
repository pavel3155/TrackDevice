package com.example.TrackDevice.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> getAllFiles(String subDir) {
        Path Directory =this.fileStorageLocation.resolve(subDir).resolve("pic");
        System.out.println("getAllFiles...");
        System.out.println("Directory:= "+Directory);
        if (Files.exists(Directory)){
            System.out.println("Directory= "+ Files.exists(Directory));

            try {
                return Files.walk(Directory, 1)
                        .filter(path -> !path.equals(Directory))
                        .map(Directory::relativize)
                        .map(Path::toString)
                        .collect(Collectors.toList());
            } catch (IOException ex) {
                throw new RuntimeException("Could not list the files!", ex);
            }
        } else return null;

    }
}
