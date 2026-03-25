package miage.spring.demo.model.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import miage.spring.demo.model.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void authenticateReturnsUserWhenPasswordMatches() {
        User user = new User();
        user.setEmail("alice@example.com");
        user.setPassword("$2a$hash");

        when(userRepository.findByEmail("alice@example.com")).thenReturn(user);
        when(passwordEncoder.matches("secret", "$2a$hash")).thenReturn(true);

        User authenticatedUser = userService.authenticate("alice@example.com", "secret");

        assertThat(authenticatedUser).isSameAs(user);
    }

    @Test
    void authenticateReturnsNullWhenPasswordDoesNotMatch() {
        User user = new User();
        user.setEmail("alice@example.com");
        user.setPassword("$2a$hash");

        when(userRepository.findByEmail("alice@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "$2a$hash")).thenReturn(false);

        User authenticatedUser = userService.authenticate("alice@example.com", "wrong");

        assertThat(authenticatedUser).isNull();
    }
}
