package mlst;

import AlgoritmoGenetico.Algoritmo;
import AlgoritmoGenetico.Popolazione;
import gestore.GestoreGrafo;
import gestore.XlsGrafo;
import grafo.GrafoColorato;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class main {

    public static void main(String[] args) throws IOException, IloException {
        //TestGreedy.test();
        //TestCPLEX.test();

        Algoritmo algoritmo = new Algoritmo();
        Popolazione popolazione = algoritmo.execute();
        
    }
}
