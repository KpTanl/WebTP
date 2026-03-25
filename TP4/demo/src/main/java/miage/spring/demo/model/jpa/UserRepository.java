package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByNameIgnoreCase(String name);

    List<User> findByStatus_CodeStatut(Long codeStatut);

    boolean existsByEmail(String email);

    default User findByUsername(String username) {
        return findByNameIgnoreCase(username);
    }
}
