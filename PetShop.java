import java.util.ArrayList;
import java.util.Scanner;

public class PetShop {
    private ArrayList<Tutor> tutores;
    private Scanner teclado;

    public PetShop() {
        this.tutores = new ArrayList<>();
        this.teclado = new Scanner(System.in);
    }
}