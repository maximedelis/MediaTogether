package tp.mediatogether.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.mediatogether.models.FileDB;

public interface FileDBRepository extends JpaRepository<FileDB, String> {
}
