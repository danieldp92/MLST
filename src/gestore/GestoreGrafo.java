package gestore;

import grafo.Arco;
import grafo.Grafo;
import grafo.Nodo;

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

    public boolean addArcoSenzaInserireCicli(int indiceArco, Arco pArco) {
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;

        componenteDiRiferimentoNodo1 = this.grafo.getNodo(pArco.getDa().getChiave()).getComponenteDiRiferimento();
        componenteDiRiferimentoNodo2 = this.grafo.getNodo(pArco.getA().getChiave()).getComponenteDiRiferimento();

        //Se l'arco non genera cicli
        if (componenteDiRiferimentoNodo1 != componenteDiRiferimentoNodo2) {
           
            this.grafo.addArco(indiceArco, pArco);

            for (Nodo n : this.grafo.getNodi()) { 
                if (n.getComponenteDiRiferimento() == componenteDiRiferimentoNodo2) {
                    n.setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                }
            }                    
            return true;
        }

        return false;
    }
    
    
    /**
     * Controllo della ciclicità, controllando che ogni sottocomponente del grafo non abbia una ciclicità
     * @return 
     */
    /*AGGIORNAMENTO HASHMAP
    public boolean ciclo(Grafo pGrafo) {
        ArrayList<Grafo> listaSottografi = getComponentiConnesse();
        for (Grafo g : listaSottografi)
            if (g.getArchi().size() >= g.dimensione())
                return true;
        
        return false;
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
    }*/

    private int getIndicePrimoFalse(boolean[] pLista) {
        for (int i = 0; i < pLista.length; i++) {
            if (!pLista[i]) {
                return i;
            }
        }

        return -1;
    }
}
