package com.example.filemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filemanager.model.StoredFile;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    
    List<StoredFile> findByOwner_Username(String username);
}
