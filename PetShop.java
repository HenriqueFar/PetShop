import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;

public class PetShop {
   private ArrayList<Tutor> tutores;
   private Scanner teclado;

   public PetShop() {
      this.tutores = new ArrayList<>();
      this.teclado = new Scanner(System.in);
   }

   public static void main(String[] args) {
      PetShop petShop = new PetShop();
      petShop.executar();
   }
    
    //gabriel: mudei a logica pra conseguir gerar um codigo de tutor automaticamente
   private int geraCodTutor(){
      if (tutores.isEmpty()){
         return 1;
      }
      
      return tutores.get(tutores.size()-1).getCod() + 1;
   }
    
   public void popularCadastro(){
      Tutor t;
      
      t = new Tutor(geraCodTutor(), "Zeca Silva", "Rua Tupi, 32", LocalDate.of(2000, 5, 11));
      t.incluiPet("Bilu", "Gato");
      t.incluiPet("Wilson", "Canario");
      tutores.add(t);
      
      t = new Tutor(geraCodTutor(), "Maria Lopes", "Av. Sergipe, 421", LocalDate.of(1988, 2, 22));
      t.incluiPet("Loro", "Papagaio");
      tutores.add(t);
      
      t = new Tutor(geraCodTutor(), "Roberto Silva", "Rua Parana, 80", LocalDate.of(1995, 8, 17));
      t.incluiPet("Nina", "Cao");
      tutores.add(t);
   }

   public void executar() {
      popularCadastro();
      char opcao;
   
      do {
         exibirMenu();
         opcao = lerOpcao();
         processarOpcao(opcao);
         System.out.println();
      } while (opcao != 'x');
   }

   private void exibirMenu() {
      System.out.println("***** ESCOLHER UMA OPCAO *****");
      System.out.println("c: cadastrar tutor/pets");
      System.out.println("i: imprimir cadastro");
      System.out.println("b: buscar tutor/pets por codigo");
      System.out.println("e: excluir tutor (+pets) por codigo");
      System.out.println("p: excluir pet");
      System.out.println("x: encerrar.");
      System.out.print("Opcao: ");
   }

   private char lerOpcao() {
      String entrada = teclado.nextLine().trim().toLowerCase();
      if (entrada.isEmpty()) {
         return '\0';
      }
      return entrada.charAt(0);
   }

   private void processarOpcao(char opcao) {
      switch (opcao) {
         case 'c':
            cadastrarTutorPets();
            break;
         case 'i':
            imprimirCadastro();
            break;
         case 'b':
            buscarTutorPetsPorCodigo();
            break;
         case 'e':
            excluirTutorPorCodigo();
            break;
         case 'p':
            excluirPet();
            break;
         case 'x':
            System.out.println("Encerrando o sistema.");
            break;
         default:
            System.out.println("Opcao invalida.");
      }
   }
    
    //fazer um if para mostrar se o tutor.incluiPet deu certo ou nn
   private void cadastrarTutorPets() {
      System.out.println("--- Cadastro de Tutor/Pets ---");
   
      String nomeTutor = lerTextoObrigatorio("Nome do tutor: ");
      String endereco = lerTextoObrigatorio("Endereco: ");
      LocalDate dataNasc = lerData("Data de nascimento (dd MM yyyy): ");
   
      Tutor tutor = new Tutor(geraCodTutor(), nomeTutor, endereco, dataNasc);
   
      int quantidadePets = lerInteiroNaoNegativo("Quantidade de pets: ");
      for (int i = 1; i <= quantidadePets; i++) {
         System.out.println("Cadastro do pet " + i + ":");
            
         boolean incluido = false;
         while(!incluido){
            String nomePet = lerTextoObrigatorio(" Nome do Pet: ");
            String tipoPet = lerTextoObrigatorio(" Tipo do Pet: ");
            
            incluido = tutor.incluiPet(nomePet, tipoPet);
            
            if(incluido){
               System.out.println(" --- Pet cadastrado ---");
            } else {
               System.out.println(" Ja existe um pet com esse nome para esse tutor! Tente outro nome.");
            }
         }
      }
        
        
   
      tutores.add(tutor);
      System.out.println("Tutor cadastrado com sucesso. Codigo gerado: " + tutor.getCod());
   }

