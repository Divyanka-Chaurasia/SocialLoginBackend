package com.dollop.app.serviceimpl;

import java.io.IOException;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dollop.app.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private Cloudinary cloudinary;
	
	@Override
	public String uploadImage(MultipartFile file, String dir) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName+extension;
		cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id",dir+"/"+fileNameWithExtension));
		return dir+"/"+fileNameWithExtension+extension;
	}

}
