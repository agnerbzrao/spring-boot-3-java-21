package br.com.spring.agner.rest_with_spring_boot.controllers.docs;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.UploadFileResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(MultipartFile file);

    List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);

    ResponseEntity<Resource> downloadFile(String fileName,
                                          HttpServletRequest request);
}