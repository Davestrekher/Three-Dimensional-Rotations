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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
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
import math.OperacaoQuaternios;
import math.Ponto2D;
import math.Ponto3D;
import math.Projection;
import math.Quaternio;
import math.Rotacao;
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
  @FXML
  private Slider sliderRotacao;
  @FXML
  private ChoiceBox<String> cbEixo;
  @FXML
  private Spinner<Double> spinnerX;
  @FXML
  private Spinner<Double> spinnerY;
  @FXML
  private Spinner<Double> spinnerZ;
  private GraphicsContext graphics;
  private ArrayList<Ponto3D> eixoX;
  private ArrayList<Ponto3D> eixoY;
  private ArrayList<Ponto3D> eixoZ;
  private ArrayList<Ponto3D> objeto;
  private ArrayList<Ponto3D> objetoInicial;
  private double offsetX;
  private double offsetY;

  private double anguloRotacaoX;
  private double anguloRotacaoY;
  private double anguloRotacaoZ;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    graphics = canvas.getGraphicsContext2D();
    offsetX = canvas.getWidth() / 2;
    offsetY = canvas.getHeight() / 2;
    eixoX = new ArrayList<>();
    eixoY = new ArrayList<>();
    eixoZ = new ArrayList<>();
    objeto = new ArrayList<>();
    objetoInicial = new ArrayList<>();

    cbEixo.setItems(FXCollections.observableArrayList("X", "Y", "Z"));

    cbEixo.setValue("X");
    cbEixo.getSelectionModel().selectedIndexProperty().addListener((obs, indiceAntigo, indiceNovo) -> {
      sliderRotacao.setValue(selecionarEixo((int) indiceNovo));
    });

    sliderRotacao.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {
      rotacionarObjeto(sliderRotacao.getValue(),
          cbEixo.getSelectionModel().getSelectedIndex());
    });

    anguloRotacaoX = 0;
    anguloRotacaoY = 0;
    anguloRotacaoZ = 0;

    render();
  }

  @FXML
  public void teste(ActionEvent e) {
    eixoX.addAll(DesenharFormas.drawLine(new Ponto3D(0, -300, 0), new Ponto3D(0, 300, 0)));
    eixoY.addAll(DesenharFormas.drawLine(new Ponto3D(-300, 0, 0), new Ponto3D(300, 0, 0)));
    eixoZ.addAll(DesenharFormas.drawLine(new Ponto3D(0, 0, -300), new Ponto3D(0, 0, 300)));
  }

  @FXML
  public void teste2(ActionEvent e) {
    objeto.addAll(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0), 100));
    objetoInicial.addAll(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0),
        100));

    // objeto.addAll(DesenharFormas.reta(new Ponto3D(1.0,
    // 1.0, 1.0), 100.0));
    // objetoInicial.addAll(DesenharFormas.reta(new Ponto3D(1.0,
    // 1.0, 1.0), 100.0));

    // objeto.addAll(DesenharFormas.desenharPlanoRaso(100, 100, new Ponto3D(-50, 0,
    // -50), 1000));
  }

  private void drawPoint(Ponto2D ponto, double tamanho, Color cor) {
    graphics.setFill(cor);
    graphics.fillRect(ponto.getX() + offsetX, offsetY - ponto.getY(), 2, 2);
  }

  private void render() {
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderPoints(eixoX, Color.RED);
        renderPoints(eixoY, Color.BLUE);
        renderPoints(eixoZ, Color.GREEN);
        renderPoints(objeto, Color.BLACK);
      }
    }.start();
  }

  private void renderPoints(ArrayList<Ponto3D> p, Color cor) {
    for (Ponto3D ponto3D : p) {
      Ponto2D ponto2D = Projection.project(ponto3D, sliderHorizontal.getValue(), sliderVertical.getValue());
      drawPoint(ponto2D, 2, cor);
    }
  }

  private void rotacionarObjeto(double angulo, int eixo) {
    switch (eixo) {
      case 0:
        anguloRotacaoX = angulo;
        break;
      case 1:
        anguloRotacaoY = angulo;
        break;
      case 2:
        anguloRotacaoZ = angulo;
        break;
      default:
        anguloRotacaoX = angulo;
    }
    objeto = Rotacao.angulosDeEuler(objetoInicial, anguloRotacaoX, anguloRotacaoY, anguloRotacaoZ);
  }

  private double selecionarEixo(int eixo) {
    switch (eixo) {
      case 0:
        return anguloRotacaoX;
      case 1:
        return anguloRotacaoY;
      case 2:
        return anguloRotacaoZ;
      default:
        return anguloRotacaoX;
    }
  }

}
