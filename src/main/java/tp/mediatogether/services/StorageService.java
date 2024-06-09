package tp.mediatogether.services;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;
import tp.mediatogether.repositories.FileDBRepository;

import java.io.IOException;
import java.util.List;


@Service
public class StorageService {

    private final FileDBRepository fileDBRepository;

    public StorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    public void store(MultipartFile multipartFile) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        FileDB fileDB = new FileDB(filename, multipartFile.getContentType(), multipartFile.getBytes());

        fileDBRepository.save(fileDB);

    }

    public FileDB getFile(Long id) {
        return fileDBRepository.findById(id);
    }

    public void deleteFile(String id) {
        fileDBRepository.deleteById(id);
    }

    public List<FileNoData> getAllFiles() {
        return fileDBRepository.findAllFilesButData();
    }


}
