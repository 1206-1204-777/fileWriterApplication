package com.example.fileWriter.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileWriter.entity.FileEntity;
import com.example.fileWriter.repository.FileRepository;
import com.example.fileWriter.service.FileService;

@Controller
@RequestMapping("/")
public class FileController {
	private final FileService fileService;
	private final FileRepository fileRepository;

	public FileController(FileService fileService,FileRepository fileRepository) {
		this.fileService = fileService;
		this.fileRepository = fileRepository;
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
	
	//*ファイルを取得するメソッド
	@GetMapping("/view/{id}")
    public ResponseEntity<UrlResource> viewFile(@PathVariable Long id) throws IOException {
        // ID に対応するファイル情報を取得
        Optional<FileEntity> optionalFile = fileRepository.findById(id);
        if (!optionalFile.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        FileEntity fileEntity = optionalFile.get();
        Path filePath = Paths.get(fileEntity.getFilePath());

        // ファイルが実際に存在するかチェック
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // ファイルを Resource に変換
        UrlResource resource = new UrlResource(filePath.toUri());

        // 適切なレスポンスヘッダーを設定
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getFileName() + "\"")
                .body(resource);
		
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
