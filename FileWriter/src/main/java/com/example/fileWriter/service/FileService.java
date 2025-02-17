package com.example.fileWriter.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileWriter.entity.FileEntity;
import com.example.fileWriter.repository.FileRepository;

@Service
public class FileService {
	private static String UPLOAD_DIR = "uploads/";
	private final FileRepository fileRepository;

	public FileService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}
	//ファイルの検索
	public List<FileEntity> listFiles(){
		return fileRepository.findAll();
	}
	//ファイルの保存先を指定
	//該当ファイルがない場合は作成し保存
	public String saveFile(MultipartFile file,boolean overwrite)throws IOException{
		Path uploadPath = Paths.get(UPLOAD_DIR); 
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		//ファイルの名前とパスを取得
		String fileName = file.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		
		Optional<FileEntity> existingFile = fileRepository.findByFileName(fileName);
		//ハッシュ化
		if(existingFile.isPresent()) {
			if(!overwrite) {
				fileName = UUID.randomUUID() + "_" + fileName;
				filePath = uploadPath.resolve(filePath);
			}
		}
		//取得したファイルを保存
		Files.write(filePath, file.getBytes(),StandardOpenOption.CREATE);
		FileEntity fileEntity = new FileEntity();
		fileEntity.setFileName(fileName);
		fileEntity.setFilePath(filePath.toString());
		fileRepository.save(fileEntity);
		
		return fileName;
	}
	//ファイルの削除処理メソッド	
	public void deleteFile(Long id)throws IOException{
		Optional<FileEntity>fileEntity = fileRepository.findById(id);
		if(fileEntity.isPresent()) {
			Path filePath = Paths.get(fileEntity.get().getFilePath());
			Files.deleteIfExists(filePath);
			fileRepository.deleteById(id);
		}
	}
	
	
}
