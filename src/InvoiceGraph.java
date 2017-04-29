import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static util.LogRecord.logger;

///**
// * Created by SPREADTRUM\jiannan.liu on 17-3-28.
// */
public class InvoiceGraph extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        logger.config("[config]==========START INIT UI");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Scence/tempUI.fxml"));
        } catch (IOException e) {
            logger.warning("[warning]==========UI fxml MISSING");
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.config("[config]==========FINISH INIT UI");
    }
}
