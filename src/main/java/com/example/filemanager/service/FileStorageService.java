package com.example.filemanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filemanager.dto.response.FileResponseDTO;
import com.example.filemanager.exception.CustomAccessDeniedException;
import com.example.filemanager.exception.FileNotFoundException;
import com.example.filemanager.model.StoredFile;
import com.example.filemanager.model.User;
import com.example.filemanager.repository.StoredFileRepository;
import com.example.filemanager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

        private final StoredFileRepository fileRepository;
        private final UserRepository userRepository;

        public StoredFile save(MultipartFile file) throws IOException {

                var username = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                User user = userRepository.findByUsername(username)
                                .orElseThrow();

                StoredFile arquivo = StoredFile.builder()
                                .fileName(file.getOriginalFilename())
                                .size(file.getSize())
                                .contentType(file.getContentType())
                                .uploadDate(LocalDateTime.now())
                                .data(file.getBytes())
                                .owner(user)
                                .build();

                return fileRepository.save(arquivo);
        }

        public StoredFile getFile(Long id) {

                var username = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                StoredFile file = fileRepository.findById(id)
                                .orElseThrow(() -> new FileNotFoundException("File with id=" + id + "not found."));

                if (!file.getOwner().getUsername().equals(username)) {
                        throw new CustomAccessDeniedException("You cannot acess this file");
                }

                return file;
        }

        public List<FileResponseDTO> listMyFiles() {

                var username = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                return fileRepository.findByOwner_Username(username)
                                .stream()
                                .map(f -> new FileResponseDTO(
                                                f.getId(),
                                                f.getFileName(),
                                                f.getContentType(),
                                                f.getSize(),
                                                f.getUploadDate()))
                                .toList();
        }

        // Métodos apenas para provimento de endpoints para role ADMIN

        public List<FileResponseDTO> listAll() {
                return fileRepository.findAll()
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
                StoredFile file = fileRepository.findById(id)
                                .orElseThrow(() -> new FileNotFoundException("File with id=" + id + " not found."));

                return new FileResponseDTO(file.getId(), file.getFileName(), file.getContentType(), file.getSize(),
                                file.getUploadDate());
        }

        public void delete(Long id) {
                if (!fileRepository.existsById(id)) {
                        throw new FileNotFoundException("File with id=" + id + " not found.");
                }

                fileRepository.deleteById(id);
        }

}
