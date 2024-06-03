package tp.mediatogether.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;
import tp.mediatogether.services.StorageService;

import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }


    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        try {
            storageService.store(file);
            return ResponseEntity.ok().body("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        if (storageService.getFile(id) == null) {
            return ResponseEntity.notFound().build();
        }
        FileDB file = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FileNoData>> searchFiles() {
        return ResponseEntity.ok(storageService.getAllFiles());
    }

}
