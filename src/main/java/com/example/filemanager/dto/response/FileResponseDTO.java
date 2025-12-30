package com.example.filemanager.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponseDTO {
    
    private Long id;
    private String fileName;
    private String contentType;
    private Long size;
    private LocalDateTime uploadDate;

}
