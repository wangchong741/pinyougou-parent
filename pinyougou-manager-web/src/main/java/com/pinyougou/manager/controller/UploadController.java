package com.pinyougou.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

/**
 * 文件上传
 * @author wangchong
 * 2019年1月9日
 */
@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;//文件服务器地址
	
	@RequestMapping("/upload")
	public Result upload( MultipartFile file){	
		System.out.println("文件上传了");
		//获取文件全文件名
		String originalFilename = file.getOriginalFilename();
		//获取文件扩展名
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			String fileId = fastDFSClient.uploadFile(file.getBytes(), extName);
			//图片存储的完整地址
			String url=FILE_SERVER_URL+fileId;
			System.out.println(url);
			return new Result(true,url);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "上传失败");
		}
		
	}
}
