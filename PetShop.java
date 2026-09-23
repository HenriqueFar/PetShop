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
   public int geraCodTutor(){
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

   public char lerOpcao() {
      String entrada = teclado.nextLine().trim().toLowerCase();
      if (entrada.isEmpty()) {
         return '\0';
      }
      return entrada.charAt(0);
   }

   public void processarOpcao(char opcao) {
      switch (opcao) {
         case 'c':
            cadastrarTutorPets();
            break;
         case 'i':
            imprimirCadastro();
            break;
         case 'b':
            buscarTutor();
            break;
         case 'e':
            excluirTutor();
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
    
   
   public void cadastrarTutorPets() {
      while (true) {
         System.out.print("Digite nome do tutor (vazio encerra cadastro tutor): ");
         String nomeTutor = teclado.nextLine().trim();
         if (nomeTutor.isEmpty()) {
            System.out.println("--- Cadastro de tutor/pets encerrado ---");
            break;
         }
      
         int dia, mes, ano;
         while (true) {
            dia = lerInteiro("Digite dia de nascimento: ");
            mes = lerInteiro("Digite mes de nascimento: ");
            ano = lerInteiro("Digite ano de nascimento: ");
         
            if (validaData(dia, mes, ano)) {
               break;
            }
            System.out.println("Data invalida! Tente novamente.");
         }
         LocalDate dataNasc = LocalDate.of(ano, mes, dia);
      
         String endereco = lerTextoObrigatorio("Digite endereco do tutor: ");
      
         Tutor tutor = new Tutor(geraCodTutor(), nomeTutor, endereco, dataNasc);
      
         System.out.println("--- Inclusao de Pets ---");
         boolean temPet = false;
         while (true) {
            System.out.print("Digite nome do pet (vazio encerra cadastro pet): ");
            String nomePet = teclado.nextLine().trim();
            if (nomePet.isEmpty()) {
               System.out.println("--- Cadastro de pets do tutor encerrado ---");
               break;
            }
         
            String tipoPet = lerTextoObrigatorio("Digite tipo do pet: ");
         
            if (tutor.incluiPet(nomePet, tipoPet)) {
               temPet = true;
               System.out.println("--- Pet cadastrado ---");
            } else {
               System.out.println("Ja existe um pet com esse nome para esse tutor! Tente outro nome.");
            }
         }
      
         if (temPet) {
            tutores.add(tutor);
            System.out.println("--- Tutor/pets cadastrado ---");
         } else {
            System.out.println("--- Tutor nao cadastrado: nenhum pet informado ---");
         }
      }
   }

   public String lerTextoObrigatorio(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String valor = teclado.nextLine().trim();
         if (!valor.isEmpty()) {
            return valor;
         }
         System.out.println("Campo obrigatorio. Tente novamente.");
      }
   }

   private int lerInteiro(String mensagem) {
      int valor;
      while (true) {
         System.out.print(mensagem);
         if (teclado.hasNextInt()) {
            valor = teclado.nextInt();
            teclado.nextLine();
            return valor;
         }
         System.out.println("Valor invalido! Digite um numero inteiro.");
         teclado.nextLine();
      }
   }

   //retorna true se a data digitada eh menor que a data limite do ano para o mes em questao 
   public boolean validaData(int dia, int mes, int ano){
      if(ano <= 0 || mes < 1 || mes > 12  || dia < 1)
      {
         return false;
      }
      
      int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
      int limite = diasPorMes[mes - 1];
      
      boolean bissexto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
      if (mes == 2 && bissexto) {
         limite = 29;
      }
   
      return dia <= limite;
   }
   
   public void imprimirCadastro() {
      System.out.println("--- CADASTRO DE TUTORES E PETS ---");
      if (tutores.isEmpty()) {
         System.out.println("Nenhum cadastro encontrado.");
         return;
      }
      for (Tutor tutor : tutores) {
         System.out.println(tutor.toString());
         System.out.println("------------------------------");
      }
   }

   public void buscarTutor() {
      int cod = lerInteiro("Digite codigo do tutor a ser localizado: ");
      boolean achou = false;
   
      for (Tutor t : tutores){
         if(t.getCod() == cod){
            System.out.println("--- Tutor localizado ---\n");
            System.out.println(t.toString());
            achou = true;
            break;
         }
      }
      if (!achou){
         System.out.println("\n---Codigo de tutor nao encontrado!---");
      }
   
   
   }

   public void excluirTutor() {
      int codigo = lerInteiro("Digite o codigo do tutor que deseja excluir: ");
   
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

   public void excluirPet() {
   
      Tutor tutorEscolhido = null;
      boolean codigoExiste = false;
   
      while (true) {
      
         int code = lerInteiro("Digite o codigo do tutor: ");
      
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
      if (tutorEscolhido.numPets() == 0) {
         System.out.println("Esse tutor nao possui pets.");
         return;
      }
   
      while (true) {
      
         String nomepet = lerTextoObrigatorio("Digite nome do pet a ser excluido: ");
      
         boolean petExiste = tutorEscolhido.excluiPet(nomepet);
      
         if(petExiste){
            System.out.println("--- Pet " + nomepet + " excluido com sucesso! ---");
         
            if (tutorEscolhido.numPets() == 0) {
               tutores.remove(tutorEscolhido);
               System.out.println("--- Tutor " + tutorEscolhido.getNomeTutor() + " excluido por nao possuir mais pets! ---");
            } 
            return;
           
         } else {
            System.out.println("Pet nao encontrado. Tente novamente!");
         }
      }
   }
}
