package tp.mediatogether.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.mediatogether.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

        User findByUsername(String username);

        boolean existsByUsername(String username);
}
