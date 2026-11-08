// Permite gravar objetos Pet por serializacao.
import java.io.Serializable;
// Representa uma data, sem horario.
import java.time.LocalDate;
// Representa a diferenca entre duas datas.
import java.time.Period;
// Permite definir o formato de apresentacao das datas.
import java.time.format.DateTimeFormatter;

// Representa um pet de um tutor, com sua data de nascimento (real ou estimada).
public class Pet implements Serializable {
    // Versao da classe. Mudou ao incluir a data de nascimento: arquivos no
    // formato antigo sao recusados na leitura, com aviso, em vez de gerar dados sem data.
    private static final long serialVersionUID = 2L;
    // Formato dd/mm/aaaa (static: nao e gravado junto com o objeto).
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String nomePet;
    private String tipoPet;
    // Data de nascimento, ja validada antes da inclusao do pet.
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

    // Calcula a idade em anos completos considerando a data atual.
    public int getIdade() {
        return Period.between(dataNasc, LocalDate.now()).getYears();
    }

   public String toString() {
       return "- Nome do pet: " + nomePet + "; Tipo: " + tipoPet + "; Nascimento: "
           + dataNasc.format(FORMATADOR_DATA) + " (" + getIdade() + " anos).";
    }
}
