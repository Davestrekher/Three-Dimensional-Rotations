package control;

public class AnguloRotacao {
   private double angulo = 0.0;
   private double anguloInicial;

   public AnguloRotacao(double angulo) {
      this.angulo = angulo;
      anguloInicial = angulo;
   }
   public void alterar(double valor) {
       this.angulo = valor;
   }
   public void alterarInicial(double valor){
        anguloInicial = valor;
   }
   
   public double obter() {
       return angulo;
   }

   public double obterInicial() {
       return anguloInicial;
   }
}