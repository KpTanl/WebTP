package miage.fr.api.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import miage.fr.api.demo.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
