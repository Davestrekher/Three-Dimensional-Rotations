package math;

public class Observador {

  private double x;
  private double y;
  private double z;

  private double altura;

  private double focus;

  private double fov;

  public Observador(double altura, double fov) {
    x = 0;
    y = 0;
    z = 150;

    this.fov = fov;
    this.altura = altura;
    this.focus = (altura / 2.0) / Math.tan(fov / 2.0);
  }

  private final static double distance = 100;

  public Ponto2D projecaoOrtografica(Ponto3D ponto3D, double anguloHorizontal, double anguloVertical) {

    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(anguloVertical), -Math.sin(anguloVertical) },
        { 0, Math.sin(anguloVertical), Math.cos(anguloVertical) } };
    double[][] matrixY = { { Math.cos(anguloHorizontal), 0, Math.sin(anguloHorizontal) },
        { 0, 1, 0 },
        { -Math.sin(anguloHorizontal), 0, Math.cos(anguloHorizontal) } };

    double[][] matrixRes = multiplicarMatrizes(matrixX, matrixY);

    double[][] matrixXYZ = { { ponto3D.getX() }, { ponto3D.getY() }, { ponto3D.getZ() } };

    double[][] matrixFinal = multiplicarMatrizes(matrixRes, matrixXYZ);

    double x = (matrixFinal[0][0] / distance * focus);
    double y = (matrixFinal[1][0] / distance * focus);

    // double z = matrixFinal[2][0] + distance;

    // double x = focus * matrixFinal[0][0] / z;
    // double y = focus * matrixFinal[1][0] / z;

    return new Ponto2D(x, y);
  }

  public Ponto2D projecaoPerspectiva(Ponto3D ponto3D, double anguloHorizontal, double anguloVertical) {

    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(anguloVertical), Math.sin(anguloVertical) },
        { 0, -Math.sin(anguloVertical), Math.cos(anguloVertical) } };
    double[][] matrixY = { { Math.cos(anguloHorizontal), 0, -Math.sin(anguloHorizontal) },
        { 0, 1, 0 },
        { Math.sin(anguloHorizontal), 0, Math.cos(anguloHorizontal) } };

    double[][] matrixRes = multiplicarMatrizes(matrixX, matrixY);

    double[][] matrixXYZ = { { ponto3D.getX() }, { ponto3D.getY() }, { ponto3D.getZ() } };

    double[][] matrixFinal = multiplicarMatrizes(matrixRes, matrixXYZ);

    double z = matrixFinal[2][0] - getZ();

    double x = focus * matrixFinal[0][0] / z;
    double y = focus * matrixFinal[1][0] / z;

    return new Ponto2D(x, y);
  }

  public Ponto2D projecaoPerspectivaQuaternios(Ponto3D ponto, double anguloHorizontal, double anguloVertical,
      double anguloEmTornoDoEixo) {
    Quaternio q = new Quaternio(Math.cos(anguloHorizontal / 2.0), 0, Math.sin(anguloVertical / 2.0), 0);

    return new Ponto2D(x, y);
  }

  private double[][] multiplicarMatrizes(double[][] X, double[][] Y) {
    double[][] res = new double[X.length][Y[0].length];
    for (int i = 0; i < X.length; i++) {
      for (int j = 0; j < Y[0].length; j++) {
        res[i][j] = 0;
        for (int k = 0; k < X[0].length; k++) {
          res[i][j] += X[i][k] * Y[k][j];
        }
      }
    }

    return res;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public void aproximar() {
    z += 10;
  }

  public void afastar() {
    z -= 10;
  }
}
