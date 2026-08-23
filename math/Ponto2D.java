/*
  Abstracao de um ponto bidimensional. As operacoes matematicas nao ocorrem nesses pontos, mas os pontos
  de tres dimensioes sao projetados na tela atraves destes pontos de duas dimensoes. As projecoes ocorrem na classe Observador.java
*/

package math;

public class Ponto2D {
  private double x;
  private double y;

  public Ponto2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }
}
