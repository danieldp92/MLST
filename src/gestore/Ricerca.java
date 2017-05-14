package gestore;

import graph.Grafo;
import graph.Nodo;
import java.util.LinkedList;
import java.util.Queue;
import stack.Stack;

/**
 * Questa classe permette di effettuare ricerche sul grafo
 * @author Stefano Dalla Palma
 */
public class Ricerca {
    
    private Grafo grafo;
    private GestoreGrafo gestoreGrafo; 
    
    public Ricerca(Grafo pGrafo) {
        this.grafo = pGrafo;
        this.gestoreGrafo = new GestoreGrafo(grafo);
    }
    
        
    private boolean[] inizializzaRicerca() {
        boolean[] visitati = new boolean[grafo.dimensione()];
        for (int i = 0; i < visitati.length; i++) {
            visitati[i] = false;
        }
        return visitati;
    }

    public boolean bsf(Nodo pRadice) {

        boolean[] visitato = inizializzaRicerca();

        Queue<Nodo> coda = new LinkedList();
        coda.add(pRadice);

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                for (Nodo adiacente : gestoreGrafo.getNodiAdiacenti(nodo)) {
                    coda.add(adiacente);
                }
            }
        }

        //Controllo se tutti i nodi sono stati visitati
        int i = 0;
        while (i < visitato.length && visitato[i]) {
            i++;
        }

        return (i == visitato.length);
    }

    public boolean dsf(Nodo pRadice) {
        boolean[] visitato = inizializzaRicerca();

        Stack<Nodo> pila = new Stack();
        pila.push(pRadice);

        while (!pila.isEmpty()) {
            Nodo nodo = pila.pop();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                //Aggiungi in coda tutti i nodi adiacenti al nodo
                for (Nodo adiacente : gestoreGrafo.getNodiAdiacenti(nodo)) {
                    pila.push(adiacente);
                }
            }
        }

        //Controllo se tutti i nodi sono stati visitati
        int i = 0;
        while (i < visitato.length && visitato[i]) {
            i++;
        }

        return (i == visitato.length);
    }

    
    
}
