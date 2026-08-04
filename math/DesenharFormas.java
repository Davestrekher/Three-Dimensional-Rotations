package math;

import java.util.ArrayList;

public class DesenharFormas {
  public static ArrayList<Ponto3D> desenharCirculoHorizontal(Ponto3D pontoInicial, double r, double numeroPontos) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();

    for (double i = -r; i < r; i += 2 * r / numeroPontos) {
      double x = i + pontoInicial.getX();
      double y = pontoInicial.getY();
      double z = pontoInicial.getZ() + Math.sqrt(r * r - (i * i));

      double z2 = pontoInicial.getZ() - Math.sqrt(r * r - (i * i));

      Ponto3D ponto = new Ponto3D(x, y, z);
      Ponto3D ponto2 = new Ponto3D(x, y, z2);

      pontos.add(ponto);
      pontos.add(ponto2);
    }

    return pontos;
  }

  public static ArrayList<Ponto3D> desenharPlanoRaso(double largura, double comprimento, Ponto3D pontoInicial,
      double numeroPontos) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();
    Ponto3D vetorLinhaParalela = new Ponto3D(largura, 0, 0);
    Ponto3D vetorPerpendicular = new Ponto3D(0, 0, comprimento);
    Ponto3D pontoAtual = pontoInicial;

    for (double i = 0; i < 1; i += 1 / Math.sqrt(numeroPontos)) {
      for (double j = 0; j < 1; j += 1 / Math.sqrt(numeroPontos)) {
        double x = pontoInicial.getX() + vetorLinhaParalela.getX() * i + vetorPerpendicular.getX() * j;
        double y = pontoInicial.getY() + vetorLinhaParalela.getY() * i + vetorPerpendicular.getY() * j;
        double z = pontoInicial.getZ() + vetorLinhaParalela.getZ() * i + vetorPerpendicular.getZ() * j;
        pontos.add(new Ponto3D(x, y, z));
      }
    }

    return pontos;
  }

  public static ArrayList<Ponto3D> desenharCirculoVertical(Ponto3D pontoInicial, double r, double numeroPontos) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();
    for (double i = -r; i < r; i += 2 * r / numeroPontos) {
      double x = pontoInicial.getX();
      double y = i + pontoInicial.getY();
      double z = pontoInicial.getZ() + Math.sqrt(r * r - (i * i));

      double z2 = pontoInicial.getZ() - Math.sqrt(r * r - (i * i));

      Ponto3D ponto = new Ponto3D(x, y, z);
      Ponto3D ponto2 = new Ponto3D(x, y, z2);

      pontos.add(ponto);
      pontos.add(ponto2);
    }

    return pontos;
  }

  public static ArrayList<Ponto3D> desenharEsfera(Ponto3D pontoInicial, double r) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();

    for (double i = -r; i < r; i += 8) {
      double raioCorte = Math.sqrt(r * r - i * i);

      pontos.addAll(
          desenharCirculoHorizontal(new Ponto3D(pontoInicial.getX(), pontoInicial.getY() + i, pontoInicial.getZ()),
              raioCorte, 200));
      pontos.addAll(
          desenharCirculoVertical(new Ponto3D(pontoInicial.getX() + i, pontoInicial.getY(), pontoInicial.getZ()),
              raioCorte, 200));
    }
    return pontos;
  }

  public static ArrayList<Ponto3D> reta(Ponto3D vetorDiretor, double L) {
    ArrayList<Ponto3D> novaReta = new ArrayList<>();

    double modulo = Math.sqrt(vetorDiretor.getX() * vetorDiretor.getX() + vetorDiretor.getY() * vetorDiretor.getY()
        + vetorDiretor.getZ() * vetorDiretor.getZ());

    double x = (vetorDiretor.getX() * L) / modulo;
    double y = (vetorDiretor.getX() * L) / modulo;
    double z = (vetorDiretor.getX() * L) / modulo;

    Ponto3D novoVetor = new Ponto3D(x, y, z);

    for (double i = 0; i < 1; i += 0.001) {
      novaReta.add(new Ponto3D(novoVetor.getX() * i, novoVetor.getY() * i, novoVetor.getZ() * i));
    }

    return novaReta;
  }

  public static ArrayList<Ponto3D> drawLine(Ponto3D pontoA, Ponto3D pontoB) {
    ArrayList<Ponto3D> eixo = new ArrayList<>();

    Ponto3D vetorDiretor = new Ponto3D(pontoB.getX() - pontoA.getX(), pontoB.getY() - pontoA.getY(),
        pontoB.getZ() - pontoA.getZ());
    for (double i = 0; i < 1.0; i += 0.001) {
      Ponto3D ponto3D = new Ponto3D(pontoA.getX() + (vetorDiretor.getX() * i),
          pontoA.getY() + (vetorDiretor.getY() * i), pontoA.getZ() + (vetorDiretor.getZ() * i));
      eixo.add(ponto3D);
    }

    return eixo;
  }
}
