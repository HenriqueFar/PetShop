import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Tutor {
   private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

   public int getCod() {
      return cod;
   }

   public String getNomeTutor() {
      return nomeTutor;
   }

   public String getEndereco() {
      return endereco;
   }

   public LocalDate getDataNasc() {
      return dataNasc;
   }

   public ArrayList<Pet> getPets() {
      return pets;
   }
   
   //faz validacao para garantir que nao terao mais de um pet com o mesmo nome para o mesmo tutor 
   public boolean incluiPet(String nomePet, String tipoPet) {
      for (Pet p : pets) {
         if (p.getNomePet().equalsIgnoreCase(nomePet)) {
            return false;
         }
      }
      pets.add(new Pet(nomePet, tipoPet));
      return true;
   }

   public void imprimirInformacoes() {
      System.out.println("Tutor #" + cod);
      System.out.println("Nome: " + nomeTutor);
      System.out.println("Endereco: " + endereco);
      System.out.println("Data de nascimento: " + dataNasc.format(FORMATADOR_DATA));
   
      if (pets.isEmpty()) {
         System.out.println("Pets: sem pets cadastrados");
         return;
      }
   
      System.out.println("Pets:");
      for (int i = 0; i < pets.size(); i++) {
         pets.get(i).imprimirInformacoes(i + 1);
      }
   }

   @Override
   public String toString() {
      return "Tutor {cod=" + cod + ", nome='" + nomeTutor + "', endereco='" + endereco
             + "', dataNasc=" + dataNasc + ", pets=" + pets + "}";
   }
}
