package com.example.filemanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filemanager.dto.response.FileResponseDTO;
import com.example.filemanager.exception.FileNotFoundException;
import com.example.filemanager.model.StoredFile;
import com.example.filemanager.repository.StoredFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final StoredFileRepository repository;

    public StoredFile save(MultipartFile file) throws IOException {

        StoredFile arquivo = StoredFile.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .uploadDate(LocalDateTime.now())
                .data(file.getBytes())
                .build();

        return repository.save(arquivo);
    }

    public StoredFile getFile(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File with id=" + id + " not found."));
    }

    public List<FileResponseDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(f -> new FileResponseDTO(
                        f.getId(),
                        f.getFileName(),
                        f.getContentType(),
                        f.getSize(),
                        f.getUploadDate()))
                .toList();
    }

    public FileResponseDTO findById(Long id) {
        StoredFile file = repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File with id=" + id + " not found."));

        return new FileResponseDTO(file.getId(), file.getFileName(), file.getContentType(), file.getSize(),
                file.getUploadDate());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new FileNotFoundException("File with id=" + id + " not found.");
        }

        repository.deleteById(id);
    }

}
