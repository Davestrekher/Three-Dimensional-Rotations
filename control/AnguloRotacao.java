package control;

public class AnguloRotacao {
   private double angulo = 0.0;
   public AnguloRotacao(double angulo) {
      this.angulo = angulo;
   }
   public void alterar(double valor) {
       this.angulo = valor;
   }
   
   public double obter() {
       return angulo;
   }
}