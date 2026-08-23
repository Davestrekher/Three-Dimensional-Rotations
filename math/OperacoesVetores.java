/*
  Classe que une as operacoes de vetores utilizadas na clsse de rotacao
*/

package math;

public class OperacoesVetores {

  public static Ponto3D produtoVetorial(Ponto3D vetorU, Ponto3D vetorW) {
    return new Ponto3D(vetorU.getY() * vetorW.getZ() - vetorU.getZ() * vetorW.getY(),
        -(vetorU.getX() * vetorW.getZ() - vetorU.getZ() * vetorW.getX()),
        vetorU.getX() * vetorW.getY() - vetorU.getY() * vetorW.getX());
  }

  public static Ponto3D normalizarVetor(Ponto3D vetor) {
    double modulo = calcularModulo(vetor);
    return new Ponto3D(vetor.getX() / modulo, vetor.getY() / modulo, vetor.getZ() / modulo);
  }

  private static double calcularModulo(Ponto3D vetorDiretor) {
    return Math.sqrt(vetorDiretor.getX() * vetorDiretor.getX() + vetorDiretor.getY() * vetorDiretor.getY()
        + vetorDiretor.getZ() * vetorDiretor.getZ());
  }

  public static Ponto3D calcularVetorNaoNulo(Ponto3D vetorDiretor) {
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
}
