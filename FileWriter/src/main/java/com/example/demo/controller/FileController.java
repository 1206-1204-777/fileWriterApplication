package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.FileEntity;
import com.example.demo.service.FileService;

@Controller
@RequestMapping("/")
public class FileController {
	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}
	//index.htmlを表示
	@GetMapping
	public String listFiles(Model model) {
		List<FileEntity> files = fileService.listFiles();
		model.addAttribute("files",files);
		return "index";
	}
	//アップロード先のUrlを作成
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file")MultipartFile file,@RequestParam(value = "orverwrite",required = false)
	boolean overwrite,Model model) {
		try {
			fileService.saveFile(file, overwrite);
		}catch(IOException e) {
			
		}
		return "redirect:/";
	}
	
	//削除後のUrlを作成
	@PostMapping("/delete/{id}")
	public String deleteFile(@PathVariable@RequestParam Long id,Model model) {
		try {
			fileService.deleteFile(id);
			model.addAttribute("files",fileService.listFiles());
		}catch(IOException e) {
			
		}
		return "redirect:/";
	}
}
