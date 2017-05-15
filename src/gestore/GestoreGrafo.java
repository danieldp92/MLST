package gestore;

import graph.Arco;
import graph.Grafo;
import graph.Nodo;
import java.util.ArrayList;

/**
 * Questa classe permette di ricavare informazioni sulla struttura del Grafo
 *
 * @author Stefano Dalla Palma
 */
public class GestoreGrafo {

    Grafo grafo;
    Ricerca ricerca;

    public GestoreGrafo(Grafo grafo) {
        this.grafo = grafo;
        this.ricerca = new Ricerca(this.grafo);
    }

    /**
     * Verifica se il grafo è connesso.
     *
     * @return true se il grafo è connesso; false altrimenti.
     */
    public boolean connesso() {
        if (this.grafo.dimensione() == 0) {
            return false;
        }

        if (this.grafo.getArchi().size() < (this.grafo.dimensione() - 1)) {
            return false;
        }

        if (this.grafo.getArchi().size() == ((this.grafo.dimensione() * (this.grafo.dimensione() - 1)) / 2)) {
            return true;
        }

        return this.ricerca.bfs(this.grafo.getNodo(0));      //Inizio la ricerca in ampiezza dal nodo avente chiave 0
        //return new Ricerca(grafo).dfs(this.grafo.getNodo(0));    //Inizio la ricerca in profondità dal nodo avente chiave 0
    }

    public boolean ciclo() {
        return connesso() && grafo.getArchi().size() != grafo.dimensione() - 1;
    }

    public boolean ciclo(Grafo sottoGrafo) {
        GestoreGrafo gestoreSottoGrafo = new GestoreGrafo(sottoGrafo);
        return gestoreSottoGrafo.connesso() && sottoGrafo.getArchi().size() != sottoGrafo.dimensione() - 1;
    }

    public ArrayList<Grafo> getComponentiConnesse() {
        return null;
    }

    public void addArchiSenzaInserireCicli(ArrayList<Arco> pArchi) {
        this.grafo.addArchi(pArchi);

        if (ciclo()) {
            this.grafo.rimuoviArchi(pArchi);

            if (pArchi.size() > 1) {
                ArrayList<Arco> primaMetaListaArchi = new ArrayList<>();
                ArrayList<Arco> secondaMetaListaArchi = new ArrayList<>();

                for (int i = 0; i < (pArchi.size() / 2); i++) {
                    primaMetaListaArchi.add(pArchi.get(i));
                }
                for (int i = (pArchi.size() / 2); i < pArchi.size(); i++) {
                    secondaMetaListaArchi.add(pArchi.get(i));
                }

                this.addArchiSenzaInserireCicli(primaMetaListaArchi);
                this.addArchiSenzaInserireCicli(secondaMetaListaArchi);
            }
        }
    }
}
