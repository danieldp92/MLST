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

    /**
     * Controllo della ciclicità, senza controllare le sottocomponenti
     * @return 
     */
    public boolean ciclo() {
        return connesso() && grafo.getArchi().size() != grafo.dimensione() - 1;
    }

     /**
     * Controllo della ciclicità, controllando che ogni sottocomponente del grafo non abbia una ciclicità
     * @return 
     */
    public boolean ciclo(Grafo pGrafo) {
        ArrayList<Grafo> listaSottografi = getComponentiConnesse();
        
        for (Grafo g : listaSottografi)
            if (g.getArchi().size() >= g.dimensione())
                return true;
        
        return false;
    }

    public void addArchiSenzaInserireCicli(ArrayList<Arco> pArchi) {
        this.grafo.addArchi(pArchi);

        if (ciclo(grafo)) {
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
    
    private ArrayList<Grafo> getComponentiConnesse() {
        ArrayList<Grafo> listaSottografi = new ArrayList<>();

        int indicePrimoNodoDelSottografo = -1;

        boolean[] nodiVisitati = new boolean[this.grafo.dimensione()];
        for (int i = 0; i < this.grafo.dimensione(); i++) {
            nodiVisitati[i] = false;
        }

        while ((indicePrimoNodoDelSottografo = getIndicePrimoFalse(nodiVisitati)) != -1) {
            boolean[] nodiSottografoVisitati = this.ricerca.bfsArray(this.grafo.getNodo(indicePrimoNodoDelSottografo));

            ArrayList<Nodo> listaNodiSottografo = new ArrayList<>();
            ArrayList<Arco> listaArchiSottografo = null;

            for (int i = 0; i < nodiSottografoVisitati.length; i++) {
                if (nodiSottografoVisitati[i]) {
                    //Aggiorna la lista dei nodi visitati
                    nodiVisitati[i] = true;
                    //Aggiungo il nodo alla lista dei nodi del sottografo
                    listaNodiSottografo.add(this.grafo.getNodo(i));
                }
            }
            listaArchiSottografo = this.grafo.getArchi(listaNodiSottografo);
            
            listaSottografi.add(new Grafo(listaNodiSottografo, listaArchiSottografo));
        }

        return listaSottografi;
    }

    private int getIndicePrimoFalse(boolean[] pLista) {
        for (int i = 0; i < pLista.length; i++) {
            if (!pLista[i]) {
                return i;
            }
        }

        return -1;
    }
}
