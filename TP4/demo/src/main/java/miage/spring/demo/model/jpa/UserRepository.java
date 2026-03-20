package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByusername(String username);   
    List<User> findAll();
    
}
