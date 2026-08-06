package math;

public class Quaternio {
  private double a;
  private double i;
  private double j;
  private double k;

  public Quaternio(double a, double i, double j, double k) {
    this.a = a;
    this.i = i;
    this.j = j;
    this.k = k;
  }

  public double getA() {
    return a;
  }

  public double getI() {
    return i;
  }

  public double getJ() {
    return j;
  }

  public double getK() {
    return k;
  }

  public void setA(double a) {
    this.a = a;
  }

  public void setI(double i) {
    this.i = i;
  }

  public void setJ(double j) {
    this.j = j;
  }

  public void setK(double k) {
    this.k = k;
  }

  @Override
  public String toString() {
    return "a" + a + " i" + i + " j" + j + " k" + k;
  }
}
