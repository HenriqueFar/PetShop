import java.time.LocalDate;
import java.util.ArrayList;

public class Tutor {
    private static int proximoCod = 1;

    private int cod;
    private String nomeTutor;
    private String endereco;
    private LocalDate dataNasc;
    private ArrayList<Pet> pets;

    public Tutor(String nomeTutor, String endereco, LocalDate dataNasc) {
        this.cod = proximoCod;
        proximoCod++;
        this.nomeTutor = nomeTutor;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.pets = new ArrayList<>();
    }

    public int getCod() {
        return cod;
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public void adicionarPet(Pet pet) {
        pets.add(pet);
    }

    @Override
    public String toString() {
        return "Tutor {cod=" + cod + ", nome='" + nomeTutor + "', endereco='" + endereco
                + "', dataNasc=" + dataNasc + ", pets=" + pets + "}";
    }
}