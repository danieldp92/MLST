package mlst;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
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
        //long inizio = System.currentTimeMillis();
        System.out.print("Caricamento grafo... ");
        Grafo grafo = GeneratoreGrafo.generaGrafoConRicorrenzaColori(new File("src/GrafiColorati3Colori/1000_8000_1000_125_1.mlst"));
        for (int i : grafo.ricorrenzaColori)
            System.out.println(i);
        //System.out.format("fatto (%d ms)\n", System.currentTimeMillis() - inizio);
        
        //Ottengo un MLT eseguendo l'algoritmo greedy sul grafo
        Grafo mlst = new Greedy(grafo).esegui();
        GestoreGrafo gG = new GestoreGrafo(mlst);
        System.out.println("Colori usati: " + mlst.getColori().size());
        System.out.println("MLST numero nodi: " + mlst.dimensione());
        System.out.println("MLST numero archi: " + mlst.getArchi().size());
        System.out.println("Connesso: " + gG.connesso());
        boolean f = gG.ciclo(mlst);
        System.out.println("Ciclo: " + f);

        //Stampo i colori usati
        for (int colore : mlst.getColori()) {
            System.out.println(colore);
        }
        //System.out.format("Tempo di esecuzione: (%d ms)\n", System.currentTimeMillis() - inizio);
    }

}
