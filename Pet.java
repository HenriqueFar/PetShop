// Permite gravar objetos Pet por serializacao.
import java.io.Serializable;

public class Pet implements Serializable {
    // Versao fixa da classe: permite ler arquivos gravados antes de alteracoes no codigo.
    private static final long serialVersionUID = 718776470454185083L;

    private String nomePet;
    private String tipoPet;

    public Pet(String nomePet, String tipoPet) {
        this.nomePet = nomePet;
        this.tipoPet = tipoPet;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getTipoPet() {
        return tipoPet;
    }

   public String toString() {
       return "- Nome do pet: " + nomePet + "; Tipo: " + tipoPet + ".";
    }
}
