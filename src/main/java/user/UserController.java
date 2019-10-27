package user;

import com.ftpix.sparknnotation.annotations.SparkController;
import com.ftpix.sparknnotation.annotations.SparkGet;
import com.ftpix.sparknnotation.annotations.SparkParam;
import spark.Response;
import util.JsonTransformer;

@SparkController("user")
public class UserController {
    private UserDao userDao;

    public UserController() {
        userDao = new UserDao();
    }

    @SparkGet(value = "/:username", transformer = JsonTransformer.class)
    public User getUser(@SparkParam("username") String username, Response response) {
        response.type("application/json");
        User user = userDao.getUserByUsername(username);
        return user;
    }
}