   private String lerTextoObrigatorio(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String valor = teclado.nextLine().trim();
         if (!valor.isEmpty()) {
            return valor;
         }
         System.out.println("Campo obrigatorio. Tente novamente.");
      }
   }

   private int lerInteiroNaoNegativo(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String entrada = teclado.nextLine().trim();
         try {
            int valor = Integer.parseInt(entrada);
            if (valor >= 0) {
               return valor;
            }
            System.out.println("Informe um numero maior ou igual a zero.");
         } catch (NumberFormatException e) {
            System.out.println("Valor invalido. Informe um numero inteiro.");
         }
      }
   }

   private LocalDate lerData(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String entrada = teclado.nextLine().trim();
         try {
            String[] partes = entrada.split("\\s+");
            if (partes.length != 3) {
               throw new IllegalArgumentException();
            }
         
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
         
            return LocalDate.of(ano, mes, dia);
         } catch (Exception e) {
            System.out.println("Data invalida. Use o formato dd MM yyyy.");
         }
      }
   }



   private void imprimirCadastro() {
      System.out.println("--- Cadastro de Tutores e Pets ---");
   
      if (tutores.isEmpty()) {
         System.out.println("Nenhum cadastro encontrado.");
         return;
      }
   
      for (Tutor tutor : tutores) {
         tutor.imprimirInformacoes();
      
         System.out.println("------------------------------");
      }
   }

   private void buscarTutorPetsPorCodigo() {
      int cod = lerInteiroNaoNegativo("Digite codigo do tutor a ser localizado: ");
      boolean achou = false;
   
      for (Tutor t : tutores){
         if(t.getCod() == cod){
            System.out.println("--- Tutor localizado ---");
            t.imprimirInformacoes();
            achou = true;
            break;
         }
      }
      if (!achou){
         System.out.println("\n---Código de tutor não encontrado!---");
      }
   
   
   }

   private void excluirTutorPorCodigo() {
      int codigo = lerInteiroNaoNegativo("Digite o codigo do tutor que deseja excluir: ");

      for (int i = 0; i < tutores.size(); i++) {
         Tutor tutor = tutores.get(i);

         if (tutor.getCod() == codigo) {
            tutores.remove(i);
            System.out.println("--- Tutor e todos os seus pets excluidos com sucesso. ---");
            return;
         }
      }

      System.out.println("--- Codigo de tutor nao encontrado. Exclusao nao realizada. ---");
   }

   private void excluirPet() {
   
      Tutor tutorEscolhido = null;
      boolean codigoExiste = false;
   
      while (true) {
      
         int code = lerInteiroNaoNegativo("Digite o codigo do tutor: ");
      
         for (int i = 0; i < tutores.size(); i++) {
         
            Tutor x = tutores.get(i);
         
            if (x.getCod() == code) {
               codigoExiste = true;
               tutorEscolhido = x;
               break;
            }
         }
      
         if (codigoExiste == false) {
            System.out.println("Codigo invalido, tente novamente!");
         } else {
            break;
         }
      }
   
    // Verifica se o tutor possui pets
      if (tutorEscolhido.getPets().isEmpty()) {
         System.out.println("Esse tutor nao possui pets.");
         return;
      }
   
      String nomeDoPet = "";
   
      while (true) {
      
         String nomepet = lerTextoObrigatorio(
            "Digite nome do pet a ser excluido: "
            );
      
         boolean petExiste = false;
      
         for (int i = 0; i < tutorEscolhido.getPets().size(); i++) {
         
            Pet petAtual = tutorEscolhido.getPets().get(i);
         
            if (nomepet.equalsIgnoreCase(petAtual.getNomePet())) {
            
               petExiste = true;
               nomeDoPet = petAtual.getNomePet();
            
               tutorEscolhido.getPets().remove(i);
            
                // Se não restaram pets, remove o tutor também
               if (tutorEscolhido.getPets().isEmpty()) {
                  tutores.remove(tutorEscolhido);
               
                  System.out.println(
                        "--- Pet " + nomeDoPet +
                        " excluido com sucesso! ---"
                     );
               
                  System.out.println(
                        "--- O tutor nao possui mais pets e foi excluido. ---"
                     );
               
                  return;
               }
            
               break;
            }
         }
      
         if (petExiste) {
            break;
         } else {
            System.out.println("Pet nao encontrado. Tente novamente!");
         }
      }
   
      System.out.println(
         "--- Pet " + nomeDoPet + " excluido com sucesso! ---"
         );
   }
}
