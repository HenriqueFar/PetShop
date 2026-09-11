import java.time.LocalDate;
import java.util.ArrayList;

public class Tutor {
    private int cod;
    private String nomeTutor;
    private String endereco;
    private LocalDate dataNasc;
    private ArrayList<Pet> pets;

    public Tutor(int cod, String nomeTutor, String endereco, LocalDate dataNasc) {
        this.cod = cod;
        this.nomeTutor = nomeTutor;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.pets = new ArrayList<>();
    }
}