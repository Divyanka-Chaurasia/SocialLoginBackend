package com.dollop.app.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
public interface ImageService {

	public String uploadImage(MultipartFile file,String dir) throws IOException;
}
