package math;

public class Projection {
  public static Ponto2D project(Ponto3D ponto3D) {

    double x = (-Math.cos(Math.PI / 4.0) * ponto3D.getX()) + ponto3D.getY();
    double y = (-Math.sin(Math.PI / 4.0) * ponto3D.getX()) + ponto3D.getZ();

    return new Ponto2D(x, y);
  }
}
