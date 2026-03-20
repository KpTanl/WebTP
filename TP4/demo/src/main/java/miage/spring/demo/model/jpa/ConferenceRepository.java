package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Conference findBytitleconf(String titleconf);


}
