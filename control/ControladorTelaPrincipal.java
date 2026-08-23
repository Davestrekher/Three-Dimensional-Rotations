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
import math.Camera;
import math.OperacaoQuaternios;
import math.Ponto2D;
import math.Ponto3D;
import math.Quaternio;
import math.Rotacao;
import model.Benchmark;
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
  Spinner<Double> spinnerX;
  @FXML
  Spinner<Double> spinnerY;
  @FXML
  Spinner<Double> spinnerZ;

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
  private ArrayList<ConjuntoPontos> conjuntoPontos;
  private Camera camera;
  private Double anguloRotacaoX = 0.0;
  private Double anguloRotacaoY = 0.0;
  private Double anguloRotacaoZ = 0.0;

  private double anguloRotacaoObjetoX;
  private double anguloRotacaoObjetoY;
  private double anguloRotacaoObjetoZ;
  
  private AnguloRotacao ponteiroAnguloRotacaoX;
  private AnguloRotacao ponteiroAnguloRotacaoY;
  
  private Quaternio orientacao;
  private double delta;

  private int eixoAnterior;
  private ArrayList<Ponto3D> sphereAtual;

  private final int ESFERA = 0;
  private final int PLANO = 1;
  private final int RETA = 2;

  private final int EULER = 0;
  private final int QUATERNIOS = 1;

  private final int EIXO_X = 0;
  private final int EIXO_Y = 1;
  private final int EIXO_Z = 2;

  public ControladorTelaPrincipal() {
    ponteiroAnguloRotacaoX = new AnguloRotacao(0.0);
    ponteiroAnguloRotacaoY = new AnguloRotacao(0.0);
    conjuntoPontos = new ArrayList<ConjuntoPontos>();
  }

  @Override
  public void initialize(URL url, ResourceBundle resource) {
      graphics = canvas.getGraphicsContext2D();
      offsetX = canvas.getWidth() / 2;
      offsetY = canvas.getHeight() / 2;
      camera = new Camera(canvas.getHeight(), Math.toRadians(40));
      estabelecerSlider();
      estabelecerAcaoMouse();

      eixoAnterior = 0;
      orientacao = new Quaternio(1, 0, 0, 0);

      estabelecerTextFields();
      estabelerChoiceBox();
      estabelecerSpinners();

      checkRotacionar.selectedProperty().addListener((observable, antigo, marcado) -> {
        cbEixo.setDisable(marcado);
        cbEixo.setOpacity(marcado ? 0.5 : 1.0);
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
        sphereAtual = new ArrayList<>(sphere.getPontoInicial());
        break;
      case PLANO:

        break;
      case RETA:
        double x1 = -getValorTf(tfPosicaoObjetoX) * getValorTf(tfTamanhoObjeto);
        double y1 = -getValorTf(tfPosicaoObjetoY) * getValorTf(tfTamanhoObjeto);
        double z1 = -getValorTf(tfPosicaoObjetoZ) * getValorTf(tfTamanhoObjeto);

        double x2 = getValorTf(tfPosicaoObjetoX) * getValorTf(tfTamanhoObjeto);
        double y2 = getValorTf(tfPosicaoObjetoY) * getValorTf(tfTamanhoObjeto);
        double z2 = getValorTf(tfPosicaoObjetoZ) * getValorTf(tfTamanhoObjeto);

        reta = new ConjuntoPontos(DesenharFormas.drawLine(new Ponto3D(x1, y1, z1), new Ponto3D(x2, y2, z2)));
        reta.setVetorInicial(new Ponto3D(x2, y2, z2));
        break;
      default:
        break;
    }
  }

  @FXML
  public void apagarObjeto(ActionEvent e) {
    switch (cbObjeto.getSelectionModel().getSelectedIndex()) {
      case ESFERA:
        sphere = null;
        break;
      case PLANO:

        break;
      case RETA:
        reta = null;
        break;
      default:
        break;
    }
  }

  @FXML
  public void benchmark() {
    new Thread(() -> {
      Benchmark.benchmarkGeral(plano, 10000);
    }).start();
  }

  private void estabelecerAcaoMouse() {
    AcaoMouse eventoMouse = new AcaoMouse(canvas, camera);
    eventoMouse.estabelecerAcaoScroll(ponteiroAnguloRotacaoX, ponteiroAnguloRotacaoY); 
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
        Ponto2D ponto2D = camera.projecaoPerspectiva(ponto3D, ponteiroAnguloRotacaoY.obter(), ponteiroAnguloRotacaoX.obter());
        drawPoint(ponto2D, 2, cor);
      }
    }
  }

  private void rotacionarObjeto(double angulo, int eixo) {

    switch (eixo) {
      case EIXO_X:
        anguloRotacaoObjetoX = angulo;
        break;
      case EIXO_Y:
        anguloRotacaoObjetoY = angulo;
        break;
      case EIXO_Z:
        anguloRotacaoObjetoZ = angulo;
        break;
      default:
        break;
    }

    switch (cbRotacao.getSelectionModel().getSelectedIndex()) {
      case EULER:
        rotacionarPorEuler(angulo, eixo);
        break;
      case QUATERNIOS:
        rotacionarPorQuaternios(angulo, eixo);
        break;

      default:
        break;
    }
  }

  private void rotacionarPorEuler(double angulo, int eixo) {
    if (!checkRotacionar.isSelected()) {
      sphere.setPonto(
          Rotacao.angulosDeEuler(sphere.getPontoInicial(), anguloRotacaoObjetoX, anguloRotacaoObjetoY,
              anguloRotacaoObjetoZ));
    } else if (reta != null) {
      Ponto3D vetor = reta.getVetorInicial();
      double x = vetor.getX();
      double y = vetor.getY();
      double z = vetor.getZ();
      if (x != 0 || y != 0 || z != 0) {
        sphere.setPonto(
            Rotacao.angulosDeEulerReta(sphere.getPontoInicial(), new Ponto3D(x, y, z),
                sliderRotacao.getValue()));
      }
    }
  }

  private void rotacionarPorQuaternios(double angulo, int eixo) {
    if (!checkRotacionar.isSelected()) {

      Ponto3D vetor = new Ponto3D(spinnerX.getValue(), spinnerY.getValue(),
          spinnerZ.getValue());

      sphere.setPonto(
          Rotacao.rotacionarUsandoQuaternios(sphere.getPontoInicial(), vetor,
              sliderRotacao.getValue()));

    } else if (reta != null)

    {
      Ponto3D vetor = reta.getVetorInicial();
      double x = vetor.getX();
      double y = vetor.getY();
      double z = vetor.getZ();
      if (x != 0 || y != 0 || z != 0) {

        sphere.setPonto(
            Rotacao.rotacionarTornoReta(sphere.getPontoInicial(), new Ponto3D(x, y, z),
                sliderRotacao.getValue()));
      }
    }
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
      delta = valorNovo.doubleValue() - valorAntigo.doubleValue();
      rotacionarObjeto(sliderRotacao.getValue(), cbEixo.getSelectionModel().getSelectedIndex());
    });

    sliderHorizontal.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoY = sliderHorizontal.getValue();
    });

    sliderVertical.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoX = sliderVertical.getValue();
    });

    sliderProfundidade.valueProperty().addListener((observable, valorAntigo, valorNovo) -> {

      anguloRotacaoZ = sliderVertical.getValue();
    });
  }

  private void estabelerChoiceBox() {
    cbEixo.setItems(FXCollections.observableArrayList("X", "Y", "Z"));
    cbEixo.setValue("X");
    cbEixo.getSelectionModel().selectedIndexProperty().addListener((obs, indiceAntigo, indiceNovo) -> {
      sliderRotacao.setValue(selecionarEixo((int) indiceNovo));
    });

    cbRotacao.setItems(FXCollections.observableArrayList("Angulos de Euler", "Quaternios"));
    cbRotacao.setValue("Angulos de Euler");

    cbObjeto.setItems(FXCollections.observableArrayList("Esfera", "Plano", "Reta"));
    cbObjeto.setValue("Esfera");

  }

  private void estabelecerTextFields() {
    tfPosicaoObjetoX.setText("0");
    tfPosicaoObjetoY.setText("0");
    tfPosicaoObjetoZ.setText("0");
    tfTamanhoObjeto.setText("25");
  }

  private void estabelecerSpinners() {
    SpinnerValueFactory.DoubleSpinnerValueFactory factoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(
        -1, // mínimo
        1, // máximo
        0, // valor inicial
        0.1 // incremento
    );

    spinnerX.setValueFactory(factoryX);

    SpinnerValueFactory.DoubleSpinnerValueFactory factoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(
        -1, // mínimo
        1, // máximo
        0, // valor inicial
        0.1 // incremento
    );

    spinnerY.setValueFactory(factoryY);

    SpinnerValueFactory.DoubleSpinnerValueFactory factoryZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(
        -1, // mínimo
        1, // máximo
        0, // valor inicial
        0.1 // incremento
    );

    spinnerZ.setValueFactory(factoryZ);

    spinnerX.valueProperty().addListener((obs, antigo, novo) -> {
      rotacionarObjeto(novo, EIXO_X);
    });

    spinnerY.valueProperty().addListener((obs, antigo, novo) -> {
      rotacionarObjeto(novo, EIXO_Y);
    });

    spinnerZ.valueProperty().addListener((obs, antigo, novo) -> {
      rotacionarObjeto(novo, EIXO_Z);
    });
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
