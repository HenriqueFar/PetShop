public class Pet {
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
