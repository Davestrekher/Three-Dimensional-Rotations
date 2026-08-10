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
  @FXML
  private Spinner<Double> spinnerX;
  @FXML
  private Spinner<Double> spinnerY;
  @FXML
  private Spinner<Double> spinnerZ;
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
    
    SpinnerValueFactory<Double> valueFactoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-85, 85, 0);
    SpinnerValueFactory<Double> valueFactoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-85, 85, 0);
    SpinnerValueFactory<Double> valueFactoryZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(-85, 85, 0);

    spinnerX.setValueFactory(valueFactoryX);
    spinnerY.setValueFactory(valueFactoryY);
    spinnerZ.setValueFactory(valueFactoryZ);
    
    canvas.setOnMousePressed(event -> {
      mouseX = event.getX();
      mouseY = event.getY();
    });

    canvas.setOnMouseDragged(event -> {

      double deltaX = event.getX() - mouseX;
      double deltaY = event.getY() - mouseY;

      double sensibilidade = 0.01;

      anguloRotacaoX += deltaX * sensibilidade;

      anguloRotacaoY += deltaY * sensibilidade;

      mouseX = event.getX();
      mouseY = event.getY();

      rotacionarGeral();
    });

  }

  @FXML
  public void teste(ActionEvent e) {
    adicionarEixo();
    adicionarPlano();
    render();
  }

  @FXML
  public void teste2(ActionEvent e) {
   // reta = new ConjuntoPontos(DesenharFormas.reta(new Ponto3D(spinnerX.getValue(), spinnerY.getValue() , spinnerZ.getValue()), 50));
   // reta.setPonto(addAll(DesenharFormas.drawLine(new Ponto3D(0, 0, 0), new Ponto3D(spinnerX.getValue() * 100, spinnerY.getValue() * 100, spinnerZ.getValue() * 100)));
    sphere = new ConjuntoPontos(DesenharFormas.desenharEsfera(new Ponto3D(0, 0, 0), 25)); 
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
        renderPoints(plano, Color.PINK);
        renderPoints(sphere, Color.BLACK);
        renderPoints(reta, Color.PURPLE);
      }
    }.start();
  }

  private void renderPoints(ConjuntoPontos lista, Color cor) {
       ArrayList<Ponto3D> p = (lista != null) ? lista.getPonto() : null;
      
      if(lista != null) {
         for (Ponto3D ponto3D : p) {
            Ponto2D ponto2D = camera.projecaoPerspectiva(ponto3D, anguloRotacaoY, anguloRotacaoX);
            drawPoint(ponto2D, 2, cor);
         }
      }
  }

  private void rotacionarObjeto(double angulo, int eixo) {
    /**switch (eixo) {
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
        anguloRotacaoX = angulo;/
    }**/
    if (spinnerX.getValue() == null || spinnerY.getValue() == null || spinnerZ.getValue() == null) {
      System.out.println("deu pau");
      return;
    }
    double x = spinnerX.getValue();
    double y = spinnerY.getValue();
    double z = spinnerZ.getValue();
    if(x != 0 || y != 0 || z !=0) sphere.setPonto(Rotacao.rotacionarTornoReta(sphere.getPontoInicial(), new Ponto3D(x,y,z), sliderRotacao.getValue()));

  }


  

  private void rotacionarGeral() {
   /*
    * eixoX.setPonto(Rotacao.angulosDeEuler(eixoX.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
    * eixoY.setPonto(Rotacao.angulosDeEuler(eixoY.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
    * eixoZ.setPonto(Rotacao.angulosDeEuler(eixoZ.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
    * plano.setPonto(Rotacao.angulosDeEuler(plano.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
    * if(sphere != null) sphere.setPonto(Rotacao.angulosDeEuler(sphere.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue()));
    * if(reta != null) reta.setPonto(Rotacao.angulosDeEuler(reta.getPontoInicial(), anguloRotacaoY, anguloRotacaoX, sliderProfundidade.getValue())); 
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
      plano =  new ConjuntoPontos(DesenharFormas.desenharPlanoRaso(170, 170, new Ponto3D(-85, 0, -85), 5000));
  }

  private void estabelecerSlider() {
    cbEixo.setItems(FXCollections.observableArrayList("X", "Y", "Z"));
    cbEixo.setValue("X");
    cbEixo.getSelectionModel().selectedIndexProperty().addListener((obs, indiceAntigo, indiceNovo) -> {
      sliderRotacao.setValue(selecionarEixo((int) indiceNovo));
    });

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
