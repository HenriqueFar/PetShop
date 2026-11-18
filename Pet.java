import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

// Representa um pet de um tutor, com sua data de nascimento (real ou estimada)
public class Pet implements Serializable {
    
    private static final long serialVersionUID = 2L;
    // Formato dd/mm/aaaa (static: nao eh gravado junto com o objeto)
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String nomePet;
    private String tipoPet;
    // Data de nascimento, ja validada antes da inclusao do pet
    private LocalDate dataNasc;

    public Pet(String nomePet, String tipoPet, LocalDate dataNasc) {
        this.nomePet = nomePet;
        this.tipoPet = tipoPet;
        this.dataNasc = dataNasc;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getTipoPet() {
        return tipoPet;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    // Calcula a idade em anos completos considerando a data atual
    public int getIdade() {
        return Period.between(dataNasc, LocalDate.now()).getYears();
    }

    // Confere os dados recuperados do arquivo (que pode ter sido alterado fora do programa)
    public boolean dadosValidos() {
        return nomePet != null && !nomePet.trim().isEmpty()
            && tipoPet != null && !tipoPet.trim().isEmpty()
            && dataNasc != null && dataNasc.getYear() > 0
            && !dataNasc.isAfter(LocalDate.now());
    }

   public String toString() {
       return "- Nome do pet: " + nomePet + "; Tipo: " + tipoPet + "; Nascimento: "
           + dataNasc.format(FORMATADOR_DATA) + " (" + getIdade() + " anos).";
    }
}
