import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    @Test
    void shouldAuthenticateUserWithCorrectLoginAndPassword() {
        IUserRepository userRepository = new UserRepositoryImpl();
        Authentication authentication = new Authentication(userRepository);

        User user = authentication.authenticateLogs("admin", "admin123");

        assertNotNull(user);
        assertEquals("admin", user.getLogin());
    }

    @Test
    void shouldNotAuthenticateUserWithWrongPassword() {
        IUserRepository userRepository = new UserRepositoryImpl();
        Authentication authentication = new Authentication(userRepository);

        User user = authentication.authenticateLogs("admin", "zlehaslo");

        assertNull(user);
    }

    @Test
    void shouldNotAuthenticateNonExistingUser() {
        IUserRepository userRepository = new UserRepositoryImpl();
        Authentication authentication = new Authentication(userRepository);

        User user = authentication.authenticateLogs("brak", "admin123");

        assertNull(user);
    }

    @Test
    void hashPasswordShouldReturnSameHashForSameInput() {
        String hash1 = Hasher.hashPassword("admin123");
        String hash2 = Hasher.hashPassword("admin123");

        assertEquals(hash1, hash2);
    }
}