package miage.spring.demo.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByNomStatutIgnoreCase(String nomStatut);
}
