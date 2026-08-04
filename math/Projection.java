package math;

public class Projection {

  private final static double focus = 2;
  private final static double distance = 2;

  public static Ponto2D project(Ponto3D ponto3D, double anguloHorizontal, double anguloVertical) {
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

    return new Ponto2D(x, y);
  }

  public static double[][] multiplicarMatrizes(double[][] X, double[][] Y) {
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
}
