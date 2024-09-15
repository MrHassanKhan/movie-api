package com.inventrevo.movieapi.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService implements IFileService{
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = path + fileName;
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + fileName;
        return new FileInputStream(filePath);
    }

//    @Override
//    public Boolean deleteFile(String path, String name) throws FileNotFoundException {
//        String filePath = path + File.separator + name;
//        File file = new File(filePath);
//        return file.delete();
//    }
}
