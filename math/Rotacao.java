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

  public void rotacionarTornoReta(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor, double angulo) {
          double modulo = calcularModulo(vetorDiretor);
          Ponto3D vetorDiretorNormalizado = normalizarVetor(vetorDiretor);
          Ponto3D vetorPerpendicular = normalizarVetor(calcularVetorNaoNulo(vetorDiretorNormalizado));
  }
  
  private Ponto3D calcularVetorNaoNulo(Ponto3D vetorDiretor) { 
        double a = vetorDiretor.getX();
        double b = vetorDiretor.getY();
        double c = vetorDiretor.getZ();
        double x,y,z;
        if(a != 0) {
            y = 1; z = 1;
            x = (b*y+c*z)/a;
            return new Ponto3D(x,y,z);
        }
  
        if(b != 0) {
            x = 1; z = 1;
            y = (a*x+c*z)/b;
            return new Ponto3D(x,y,z);
        }   

        if(c != 0) {
            y = 1; x = 1;
            z = (a*x+b*y)/c;
            return new Ponto3D(x,y,z);
        }
         return null;
  }

//  private Ponto3D produtoVetorial(Ponto3D vetorU, Ponto3D vetorW) {
  //    retrun new Ponto3D(vetorU.getY()*vetorW.getZ()-vetorU.getZ()*vetorW.getY(), vetorU.getZ()-, );
//  }
  private Ponto3D normalizarVetor(Ponto3D vetor) {
        double modulo = calcularModulo(vetor);
        return new Ponto3D(vetor.getX()/modulo, vetor.getY()/modulo, vetor.getZ()/modulo);
  }

  private double calcularModulo(Ponto3D vetorDiretor) { 
        return Math.sqrt(vetorDiretor.getX()*vetorDiretor.getX()+vetorDiretor.getY()*vetorDiretor.getY()+vetorDiretor.getZ()*vetorDiretor.getZ());
  }
}
