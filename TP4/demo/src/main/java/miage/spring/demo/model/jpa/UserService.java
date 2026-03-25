package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import miage.spring.demo.model.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User authenticate(String email, String rawPassword) {
        User user = findByEmail(email);
        if (user == null || isBlank(rawPassword) || isBlank(user.getPassword())) {
            return null;
        }

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }

        return null;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean emailExistsForAnotherUser(Long id, String email) {
        User existingUser = findByEmail(email);
        return existingUser != null && !existingUser.getId().equals(id);
    }

    public List<User> findByStatusCode(Long codeStatut) {
        return userRepository.findByStatus_CodeStatut(codeStatut);
    }

    public User addUser(User user) {
        encodePasswordIfNecessary(user);
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        mergeWithExistingUser(user);
        encodePasswordIfNecessary(user);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void mergeWithExistingUser(User user) {
        if (user.getId() == null) {
            return;
        }

        User current = findById(user.getId());
        if (current == null) {
            return;
        }

        if (isBlank(user.getName())) {
            user.setName(current.getName());
        }
        if (isBlank(user.getEmail())) {
            user.setEmail(current.getEmail());
        }
        if (isBlank(user.getPassword())) {
            user.setPassword(current.getPassword());
        }
        if (user.getStatus() == null) {
            user.setStatus(current.getStatus());
        }
        if (user.getConferencesParticipated() == null || user.getConferencesParticipated().isEmpty()) {
            user.setConferencesParticipated(current.getConferencesParticipated());
        }
        if (user.getConferencesOrganized() == null || user.getConferencesOrganized().isEmpty()) {
            user.setConferencesOrganized(current.getConferencesOrganized());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void encodePasswordIfNecessary(User user) {
        if (isBlank(user.getPassword()) || looksLikeBcryptHash(user.getPassword())) {
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private boolean looksLikeBcryptHash(String value) {
        return value.startsWith("$2a$")
                || value.startsWith("$2b$")
                || value.startsWith("$2y$");
    }
}
