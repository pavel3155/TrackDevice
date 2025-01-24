package com.example.TrackDevice.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
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

    public Resource  loadFile(String subDir,String fileName){
        try {
            Path subDirectory =this.fileStorageLocation.resolve(subDir).resolve("pic");
            Path filePath = subDirectory.resolve(fileName).normalize();
            System.out.println("loadFile(String subDir,String fileName)...");
            System.out.println("filePath:="+filePath);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }


     public String encodeFile(String fileName){
         try {
             // Кодируем имя файла в UTF-8
            return  URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
         } catch (UnsupportedEncodingException e) {
             return "Ошибка кодирования имени файла";
         }
     }
    public void delFile(String subDir,String fileName) {
        System.out.println("delFile(String subDir,String fileName)...");
        System.out.println("fileName:= " +fileName);
        if (!fileName.isEmpty()) {
            try {
                Path subDirectory = this.fileStorageLocation.resolve(subDir).resolve("pic");
                Path filePath = subDirectory.resolve(fileName).normalize();
                System.out.println("filePath:=" + filePath);
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Boolean saveFile(MultipartFile file, String subDir) {
        System.out.println("saveFile(MultipartFile file, String subDir...");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("fileName:= " +fileName);
        if (!fileName.isEmpty()){
            try {
                Path subDirectory =this.fileStorageLocation.resolve(subDir).resolve("pic");
                Files.createDirectories(subDirectory);
                Path targetLocation = subDirectory.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory 'subDir'", ex);
            }
        }
        return false;
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
