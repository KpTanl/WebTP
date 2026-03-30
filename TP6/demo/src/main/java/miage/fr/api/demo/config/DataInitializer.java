package miage.fr.api.demo.config;

import miage.fr.api.demo.Entity.User;
import miage.fr.api.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeDefaultUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.default-user.name}") String defaultUsername,
            @Value("${app.security.default-user.password}") String defaultPassword) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                if (userRepository.findByName(defaultUsername).isPresent()) {
                    return;
                }

                User user = new User();
                user.setName(defaultUsername);
                user.setPassword(passwordEncoder.encode(defaultPassword));
                userRepository.save(user);
            }
        };
    }
}
