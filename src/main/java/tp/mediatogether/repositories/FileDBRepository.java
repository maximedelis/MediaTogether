package tp.mediatogether.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;

import java.util.List;

public interface FileDBRepository extends JpaRepository<FileDB, String> {

    // Query to get all files but data
    @Query("SELECT f.id as id, f.name as name, f.type as type, f.duration as duration FROM FileDB f")
    List<FileNoData> findAllFilesButData();

    // Query to get a file by its id
    FileDB findById(Long id);

}
