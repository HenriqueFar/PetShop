//PetInterface
// Janela principal do cadastro de tutores e pets. Define as telas de cada operacao, executa as operacoes do cadastro e mantem os dados
// gravados no arquivo PetShop.dat (persistencia por serializacao).

//Importacoes para interface e imagem
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.time.LocalDate;

public class PetInterface extends JFrame {
   private int largura, altura; 
   private static JTextArea logArea = new JTextArea("", 10, 72); // Area de log das acoes da sessao 
   private static int sizeLog = 0; // Variavel que serve pra fazer o contador de mensagens do log
   private static final String ARQ_DADOS = "PetShop.dat";  // Arquivo de dados
   private static final String ARQ_IMAGEM = "pets.png";    // arquivo da faixa decorativa
   
   private static ArrayList<Tutor> tutores = new ArrayList<Tutor>();
   // Funcionalidades - texto dos botoes da tela principal
   private static final String[] option = {"Cadastrar tutor + pets", "Imprimir cadastro",
      "Buscar por codigo", "Excluir tutor", "Excluir pet", "Encerrar"};
      
   // Impede gravar por cima de um arquivo que nao pode ser recuperado
   private static boolean leituraValida = true;
   
   // Guarda uma explicacao simples para uma eventual falha de gravacao
   private static String erroGravacao = "";
   
   // Fonte monoespacada pra manter as colunas alinhadas nas areas de texto
   static Font f = new Font("Monospaced", Font.PLAIN, 13);

   // Criacao da tela principal 
   public PetInterface(int largura, int altura) {
      super("CADASTRO DE TUTORES E PETS"); 
      this.largura = largura;
      this.altura = altura;
      setLayout(new FlowLayout());
      JPanel painel = new JPanel(); 
      painel.setPreferredSize(new Dimension(largura, altura));
      add(painel);

      // Botoes das operacoes
      ButtonHandler handler = new ButtonHandler();
      Box botoes = Box.createVerticalBox();
      JPanel linhaBotoes = null;
      for (int k = 0; k < option.length; k++) {
         if (k % 3 == 0) { // fizemos isso pra que inicie uma nova linha a cada tres botoes
            linhaBotoes = new JPanel(new FlowLayout());
            botoes.add(linhaBotoes);
         }
         JButton button = new JButton(option[k]);
         button.addActionListener(handler); 
         linhaBotoes.add(button);
      }
      painel.add(botoes);

      // log com as acoes realizadas na sessao
      painel.add(new JLabel("Log de acoes realizadas na sessao."));
      Box box = Box.createHorizontalBox();
      logArea.setFont(f);
      logArea.setEditable(false);
      logArea.setLineWrap(true);       
      logArea.setWrapStyleWord(true);  
      box.add(new JScrollPane(logArea));
      painel.add(box);

      // Faixa decorativa 
      BufferedImage imgDecor = null;
      try {
         imgDecor = ImageIO.read(new File(ARQ_IMAGEM));
      } catch (IOException erro) {
         // arquivo ausente ou ilegivel. O aplicativo continua sem a faixa
         imgDecor = null;
      }
      JPanel decor = new JPanel(new FlowLayout());
      if (imgDecor != null) { // ImageIO.read devolve null se nao for imagem
         decor.add(new JLabel(new ImageIcon(imgDecor)));
      } else {
         writeLog("Nao foi possivel carregar a imagem " + ARQ_IMAGEM + ".");
      }
      painel.add(decor);

      // Recupera os dados do arquivo ou faz o cadastro previo no primeiro uso
      int nTutores = recuperaDados();
      writeLog(nTutores + " tutor(es) carregado(s).");
      pack(); // calcular a moldura apos incluir todos os componentes

      if (!leituraValida) {
         JOptionPane.showMessageDialog(this,
            "Nao foi possivel ler o arquivo " + ARQ_DADOS + " (detalhes no log).\n" +
            "Para nao perder os dados existentes, nenhuma alteracao sera gravada.",
            "Aviso", JOptionPane.WARNING_MESSAGE);
      }
   }

