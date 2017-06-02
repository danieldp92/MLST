package gestore;

import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import grafo.Nodo;
import java.util.Iterator;

/**
 * Offre metodi per la stampa di informazioni relative al grafo
 *
 * @author Stefano Dalla Palma
 */
public class InfoGrafo {

    Grafo grafo;

    public InfoGrafo(Grafo grafo) {
        this.grafo = grafo;
    }

    public void stampaNodi() {
        System.out.println("___________Nodi___________");
        for (Nodo nodo : grafo.getNodi()) {
            System.out.format("Chiave: %d\n", nodo.getChiave());
        }
        System.out.println();
    }

    public void stampaArchi() {
        System.out.println("___________Archi___________");

        for (Arco arco : grafo.getArchi()) {
            System.out.format("Arco: (%d, %d) - colori: ", arco.getDa().getChiave(), arco.getA().getChiave());
            for (int colore : arco.getColori()) {
                System.out.format("%d ", colore);
            }
            System.out.println();

        }
        System.out.println();
    }

    public void stampaColori() {
        System.out.println("___________Colori___________");

        for (Iterator<Colore> iterator = grafo.getColori().iterator(); iterator.hasNext();) {
            Colore colore = iterator.next();
            System.out.format("Colore: %d\n", colore.getColore());
        }

        System.out.println();
    }

    public static void stampaArco(Arco arco) {
        System.out.format("Arco: (%d, %d) - colori: ", arco.getDa().getChiave(), arco.getA().getChiave());
        for (int colore : arco.getColori()) {
            System.out.format("%d ", colore);
        }
        System.out.println();
    }
}
