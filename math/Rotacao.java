package math;

import java.util.ArrayList;

public class Rotacao {

  public static Ponto3D angulosEulerX(Ponto3D ponto, double angulo) {

    return ponto;
  }

  public static Ponto3D angulosEulerY(Ponto3D ponto, double angulo) {

    return ponto;
  }

  public static Ponto3D angulosEulerZ(Ponto3D ponto, double angulo) {

    return ponto;
  }

  public static ArrayList<Ponto3D> angulosDeEuler(ArrayList<Ponto3D> pontos, double anguloX, double anguloY,
      double anguloZ) {
    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(anguloX), -Math.sin(anguloX) },
        { 0, Math.sin(anguloX), Math.cos(anguloX) } };

    double[][] matrixY = { { Math.cos(anguloY), 0, Math.sin(anguloY) },
        { 0, 1, 0 },
        { -Math.sin(anguloY), 0, Math.cos(anguloY) } };

    double[][] matrixZ = { { Math.cos(anguloZ), -Math.sin(anguloZ), 0 },
        { Math.sin(anguloZ), Math.cos(anguloZ), 0 },
        { 0, 0, 1 } };

    double[][] matrixGeral = Projection.multiplicarMatrizes(Projection.multiplicarMatrizes(matrixZ, matrixY), matrixX);

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = Projection.multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }
}
