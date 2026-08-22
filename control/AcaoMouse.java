package control;

import javafx.scene.canvas.Canvas;
import math.Observador;
public class AcaoMouse {

     private Canvas canvas;
     private Observador camera;
     private double mouseX, mouseY;
     private AnguloRotacao anguloX, anguloY; 
     public AcaoMouse(Canvas canvas, Observador camera) {
            this.canvas = canvas;
            this.camera = camera;
     }

     public void estabelecerAcaoScrool(AnguloRotacao anguloRotacaoY, AnguloRotacao anguloRotacaoX) {
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
                 anguloY.alterar(deltaY * sensibilidade);
                 anguloX.alterar(deltaX * sensibilidade);
                  
          });
     }
}