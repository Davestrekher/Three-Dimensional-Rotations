package math;

import java.util.ArrayList;

public class DesenharFormas {
  public static ArrayList<Ponto3D> desenharCirculoHorizontal(Ponto3D pontoInicial, double r) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();

    for (double i = -r; i < r; i += 0.01) {
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

  public static ArrayList<Ponto3D> desenharCirculoVertical(Ponto3D pontoInicial, double r) {
    ArrayList<Ponto3D> pontos = new ArrayList<>();

    for (double i = -r; i < r; i += 0.01) {
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

    for (double i = -r; i < r; i += 10) {
      double raioCorte = Math.sqrt(r * r - i * i);

      pontos.addAll(
          desenharCirculoHorizontal(new Ponto3D(pontoInicial.getX(), pontoInicial.getY() + i, pontoInicial.getZ()),
              raioCorte));
    }
    return pontos;
  }
}
