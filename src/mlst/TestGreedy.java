package mlst;

import gestore.GeneratoreGrafo;
import graph.Grafo;
import greedy.Greedy;
import java.io.File;

/**
 * Test greedy Krumke
 *
 * @author Stefano Dalla Palma
 */
public class TestGreedy {

    
    public static void main() {
       //Carico il grafo
        long inizio = System.currentTimeMillis();
        System.out.print("Caricamento grafo... ");
        Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/100_1000_100_10_1.mlst"));
        System.out.format("fatto (%d ms)\n", System.currentTimeMillis() - inizio);
        
        //Ottengo un MLT eseguendo l'algoritmo greedy sul grafo
        Grafo mlst = new Greedy(grafo).esegui();
        System.out.println("Colori usati: " + mlst.getColori().size());
        System.out.println("MLST numero nodi: " + mlst.dimensione());
        System.out.println("MLST numero archi: " + mlst.getArchi().size());

        //Stampo i colori usati
        for (int colore : mlst.getColori()) {
            System.out.println(colore);
        }
    }

}
