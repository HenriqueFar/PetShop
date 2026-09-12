import java.util.ArrayList;
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
        System.out.println("funcao ainda indisponivel");
    }

    private void imprimirCadastro() {
        System.out.println("funcao ainda indisponivel");
    }

    private void buscarTutorPetsPorCodigo() {
        System.out.println("funcao ainda indisponivel");
    }

    private void excluirTutorPorCodigo() {
        System.out.println("funcao ainda indisponivel");
    }

    private void excluirPet() {
        System.out.println("funcao ainda indisponivel");
    }
}