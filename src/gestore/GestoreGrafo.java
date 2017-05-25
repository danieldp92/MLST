package gestore;

import grafo.Arco;
import grafo.Colore;
import grafo.GrafoColorato;
import grafo.Nodo;
import java.util.ArrayList;

/**
 * Questa classe permette di ricavare informazioni sulla struttura del GrafoColorato
 *
 * @author Stefano Dalla Palma
 */
public class GestoreGrafo {

    GrafoColorato grafo;
    Ricerca ricerca;

    public GestoreGrafo(GrafoColorato grafo) {
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
     *
     * @return true se è presente un ciclo; false altrimenti
     */
    public boolean ciclo() {
        return connesso() && grafo.getArchi().size() != grafo.dimensione() - 1;
    }

    public void addArcoSenzaInserireCicli(Arco pArco) {
        int componenteDiRiferimentoNodo1 = this.grafo.getNodo(pArco.getDa().getChiave()).getComponenteDiRiferimento();
        int componenteDiRiferimentoNodo2 = this.grafo.getNodo(pArco.getA().getChiave()).getComponenteDiRiferimento();

        //Se l'arco non genera cicli
        if (componenteDiRiferimentoNodo1 != componenteDiRiferimentoNodo2) {
            this.grafo.addArco(pArco);

            for (Nodo n : this.grafo.getNodi()) {
                if (n.getComponenteDiRiferimento() == componenteDiRiferimentoNodo2) {
                    n.setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                }
            }
        }
    }

    /**
     * Controllo della ciclicità, controllando che ogni sottocomponente del
     * grafo non abbia una ciclicità
     *
     * @param grafo il grafo da analizzare
     * @return true se è presente un ciclo; false altrimenti
     */
    public boolean ciclo(GrafoColorato grafo) {
        ArrayList<GrafoColorato> listaSottografi = getComponentiConnesse();
        for (GrafoColorato g : listaSottografi) {
            if (g.getArchi().size() >= g.dimensione()) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<GrafoColorato> getComponentiConnesse() {
        ArrayList<GrafoColorato> listaSottografi = new ArrayList<>();

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

            listaSottografi.add(new GrafoColorato(listaNodiSottografo, listaArchiSottografo));
        }

        return listaSottografi;
    }

    public ArrayList<GrafoColorato> getComponentiConnesse(Colore pColore) {
        ArrayList<GrafoColorato> listaSottografi = new ArrayList<>();

        int indicePrimoNodoDelSottografo = -1;

        boolean[] nodiVisitati = new boolean[this.grafo.dimensione()];

        while ((indicePrimoNodoDelSottografo = getIndicePrimoFalse(nodiVisitati)) != -1) {
            boolean[] nodiSottografoVisitati = this.ricerca.bfsArray(this.grafo.getNodo(indicePrimoNodoDelSottografo), pColore);

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

            listaSottografi.add(new GrafoColorato(listaNodiSottografo, listaArchiSottografo));
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
