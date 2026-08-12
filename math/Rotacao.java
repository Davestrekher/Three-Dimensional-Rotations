package math;

import java.util.ArrayList;

public class Rotacao {

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

    double[][] matrixGeral = multiplicarMatrizes(multiplicarMatrizes(matrixX, matrixY), matrixZ);

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }

  public static Ponto3D angulosDeEuler(Ponto3D pontos, double anguloX, double anguloY,
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

    double[][] matrixGeral = multiplicarMatrizes(multiplicarMatrizes(matrixX, matrixY), matrixZ);

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    double[][] matrixXYZ = { { pontos.getX() }, { pontos.getY() }, { pontos.getZ() } };
    double[][] matrixFinal = multiplicarMatrizes(matrixGeral, matrixXYZ);

    double x = matrixFinal[0][0];
    double y = matrixFinal[1][0];
    double z = matrixFinal[2][0];

    return new Ponto3D(x, y, z);
  }

  public static ArrayList<Ponto3D> rotacionarTornoReta(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor, double angulo) {
    // double modulo = calcularModulo(vetorDiretor);
    Ponto3D vetorDiretorNormalizado = normalizarVetor(vetorDiretor);
    Ponto3D vetorPerpendicular = normalizarVetor(calcularVetorNaoNulo(vetorDiretorNormalizado));
    Ponto3D vetorA = normalizarVetor(produtoVetorial(vetorDiretorNormalizado, vetorPerpendicular));

    double[][] matrixMudancaBase = { { vetorA.getX(), vetorPerpendicular.getX(), vetorDiretorNormalizado.getX() },
        { vetorA.getY(), vetorPerpendicular.getY(), vetorDiretorNormalizado.getY() },
        { vetorA.getZ(), vetorPerpendicular.getZ(), vetorDiretorNormalizado.getZ() } };

    double[][] matrixMudancaBaseInvertida = inversa(matrixMudancaBase);

    double[][] matrixZ = { { Math.cos(angulo), -Math.sin(angulo), 0 },
        { Math.sin(angulo), Math.cos(angulo), 0 },
        { 0, 0, 1 } };

    double[][] matrixGeral = multiplicarMatrizes(matrixMudancaBase,
        multiplicarMatrizes(matrixZ, matrixMudancaBaseInvertida));

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();

    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }

  private static Ponto3D calcularVetorNaoNulo(Ponto3D vetorDiretor) {
    double a = vetorDiretor.getX();
    double b = vetorDiretor.getY();
    double c = vetorDiretor.getZ();
    double x, y, z;
    if (a != 0) {
      y = 1;
      z = 1;
      x = -(b * y + c * z) / a;
      return new Ponto3D(x, y, z);
    }

    if (b != 0) {
      x = 1;
      z = 1;
      y = -(a * x + c * z) / b;
      return new Ponto3D(x, y, z);
    }

    if (c != 0) {
      y = 1;
      x = 1;
      z = -(a * x + b * y) / c;
      return new Ponto3D(x, y, z);
    }
    return null;
  }

  private static Ponto3D produtoVetorial(Ponto3D vetorU, Ponto3D vetorW) {
    return new Ponto3D(vetorU.getY() * vetorW.getZ() - vetorU.getZ() * vetorW.getY(),
        -(vetorU.getX() * vetorW.getZ() - vetorU.getZ() * vetorW.getX()),
        vetorU.getX() * vetorW.getY() - vetorU.getY() * vetorW.getX());
  }

  private static Ponto3D normalizarVetor(Ponto3D vetor) {
    double modulo = calcularModulo(vetor);
    return new Ponto3D(vetor.getX() / modulo, vetor.getY() / modulo, vetor.getZ() / modulo);
  }

  private static double calcularModulo(Ponto3D vetorDiretor) {
    return Math.sqrt(vetorDiretor.getX() * vetorDiretor.getX() + vetorDiretor.getY() * vetorDiretor.getY()
        + vetorDiretor.getZ() * vetorDiretor.getZ());
  }

  public static double[][] inversa(double[][] matrix) {
    double determinante = determinante3por3(matrix);
    double[][] matrixCofatores = cofatores3por3(matrix);

    return dividaTudo(transposta(matrixCofatores), determinante);
  }

  private static double[][] dividaTudo(double[][] matrix, double divisor) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i][j] /= divisor;
      }
    }

    return matrix;
  }

  private static double determinante2por2(double[][] matrix) {
    return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
  }

  private static double[][] cofatores3por3(double[][] matrix) {
    double a11 = matrix[0][0];
    double a12 = matrix[0][1];
    double a13 = matrix[0][2];
    double a21 = matrix[1][0];
    double a22 = matrix[1][1];
    double a23 = matrix[1][2];
    double a31 = matrix[2][0];
    double a32 = matrix[2][1];
    double a33 = matrix[2][2];

    double[][] cofatores = new double[3][3];

    cofatores[0][0] = determinante2por2(new double[][] { { a22, a23 }, { a32, a33 } });
    cofatores[0][1] = -determinante2por2(new double[][] { { a21, a23 }, { a31, a33 } });
    cofatores[0][2] = determinante2por2(new double[][] { { a21, a22 }, { a31, a32 } });

    cofatores[1][0] = -determinante2por2(new double[][] { { a12, a13 }, { a32, a33 } });
    cofatores[1][1] = determinante2por2(new double[][] { { a11, a13 }, { a31, a33 } });
    cofatores[1][2] = -determinante2por2(new double[][] { { a11, a12 }, { a31, a32 } });

    cofatores[2][0] = determinante2por2(new double[][] { { a12, a13 }, { a22, a23 } });
    cofatores[2][1] = -determinante2por2(new double[][] { { a11, a13 }, { a21, a23 } });
    cofatores[2][2] = determinante2por2(new double[][] { { a11, a12 }, { a21, a22 } });

    return cofatores;
  }

  private static double[][] transposta(double[][] matrix) {
    double[][] matrixTransposta = new double[matrix.length][matrix[0].length];

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrixTransposta[i][j] = matrix[j][i];
      }
    }

    return matrixTransposta;
  }

  private static double determinante3por3(double[][] matrix) {
    double ladoEsquerdo = (matrix[0][0] * matrix[1][1] * matrix[2][2]) + (matrix[0][1] * matrix[1][2] * matrix[2][0])
        + (matrix[0][2] * matrix[1][0] * matrix[2][1]);

    double ladoDireito = (matrix[0][2] * matrix[1][1] * matrix[2][0]) + (matrix[0][0] * matrix[1][2] * matrix[2][1])
        + (matrix[0][1] * matrix[1][0] * matrix[2][2]);

    return ladoEsquerdo - ladoDireito;
  }

  public static ArrayList<Ponto3D> rotacionarUsandoQuaternios(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor,
      double angulo) {
    Ponto3D vetorNormalizado = normalizarVetor(vetorDiretor);

    double cosseno = Math.cos(angulo / 2);
    double seno = Math.sin(angulo / 2);
    Quaternio P = new Quaternio(cosseno, seno * vetorNormalizado.getX(), seno * vetorNormalizado.getY(),
        seno * vetorNormalizado.getZ());
    Quaternio conjugadoP = OperacaoQuaternios.inversa(P);

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    for (Ponto3D ponto : pontos) {
      Quaternio V = new Quaternio(0, ponto.getX(), ponto.getY(), ponto.getZ());

      Quaternio pontoRotacionado = OperacaoQuaternios.prod(OperacaoQuaternios.prod(P, V), conjugadoP);

      novosPontos.add(new Ponto3D(pontoRotacionado.getI(), pontoRotacionado.getJ(), pontoRotacionado.getK()));
    }

    return novosPontos;
  }

  private static double[][] multiplicarMatrizes(double[][] X, double[][] Y) {
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
