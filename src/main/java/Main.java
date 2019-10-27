import user.UserController;
import user.UserDao;
import util.JsonTransformer;

import static spark.Spark.*;


public class Main {
    public static UserDao userDao;

    public static void main(String[] args) {
        userDao = new UserDao();

        port(8000);
        get("/", (req, res) -> "Hello, world!");
        get("/users/:username", new UserController().findUser, new JsonTransformer());
    }
}
