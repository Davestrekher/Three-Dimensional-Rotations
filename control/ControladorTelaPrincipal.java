package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.animation.AnimationTimer;
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
import javafx.scene.control.Slider;
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
import math.DesenharFormas;
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
  @FXML
  private Slider sliderHorizontal;
  @FXML
  private Slider sliderVertical;
  private GraphicsContext graphics;
  private ArrayList<Ponto3D> pontos;
  private double offsetX;
  private double offsetY;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    graphics = canvas.getGraphicsContext2D();
    offsetX = canvas.getWidth() / 2;
    offsetY = canvas.getHeight() / 2;
    pontos = new ArrayList<>();
    renderLines();
  }

  @FXML
  public void teste(ActionEvent e) {
    drawLine(new Ponto3D(0, -300, 0), new Ponto3D(0, 300, 0));
    drawLine(new Ponto3D(-300, 0, 0), new Ponto3D(300, 0, 0));
    drawLine(new Ponto3D(0, 0, -300), new Ponto3D(0, 0, 300));
  }

  @FXML
  public void teste2(ActionEvent e) {
    //addPoints(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0), 100));
    addPoints(DesenharFormas.desenharPlanoRaso(100,100, new Ponto3D(-50, 0, -50), 1000));
  }

  private void drawPoint(Ponto2D ponto, double tamanho) {
    graphics.fillRect(ponto.getX() + offsetX, offsetY - ponto.getY(), 2, 2);
  }


  private void drawLine(Ponto3D pontoA, Ponto3D pontoB) {
      Ponto3D vetorDiretor = new Ponto3D(pontoB.getX() - pontoA.getX(), pontoB.getY() - pontoA.getY(),
      pontoB.getZ() - pontoA.getZ());
      for (double i = 0; i < 1.0; i += 0.001) {
           Ponto3D ponto3D = new Ponto3D(pontoA.getX() + (vetorDiretor.getX() * i),
           pontoA.getY() + (vetorDiretor.getY() * i), pontoA.getZ() + (vetorDiretor.getZ() * i));
           pontos.add(ponto3D);
      }
  }

  private void renderLines() {
     new AnimationTimer() {
      @Override
      public void handle(long now) {
           graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
           for (Ponto3D ponto3D : pontos) {
              Ponto2D ponto2D = Projection.project(ponto3D, sliderHorizontal.getValue(), sliderVertical.getValue());
              drawPoint(ponto2D, 2);
           }
      }
    }.start();
  }

  private void addPoints(ArrayList<Ponto3D> pontos3D) {
    Platform.runLater(() -> {
      for (Ponto3D pontoNovos : pontos3D) {
        pontos.add(pontoNovos);
      }
    });
  }


/*****************************************************************************
*  public void drawPointColorido(Ponto2D ponto, double tamanho) {
*
*    graphics.fillRect(ponto.getX() + offsetX, offsetY - ponto.getY(), 2, 2);
*
*  }
*****************************************************************************/

}
