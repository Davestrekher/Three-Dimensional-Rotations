package math;

public class OperacaoQuaternios {
  public static Quaternio soma(Quaternio qA, Quaternio qB) {
    return new Quaternio(qA.getA() + qB.getA(), qA.getI() + qB.getI(), qA.getJ() + qB.getJ(), qA.getK() + qB.getK());
  }

  public static Quaternio sub(Quaternio qA, Quaternio qB) {
    return new Quaternio(qA.getA() - qB.getA(), qA.getI() - qB.getI(), qA.getJ() - qB.getJ(), qA.getK() - qB.getK());
  }

  public static Quaternio prod(Quaternio qA, Quaternio qB) {
    double a = qA.getA() * qB.getA() - qA.getI() * qB.getI() - qA.getJ() * qB.getJ() - qA.getK() * qB.getK();
    double i = qA.getA() * qB.getI() + qA.getI() * qB.getA() + qA.getJ() * qB.getK() - qA.getK() * qB.getJ();
    double j = qA.getA() * qB.getJ() - qA.getI() * qB.getK() + qA.getJ() * qB.getA() + qA.getK() * qB.getI();
    double k = qA.getA() * qB.getK() + qA.getI() * qB.getJ() - qA.getJ() * qB.getI() + qA.getK() * qB.getA();

    return new Quaternio(a, i, j, k);
  }

  public static Quaternio div(Quaternio qA, Quaternio qB) {
    return prod(qA, inversa(qB));
  }

  public static Quaternio inversa(Quaternio q) {
    Quaternio qConjugado = new Quaternio(q.getA(), -q.getI(), -q.getJ(), -q.getK());

    double modulo = calculaModulo(q);
    modulo *= modulo;

    return new Quaternio(qConjugado.getA() / modulo, qConjugado.getI() / modulo, qConjugado.getJ() / modulo,
        qConjugado.getK() / modulo);
  }

  private static double calculaModulo(Quaternio q) {
    return Math.sqrt(q.getA() * q.getA() + q.getI() * q.getI() + q.getJ() * q.getJ() + q.getK() * q.getK());
  }
}