   // ouvinte dos botoes da tela principal assim cada opcao chama o seu metodo
   private class ButtonHandler implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent event) {
         String opcao = event.getActionCommand();
         if (opcao.equals(option[0])) {
            cadastrar();
         } else if (opcao.equals(option[1])) {
            imprimirCadastro();
         } else if (opcao.equals(option[2])) {
            buscarTutor();
         } else if (opcao.equals(option[3])) {
            excluirTutor();
         } else if (opcao.equals(option[4])) {
            excluirPet();
         } else if (opcao.equals(option[5])) {
            encerrar();
         }
      }
   }

   // Metodos

 
   public static void writeLog(String s) {
      if (sizeLog == 0) { // O cabecalho eh escrito uma unica vez
         logArea.append("LOG CRIADO...");
      }
      logArea.append("\n- " + s);
      sizeLog++;
   }

   // mostra um aviso sobre dado de entrada do usuario que precisa ser corrigido
   private static void aviso(Component janela, String msg) {
      JOptionPane.showMessageDialog(janela, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
   }

   // mstra uma falha de operacao 
   private static void erro(Component janela, String msg) {
      JOptionPane.showMessageDialog(janela, msg, "Erro", JOptionPane.ERROR_MESSAGE);
   }

   // monta a linha de componentes alinhados a esquerda
   private static JPanel linha(Component... itens) {
      JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
      for (Component c : itens) {
         p.add(c);
      }
      return p;
   }

   // area de texto somente leitura 
   private static JTextArea areaTexto(int linhas, int colunas) {
      JTextArea area = new JTextArea("", linhas, colunas);
      area.setFont(f);
      area.setEditable(false);
      return area;
   }

   // produz o proximo codigo de tutor a partir do ultimo da lista
   // devolve -1 se o ultimo codigo ja for o maior inteiro possivel do java (meio impossivel mas eh boa pratica kkkkkk)
   public static int geraCod() {
      if (tutores.isEmpty()) {
         return 1; //Primeiro cadastro
      }
      int ultimo = tutores.get(tutores.size() - 1).getCod();
      if (ultimo == Integer.MAX_VALUE) {
         return -1; // Evita que o codigo fique negativo
      }
      return ultimo + 1;
   }

   // devolve o tutor com o codigo informado ou null se nao existir
   private static Tutor buscaTutor(int cod) {
      for (Tutor t : tutores) {
         if (t.getCod() == cod) {
            return t;
         }
      }
      return null;
   }

   // converte o codigo digitado - mostra aviso e devolve -1 se nao for inteiro positivo
   private static int lerCodigo(Component janela, JTextField campo) {
      int cod;
      try {
         cod = Integer.parseInt(campo.getText().trim());
      } catch (NumberFormatException erro) { // texto vazio ou nao inteiro
         aviso(janela, "Informe o codigo do tutor (numero inteiro).");
         return -1;
      }
      if (cod <= 0) {
         aviso(janela, "O codigo do tutor deve ser maior que zero.");
         return -1;
      }
      return cod;
   }

   // Converte e valida a data informada nos campos dia, mes e ano
   // a data deve existir no calendario (inclusive bissextos), ter ano positivo e nao estar no futuro
   
   private static LocalDate lerData(Component janela, String quem,
                                    JTextField dia, JTextField mes, JTextField ano) {
      String d = dia.getText().trim();
      String m = mes.getText().trim();
      String a = ano.getText().trim();
      if (d.isEmpty() || m.isEmpty() || a.isEmpty()) {
         aviso(janela, "Preencha dia, mes e ano de nascimento " + quem + ".");
         return null;
      }
      int D, M, A;
      try {
         D = Integer.parseInt(d);
         M = Integer.parseInt(m);
         A = Integer.parseInt(a);
      } catch (NumberFormatException erro) { // Trta exto nao inteiro
         aviso(janela, "Os campos da data de nascimento " + quem + " devem ser numericos.");
         return null;
      }
      if (A <= 0) {
         aviso(janela, "O ano de nascimento " + quem + " deve ser maior que zero.");
         return null;
      }
      LocalDate data;
      try {
         data = LocalDate.of(A, M, D); //DateTimeException se a data nao existir
      } catch (DateTimeException erro) {
         aviso(janela, "Data de nascimento " + quem + " invalida (dia ou mes inexistente).");
         return null;
      }
      if (data.isAfter(LocalDate.now())) {
         aviso(janela, "A data de nascimento " + quem + " nao pode estar no futuro.");
         return null;
      }
      return data;
   }

   // Cadastro de tutor + pets 
   private void cadastrar() {
      CadastrarFrame cf = new CadastrarFrame();
      cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
      cf.pack();
      cf.setLocationRelativeTo(this);
      cf.setVisible(true);
   }

   private class CadastrarFrame extends JFrame {
      // campos do tutor
      private JTextField nome = new JTextField(35);
      private JTextField endereco = new JTextField(35);
      private JTextField dia = new JTextField(2), mes = new JTextField(2), ano = new JTextField(4);
      // campos do pet 
      private JTextField nomePet = new JTextField(15);
      private JTextField tipoPet = new JTextField(12);
      private JTextField diaPet = new JTextField(2), mesPet = new JTextField(2), anoPet = new JTextField(4);
      // lista pra armazenar os pets ja incluidos neste formulario mas ainda nao gravados
      private ArrayList<Pet> petsNovos = new ArrayList<Pet>();
      private JTextArea listaPets = areaTexto(5, 72);

      public CadastrarFrame() {
         super("CADASTRAR TUTOR + PETS");
         Box box = Box.createVerticalBox();
         add(box);
         box.add(linha(new JLabel("Nome do tutor"), nome));
         box.add(linha(new JLabel("Endereco"), endereco));
         box.add(linha(new JLabel("Data de nasc.(dd/mm/aaaa)"),
            dia, new JLabel("/"), mes, new JLabel("/"), ano));

         box.add(linha(new JLabel("Pets do tutor (inclua pelo menos um):")));
         box.add(linha(new JLabel("Nome do pet"), nomePet, new JLabel("Tipo"), tipoPet));
         JButton incluir = new JButton("Incluir pet");
         incluir.addActionListener(new IncluirPetHandler());
         box.add(linha(new JLabel("Data de nasc.(dd/mm/aaaa)"),
            diaPet, new JLabel("/"), mesPet, new JLabel("/"), anoPet, incluir));
         box.add(linha(new JScrollPane(listaPets)));

         JButton ok = new JButton("Cadastrar tutor + pets");
         ok.addActionListener(new CadastroHandler());
         JButton limpar = new JButton("Limpar pets");
         limpar.addActionListener(new LimparPetsHandler());
         box.add(linha(ok, limpar));
         atualizaListaPets();
      }

      // Mostra os pets incluidos ate o momento
      private void atualizaListaPets() {
         listaPets.setText("");
         if (petsNovos.isEmpty()) {
            listaPets.append("<nenhum pet incluido>");
         }
         for (Pet p : petsNovos) {
            listaPets.append(p.toString() + "\n");
         }
      }

      // valida o pet digitado e o inclui na lista do formulario
      private class IncluirPetHandler implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent evento) {
            String n = nomePet.getText().trim();
            String t = tipoPet.getText().trim();
            if (n.isEmpty() || t.isEmpty()) {
               aviso(CadastrarFrame.this, "Informe o nome e o tipo do pet.");
               return;
            }
            // trata nome de pets que nao podem se repetir para o mesmo tutor
            for (Pet p : petsNovos) {
               if (p.getNomePet().equalsIgnoreCase(n)) {
                  aviso(CadastrarFrame.this, "Ja existe um pet chamado " + n + " para este tutor.");
                  return;
               }
            }
            LocalDate dataNasc = lerData(CadastrarFrame.this, "do pet", diaPet, mesPet, anoPet);
            if (dataNasc == null) {
               return; // mantem os campos para correcao
            }
            petsNovos.add(new Pet(n, t, dataNasc));
            atualizaListaPets();
            // limpa os campos do pet para a proxima inclusao
            nomePet.setText(""); tipoPet.setText("");
            diaPet.setText(""); mesPet.setText(""); anoPet.setText("");
            nomePet.requestFocusInWindow();
         }
      }

      // descarta os pets incluidos no formulario mas ainda nao gravados
      private class LimparPetsHandler implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent evento) {
            petsNovos.clear();
            atualizaListaPets();
         }
      }

      // valida o tutor, cadastra com os pets incluidos e grava o arquivo
      private class CadastroHandler implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent evento) {
            String n = nome.getText().trim();
            String end = endereco.getText().trim();
            if (n.isEmpty() || end.isEmpty()) {
               aviso(CadastrarFrame.this, "Informe o nome e o endereco do tutor.");
               return;
            }
            LocalDate dataNasc = lerData(CadastrarFrame.this, "do tutor", dia, mes, ano);
            if (dataNasc == null) {
               return;
            }
            // evita perder um pet digitado sem clicar em Incluir pet antes de clicar em cadastrar
            if (!nomePet.getText().trim().isEmpty()) {
               aviso(CadastrarFrame.this, "Ha um pet digitado e nao incluido.\n" +
                  "Clique em \"Incluir pet\" ou apague o nome do pet.");
               return;
            }
            if (petsNovos.isEmpty()) {
               aviso(CadastrarFrame.this, "Inclua pelo menos um pet antes de cadastrar o tutor.");
               return;
            }

            int cod = geraCod();
            if (cod < 0) {
               erro(CadastrarFrame.this, "Limite de codigos de tutor atingido. Cadastro nao realizado.");
               return;
            }
            Tutor novo = new Tutor(cod, n, end, dataNasc);
            for (Pet p : petsNovos) {
               novo.incluiPet(p.getNomePet(), p.getTipoPet(), p.getDataNasc());
            }
            tutores.add(novo);
            // so confirma o cadastro se a gravacao funcionar senao desfaz
            if (!gravaDados()) {
               tutores.remove(novo);
               erro(CadastrarFrame.this, "Cadastro nao gravado. " + erroGravacao +
                  "\nOs campos foram mantidos para nova tentativa.");
               return;
            }
            String msg = "Cadastrado: " + novo.getCod() + " - " + n + " (" +
               novo.getDataNascFormatada() + "), com " + novo.numPets() + " pet(s).";
            JOptionPane.showMessageDialog(CadastrarFrame.this, msg);
            writeLog(msg);
            // Limpa o formulario para um novo cadastro
            nome.setText(""); endereco.setText("");
            dia.setText(""); mes.setText(""); ano.setText("");
            petsNovos.clear();
            atualizaListaPets();
            nome.requestFocusInWindow();
         }
      }
   }

   // Impressao do cadastro
   public void imprimirCadastro() {
      JFrame jf = new JFrame("CADASTRO IMPRESSO");
      jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JTextArea cadArea = areaTexto(20, 72);
      jf.add(linha(new JScrollPane(cadArea)));
      if (tutores.isEmpty()) {
         cadArea.append("<Cadastro vazio>");
         writeLog("Impressao: <cadastro vazio>");
      } else {
         for (Tutor t : tutores) {
            cadArea.append(t.toString());
            cadArea.append("------------------------------------------------------------\n");
         }
         cadArea.setCaretPosition(0); // Mostra o inicio da listagem
         writeLog("Cadastro impresso (" + tutores.size() + " tutor(es)).");
      }
      jf.pack();
      jf.setLocationRelativeTo(this);
      jf.setVisible(true);
   }

   // Busca de tutor e seus pets pelo codigo 
   public void buscarTutor() {
      JFrame jf = new JFrame("BUSCA POR CODIGO");
      jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JTextField codBus = new JTextField(3);
      JTextArea resBusca = areaTexto(10, 72);
      JButton buscar = new JButton("Buscar");
      buscar.addActionListener(new BuscarHandler(jf, codBus, resBusca));
      Box box = Box.createVerticalBox();
      box.add(linha(new JLabel("Cod.: "), codBus, buscar));
      box.add(linha(new JScrollPane(resBusca)));
      jf.add(box);
      jf.pack();
      jf.setLocationRelativeTo(this);
      jf.setVisible(true);
   }

   private class BuscarHandler implements ActionListener {
      private JFrame janela;
      private JTextField codBus;
      private JTextArea resBusca;

      public BuscarHandler(JFrame janela, JTextField codBus, JTextArea resBusca) {
         this.janela = janela;
         this.codBus = codBus;
         this.resBusca = resBusca;
      }

      @Override
      public void actionPerformed(ActionEvent evento) {
         int c = lerCodigo(janela, codBus);
         if (c < 0) {
            return; // aguarda um codigo valido
         }
         Tutor t = buscaTutor(c);
         if (t == null) {
            String msg = "Tutor com codigo " + c + " inexistente.";
            resBusca.setText(msg);
            writeLog("Busca: " + msg);
            return;
         }
         resBusca.setText(t.toString());
         resBusca.setCaretPosition(0);
         writeLog("Busca: " + c + " - " + t.getNomeTutor() + ", " + t.numPets() + " pet(s).");
      }
   }

   // Exclusao de tutor (com todos os seus pets)
   public void excluirTutor() {
      JFrame jf = new JFrame("EXCLUIR TUTOR");
      jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JTextField codigo = new JTextField(3);
      JButton excluir = new JButton("Excluir tutor e seus pets");
      excluir.addActionListener(new ExcluirTutorHandler(jf, codigo));
      jf.add(linha(new JLabel("Cod. do tutor: "), codigo, excluir));
      jf.pack();
      jf.setLocationRelativeTo(this);
      jf.setVisible(true);
   }

   private class ExcluirTutorHandler implements ActionListener {
      private JFrame janela;
      private JTextField codigo;

      public ExcluirTutorHandler(JFrame janela, JTextField codigo) {
         this.janela = janela;
         this.codigo = codigo;
      }

      @Override
      public void actionPerformed(ActionEvent evento) {
         int c = lerCodigo(janela, codigo);
         if (c < 0) {
            return;
         }
         Tutor t = buscaTutor(c);
         if (t == null) {
            aviso(janela, "Tutor com codigo " + c + " inexistente.");
            writeLog("Exclusao: tutor com codigo " + c + " inexistente.");
            return;
         }
         int resp = JOptionPane.showConfirmDialog(janela, "Excluir o tutor " + c + " - " +
            t.getNomeTutor() + " e seus " + t.numPets() + " pet(s)?",
            "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
         if (resp != JOptionPane.YES_OPTION) {
            return;
         }
         int pos = tutores.indexOf(t);
         tutores.remove(pos);
         // Se a gravacao falhar, recoloca o tutor na mesma posicao
         if (!gravaDados()) {
            tutores.add(pos, t);
            erro(janela, "Exclusao nao gravada. " + erroGravacao);
            return;
         }
         String msg = "Excluido: tutor " + c + " - " + t.getNomeTutor() + " e seus pets.";
         JOptionPane.showMessageDialog(janela, msg);
         writeLog(msg);
         codigo.setText("");
      }
   }

   // Exclusao de pet pelo nome
   public void excluirPet() {
      JFrame jf = new JFrame("EXCLUIR PET");
      jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JTextField codigo = new JTextField(3);
      JTextField nomePet = new JTextField(15);
      JButton excluir = new JButton("Excluir pet");
      excluir.addActionListener(new ExcluirPetHandler(jf, codigo, nomePet));
      jf.add(linha(new JLabel("Cod. do tutor: "), codigo,
         new JLabel("Nome do pet: "), nomePet, excluir));
      jf.pack();
      jf.setLocationRelativeTo(this);
      jf.setVisible(true);
   }

   private class ExcluirPetHandler implements ActionListener {
      private JFrame janela;
      private JTextField codigo, nomePet;

      public ExcluirPetHandler(JFrame janela, JTextField codigo, JTextField nomePet) {
         this.janela = janela;
         this.codigo = codigo;
         this.nomePet = nomePet;
      }

      @Override
      public void actionPerformed(ActionEvent evento) {
         int c = lerCodigo(janela, codigo);
         if (c < 0) {
            return;
         }
         Tutor t = buscaTutor(c);
         if (t == null) {
            aviso(janela, "Tutor com codigo " + c + " inexistente.");
            writeLog("Exclusao de pet: tutor com codigo " + c + " inexistente.");
            return;
         }
         String n = nomePet.getText().trim();
         if (n.isEmpty()) {
            aviso(janela, "Informe o nome do pet.");
            return;
         }
         Pet pet = t.buscaPet(n);
         if (pet == null) {
            aviso(janela, "O tutor " + c + " - " + t.getNomeTutor() + " nao possui pet chamado " + n + ".");
            writeLog("Exclusao de pet: " + n + " inexistente para o tutor " + c + ".");
            return;
         }
         String pergunta = "Excluir o pet " + pet.getNomePet() + " do tutor " + c + " - " +
            t.getNomeTutor() + "?";
         if (t.numPets() == 1) {
            pergunta += "\nO tutor tambem sera excluido, pois ficara sem pets.";
         }
         int resp = JOptionPane.showConfirmDialog(janela, pergunta,
            "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
         if (resp != JOptionPane.YES_OPTION) {
            return;
         }

         int posPet = t.excluiPet(n);
         int posTutor = tutores.indexOf(t);
         boolean tutorExcluido = false;
         if (t.numPets() == 0) { // tutor sem pets tambem e excluido
            tutores.remove(posTutor);
            tutorExcluido = true;
         }
         // Se a gravacao falhar, desfaz as alteracoes em memoria
         if (!gravaDados()) {
            t.restauraPet(posPet, pet);
            if (tutorExcluido) {
               tutores.add(posTutor, t);
            }
            erro(janela, "Exclusao nao gravada. " + erroGravacao);
            return;
         }
         String msg = "Excluido: pet " + pet.getNomePet() + " do tutor " + c + " - " + t.getNomeTutor() + ".";
         if (tutorExcluido) {
            msg += " Tutor excluido por nao possuir mais pets.";
         }
         JOptionPane.showMessageDialog(janela, msg);
         writeLog(msg);
         nomePet.setText("");
      }
   }

   // Encerramento 
   public void encerrar() {
      dispose();      // Fecha a janela principal
      System.exit(0); // Encerra a aplicacao
   }

   // Cadastro previo (somente quando o arquivo ainda nao existe)
   private static void popularCadastro() {
      Tutor t;

      t = new Tutor(geraCod(), "Zeca Silva", "Rua Tupi, 32", LocalDate.of(2000, 5, 11));
      t.incluiPet("Bilu", "Gato", LocalDate.of(2020, 3, 10));
      t.incluiPet("Wilson", "Canario", LocalDate.of(2022, 8, 5));
      tutores.add(t);

      t = new Tutor(geraCod(), "Maria Lopes", "Av. Sergipe, 421", LocalDate.of(1988, 2, 22));
      t.incluiPet("Loro", "Papagaio", LocalDate.of(2010, 1, 15));
      tutores.add(t);

      t = new Tutor(geraCod(), "Roberto Silva", "Rua Parana, 80", LocalDate.of(1995, 8, 17));
      t.incluiPet("Nina", "Cao", LocalDate.of(2018, 11, 20));
      tutores.add(t);
   }

   // Recuperacao dos dados do arquivo 
   // Retorna a quantidade de tutores recuperados  
   public static int recuperaDados() {
      // usa uma lista provisoria para nao sair substituindo dados por uma leitura falha
      ArrayList<Tutor> lidos = new ArrayList<Tutor>();
      FileInputStream fluxoEntrada = null; // fluxo basico do arquivo.
      ObjectInputStream entrada = null;    // referencia usada no finally
      boolean concluida = false;           // indica leitura completa e fechamento correto
      leituraValida = false;               // a gravacao fica bloqueada ate concluir a leitura
      File arquivo = new File(ARQ_DADOS);                 // cadastro principa
      File anterior = new File(ARQ_DADOS + ".anterior");  // copia de seguranca - fizemos conforme o exemplo no pdf da atividade 
      
      // Recupera a copia se uma execucao anterior parou durante a substituicao
      if (!arquivo.exists() && anterior.exists()) {
         if (!anterior.renameTo(arquivo)) {
            writeLog("Nao foi possivel restaurar a copia do cadastro. Gravacao bloqueada.");
            return 0;
         }
         writeLog("Cadastro restaurado a partir da copia de seguranca.");
      }

      // Primeiro uso - faz o cadastro previo e grava o arquivo inicial
      if (!arquivo.exists()) {
         leituraValida = true;
         writeLog("Arquivo " + ARQ_DADOS + " inexistente. Cadastramento previo realizado.");
         popularCadastro();
         if (!gravaDados()) {
            writeLog("Cadastro previo nao gravado. " + erroGravacao);
         }
         return tutores.size();
      }

      try {
         // Abre o fluxo que recupera a quantidade e os objetos gravados
         fluxoEntrada = new FileInputStream(arquivo);
         entrada = new ObjectInputStream(fluxoEntrada);
         // A quantidade indica quantos tutores precisam ser recuperados
         int quantidade = entrada.readInt();
         if (quantidade < 0) {
            writeLog("Quantidade de tutores invalida no arquivo. Gravacao bloqueada.");
            return 0; // O finally ainda tentara fechar o arquivo
         }
         for (int i = 0; i < quantidade; i++) {
            Object objeto = entrada.readObject();
            if (!(objeto instanceof Tutor)) {
               writeLog("O arquivo contem um objeto que nao e um Tutor. Gravacao bloqueada.");
               return 0;
            }
            Tutor tutor = (Tutor) objeto; // Cada tutor vem com a sua lista de pets
            // O arquivo pode ter sido alterado fora do programa: confere o conteudo
            // e exige codigos positivos, sem repeticao e em ordem crescente
            int codAnterior = lidos.isEmpty() ? 0 : lidos.get(lidos.size() - 1).getCod();
            if (!tutor.dadosValidos() || tutor.getCod() <= codAnterior) {
               writeLog("O arquivo contem dados de tutor invalidos. Gravacao bloqueada.");
               return 0;
            }
            lidos.add(tutor);
         }
         concluida = true; // Todos os objetos previstos foram recuperados
      } catch (EOFException erro) {
         // O arquivo terminou antes da leitura de todos os dados previstos
         writeLog("Arquivo de dados incompleto. Gravacao bloqueada.");
      } catch (FileNotFoundException erro) {
         // O arquivo existe, mas pode estar inacessivel ou ser um diretorio
         writeLog("Nao foi possivel abrir o arquivo para leitura: " + erro.getMessage());
      } catch (ClassNotFoundException erro) {
         // Um objeto salvo depende de uma classe que nao foi encontrada
         writeLog("Classe dos dados gravados nao encontrada: " + erro.getMessage());
      } catch (IOException erro) {
         // Abrange falhas de leitura e incompatibilidade da serializacao
         writeLog("Falha ao ler o arquivo de dados: " + erro.getMessage());
      } catch (RuntimeException erro) {
         // Dados adulterados podem gerar erros na reconstrucao dos objetos
         writeLog("Arquivo de dados com conteudo invalido: " + erro);
      } finally {
         // O fechamento e tentado mesmo se a leitura for interrompida por erro
         if (entrada != null) {
            try {
               entrada.close();
            } catch (IOException erro) {
               concluida = false; // Nao informa recuperacao bem-sucedida
               writeLog("Falha ao fechar o arquivo lido: " + erro.getMessage());
            }
         } else if (fluxoEntrada != null) {
            // Fecha tambem se o construtor do fluxo de objetos tiver falhado
            try {
               fluxoEntrada.close();
            } catch (IOException erro) {
               writeLog("Falha ao fechar o arquivo de leitura: " + erro.getMessage());
            }
         }
      }

      if (!concluida) {
         return 0; // Preserva a lista atual e bloqueia novas gravacoes
      }

      tutores.clear(); // Substitui a lista somente apos concluir a recuperacao
      tutores.addAll(lidos); // Mantem a ordem dos objetos recuperados
      leituraValida = true; // Libera a gravacao depois de uma leitura valida
      return tutores.size();
   }

   //  Gravacao dos dados no arquivo 
   // Grava primeiro em arquivo provisorio para preservar o cadastro existente
   // Retorna true somente apos gravar, fechar e instalar o novo arquivo
   public static boolean gravaDados() {
      erroGravacao = ""; // Descarta a explicacao da tentativa anterior
      if (!leituraValida) {
         erroGravacao = "O arquivo existente nao foi recuperado corretamente.";
         writeLog(erroGravacao);
         return false; // Evita substituir dados inacessiveis
      }

      File arquivo = new File(ARQ_DADOS);                 // Arquivo principal
      File novo = new File(ARQ_DADOS + ".novo");          // Recebera o cadastro atualizado
      File anterior = new File(ARQ_DADOS + ".anterior");  // Preserva o original
      FileOutputStream fluxoSaida = null;  // Fluxo basico do arquivo
      ObjectOutputStream saida = null;     // Sera fechado no bloco finally

      // Impede substituir um diretorio por um arquivo de cadastro
      if ((arquivo.exists() && !arquivo.isFile()) || (anterior.exists() && !anterior.isFile())) {
         erroGravacao = "O caminho do cadastro ou da copia nao e um arquivo.";
         writeLog(erroGravacao);
         return false;
      }

      boolean gravado = false; // So muda apos escrever e descarregar o fluxo
      try {
         // O arquivo principal continua intacto durante esta etapa
         fluxoSaida = new FileOutputStream(novo);
         saida = new ObjectOutputStream(fluxoSaida);
         // Grava a quantidade antes dos objetos para conferir a leitura
         saida.writeInt(tutores.size());
         for (Tutor tutor : tutores) {
            saida.writeObject(tutor); // Serializa o tutor junto com seus pets
         }
         saida.flush(); // Descarrega os dados antes de tentar o fechamento
         gravado = true;
      } catch (FileNotFoundException erro) {
         // Inclui caminho inadequado e impossibilidade de abrir para escrita
         erroGravacao = "Nao foi possivel abrir o arquivo para gravacao.";
         writeLog(erroGravacao + " " + erro.getMessage());
      } catch (IOException erro) {
         // Falha durante a escrita dos dados
         erroGravacao = "Ocorreu uma falha durante a gravacao dos dados.";
         writeLog(erroGravacao + " " + erro.getMessage());
      } finally {
         // O close e tentado mesmo se a escrita tiver falhado
         if (saida != null) {
            try {
               saida.close();
            } catch (IOException erro) {
               gravado = false; // Uma falha de fechamento impede confirmar
               erroGravacao = "Nao foi possivel concluir o arquivo gravado.";
               writeLog(erroGravacao + " " + erro.getMessage());
            }
         } else if (fluxoSaida != null) {
            // Garante fechamento se o fluxo de objetos nao chegou a ser criado
            try {
               fluxoSaida.close();
            } catch (IOException erro) {
               writeLog("Falha ao fechar o arquivo provisorio: " + erro.getMessage());
            }
         }
      }

      if (!gravado) {
         return false; // O cadastro principal nao foi modificado
      }

      // So elimina uma copia antiga se o cadastro principal ainda estiver presente
      if (anterior.exists()) {
         if (!arquivo.exists() || !anterior.delete()) {
            erroGravacao = "A copia anterior do cadastro precisa ser verificada.";
            writeLog(erroGravacao);
            return false;
         }
      }

      boolean haviaArquivo = arquivo.exists(); // Registra se havera restauracao
      if (haviaArquivo && !arquivo.renameTo(anterior)) {
         erroGravacao = "Nao foi possivel preservar o cadastro anterior.";
         writeLog(erroGravacao);
         return false; // Mantem o original e o arquivo provisorio
      }

      if (!novo.renameTo(arquivo)) {
         erroGravacao = "Nao foi possivel colocar o novo cadastro em uso.";
         // Se havia um cadastro, tenta restabelecer seu nome original
         if (haviaArquivo && !anterior.renameTo(arquivo)) {
            erroGravacao += " Os dados anteriores estao em " + anterior.getName() + ".";
         }
         writeLog(erroGravacao);
         return false;
      }

      // O arquivo anterior permanece como copia de seguranca ate a proxima gravacao
      return true;
   }
}
