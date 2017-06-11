package gestore;

import grafo.Arco;
import grafo.Grafo;
import grafo.Nodo;
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
    
    public void aggiornaGrafo (Grafo grafo) {
        this.grafo = grafo;
        this.ricerca.aggiornaGrafo(grafo);
    }

    /**
     * Verifica se il grafo è connesso.
     *
     * @return true se il grafo è connesso; false altrimenti.
     */
    public boolean connesso() {
        /*if (this.grafo.getTotaleSottoComponenti() == 1)
            return true;
        
        return false;*/
        
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

        Nodo nodo1 = this.grafo.getNodo(pArco.getDa().getChiave());
        Nodo nodo2 = this.grafo.getNodo(pArco.getA().getChiave());
        componenteDiRiferimentoNodo1 = nodo1.getComponenteDiRiferimento();
        componenteDiRiferimentoNodo2 = nodo2.getComponenteDiRiferimento();

        //Se l'arco non genera cicli
        if (componenteDiRiferimentoNodo1 != componenteDiRiferimentoNodo2) {
            this.grafo.addArco(indiceArco, pArco);
            
            ArrayList<Integer> listaNodiComponente1 = this.grafo.getListaNodiConnessiAComponente().get(componenteDiRiferimentoNodo1);
            ArrayList<Integer> listaNodiComponente2 = this.grafo.getListaNodiConnessiAComponente().get(componenteDiRiferimentoNodo2);

            if (listaNodiComponente1.size() < listaNodiComponente2.size()) {
                for (int indiceNodo : listaNodiComponente1) {
                    this.grafo.getNodo(indiceNodo).setComponenteDiRiferimento(componenteDiRiferimentoNodo2);
                }
                
                listaNodiComponente2.addAll(listaNodiComponente1);
                
                this.grafo.getListaNodiConnessiAComponente().remove(componenteDiRiferimentoNodo1);
                this.grafo.getListaNodiConnessiAComponente().put(componenteDiRiferimentoNodo2, listaNodiComponente2);
            } else {
                for (int indiceNodo : listaNodiComponente2) {
                    this.grafo.getNodo(indiceNodo).setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                }
                
                listaNodiComponente1.addAll(listaNodiComponente2);
                
                this.grafo.getListaNodiConnessiAComponente().remove(componenteDiRiferimentoNodo2);
                this.grafo.getListaNodiConnessiAComponente().put(componenteDiRiferimentoNodo1, listaNodiComponente1);
            }
            
            
            
            /*for (Nodo n : this.grafo.getNodi()) {
                if (n.getComponenteDiRiferimento() == componenteDiRiferimentoNodo2) {
                    n.setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                }
            }*/
            
            return true;
        }

        return false;
    }
}
