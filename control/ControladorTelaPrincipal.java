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
import math.ConjuntoPontos;

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
  // @FXMLprivate ChoiceBox<String> cbProjecao;
  @FXML
  private ChoiceBox<String> cbRotacao;
  @FXML
  private ChoiceBox<String> cbObjeto;
  @FXML
  private TextField tfPosicaoObjetoX;
  @FXML
  private TextField tfPosicaoObjetoY;
  @FXML
  private TextField tfPosicaoObjetoZ;
  @FXML
  private TextField tfTamanhoObjeto;
  @FXML
  private TextField tfPosicaoRetaX;
  @FXML
  private TextField tfPosicaoRetaY;
  @FXML
  private TextField tfPosicaoRetaZ;
  @FXML
  private TextField tfTamanhoReta;
  @FXML
  private CheckBox checkRotacionar;

  private GraphicsContext graphics;
  private ConjuntoPontos eixoX;
  private ConjuntoPontos eixoY;
  private ConjuntoPontos eixoZ;
  private ConjuntoPontos plano;
  private ConjuntoPontos sphere;
  private ConjuntoPontos reta;
  private double offsetX;
  private double offsetY;
  private double mouseX;
  private double mouseY;
  private ArrayList<ConjuntoPontos> conjuntoPontos;
  private Observador camera;
  private double anguloRotacaoX;
  private double anguloRotacaoY;
  private double anguloRotacaoZ;

  private double anguloRotacaoObjetoX;
  private double anguloRotacaoObjetoY;
  private double anguloRotacaoObjetoZ;

  private final int ESFERA = 0;
  private final int PLANO = 1;

  public ControladorTelaPrincipal() {
    conjuntoPontos = new ArrayList<ConjuntoPontos>();
  }

  @Override
  public void initialize(URL url, ResourceBundle resource) {
    graphics = canvas.getGraphicsContext2D();
    offsetX = canvas.getWidth() / 2;
    offsetY = canvas.getHeight() / 2;
    camera = new Observador(canvas.getHeight(), Math.toRadians(40));
    estabelecerSlider();
    canvas.setOnScroll(event -> {
      double delta = event.getDeltaY();

      if (delta > 0) {
        camera.afastar();
      } else if (delta < 0) {
        camera.aproximar();
      }
    });

    anguloRotacaoX = 0;
    anguloRotacaoY = 0;
    anguloRotacaoZ = 0;

    anguloRotacaoObjetoX = 0;
    anguloRotacaoObjetoY = 0;
    anguloRotacaoObjetoZ = 0;

    estabelecerTextFields();

    estabelerChoiceBox();

    checkRotacionar.selectedProperty().addListener((observable, antigo, marcado) -> {

      cbEixo.setDisable(marcado);
      cbEixo.setOpacity(marcado ? 0.5 : 1.0);

    });

    canvas.setOnMousePressed(event -> {
      mouseX = event.getX();
      mouseY = event.getY();
    });

    canvas.setOnMouseDragged(event -> {

      double deltaX = event.getX() - mouseX;
      double deltaY = event.getY() - mouseY;

      double sensibilidade = 0.01;

      anguloRotacaoY += deltaX * sensibilidade;

      anguloRotacaoX += deltaY * sensibilidade;

      mouseX = event.getX();
      mouseY = event.getY();

      // rotacionarGeral();
    });

  }

  @FXML
  public void desenharEixos(ActionEvent e) {
    adicionarEixo();
    adicionarPlano();
    render();
  }

  @FXML
  public void desenharObjeto(ActionEvent e) {

    switch (cbObjeto.getSelectionModel().getSelectedIndex()) {
      case ESFERA:
        double x = getValorTf(tfPosicaoObjetoX);
        double y = getValorTf(tfPosicaoObjetoY);
        double z = getValorTf(tfPosicaoObjetoZ);
        double raio = getValorTf(tfTamanhoObjeto);
        sphere = new ConjuntoPontos(DesenharFormas.desenharEsfera(new Ponto3D(x, y, z), raio));
        break;
      case PLANO:

        break;

      default:
        break;
    }
    // reta = new ConjuntoPontos(DesenharFormas.reta(new
    // Ponto3D(spinnerX.getValue(), spinnerY.getValue() , spinnerZ.getValue()),
    // 50));
  }

  @FXML
  public void apagarObjeto(ActionEvent e) {
    switch (cbObjeto.getSelectionModel().getSelectedIndex()) {
      case ESFERA:
        sphere = null;
        break;
      case PLANO:

        break;

      default:
        break;
    }
  }

  @FXML
  public void desenharReta(ActionEvent e) {
    double x1 = -getValorTf(tfPosicaoRetaX) * getValorTf(tfTamanhoReta);
    double y1 = -getValorTf(tfPosicaoRetaY) * getValorTf(tfTamanhoReta);
    double z1 = -getValorTf(tfPosicaoRetaZ) * getValorTf(tfTamanhoReta);

    double x2 = getValorTf(tfPosicaoRetaX) * getValorTf(tfTamanhoReta);
    double y2 = getValorTf(tfPosicaoRetaY) * getValorTf(tfTamanhoReta);
    double z2 = getValorTf(tfPosicaoRetaZ) * getValorTf(tfTamanhoReta);

    reta = new ConjuntoPontos(DesenharFormas.drawLine(new Ponto3D(x1, y1, z1), new Ponto3D(x2, y2, z2)));
  }

  @FXML
  public void apagarReta(ActionEvent e) {
    reta = null;
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
        renderPoints(plano, Color.PINK);
        renderPoints(sphere, Color.BLACK);
        renderPoints(reta, Color.PURPLE);
      }
    }.start();
  }

  private void renderPoints(ConjuntoPontos lista, Color cor) {
    ArrayList<Ponto3D> p = (lista != null) ? lista.getPonto() : null;

    if (lista != null) {
      for (Ponto3D ponto3D : p) {
        Ponto2D ponto2D = camera.projecaoPerspectiva(ponto3D, anguloRotacaoY, anguloRotacaoX);
        drawPoint(ponto2D, 2, cor);
      }
    }
  }

  private void rotacionarObjeto(double angulo, int eixo) {

    switch (cbRotacao.getSelectionModel().getSelectedIndex()) {
      default:
        break;
    }
    /**
     * switch (eixo) {
     * case 0:
     * anguloRotacaoX = angulo;
     * break;
     * case 1:
     * anguloRotacaoY = angulo;
     * break;
     * case 2:
     * anguloRotacaoZ = angulo;
     * break;
     * default:
     * anguloRotacaoX = angulo;/
     * }
     **/

    switch (eixo) {
      case 0:
        anguloRotacaoObjetoX = angulo;
        break;
      case 1:
        anguloRotacaoObjetoY = angulo;
        break;
      case 2:
        anguloRotacaoObjetoZ = angulo;
        break;
      default:
        break;
    }

    double x = getValorTf(tfPosicaoRetaX);
    double y = getValorTf(tfPosicaoRetaY);
    double z = getValorTf(tfPosicaoRetaZ);
    if (x != 0 || y != 0 || z != 0)
      // sphere.setPonto(
      // Rotacao.rotacionarTornoReta(sphere.getPontoInicial(), new Ponto3D(x, y, z),
      // sliderRotacao.getValue()));
      reta.setPonto(Rotacao.angulosDeEuler(reta.getPontoInicial(), anguloRotacaoObjetoX, anguloRotacaoObjetoY,
          anguloRotacaoObjetoZ));

  }

  private void rotacionarGeral() {
    /*
     * eixoX.setPonto(Rotacao.angulosDeEuler(eixoX.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     * eixoY.setPonto(Rotacao.angulosDeEuler(eixoY.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     * eixoZ.setPonto(Rotacao.angulosDeEuler(eixoZ.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     * plano.setPonto(Rotacao.angulosDeEuler(plano.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     * if(sphere != null)
     * sphere.setPonto(Rotacao.angulosDeEuler(sphere.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     * if(reta != null) reta.setPonto(Rotacao.angulosDeEuler(reta.getPontoInicial(),
     * anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
     */
    /*
     * eixoX = Rotacao.rotacionarUsandoQuaternios(eixoXInicial,
     * new Ponto3D(spinnerX.getValue(), spinnerY.getValue(), spinnerZ.getValue()),
     * sliderRotacao.getValue());
     * eixoY = Rotacao.rotacionarUsandoQuaternios(eixoYInicial,
     * new Ponto3D(spinnerX.getValue(), spinnerY.getValue(), spinnerZ.getValue()),
     * sliderRotacao.getValue());
     * eixoZ = Rotacao.rotacionarUsandoQuaternios(eixoZInicial,
     * new Ponto3D(spinnerX.getValue(), spinnerY.getValue(), spinnerZ.getValue()),
     * sliderRotacao.getValue());
     */

    // objeto = Rotacao.angulosDeEulerReta(new Ponto3D(100, 100, 100),
    // anguloRotacaoX,
    // objetoInicial);

  }

  private void adicionarEixo() {
    eixoX = new ConjuntoPontos(DesenharFormas.drawLine(new Ponto3D(0, -85, 0), new Ponto3D(0, 85, 0)));
    eixoY = new ConjuntoPontos(DesenharFormas.drawLine(new Ponto3D(-85, 0, 0), new Ponto3D(85, 0, 0)));
    eixoZ = new ConjuntoPontos(DesenharFormas.drawLine(new Ponto3D(0, 0, -85), new Ponto3D(0, 0, 85)));
  }

  private void adicionarPlano() {
    plano = new ConjuntoPontos(DesenharFormas.desenharPlanoRaso(170, 170, new Ponto3D(-85, 0, -85), 5000));
  }

  private void estabelecerSlider() {
    sliderRotacao.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      rotacionarObjeto(sliderRotacao.getValue(), cbEixo.getSelectionModel().getSelectedIndex());
    });

    sliderHorizontal.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoY = sliderHorizontal.getValue();
      rotacionarGeral();
    });

    sliderVertical.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoX = sliderVertical.getValue();
      rotacionarGeral();
    });

    sliderProfundidade.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoZ = sliderVertical.getValue();
      rotacionarGeral();
    });
  }

  private void estabelerChoiceBox() {
    cbEixo.setItems(FXCollections.observableArrayList("X", "Y", "Z"));
    cbEixo.setValue("X");
    cbEixo.getSelectionModel().selectedIndexProperty().addListener((obs, indiceAntigo, indiceNovo) -> {
      sliderRotacao.setValue(selecionarEixo((int) indiceNovo));
    });

    // cbProjecao.setItems(FXCollections.observableArrayList("Projecao perspectiva",
    // "Projecao ortografica"));
    // cbProjecao.setValue("Projecao perspectiva");

    cbRotacao.setItems(FXCollections.observableArrayList("Angulos de Euler", "Quaternios"));
    cbRotacao.setValue("Angulos de Euler");

    cbObjeto.setItems(FXCollections.observableArrayList("Esfera", "Plano"));
    cbObjeto.setValue("Esfera");

  }

  private void estabelecerTextFields() {
    tfPosicaoObjetoX.setText("0");
    tfPosicaoObjetoY.setText("0");
    tfPosicaoObjetoZ.setText("0");
    tfTamanhoObjeto.setText("25");

    tfPosicaoRetaX.setText("0");
    tfPosicaoRetaY.setText("0");
    tfPosicaoRetaZ.setText("0");
    tfTamanhoReta.setText("0");
  }

  private double selecionarEixo(int eixo) {
    switch (eixo) {
      case 0:
        return anguloRotacaoObjetoX;
      case 1:
        return anguloRotacaoObjetoY;
      case 2:
        return anguloRotacaoObjetoZ;
      default:
        return anguloRotacaoObjetoX;
    }
  }

  private double getValorTf(TextField tf) {
    double valor = 0;
    try {
      valor = Double.parseDouble(tf.getText());
    } catch (NumberFormatException | NullPointerException e) {
      // Usuário ainda está digitando um valor incompleto
      System.out.println("Digite um numero valido.");
    }
    return valor;
  }
}
