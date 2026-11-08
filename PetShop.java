// Arquivo inicial do aplicativo de cadastro de tutores e pets.
// Compilar: javac *.java
// Executar: java PetShop

// Importa a classe usada para definir o fechamento da janela principal.
import javax.swing.JFrame;

// Reune o ponto de entrada do aplicativo.
public class PetShop {
   // Inicia a execucao e apresenta a janela principal.
   public static void main(String[] args) {
      // O construtor recupera os dados do arquivo e usa pack() para
      // calcular o tamanho externo do JFrame a partir do painel principal.
      PetInterface pi = new PetInterface(640, 440);
      // Encerra a aplicacao quando a janela principal e fechada pelo usuario.
      pi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Centraliza a janela na tela.
      pi.setLocationRelativeTo(null);
      // Mostra a janela depois de criar e organizar seus componentes.
      pi.setVisible(true);
   }
}
