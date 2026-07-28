package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControladorTelaPrincipal {
  @FXML
  AnchorPane raiz;

  @FXML
  public void teste(ActionEvent e) {
    Scene scene = ((Node) e.getSource()).getScene();

    Camera camera = new PerspectiveCamera(true);
    scene.setCamera(camera);
  }
}
