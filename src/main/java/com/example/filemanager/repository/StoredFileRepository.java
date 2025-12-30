package com.example.filemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filemanager.model.StoredFile;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    
}
