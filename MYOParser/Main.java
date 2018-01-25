import java.io.File;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main
  extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }
  
  public void start(Stage primaryStage)
    throws Exception
  {
    DirectoryChooser configFileChooser = new DirectoryChooser();
    File emgFolder = configFileChooser.showDialog(primaryStage);
    if (emgFolder != null)
    {
      new Parser(emgFolder);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Ferdig!!");
      alert.setHeaderText(null);
      alert.setContentText("Palimpalim!!");
      alert.showAndWait();
    }
    System.exit(0);
  }
}
