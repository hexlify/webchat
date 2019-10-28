package user;

import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;
import spark.Response;
import util.JsonTransformer;


@SparkController("user")
public class UserController {
    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @SparkGet(value = "/e/:email", transformer = JsonTransformer.class)
    public User findUserByEmail(@SparkParam("email") String email, Response response) {
        response.type("application/json");
        return userDao.findByEmail(email);
    }

    @SparkGet(value = "/:username", transformer = JsonTransformer.class)
    public User findUserByUsername(@SparkParam("username") String username, Response response) {
        response.type("application/json");
        return userDao.findByUsername(username);
    }
}
