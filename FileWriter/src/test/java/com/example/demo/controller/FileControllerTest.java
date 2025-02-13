package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;

import com.example.demo.FileWriterApplication;
import com.example.demo.entity.FileEntity;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.FileService;

@AutoConfigureMockMvc
@SpringBootTest(classes = FileWriterApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;

    @Test
    @Rollback
    @Transactional
    void testSaveFile() throws IOException {
        // MultipartFileのモックを作成
        MockMultipartFile file = new MockMultipartFile(
            "file", "service_test.txt", "text/plain", "Hello, World!".getBytes()
        );

        // ファイルを保存
        String savedFileName = fileService.saveFile(file, true);

        // データベースから取得
        Optional<FileEntity> savedFile = fileRepository.findByFileName(savedFileName);
        		
        // 検証
        assertTrue(savedFile.isPresent());
        assertEquals("service_test.txt", savedFile.get().getFileName());
        
        //削除(savedFileのidをLong型に変換して使用)
        Long fileId = savedFile.get().getId();      
        fileService.deleteFile(fileId);
        
        //削除が完了したかの検証
        Optional<FileEntity> deleteFile = fileRepository.findById(fileId);
        assertFalse(deleteFile.isPresent());
        
              }
}