package miage.spring.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import miage.spring.demo.model.Status;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.StatusRepository;
import miage.spring.demo.model.jpa.UserRepository;

@Component
public class StatusDataInitializer implements CommandLineRunner {

    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StatusDataInitializer(StatusRepository statusRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        addStatusIfMissing("Etudiant");
        addStatusIfMissing("Chercheur");
        addStatusIfMissing("Industriel");
        addTestUserIfMissing();
    }

    private void addStatusIfMissing(String statusName) {
        if (statusRepository.findByNomStatutIgnoreCase(statusName) == null) {
            statusRepository.save(new Status(statusName));
        }
    }

    private void addTestUserIfMissing() {
        if (userRepository.findByEmail("demo@test.com") != null) {
            return;
        }

        Status status = statusRepository.findByNomStatutIgnoreCase("Etudiant");
        if (status == null) {
            return;
        }

        User user = new User();
        user.setName("Demo");
        user.setEmail("demo@test.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setStatus(status);

        userRepository.save(user);
    }
}
