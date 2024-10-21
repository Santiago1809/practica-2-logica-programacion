import java.util.ArrayList;
import java.util.Random;

public class Arreglo {
    public static void llenarVector(int n, int[] vec){
        Random rand = new Random();
        ArrayList<Integer> uniqueNumbers = new ArrayList<>();

        while (uniqueNumbers.size() < n) {
            int num = rand.nextInt(100); // Números aleatorios entre 0 y 99
            if (!uniqueNumbers.contains(num)) { // Verificar si ya existe
                uniqueNumbers.add(num); // Solo agregar si es único
            }
        }

        // Llenar el array pasado como argumento
        for (int i = 0; i < n; i++) {
            vec[i] = uniqueNumbers.get(i);
        }
    }
    //------------------------------------------------
    public static String mostrar(int n, int vec[]){
        String salida="";
        for (int i=0; i<n;i++){
            if (i%10==0)
                salida+="\n";
            salida+=vec[i]+"\t";
            
        }
        return salida;
    }
    //------------------------------------------------
    public static int buscarSecuencial(int n, int[] vec){
        int pos = -1;
        for (int num: vec) {
            if (num == n){
                pos = num;
                break;
            }
        }
        return pos;
    }
}
