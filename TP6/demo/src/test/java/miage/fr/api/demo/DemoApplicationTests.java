package miage.fr.api.demo;

import miage.fr.api.demo.Entity.FilmEntity;
import miage.fr.api.demo.Entity.User;
import miage.fr.api.demo.repository.FilmRepository;
import miage.fr.api.demo.repository.UserFilmRepository;
import miage.fr.api.demo.repository.UserRepository;
import miage.fr.api.demo.service.UserFilmService;
import miage.fr.api.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private UserFilmService userFilmService;

	@Autowired
	private FilmRepository filmRepository;

	@Autowired
	private UserFilmRepository userFilmRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void defaultUserIsInitialized() {
		User user = userRepository.findByName("admin").orElseThrow();
		Assertions.assertTrue(passwordEncoder.matches("admin123", user.getPassword()));
	}

	@Test
	void registerCreatesNewUser() {
		String username = "new-user-test";
		User existingUser = userRepository.findByName(username).orElse(null);
		if (existingUser != null) {
			userRepository.delete(existingUser);
		}

		User user = userService.register(username, "secret123");

		Assertions.assertEquals(username, user.getName());
		Assertions.assertTrue(passwordEncoder.matches("secret123", user.getPassword()));
	}

	@Test
	void favoriteCanBeAdded() {
		String username = "favorite-user-test";
		userFilmRepository.deleteAll();
		User existingUser = userRepository.findByName(username).orElse(null);
		if (existingUser != null) {
			userRepository.delete(existingUser);
		}
		User user = userService.register(username, "secret123");

		FilmEntity film = new FilmEntity(99999L, "Test Film", "2026-03-26", 8.5, "/poster.jpg", "Overview");
		filmRepository.save(film);

		userFilmService.addToFavorite(user.getId(), film.getId());
		Assertions.assertTrue(userFilmService.isFavorite(user.getId(), film.getId()));
	}

}
