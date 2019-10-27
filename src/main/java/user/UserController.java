package user;


import spark.Response;
import spark.Route;
import spark.Request;

public class UserController {
    private UserDao userDao;

    public UserController() {
        userDao = new UserDao();
    }

    public Route findUser = (Request request, Response response) -> {
        response.type("application/json");
        User user = userDao.getUserByUsername(request.params(":username"));
        return user;
    };
}
