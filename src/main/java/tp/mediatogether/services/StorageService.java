package tp.mediatogether.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.repositories.FileDBRepository;

import java.io.IOException;

@Service
public class StorageService {

    private final FileDBRepository fileDBRepository;

    public StorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    public void store(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(filename, file.getContentType(), file.getBytes());

        fileDBRepository.save(fileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).orElse(null);
    }

    public void deleteFile(String id) {
        fileDBRepository.deleteById(id);
    }

}
