package tp.mediatogether.controllers.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;
import tp.mediatogether.services.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(MultipartFile file, RedirectAttributes redirectAttributes) {
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
