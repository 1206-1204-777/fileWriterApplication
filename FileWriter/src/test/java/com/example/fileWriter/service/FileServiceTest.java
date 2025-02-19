package com.example.fileWriter.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileWriter.entity.FileEntity;
import com.example.fileWriter.repository.FileRepository;

@SpringBootTest
class FileServiceTest {

	@Autowired
	private FileService fileService;

	@Autowired
	private FileRepository fileRepository;

	@Test
	@Transactional
	@Rollback
	void testSaveFile() throws IOException {
		MultipartFile file = new MockMultipartFile("file","test_text.txt","text/plan","Hello!".getBytes());

		String savedFileName = fileService.saveFile(file, true);
		
		Optional<FileEntity> savedFile = fileRepository.findByFileName(savedFileName);
		assertTrue(savedFile.isPresent());
		assertEquals("test_text.txt",savedFile.get().getFileName());
	}
	@Test
	@Transactional
	@Rollback
	void deleteFile() throws IOException{
		MultipartFile file = new MockMultipartFile("file","test.text","text/plan","Hello".getBytes());
		
		String FileName = fileService.saveFile(file,true);
		
		Optional<FileEntity> sevedFile = fileRepository.findByFileName(FileName);
		
		Long fileId = sevedFile.get().getId();
		fileService.deleteFile(fileId);
		
		Optional<FileEntity> deleteFile = fileRepository.findById(fileId);
		assertFalse(deleteFile.isPresent());
		
	}
}
