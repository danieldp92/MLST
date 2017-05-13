package mlst;

import gestore.GeneratoreGrafo;
import graph.Grafo;
import greedy.GreedyKrumke;
import java.io.File;

/**
 * Test greedy Krumke
 *
 * @author Stefano Dalla Palma
 */
public class TestGreedy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/7_11_4.mlst"));
        GreedyKrumke greedy = new GreedyKrumke(grafo);
        //greedy.esegui();

    }

}
