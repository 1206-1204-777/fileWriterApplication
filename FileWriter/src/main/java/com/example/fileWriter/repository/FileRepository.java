package com.example.fileWriter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fileWriter.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity,Long>{
	Optional<FileEntity> findByFileName(String name);
}
