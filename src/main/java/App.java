
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author: piotr
 * @version: 2.0 2014-01-19
 */
public class App extends Application {
    public static void main(String[] args) {
        // TODO: Init table by User class
        // CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name CHAR(20), registered DATE);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("app/users_list.fxml"));

        Scene scene = new Scene(root, 400, 350);

        stage.setTitle("Users manager");
        stage.setScene(scene);
        stage.show();
    }
}
