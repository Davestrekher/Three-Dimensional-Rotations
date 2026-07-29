package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
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
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import math.Ponto2D;
import math.Ponto3D;
import math.Projection;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControladorTelaPrincipal implements Initializable {
  @FXML
  private AnchorPane raiz;

  @FXML
  private Canvas canvas;

  private double offsetX;
  private double offsetY;

  private GraphicsContext graphics;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    graphics = canvas.getGraphicsContext2D();

    offsetX = canvas.getWidth() / 2;
    offsetY = canvas.getHeight() / 2;
  }

  @FXML
  public void teste(ActionEvent e) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // gc.strokeLine(x1, y1, x2, y2);
    drawLine(new Ponto3D(0, -offsetY, 0), new Ponto3D(0, offsetY, 0));
    drawLine(new Ponto3D(-300, 0, 0), new Ponto3D(300, 0, 0));
    drawLine(new Ponto3D(0, 0, -offsetX), new Ponto3D(0, 0, offsetX));
  }

  @FXML
  public void teste2(ActionEvent e) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // gc.strokeLine(x1, y1, x2, y2);
    // erasePoint(new Point2D(0, 0), 5);
  }

  public void drawPoint(Ponto2D ponto, double tamanho) {

    graphics.setFill(Color.BLACK);
    // Parameters: (x-coordinate, y-coordinate, width, height)
    graphics.fillOval(ponto.getX() + offsetX, offsetY - ponto.getY(), tamanho, tamanho);

    // 4. Draw an outlined circle
    graphics.setStroke(Color.BLACK);
    graphics.setLineWidth(tamanho);
    graphics.strokeOval(ponto.getX() + offsetX, offsetY - ponto.getY(), tamanho, tamanho);

  }

  public void erasePoint(Ponto2D ponto, double tamanho) {
    // Makes sure the outline is fully erased
    tamanho++;

    graphics.setFill(Color.WHITE);
    // Parameters: (x-coordinate, y-coordinate, width, height)
    graphics.fillOval(ponto.getX() + offsetX, offsetY - ponto.getY(), tamanho, tamanho);

    // 4. Draw an outlined circle
    graphics.setStroke(Color.WHITE);
    graphics.setLineWidth(tamanho);
    graphics.strokeOval(ponto.getX() + offsetX, offsetY - ponto.getY(), tamanho, tamanho);

  }

  public void drawLine(Ponto3D pontoA, Ponto3D pontoB) {
    Ponto3D vetorDiretor = new Ponto3D(pontoB.getX() - pontoA.getX(), pontoB.getY() - pontoA.getY(),
        pontoB.getZ() - pontoA.getZ());
    for (double i = 0; i < 1.0; i += 0.001) {
      Ponto3D ponto3D = new Ponto3D(pontoA.getX() + (vetorDiretor.getX() * i),
          pontoA.getY() + (vetorDiretor.getY() * i), pontoA.getZ() + (vetorDiretor.getZ() * i));

      Ponto2D ponto2D = Projection.project(ponto3D);

      drawPoint(ponto2D, 2);
    }

  }
}
