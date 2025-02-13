package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity,Long>{
	Optional<FileEntity> findByFileName(String fileName);
}
