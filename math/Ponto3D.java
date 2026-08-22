/*
  Abstracao de um ponto tridimensional. As operacoes matematicas ocorrem utilizando pontos desta classe
*/

package math;

public class Ponto3D {
  private double x;
  private double y;
  private double z;

  public Ponto3D(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
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

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ", " + z + ")";
  }
}
