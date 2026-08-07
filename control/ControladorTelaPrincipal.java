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
import javafx.scene.control.SpinnerValueFactory;
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
import math.Observador;
import math.OperacaoQuaternios;
import math.Ponto2D;
import math.Ponto3D;
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
  private Slider sliderProfundidade;
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
  private ArrayList<Ponto3D> eixoXInicial;
  private ArrayList<Ponto3D> eixoYInicial;
  private ArrayList<Ponto3D> eixoZInicial;
  private ArrayList<Ponto3D> objeto;
  private ArrayList<Ponto3D> objetoInicial;
  private ArrayList<Ponto3D> plano;
  private ArrayList<Ponto3D> reta;
  private double offsetX;
  private double offsetY;

  private Observador camera;

  private double anguloRotacaoX;
  private double anguloRotacaoY;
  private double anguloRotacaoZ;

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    graphics = canvas.getGraphicsContext2D();
    offsetX = canvas.getWidth() / 2;
    offsetY = canvas.getHeight() / 2;
    eixoX = new ArrayList<Ponto3D>();
    eixoY = new ArrayList<Ponto3D>();
    eixoZ = new ArrayList<Ponto3D>();
    eixoXInicial = new ArrayList<Ponto3D>();
    eixoYInicial = new ArrayList<Ponto3D>();
    eixoZInicial = new ArrayList<Ponto3D>();
    objeto = new ArrayList<Ponto3D>();
    plano = new ArrayList<Ponto3D>();
    reta = new ArrayList<Ponto3D>();
    objetoInicial = new ArrayList<Ponto3D>();
    camera = new Observador(canvas.getHeight(), Math.toRadians(40));

    cbEixo.setItems(FXCollections.observableArrayList("X", "Y", "Z"));

    cbEixo.setValue("X");
    cbEixo.getSelectionModel().selectedIndexProperty().addListener((obs, indiceAntigo, indiceNovo) -> {
      sliderRotacao.setValue(selecionarEixo((int) indiceNovo));
    });

    sliderRotacao.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {
      rotacionarObjeto(sliderRotacao.getValue(),
          cbEixo.getSelectionModel().getSelectedIndex());
    });

    sliderHorizontal.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {
      rotacionarGeral();
    });

    sliderVertical.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {
      rotacionarGeral();
    });

    sliderProfundidade.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {
      rotacionarGeral();
    });

    anguloRotacaoX = 0;
    anguloRotacaoY = 0;
    anguloRotacaoZ = 0;

    SpinnerValueFactory<Double> valueFactoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 300, 0);
    SpinnerValueFactory<Double> valueFactoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 300, 0);
    SpinnerValueFactory<Double> valueFactoryZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 300, 0);

    spinnerX.setValueFactory(valueFactoryX);
    spinnerY.setValueFactory(valueFactoryY);
    spinnerZ.setValueFactory(valueFactoryZ);

    render();
  }

  @FXML
  public void teste(ActionEvent e) {
    eixoX.addAll(DesenharFormas.drawLine(new Ponto3D(0, -100, 0), new Ponto3D(0, 100, 0)));
    eixoY.addAll(DesenharFormas.drawLine(new Ponto3D(-100, 0, 0), new Ponto3D(100, 0, 0)));
    eixoZ.addAll(DesenharFormas.drawLine(new Ponto3D(0, 0, -100), new Ponto3D(0, 0, 100)));
    // plano.addAll(DesenharFormas.desenharPlanoRaso(200, 200, new Ponto3D(-100, 0,
    // -100), 5000));

    eixoXInicial.addAll(eixoX);
    eixoYInicial.addAll(eixoY);
    eixoZInicial.addAll(eixoZ);

  }

  @FXML
  public void teste2(ActionEvent e) {
    reta.addAll(DesenharFormas.drawLine(new Ponto3D(0, 0, 0),
        new Ponto3D(spinnerX.getValue() * 100, spinnerY.getValue() * 100, spinnerZ.getValue() * 100)));
    objeto.addAll(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0), 50));
    objetoInicial.addAll(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0),
        50));

    // objeto.addAll(DesenharFormas.reta(new Ponto3D(1.0,
    // 1.0, 1.0), 100.0));
    // objetoInicial.addAll(DesenharFormas.reta(new Ponto3D(1.0,
    // 1.0, 1.0), 100.0));
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
        renderPoints(plano, Color.PINK);
        renderPoints(reta, Color.PURPLE);
      }
    }.start();
  }

  private void renderPoints(ArrayList<Ponto3D> p, Color cor) {
    for (Ponto3D ponto3D : p) {
      // Ponto2D ponto2D = camera.projecaoPerspectiva(ponto3D,
      // sliderHorizontal.getValue(), sliderVertical.getValue());
      Ponto2D ponto2D = camera.projecaoPerspectiva(ponto3D, 0, 0);
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
    if (spinnerX.getValue() == null || spinnerY.getValue() == null || spinnerZ.getValue() == null) {
      System.out.println("deu pau");
      return;
    }
    objeto = Rotacao.rotacionarTornoReta(objetoInicial,
        new Ponto3D(spinnerX.getValue(), spinnerY.getValue(), spinnerZ.getValue()),
        anguloRotacaoX);

    // objeto = Rotacao.angulosDeEulerReta(new Ponto3D(100, 100, 100),
    // anguloRotacaoX,
    // objetoInicial);

  }

  private void rotacionarGeral() {

    eixoX = Rotacao.angulosDeEuler(eixoXInicial,
        sliderHorizontal.getValue(), sliderVertical.getValue(), sliderProfundidade.getValue());
    eixoY = Rotacao.angulosDeEuler(eixoYInicial,
        sliderHorizontal.getValue(), sliderVertical.getValue(), sliderProfundidade.getValue());
    eixoZ = Rotacao.angulosDeEuler(eixoZInicial,
        sliderHorizontal.getValue(), sliderVertical.getValue(), sliderProfundidade.getValue());

    // objeto = Rotacao.angulosDeEulerReta(new Ponto3D(100, 100, 100),
    // anguloRotacaoX,
    // objetoInicial);

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
