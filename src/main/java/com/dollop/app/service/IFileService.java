package com.dollop.app.service;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IFileService {
	String uploadFile(MultipartFile file,String path) throws IOException;
    InputStream getResource(String path,String name) throws FileNotFoundException;
  
}
