/* 
  Classe que representa a camera
*/

package math;

public class Camera {

  //Posicao da camera no espaco
  private double x;
  private double y;
  private double z;

  //Altura do canvas
  private double altura;

  //Foco da camera
  private double focus;

  //Campo de visao da camera
  private double fov;

  public Camera(double altura, double fov) {
    x = 0;
    y = 0;
    z = 150;

    this.fov = fov;
    this.altura = altura;
    //O foco eh definido atraves de uma relacao da altura do canvas e do campo de visao
    this.focus = (altura / 2.0) / Math.tan(fov / 2.0);
  }

  //Distancia da camera (so eh utilizada na projecao ortografica)
  private final static double distance = 100;

  /*
    Projecao que ignora a coordenada z do ponto ao projeta-lo em duas dimensoes.
    Util para projecoes mais simples, porem nao causa boa sensacao de perspectiva.
    As matrizes existem para rotacionar os pontos renderizados em relacao a camera antes de projeta-los,
    permitindo que o usuario altere a sua visao dos objetos no canvas
  */
  public Ponto2D projecaoOrtografica(Ponto3D ponto3D, double anguloHorizontal, double anguloVertical) {

    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(anguloVertical), -Math.sin(anguloVertical) },
        { 0, Math.sin(anguloVertical), Math.cos(anguloVertical) } };
    double[][] matrixY = { { Math.cos(anguloHorizontal), 0, Math.sin(anguloHorizontal) },
        { 0, 1, 0 },
        { -Math.sin(anguloHorizontal), 0, Math.cos(anguloHorizontal) } };

    double[][] matrixRes = OperacoesMatrizes.multiplicarMatrizes(matrixX, matrixY);

    double[][] matrixXYZ = { { ponto3D.getX() }, { ponto3D.getY() }, { ponto3D.getZ() } };

    double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixRes, matrixXYZ);

    double x = (matrixFinal[0][0] / distance * focus);
    double y = (matrixFinal[1][0] / distance * focus);

    // double z = matrixFinal[2][0] + distance;

    // double x = focus * matrixFinal[0][0] / z;
    // double y = focus * matrixFinal[1][0] / z;

    return new Ponto2D(x, y);
  }

  /* 
    Projecao que leva em consideracao a coordenada z dos objetos e da camera, causando uma melhor sensacao
    de projundidade e perspectiva.
    As matrizes existem para rotacionar os pontos renderizados em relacao a camera antes de projeta-los,
    permitindo que o usuario altere a sua visao dos objetos no canvas
  */
  public Ponto2D projecaoPerspectiva(Ponto3D ponto3D, double anguloHorizontal, double anguloVertical) {

    double[][] matrixX = { { 1, 0, 0 },
        { 0, Math.cos(anguloVertical), Math.sin(anguloVertical) },
        { 0, -Math.sin(anguloVertical), Math.cos(anguloVertical) } };
    double[][] matrixY = { { Math.cos(anguloHorizontal), 0, -Math.sin(anguloHorizontal) },
        { 0, 1, 0 },
        { Math.sin(anguloHorizontal), 0, Math.cos(anguloHorizontal) } };

    double[][] matrixRes = OperacoesMatrizes.multiplicarMatrizes(matrixX, matrixY);

    double[][] matrixXYZ = { { ponto3D.getX() }, { ponto3D.getY() }, { ponto3D.getZ() } };

    double[][] matrixFinal = OperacoesMatrizes.multiplicarMatrizes(matrixRes, matrixXYZ);

    /*
      Caso invertido (getZ() - matrixFinal[2][0]), pontos que estao longe irao se comportar
      como se estivessem perto e vice-versa
    */
    double z = matrixFinal[2][0] - getZ();

    double x = focus * matrixFinal[0][0] / z;
    double y = focus * matrixFinal[1][0] / z;

    return new Ponto2D(x, y);
  }

  public Ponto2D projecaoPerspectivaQuaternios(Ponto3D ponto, double anguloHorizontal, double anguloVertical,
      double anguloEmTornoDoEixo) {
    Quaternio q = new Quaternio(Math.cos(anguloHorizontal / 2.0), 0, Math.sin(anguloVertical / 2.0), 0);

    return new Ponto2D(x, y);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  /* 
    Os metodos abaixo sao chamados quando o usuario da zoom no canvas ao girar o scroll do mouse,
    alterando a distancia da camera pelo eixo z
  */

  public void aproximar() {
    z += 10;
  }

  public void afastar() {
    z -= 10;
  }
}
