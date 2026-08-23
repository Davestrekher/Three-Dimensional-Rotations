/*
  Classe que une as operacoes de matrizes utilizadas na classe de rotacao
*/


package math;

public class OperacoesMatrizes {
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

  public static double determinante3por3(double[][] matrix) {
    double ladoEsquerdo = (matrix[0][0] * matrix[1][1] * matrix[2][2]) + (matrix[0][1] * matrix[1][2] * matrix[2][0])
        + (matrix[0][2] * matrix[1][0] * matrix[2][1]);

    double ladoDireito = (matrix[0][2] * matrix[1][1] * matrix[2][0]) + (matrix[0][0] * matrix[1][2] * matrix[2][1])
        + (matrix[0][1] * matrix[1][0] * matrix[2][2]);

    return ladoEsquerdo - ladoDireito;
  }

  public static double[][] transposta(double[][] matrix) {
    double[][] matrixTransposta = new double[matrix.length][matrix[0].length];

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrixTransposta[i][j] = matrix[j][i];
      }
    }

    return matrixTransposta;
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

  private static double determinante2por2(double[][] matrix) {
    return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
  }

  private static double[][] dividaTudo(double[][] matrix, double divisor) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i][j] /= divisor;
      }
    }

    return matrix;
  }

  public static double[][] inversa(double[][] matrix) {
    double determinante = determinante3por3(matrix);
    double[][] matrixCofatores = cofatores3por3(matrix);

    return dividaTudo(transposta(matrixCofatores), determinante);
  }
}
