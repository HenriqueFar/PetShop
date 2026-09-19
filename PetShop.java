import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    public void executar() {
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

    private void cadastrarTutorPets() {
        System.out.println("--- Cadastro de Tutor/Pets ---");

        String nomeTutor = lerTextoObrigatorio("Nome do tutor: ");
        String endereco = lerTextoObrigatorio("Endereco: ");
        LocalDate dataNasc = lerData("Data de nascimento (dd MM yyyy): ");

        Tutor tutor = new Tutor(nomeTutor, endereco, dataNasc);

        int quantidadePets = lerInteiroNaoNegativo("Quantidade de pets: ");
        for (int i = 1; i <= quantidadePets; i++) {
            System.out.println("Cadastro do pet " + i + ":");
            String nomePet = lerTextoObrigatorio("  Nome do pet: ");
            String tipoPet = lerTextoObrigatorio("  Tipo do pet: ");
            tutor.adicionarPet(new Pet(nomePet, tipoPet));
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
                    throw new DateTimeParseException("Formato invalido", entrada, 0);
                }

                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int ano = Integer.parseInt(partes[2]);

                return LocalDate.of(ano, mes, dia);
            } catch (DateTimeParseException | NumberFormatException e) {
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
        System.out.println("funcao ainda indisponivel");
    }

    private void excluirPet() {
        System.out.println("funcao ainda indisponivel");
    }
}