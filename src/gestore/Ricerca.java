package gestore;

import grafo.Grafo;
import grafo.Nodo;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Questa classe permette di effettuare ricerche sul grafo <<<<<<< HEAD
 * @a
 *
 * uthor Stefano Dalla Palma
 */
public class Ricerca {

    private Grafo grafo;

    public Ricerca(Grafo pGrafo) {
        this.grafo = pGrafo;
    }
    
    public void aggiornaGrafo (Grafo grafo) {
        this.grafo = grafo;
    }

    private boolean[] inizializzaRicerca() {
        boolean[] visitati = new boolean[grafo.dimensione()];
        for (int i = 0; i < visitati.length; i++) {
            visitati[i] = false;
        }
        return visitati;
    }

    public boolean bfs(Nodo pRadice) {
        
        boolean[] visitato = bfsArray(pRadice);
        
        int i = 0;
        while (i < visitato.length) {
            if (!visitato[i++]) {
                return false;
            }
        }

        return true;
    }

    public boolean[] bfsArray(Nodo pRadice) {

        boolean[] visitato = inizializzaRicerca();

        Queue<Nodo> coda = new LinkedList();
        coda.add(pRadice);

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                for (Nodo adiacente : nodo.getAdiacenti()) {
                    if (!visitato[adiacente.getChiave()])
                        coda.add(adiacente);
                }
            }
        }

        return visitato;
    }

    public boolean dfs(Nodo pRadice) {
        boolean[] visitato = dfsArray(pRadice);

        //Controllo se tutti i nodi sono stati visitati
        int i = 0;
        while (i < visitato.length) {
            if (!visitato[i++]) {
                return false;
            }
        }
        return true;
    }

    public boolean[] dfsArray(Nodo pRadice) {
        boolean[] visitato = inizializzaRicerca();

        Stack<Nodo> pila = new Stack();
        pila.push(pRadice);

        while (!pila.isEmpty()) {
            Nodo nodo = pila.pop();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                //Aggiungi in coda tutti i nodi adiacenti al nodo
                for (Nodo adiacente : nodo.getAdiacenti()) {
                    pila.push(adiacente);
                }
            }
        }
        return visitato;
    }
}
