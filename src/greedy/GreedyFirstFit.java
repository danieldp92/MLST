package greedy;

import graph.Arco;
import graph.Grafo;
import graph.Nodo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Stefano Dalla Palma
 */
public class GreedyFirstFit {

    Grafo grafo;

    public GreedyFirstFit(Grafo grafo) {
        this.grafo = grafo;
    }

    public Grafo esegui() {
        Grafo mlst = new Grafo(this.grafo.getCopiaNodi());

        boolean[] processato = new boolean[mlst.dimensione()];
        Queue<Nodo> coda = new LinkedList();

        coda.add(mlst.getNodo(0));      //Aggiungo il primo nodo 

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!processato[nodo.getChiave()]) {
                processato[nodo.getChiave()] = true;
                for (Nodo adiacente : nodo.getAdiacenti()) {
                    coda.add(adiacente);
                }

                int i = 0;
                ArrayList<Arco> incidenti = nodo.getIncidenti();
                while (i < incidenti.size()) {
                    Arco arco = incidenti.get(i++);

                    if (!mlst.getArchi().contains(arco)) {
                        //Prendi il primo arco della lista ed esci
                        if (!arco.getA().equals(nodo) && !processato[arco.getA().getChiave()]) {
                            mlst.addArco(arco);
                            break;
                        } else if (!arco.getDa().equals(nodo) && !processato[arco.getDa().getChiave()]) {
                            mlst.addArco(arco);
                            break;
                        }

                        //Se i == incidenti - 1 e non ho ancora inserito, aggiungi per forza
                        if (i == incidenti.size() - 1 && !mlst.getArchi().contains(arco)) {
                            mlst.addArco(arco);
                        }

                    }
                }
            }
        }
        return mlst;
    }
}
