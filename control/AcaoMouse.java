package control;

import javafx.scene.canvas.Canvas;
import math.Camera;

public class AcaoMouse {

     private Canvas canvas;
     private Camera camera;
     private double mouseX, mouseY;
     private AnguloRotacao anguloX, anguloY; 
     public AcaoMouse(Canvas canvas, Camera camera) {
            this.canvas = canvas;
            this.camera = camera;
     }

     public void estabelecerAcaoScroll(AnguloRotacao anguloRotacaoY, AnguloRotacao anguloRotacaoX) {
           canvas.setOnScroll(event -> {
             double delta = event.getDeltaY();
             if (delta > 0) {
                camera.afastar();
             } else if (delta < 0) {
                camera.aproximar();
             }
          });

          canvas.setOnMousePressed(event -> {
             mouseX = event.getX();
             mouseY = event.getY();
          });
         anguloY = anguloRotacaoY; anguloX = anguloRotacaoX;
         canvas.setOnMouseDragged(event -> {
                 double deltaX = event.getX() - mouseX;
                 double deltaY = event.getY() - mouseY;
                 double sensibilidade = 0.01;
                 anguloY.alterar(anguloY.obterInicial() + deltaY * sensibilidade);
                 anguloX.alterar(anguloX.obterInicial() + deltaX * sensibilidade);
                  
          });

          canvas.setOnMouseReleased(event ->{
            anguloX.alterarInicial(anguloX.obter());
            anguloY.alterarInicial(anguloY.obter());
          });
     }
}