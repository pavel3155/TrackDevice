package com.example.TrackDevice.service;

import com.example.TrackDevice.DTO.CommentDTO;
import com.example.TrackDevice.DTO.MessageDTO;
import com.example.TrackDevice.Poiji.PoijiDevice;
import com.poiji.bind.Poiji;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final Path fileStorageLocation;



    public FileService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("fileStorageLocation= "+this.fileStorageLocation.toAbsolutePath());
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
    // получаем путь к файлу
    public String getPathFile(MultipartFile file, String rDir,String sDir){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!fileName.isEmpty()){
            Path subDirectory =this.fileStorageLocation.resolve(rDir).resolve(sDir);
            Path targetLocation = subDirectory.resolve(fileName);
            return targetLocation.toString();
        }
        return null;
    }
    //копируем фал в директорию
    public Boolean saveFile(MultipartFile file, String rDir,String sDir) {
        System.out.println("saveFile(MultipartFile file, String subDir)...");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("fileName:= " +fileName);
        System.out.println("fileName.isEmpty()= " + fileName.isEmpty());
        if (!fileName.isEmpty()){
            try {
                Path subDirectory =this.fileStorageLocation.resolve(rDir).resolve(sDir);
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

    public String createSubDirCons(String subDir,String fileName){
        System.out.println("subDir:= " +subDir);
        System.out.println("fileName:= " +fileName);
        String pathFileName;
        if (!fileName.isEmpty()){
            try {
                Path subDirectory =this.fileStorageLocation.resolve(subDir).resolve("cons");
                Files.createDirectories(subDirectory);
                Path targetLocation = subDirectory.resolve(fileName);
                pathFileName =targetLocation.toString();
                return pathFileName;
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory 'subDir'", ex);
            }
        }
        return "";
    }

    public void addComment(String fileName, String comment){
        // Проверка на null
        if (fileName == null || comment == null) {
            System.out.println("Имя файла или комментарий null");
            return;
        }
        try (FileWriter writer = new FileWriter(fileName, true))
        {
                writer.write(comment+"\n");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public List<PoijiDevice> getListDevicesFromFileExcel(String fileLocation){
        return Poiji.fromExcel(new File(fileLocation), PoijiDevice.class);
    }










//    public List<String> loadConsult (String subDir){
//        System.out.println("loadConsult...");
//        System.out.println("subDir:= "+subDir);
//
//        Path Directory =this.fileStorageLocation.resolve(subDir).resolve("cons");
//        String fileName = "consult_"+subDir+".txt";
//        Path targetLocation = Directory.resolve(fileName);
//        String pathFileName =targetLocation.toString();
//        System.out.println("pathFileName:= "+pathFileName);
//
//        List<String> lst = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(pathFileName))) {
//            String read=br.readLine();
//            while (read!=null) {
//                lst.add(read);
//                read=br.readLine();
//            }
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//        return lst;
//    }
    public List<CommentDTO> loadConsult (String subDir){
        System.out.println("loadConsult...");
        System.out.println("subDir:= "+subDir);

        Path Directory =this.fileStorageLocation.resolve(subDir).resolve("cons");
        String fileName = "consult_"+subDir+".txt";
        System.out.println("fileName="+fileName);
        Path targetLocation = Directory.resolve(fileName);
        String pathFileName =targetLocation.toString();
        System.out.println("pathFileName:= "+pathFileName);

        List<CommentDTO> lstConsul = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathFileName))) {
            String read=br.readLine();
            System.out.println("read="+read);
            while (read!=null&&!read.isEmpty()) {
                String[] strArr= read.split(":");
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setCsa(strArr[0]);
                commentDTO.setUser(strArr[1]);
                commentDTO.setComment(strArr[2]);
                lstConsul.add(commentDTO);
                read=br.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return lstConsul;
    }
    public List<MessageDTO> getListMessages (String subDir){
        System.out.println("loadConsult...");
        System.out.println("subDir:= "+subDir);

        Path Directory =this.fileStorageLocation.resolve(subDir).resolve("cons");
        String fileName = "consult_"+subDir+".txt";
        System.out.println("fileName="+fileName);
        Path targetLocation = Directory.resolve(fileName);
        String pathFileName =targetLocation.toString();
        System.out.println("pathFileName:= "+pathFileName);

        List<MessageDTO> lstConsul = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathFileName))) {
            String read=br.readLine();
            System.out.println("read="+read);
            while (read!=null&&!read.isEmpty()) {
                String[] strArr= read.split(":");
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setCsa(strArr[0]);
                messageDTO.setUser(strArr[1]);
                messageDTO.setMessage(strArr[2]);
                lstConsul.add(messageDTO);
                read=br.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return lstConsul;
    }



    public List<String> getAllFiles(String subDir1,String subDir2) {
        Path Directory =this.fileStorageLocation.resolve(subDir1).resolve(subDir2);
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
