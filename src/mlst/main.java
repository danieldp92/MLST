package mlst;

import Pilot.TestPilot;
import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import greedy.Greedy;
import greedy.Statistiche;
import ilog.concert.IloException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class main {

    public static void main(String[] args) throws IOException, IloException {
        //TestGreedy.test();
        //TestCPLEX.test();
        TestPilot.test();

        /*GrafoColorato grafo = new GeneratoreGrafo().generaGrafo(new File("src/GrafiColorati3Colori/1000_8000_1000_125_1.mlst"));
        Greedy greedy = new Greedy(grafo);
        GrafoColorato mlst = greedy.esegui();
        GestoreGrafo gestore = new GestoreGrafo(mlst);
        System.out.println("Numero colori: " + mlst.getListaColori().size());
        System.out.println("Ciclo: " + gestore.ciclo());
        System.out.println("Connesso: " + gestore.connesso());*/
    }
}
