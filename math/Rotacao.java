/* 
  Classe que realiza as operacoes matematicas necessarias para rotacionar pontos em tres dimensoes. Esta classe
  nao trata das operacoes de vetores (OperacoesVetores.java), das operacoes de matrizes (OperacoesMatrizes.java), 
  ou das operacoes de quaternios (OperacaoQuaternios.java), assim aqui sao implementados apenas os algoritmos de rotacao, sendo esses os angulos de euler, matriz de mudanca de base e quaternios. Note que esta classe nao realiza as rotacoes dos pontos quando a camera eh mexida. Essas rotacoes sao realizadas na propria classe do Observador.
*/

package math;

import java.util.ArrayList;

public class Rotacao {

  /*
    Rotacoes ocorrem atraves de multiplicacoes sucessivas entre eixos
    coordenados.
    Note que a matriz do meio da multiplicacao eh a Y, o que permite o bloqueio
    de Gimbal quando o anguloY atinge valores como pi/2 ou-pi/2
  */

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

    double[][] matrixGeral = OperacoesMatrizes
        .multiplicarMatrizes(OperacoesMatrizes.multiplicarMatrizes(matrixX, matrixY), matrixZ);

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }

  // Igual ao metodo de rotacao acima, mas rotaciona um ponto de cada vez em vez de uma lista de pontos
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

    double[][] matrixGeral = OperacoesMatrizes
        .multiplicarMatrizes(OperacoesMatrizes.multiplicarMatrizes(matrixX, matrixY), matrixZ);

    double[][] matrixXYZ = { { pontos.getX() }, { pontos.getY() }, { pontos.getZ() } };
    double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixGeral, matrixXYZ);

    double x = matrixFinal[0][0];
    double y = matrixFinal[1][0];
    double z = matrixFinal[2][0];

    return new Ponto3D(x, y, z);
  }
  /* 
    Em construcao...
  */
  public static ArrayList<Ponto3D> angulosDeEulerReta(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor, double anguloRotacao){
    double denominadorHorizontal = Math.sqrt(vetorDiretor.getZ()*vetorDiretor.getZ() + vetorDiretor.getX()*vetorDiretor.getX());
    double coordenadaHorizontal = vetorDiretor.getZ() > 0 ? Math.asin(vetorDiretor.getX() / denominadorHorizontal): Math.PI - Math.asin(vetorDiretor.getX() / denominadorHorizontal);
    
    double denominadorVertical = Math.sqrt(vetorDiretor.getZ()*vetorDiretor.getZ() + vetorDiretor.getX()*vetorDiretor.getX() + vetorDiretor.getY()*vetorDiretor.getY());
    double coordenadaVertical = vetorDiretor.getZ() > 0 ? Math.asin(vetorDiretor.getY() / denominadorVertical): Math.PI - Math.asin(vetorDiretor.getY() / denominadorVertical);

    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(coordenadaVertical), -Math.sin(coordenadaVertical) },
        { 0, Math.sin(coordenadaVertical), Math.cos(coordenadaVertical) } };

    double[][] matrixY = { { Math.cos(coordenadaHorizontal), 0, Math.sin(coordenadaHorizontal) },
        { 0, 1, 0 },
        { -Math.sin(coordenadaHorizontal), 0, Math.cos(coordenadaHorizontal) } };

    double[][] matrixZ = { { Math.cos(anguloRotacao), -Math.sin(anguloRotacao), 0 },
        { Math.sin(anguloRotacao), Math.cos(anguloRotacao), 0 },
        { 0, 0, 1 } };

    double[][] matrixGeral = OperacoesMatrizes
        .multiplicarMatrizes(OperacoesMatrizes.multiplicarMatrizes(matrixX, matrixY), matrixZ);
    
    ArrayList<Ponto3D> novosPontos = new ArrayList<>();
    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }

  /*
    Rotaciona uma lista de pontos em torno de uma reta (determinada por um vetor diretor), utilizando
    tres vetores perpendiculares para implementar um algoritmo de matriz de mudanca de base. 
  */
  public static ArrayList<Ponto3D> rotacionarTornoReta(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor, double angulo) {
    // double modulo = calcularModulo(vetorDiretor);
    Ponto3D vetorDiretorNormalizado = OperacoesVetores.normalizarVetor(vetorDiretor);
    Ponto3D vetorPerpendicular = OperacoesVetores.normalizarVetor(OperacoesVetores.calcularVetorNaoNulo(vetorDiretorNormalizado));
    Ponto3D vetorA = OperacoesVetores
        .normalizarVetor(OperacoesVetores.produtoVetorial(vetorDiretorNormalizado, vetorPerpendicular));

    double[][] matrixMudancaBase = { { vetorA.getX(), vetorPerpendicular.getX(), vetorDiretorNormalizado.getX() },
        { vetorA.getY(), vetorPerpendicular.getY(), vetorDiretorNormalizado.getY() },
        { vetorA.getZ(), vetorPerpendicular.getZ(), vetorDiretorNormalizado.getZ() } };

    double[][] matrixMudancaBaseInvertida = OperacoesMatrizes.inversa(matrixMudancaBase);

    double[][] matrixZ = { { Math.cos(angulo), -Math.sin(angulo), 0 },
        { Math.sin(angulo), Math.cos(angulo), 0 },
        { 0, 0, 1 } };

    double[][] matrixGeral = OperacoesMatrizes.multiplicarMatrizes(matrixMudancaBase,
        OperacoesMatrizes.multiplicarMatrizes(matrixZ, matrixMudancaBaseInvertida));

    ArrayList<Ponto3D> novosPontos = new ArrayList<>();

    for (Ponto3D ponto : pontos) {
      double[][] matrixXYZ = { { ponto.getX() }, { ponto.getY() }, { ponto.getZ() } };
      double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixGeral, matrixXYZ);

      double x = matrixFinal[0][0];
      double y = matrixFinal[1][0];
      double z = matrixFinal[2][0];

      novosPontos.add(new Ponto3D(x, y, z));
    }

    return novosPontos;
  }

  /* 
    Rotaciona uma lista de pontos em tres dimensoes utilizando as operacoes dos numeros quaternios.

    Sendo p o ponto a ser rotacionado, q o numero quaternio que representa o ponto em torno do qual a rotacao
    deve ocorrer e q⁻1 o conjugado de q.
    ponto rotacionado = q*p*q⁻1
  */
  public static ArrayList<Ponto3D> rotacionarUsandoQuaternios(ArrayList<Ponto3D> pontos, Ponto3D vetorDiretor,
      double angulo) {
    //Normaliza o vetor antes de realizar as operacoes
    Ponto3D vetorNormalizado = OperacoesVetores.normalizarVetor(vetorDiretor);

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
}
