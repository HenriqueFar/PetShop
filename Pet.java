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

    public void imprimirInformacoes(int numero) {
        System.out.println("  " + numero + ". " + nomePet + " (" + tipoPet + ")");
    }

    @Override
    public String toString() {
        return "Pet {nome='" + nomePet + "', tipo='" + tipoPet + "'}";
    }
}