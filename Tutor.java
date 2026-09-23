import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.Period;

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
   
   public int getIdade()
   {
      return Period.between(dataNasc, LocalDate.now()).getYears();
   }
   
   public String getDataNascFormatada(){
      return dataNasc.format(FORMATADOR_DATA);
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
   
   //logica para excluir pet em Tutor
   public boolean excluiPet(String nomePet)
   {
      for (int i = 0; i < pets.size(); i++) {
         if (pets.get(i).getNomePet().equalsIgnoreCase(nomePet)) {
            pets.remove(i);
            return true;
         }
      }
      return false;
   }
   
   public int numPets()
   {
      return pets.size();
   }

   public String toString() {
    //ts -> texto
    String ts = "Cod. do Tutor.....: " + cod + "\n";
    ts += "Nome..............: " + nomeTutor + "\n";
    ts += "Data de nascimento: " + dataNasc.format(FORMATADOR_DATA) + " (" + getIdade() + " anos)\n";
    ts += "Endereco..........: " + endereco + "\n";

    if (pets.isEmpty()) {
        ts += "Pets: sem pets cadastrados";
        return ts;
    }

    ts += "Relacao de Pets...:\n";
    for (Pet p : pets) {
        ts += p.toString() + "\n";
    }

    return ts;
   }
}
