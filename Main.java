
//Imports necessarios para o funcionamento do programa
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vision/TelaLogin.fxml"));// Carrega o FXML da tela
                                                                                         // principal
    Parent root = loader.load();// Carrega o conteudo do FXML para um objeto Parent, que sera usado como raiz da
                                // cena

    Scene scene = new Scene(root);// Cria uma nova cena com a tela inicial como raiz

    primaryStage.setScene(scene);// Adiciona a cena ao stage
    primaryStage.setResizable(false);// Impede redimensionamento da tela para evitar problemas de layout
    primaryStage.setTitle("TDR");// Define o titulo da janela do programa
    // primaryStage.getIcons().add(new Image("file:assets/tankblue.png"));//Define
    // um icone para o programa
    primaryStage.show();// Exibe o stage
  }// Fim do metodo start

  public static void main(String[] args) {
    launch(args);
  }
}
