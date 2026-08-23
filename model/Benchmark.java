/* 
  Classe que testa os algoritmos de rotacao de acordo com algum conjunto de pontos qualquer, rotacionando todos
  os pontos por uma certa quantidade de repeticoes. A quantidade de tempo médio gasto para cada algoritmo eh exibida junto com a quantidade de operacoes realizadas no total.
*/

package model;

import math.ConjuntoPontos;
import math.Ponto3D;
import math.Rotacao;

public class Benchmark {

  public static void benchmarkGeral(ConjuntoPontos plano, int repeticoes) {
    System.out.println();
    System.out.println("============================================================");
    System.out.println("                    BENCHMARK DE PERFORMANCE");
    System.out.println("============================================================");
    System.out.println();

    System.out.println("Comparacao:");
    System.out.println("  [1] Quaternios");
    System.out.println("  [2] Angulos de Euler");
    System.out.println("  [3] Matriz de mudanca de base");
    System.out.println();

    System.out.println("------------------------------------------------------------");
    System.out.println("Configuracao do teste");
    System.out.println("------------------------------------------------------------");
    System.out.println("Numero de repeticoes: " + repeticoes);

    System.out.println("------------------------------------------------------------");
    System.out.println("Executando benchmarks...");
    System.out.println("------------------------------------------------------------");

    double tempoMedioEuler = benchmarkAngulosDeEuler(plano, repeticoes);
    double tempoMedioQuaternios = benchmarkQuaternios(plano, repeticoes);
    double tempoMedioMudancaDeBase = benchmarkMatrizDeMudancaDeBase(plano, repeticoes);

    System.out.println();
    System.out.println("============================================================");
    System.out.println("                    RESULTADOS FINAIS");
    System.out.println("============================================================");
    System.out.println();

    System.out.println("Tempo de execucao medio em milisegundos:");
    System.out.println();

    System.out.println("  +--------------------------------------+");
    System.out.println("  | Metodo: tempo                             |");
    System.out.println("  +--------------------------------------+");
    System.out.println("  | Angulos de Euler                     | " + tempoMedioEuler + " ms");
    System.out.println("  | Quaternios                           | " + tempoMedioQuaternios + " ms");
    System.out.println("  | Matriz de mudanca de base            | " + tempoMedioMudancaDeBase + " ms");
    System.out.println("  +--------------------------------------+");

    System.out.println("Quantidade total de operacoes:");
    System.out.println();

    System.out.println("  +--------------------------------------+");
    System.out.println("  | Metodo: operacoes                             |");
    System.out.println("  +--------------------------------------+");
    System.out.println("  | Angulos de Euler                     | " + (60 * repeticoes) + " ms");
    System.out.println("  | Quaternios                           | " + (56 * repeticoes) + " ms");
    System.out.println("  | Matriz de mudanca de base            | " + (0) + " ms");
    System.out.println("  +--------------------------------------+");

    System.out.println();
    System.out.println("============================================================");
    System.out.println("                    FIM DO BENCHMARK");
    System.out.println("============================================================");
    System.out.println();
  }

  /* 
    Na implementacao dos angulos de euler, a multiplicacao de matrizes entre os angulos ocorre
    em toda chamada de metodo. Isso so deveria ser necessario caso os angulos mudassem, entao por isso
    os angulos variam com i.
  */

  private static double benchmarkAngulosDeEuler(ConjuntoPontos plano, int repeticoes){
    ConjuntoPontos planoTeste = new ConjuntoPontos(plano.getPontoInicial());

    long inicio = System.currentTimeMillis();

    for (int i = 0; i < repeticoes; i++) {
      Rotacao.angulosDeEuler(
          planoTeste.getPontoInicial(),
          i,
          i,
          i);
    }

    long fim = System.currentTimeMillis();

    return(fim - inicio) / (double) repeticoes;
  }

  /* 
    Para manter a justica do benchmark, os quaternios e a mudanca de base executam uma composicao de rotacoes,
    alem dos angulos tambem variarem com i
  */
  private static double benchmarkQuaternios(ConjuntoPontos plano, int repeticoes){
    ConjuntoPontos planoTeste2 = new ConjuntoPontos(plano.getPontoInicial());
    double inicio = System.currentTimeMillis();

    for (int i = 0; i < repeticoes; i++) {
      planoTeste2.setPonto(Rotacao.rotacionarUsandoQuaternios(planoTeste2.getPontoInicial(), new Ponto3D(1, 0, 0),
          i));
      planoTeste2.setPonto(
          Rotacao.rotacionarUsandoQuaternios(planoTeste2.getPonto(), new Ponto3D(0, 1, 0), i));
      planoTeste2.setPonto(Rotacao.rotacionarUsandoQuaternios(planoTeste2.getPonto(), new Ponto3D(0, 0, 1),
          i));
    }

    double fim = System.currentTimeMillis();

    return (fim - inicio) / (double) repeticoes;
  }

  private static double benchmarkMatrizDeMudancaDeBase(ConjuntoPontos plano, int repeticoes){
    ConjuntoPontos planoTeste3 = new ConjuntoPontos(plano.getPontoInicial());
    double inicio = System.currentTimeMillis();

    for (int i = 0; i < repeticoes; i++) {
      planoTeste3.setPonto(Rotacao.rotacionarTornoReta(planoTeste3.getPontoInicial(), new Ponto3D(1, 0, 0),
          i));
      planoTeste3.setPonto(
          Rotacao.rotacionarTornoReta(planoTeste3.getPonto(), new Ponto3D(0, 1, 0), i));
      planoTeste3.setPonto(Rotacao.rotacionarTornoReta(planoTeste3.getPonto(), new Ponto3D(0, 0, 1),
          i));
    }

    double fim = System.currentTimeMillis();

    return (fim - inicio) / (double) repeticoes;
  }
}
