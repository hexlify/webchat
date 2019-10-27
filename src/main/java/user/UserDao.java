package user;

import com.google.common.collect.ImmutableList;

import java.util.List;


public class UserDao {
    private final List<User> users = ImmutableList.of(
            new User("TestUser1", "testuser1@email.net", "qwerty01"),
            new User("TestUser2", "testuser2@email.net", "qwerty02"),
            new User("TestUser3", "testuser3@email.net", "qwerty03")
    );

    public User getUserByEmail(String email) {
        return users.stream().filter(x -> x.email.equals(email)).findFirst().orElse(null);
    }

    public User getUserByUsername(String username) {
        return users.stream().filter(x -> x.username.equals(username)).findFirst().orElse(null);
    }
}