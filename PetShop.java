import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;
// *** Importacoes para validacao de datas ***
import java.time.DateTimeException;
// *** Importacoes para persistencia dos dados em arquivo ***
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PetShop {
   // Nome do arquivo onde o cadastro de tutores e pets e gravado.
   private static final String ARQ_DADOS = "PetShop.dat";

   private ArrayList<Tutor> tutores;
   private Scanner teclado;
   // Impede gravar por cima de um arquivo que nao pode ser recuperado.
   private boolean leituraValida = true;
   // Guarda uma explicacao simples para uma eventual falha de gravacao.
   private String erroGravacao = "";

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
      // Recupera os dados do arquivo (ou faz o cadastramento previo no primeiro uso).
      int nTutores = recuperaDados();
      System.out.println(nTutores + " tutor(es) carregado(s).");
      if (!leituraValida) {
         System.out.println("ATENCAO: alteracoes nao serao gravadas. Verifique o arquivo " + ARQ_DADOS + ".");
      }
      System.out.println();
      char opcao;
   
      do {
         exibirMenu();
         opcao = lerOpcao();
         try {
            processarOpcao(opcao);
         } catch (RuntimeException erro) {
            // Ultima protecao: um erro inesperado nao derruba o sistema; volta ao menu.
            System.out.println("Erro inesperado na operacao: " + erro);
         }
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
      String entrada = lerLinha().toLowerCase();
      if (entrada.isEmpty()) {
         return '\0';
      }
      return entrada.charAt(0);
   }

   // Le uma linha do teclado, sem espacos externos.
   // Se a entrada terminar (Ctrl+Z/Ctrl+D ou fim de arquivo), encerra o sistema
   // em vez de lancar NoSuchElementException.
   private String lerLinha() {
      if (!teclado.hasNextLine()) {
         System.out.println();
         System.out.println("Fim da entrada de dados.");
         encerrar();
      }
      return teclado.nextLine().trim();
   }

   // Encerra o sistema. Os dados ja estao gravados, pois cada alteracao e gravada na hora.
   public void encerrar() {
      System.out.println("Encerrando o sistema.");
      teclado.close();
      System.exit(0);
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
            encerrar();
            break;
         default:
            System.out.println("Opcao invalida.");
      }
   }
    
   
   public void cadastrarTutorPets() {
      while (true) {
         System.out.print("Digite nome do tutor (vazio encerra cadastro tutor): ");
         String nomeTutor = lerLinha();
         if (nomeTutor.isEmpty()) {
            System.out.println("--- Cadastro de tutor/pets encerrado ---");
            break;
         }
      
         LocalDate dataNasc = lerData("Digite data de nascimento (dd mm aaaa): ");
      
         String endereco = lerTextoObrigatorio("Digite endereco do tutor: ");
      
         Tutor tutor = new Tutor(geraCodTutor(), nomeTutor, endereco, dataNasc);
      
         System.out.println("--- Inclusao de Pets ---");
         boolean temPet = false;
         while (true) {
            System.out.print("Digite nome do pet (vazio encerra cadastro pet): ");
            String nomePet = lerLinha();
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
            // So confirma o cadastro se a gravacao no arquivo funcionar.
            if (gravaDados()) {
               System.out.println("--- Tutor/pets cadastrado ---");
            } else {
               // Desfaz a inclusao ainda nao gravada.
               tutores.remove(tutor);
               System.out.println("--- Cadastro nao gravado. " + erroGravacao + " ---");
            }
         } else {
            System.out.println("--- Tutor nao cadastrado: nenhum pet informado ---");
         }
      }
   }

   public String lerTextoObrigatorio(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String valor = lerLinha();
         if (!valor.isEmpty()) {
            return valor;
         }
         System.out.println("Campo obrigatorio. Tente novamente.");
      }
   }

   private int lerInteiro(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         String entrada = lerLinha();
         try {
            // Converte o texto digitado para inteiro.
            return Integer.parseInt(entrada);
         } catch (NumberFormatException erro) {
            // Trata texto nao inteiro e pede nova digitacao.
            System.out.println("Valor invalido! Digite um numero inteiro.");
         }
      }
   }

   // Le uma data em uma unica linha no formato "dd mm aaaa" e repete ate ser valida.
   private LocalDate lerData(String mensagem) {
      while (true) {
         System.out.print(mensagem);
         // Separa dia, mes e ano por um ou mais espacos.
         String[] partes = lerLinha().split("\\s+");
         if (partes.length != 3) {
            System.out.println("Formato invalido! Use dd mm aaaa (ex.: 11 05 2000).");
            continue;
         }
         int dia, mes, ano;
         try {
            dia = Integer.parseInt(partes[0]);
            mes = Integer.parseInt(partes[1]);
            ano = Integer.parseInt(partes[2]);
         } catch (NumberFormatException erro) {
            // Trata dia, mes ou ano nao numericos.
            System.out.println("Dia, mes e ano devem ser numeros inteiros.");
            continue;
         }
         if (validaData(dia, mes, ano)) {
            return LocalDate.of(ano, mes, dia);
         }
         System.out.println("Data invalida! Tente novamente.");
      }
   }

   // Retorna true se a data existe no calendario (inclusive bissextos) e nao esta no futuro.
   public boolean validaData(int dia, int mes, int ano){
      // Anos zero ou negativos nao sao aceitos como nascimento.
      if (ano <= 0) {
         return false;
      }
      LocalDate data;
      try {
         // LocalDate.of lanca DateTimeException para dia/mes invalidos.
         data = LocalDate.of(ano, mes, dia);
      } catch (DateTimeException erro) {
         return false;
      }
      // Uma data valida no calendario pode estar no futuro.
      return !data.isAfter(LocalDate.now());
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
            // Grava a atualizacao no arquivo.
            if (!gravaDados()) {
               System.out.println("--- Exclusao nao gravada no arquivo. " + erroGravacao + " ---");
            }
            return;
         }
      }
   
      System.out.println("--- Codigo de tutor nao encontrado. Exclusao nao realizada. ---");
   }

   public void excluirPet() {
   
      // Sem tutores, nenhum codigo seria aceito e o laco abaixo nunca terminaria.
      if (tutores.isEmpty()) {
         System.out.println("Nenhum tutor cadastrado.");
         return;
      }

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
            // Grava a atualizacao no arquivo.
            if (!gravaDados()) {
               System.out.println("--- Exclusao nao gravada no arquivo. " + erroGravacao + " ---");
            }
            return;
           
         } else {
            System.out.println("Pet nao encontrado. Tente novamente!");
         }
      }
   }

   // *** Recuperacao dos dados do arquivo ***
   // Retorna a quantidade de tutores recuperados.
   public int recuperaDados() {
      // Usa uma lista provisoria para nao substituir dados por uma leitura falha.
      ArrayList<Tutor> lidos = new ArrayList<>();
      FileInputStream fluxoEntrada = null; // Fluxo basico do arquivo.
      ObjectInputStream entrada = null;    // Referencia usada no finally.
      boolean concluida = false;           // Indica leitura completa e fechamento correto.
      leituraValida = false;               // A gravacao fica bloqueada ate concluir a leitura.
      File arquivo = new File(ARQ_DADOS);                 // Cadastro principal.
      File anterior = new File(ARQ_DADOS + ".anterior");  // Copia de seguranca.

      // Recupera a copia se uma execucao anterior parou durante a substituicao.
      if (!arquivo.exists() && anterior.exists()) {
         if (!anterior.renameTo(arquivo)) {
            System.out.println("Nao foi possivel restaurar a copia do cadastro. Gravacao bloqueada.");
            return 0;
         }
         System.out.println("Cadastro restaurado a partir da copia de seguranca.");
      }

      // Primeiro uso: faz o cadastramento previo e grava o arquivo inicial.
      if (!arquivo.exists()) {
         leituraValida = true;
         System.out.println("Arquivo de dados inexistente. Realizando cadastramento previo.");
         popularCadastro();
         if (!gravaDados()) {
            System.out.println("Cadastro previo nao gravado. " + erroGravacao);
         }
         return tutores.size();
      }

      try {
         // Abre o fluxo que recupera a quantidade e os objetos gravados.
         fluxoEntrada = new FileInputStream(arquivo);
         entrada = new ObjectInputStream(fluxoEntrada);
         // A quantidade indica quantos objetos precisam ser recuperados.
         int quantidade = entrada.readInt();
         if (quantidade < 0) {
            System.out.println("Quantidade de tutores invalida no arquivo. Gravacao bloqueada.");
            return 0; // O finally ainda tentara fechar o arquivo.
         }
         for (int i = 0; i < quantidade; i++) {
            Object objeto = entrada.readObject();
            if (!(objeto instanceof Tutor)) {
               System.out.println("O arquivo contem um objeto que nao e um Tutor. Gravacao bloqueada.");
               return 0;
            }
            lidos.add((Tutor) objeto); // Guarda o tutor (com seus pets) recuperado.
         }
         concluida = true; // Todos os objetos previstos foram recuperados.
      } catch (EOFException erro) {
         // O arquivo terminou antes da leitura de todos os dados previstos.
         System.out.println("Arquivo de dados incompleto. Gravacao bloqueada.");
      } catch (FileNotFoundException erro) {
         // O arquivo existe, mas pode estar inacessivel ou ser um diretorio.
         System.out.println("Nao foi possivel abrir o arquivo para leitura: " + erro.getMessage());
      } catch (ClassNotFoundException erro) {
         // Um objeto salvo depende de uma classe que nao foi encontrada.
         System.out.println("Classe dos dados gravados nao encontrada: " + erro.getMessage());
      } catch (IOException erro) {
         // Abrange falhas de leitura e incompatibilidade da serializacao.
         System.out.println("Falha ao ler o arquivo de dados: " + erro.getMessage());
      } catch (RuntimeException erro) {
         // Dados adulterados podem gerar erros na reconstrucao dos objetos (ex.: data invalida).
         System.out.println("Arquivo de dados com conteudo invalido: " + erro);
      } finally {
         // O fechamento e tentado mesmo se a leitura for interrompida por erro.
         if (entrada != null) {
            try {
               entrada.close();
            } catch (IOException erro) {
               concluida = false; // Nao informa recuperacao bem-sucedida.
               System.out.println("Falha ao fechar o arquivo lido: " + erro.getMessage());
            }
         } else if (fluxoEntrada != null) {
            // Fecha tambem se o construtor do fluxo de objetos tiver falhado.
            try {
               fluxoEntrada.close();
            } catch (IOException erro) {
               System.out.println("Falha ao fechar o arquivo de leitura: " + erro.getMessage());
            }
         }
      }

      if (!concluida) {
         return 0; // Preserva a lista atual e bloqueia novas gravacoes.
      }

      tutores.clear(); // Substitui a lista somente apos concluir a recuperacao.
      tutores.addAll(lidos); // Mantem a ordem dos objetos recuperados.
      leituraValida = true; // Libera a gravacao depois de uma leitura valida.
      return tutores.size();
   }

   // *** Gravacao dos dados no arquivo ***
   // Grava primeiro em arquivo provisorio para preservar o cadastro existente.
   // Retorna true somente apos gravar, fechar e instalar o novo arquivo.
   public boolean gravaDados() {
      erroGravacao = ""; // Descarta a explicacao da tentativa anterior.
      if (!leituraValida) {
         erroGravacao = "O arquivo existente nao foi recuperado corretamente.";
         return false; // Evita substituir dados inacessiveis.
      }

      File arquivo = new File(ARQ_DADOS);                 // Arquivo principal.
      File novo = new File(ARQ_DADOS + ".novo");          // Recebera o cadastro atualizado.
      File anterior = new File(ARQ_DADOS + ".anterior");  // Preserva o original.
      FileOutputStream fluxoSaida = null;  // Fluxo basico do arquivo.
      ObjectOutputStream saida = null;     // Sera fechado no bloco finally.

      // Impede substituir um diretorio por um arquivo de cadastro.
      if ((arquivo.exists() && !arquivo.isFile()) || (anterior.exists() && !anterior.isFile())) {
         erroGravacao = "O caminho do cadastro ou da copia nao e um arquivo.";
         return false;
      }

      boolean gravado = false; // So muda apos escrever e descarregar o fluxo.
      try {
         // O arquivo principal continua intacto durante esta etapa.
         fluxoSaida = new FileOutputStream(novo);
         saida = new ObjectOutputStream(fluxoSaida);
         // Grava a quantidade antes dos objetos para conferir a leitura.
         saida.writeInt(tutores.size());
         for (Tutor tutor : tutores) {
            saida.writeObject(tutor); // Serializa cada tutor (com seus pets).
         }
         saida.flush(); // Descarrega os dados antes de tentar o fechamento.
         gravado = true;
      } catch (FileNotFoundException erro) {
         // Inclui caminho inadequado e impossibilidade de abrir para escrita.
         erroGravacao = "Nao foi possivel abrir o arquivo para gravacao. " + erro.getMessage();
      } catch (IOException erro) {
         // Falha durante a escrita dos dados.
         erroGravacao = "Ocorreu uma falha durante a gravacao dos dados. " + erro.getMessage();
      } finally {
         // O close e tentado mesmo se a escrita tiver falhado.
         if (saida != null) {
            try {
               saida.close();
            } catch (IOException erro) {
               gravado = false; // Uma falha de fechamento impede confirmar.
               erroGravacao = "Nao foi possivel concluir o arquivo gravado. " + erro.getMessage();
            }
         } else if (fluxoSaida != null) {
            // Garante fechamento se o fluxo de objetos nao chegou a ser criado.
            try {
               fluxoSaida.close();
            } catch (IOException erro) {
               System.out.println("Falha ao fechar o arquivo provisorio: " + erro.getMessage());
            }
         }
      }

      if (!gravado) {
         return false; // O cadastro principal nao foi modificado.
      }

      // So elimina uma copia antiga se o cadastro principal ainda estiver presente.
      if (anterior.exists()) {
         if (!arquivo.exists() || !anterior.delete()) {
            erroGravacao = "A copia anterior do cadastro precisa ser verificada.";
            return false;
         }
      }

      boolean haviaArquivo = arquivo.exists(); // Registra se havera restauracao.
      if (haviaArquivo && !arquivo.renameTo(anterior)) {
         erroGravacao = "Nao foi possivel preservar o cadastro anterior.";
         return false; // Mantem o original e o arquivo provisorio.
      }

      if (!novo.renameTo(arquivo)) {
         erroGravacao = "Nao foi possivel colocar o novo cadastro em uso.";
         // Se havia um cadastro, tenta restabelecer seu nome original.
         if (haviaArquivo && !anterior.renameTo(arquivo)) {
            erroGravacao += " Os dados anteriores estao em " + anterior.getName() + ".";
         }
         return false;
      }

      // O arquivo anterior permanece como copia de seguranca ate a proxima gravacao.
      return true;
   }
}
