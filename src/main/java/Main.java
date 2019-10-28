import com.ftpix.sparknnotation.Sparknotation;
import com.google.common.collect.ImmutableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import user.User;
import user.UserController;
import user.UserDao;

import java.io.IOException;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;


public class Main {
    private static final List<User> users = ImmutableList.of(
            new User("TestUser1", "testuser1@email.net", "qwerty01"),
            new User("TestUser2", "testuser2@email.net", "qwerty02"),
            new User("TestUser3", "testuser3@email.net", "qwerty03")
    );
    public static SessionFactory sessionFactory;

    public static void main(String[] args) throws IOException {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        initializeDb();
        port(8000);
        get("/", (req, res) -> "Hello, world!");
        Sparknotation.init(new UserController(new UserDao(sessionFactory)));
    }

    private static void initializeDb() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            for (User user : users) {
                session.save(user);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}