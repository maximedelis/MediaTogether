package tp.mediatogether.services;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;
import tp.mediatogether.repositories.FileDBRepository;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class StorageService {

    private final FileDBRepository fileDBRepository;

    public StorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    public void store(MultipartFile multipartFile) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        FileDB fileDB = new FileDB(filename, multipartFile.getContentType(), multipartFile.getBytes());

        setDurationOfFile(fileDB);

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

    public void setDurationOfFile(FileDB fileDB) throws IOException {
        File convFile = new File(fileDB.getName());
        byte[] data = fileDB.getData();

        FileOutputStream outputStream = new FileOutputStream(convFile);
        outputStream.write(data);
        outputStream.close();

        try {
            AudioFileFormat baseFileFormat = null;
            baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(convFile);

            Map properties = baseFileFormat.properties();

            System.out.println(properties);
            Long duration = (Long) properties.get("duration");
            fileDB.setDuration(duration);
        } catch (UnsupportedAudioFileException e) {
            convFile.delete();
            throw new IOException("Unsupported audio file format");
        }

        convFile.delete();
        fileDBRepository.save(fileDB);
    }

}
