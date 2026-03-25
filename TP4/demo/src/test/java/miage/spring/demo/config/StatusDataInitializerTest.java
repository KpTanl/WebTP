package miage.spring.demo.config;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import miage.spring.demo.model.Status;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.StatusRepository;
import miage.spring.demo.model.jpa.UserRepository;

@ExtendWith(MockitoExtension.class)
class StatusDataInitializerTest {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void runAddsOnlyMissingDefaultStatuses() throws Exception {
        StatusDataInitializer initializer = new StatusDataInitializer(statusRepository, userRepository, passwordEncoder);

        when(statusRepository.findByNomStatutIgnoreCase("Etudiant")).thenReturn(null);
        when(statusRepository.findByNomStatutIgnoreCase("Chercheur")).thenReturn(new Status("Chercheur"));
        when(statusRepository.findByNomStatutIgnoreCase("Industriel")).thenReturn(null);
        when(userRepository.findByEmail("demo@test.com")).thenReturn(new User());

        initializer.run();

        verify(statusRepository).save(argThat(status -> "Etudiant".equals(status.getNomStatut())));
        verify(statusRepository).save(argThat(status -> "Industriel".equals(status.getNomStatut())));
        verify(statusRepository, never()).save(argThat(status -> "Chercheur".equals(status.getNomStatut())));
    }

    @Test
    void runAddsTestUserWhenMissing() throws Exception {
        StatusDataInitializer initializer = new StatusDataInitializer(statusRepository, userRepository, passwordEncoder);
        Status etudiant = new Status("Etudiant");
        etudiant.setCodeStatut(1L);

        when(statusRepository.findByNomStatutIgnoreCase("Etudiant")).thenReturn(etudiant);
        when(statusRepository.findByNomStatutIgnoreCase("Chercheur")).thenReturn(new Status("Chercheur"));
        when(statusRepository.findByNomStatutIgnoreCase("Industriel")).thenReturn(new Status("Industriel"));
        when(userRepository.findByEmail("demo@test.com")).thenReturn(null);
        when(passwordEncoder.encode("1234")).thenReturn("HASH_1234");

        initializer.run();

        verify(userRepository).save(argThat(user ->
                "Demo".equals(user.getName())
                        && "demo@test.com".equals(user.getEmail())
                        && "HASH_1234".equals(user.getPassword())
                        && user.getStatus() == etudiant));
    }
}
