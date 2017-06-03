package gestore;

import grafo.Arco;
import grafo.GrafoColorato;
import grafo.Nodo;

/**
 * Offre metodi per la stampa di informazioni relative al grafo
 *
 * @author Stefano Dalla Palma
 */
public class InfoGrafo {

    GrafoColorato grafo;

    public InfoGrafo(GrafoColorato grafo) {
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

        for (Arco arco : grafo.getArchi().values()) {
            System.out.format("Arco: (%d, %d) - colori: ", arco.getDa().getChiave(), arco.getA().getChiave());
            for (int colore : arco.getColori()) {
                System.out.format("%d ", colore);
            }
            System.out.println();

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

    public void stampaColori() {
        System.out.println("Numero colori: " + grafo.getListaColori().size());
        System.out.println("___________Colori___________");
        for (Integer colore : grafo.getListaColori()) {
            System.out.print(colore + " ");
        }
    }

}
