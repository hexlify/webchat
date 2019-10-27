import com.ftpix.sparknnotation.Sparknotation;

import java.io.IOException;

import static spark.Spark.get;
import static spark.Spark.port;


public class Main {
    public static void main(String[] args) throws IOException {
        port(8000);
        get("/", (req, res) -> "Hello, world!");
        Sparknotation.init();
    }
}