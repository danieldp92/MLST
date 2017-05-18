package greedy;

import gestore.GestoreGrafo;
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
public class GreedyBFS {

    Grafo grafo;
    ArrayList<Integer> colori;

    public GreedyBFS(Grafo grafo) {
        this.grafo = grafo;
    }

    public Grafo esegui() {
        Grafo mlst = new Grafo(this.grafo.getCopiaNodi());
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        boolean[] processato = new boolean[mlst.dimensione()];
        int numeroArchi = 0;
        Queue<Nodo> coda = new LinkedList();

        coda.add(mlst.getNodo(0));      //Aggiungo il primo nodo 

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!processato[nodo.getChiave()]) {
                processato[nodo.getChiave()] = true;
                for (Nodo adiacente : nodo.getAdiacenti()) {
                    coda.add(adiacente);
                }

                if (numeroArchi == 0) {
                    //è il primo nodo. Scegli il primo arco

                    if (nodo.getIncidenti().isEmpty()) {
                        processato[nodo.getChiave()] = false;
                    } else {
                        mlst.addArco(nodo.getIncidenti().get(0));
                    }
                    numeroArchi++;
                    continue;
                }

                //Scelgi l'arco migliore
                ArrayList<Arco> archiColorati = new ArrayList<>();
                int grandezza = 0;
                for (int colore : mlst.getColori()) {           //Prendo gli archi che hanno il colore più ricorrente
                    ArrayList<Arco> tmp = nodo.getIncidenti(colore);
                    if (tmp.size() > grandezza) {
                        grandezza = tmp.size();
                    }
                    archiColorati = tmp;
                }
                if (archiColorati.isEmpty()) {
                    archiColorati = nodo.getIncidenti();

                    if (nodo.getIncidenti().isEmpty()) {
                        processato[nodo.getChiave()] = false;
                    } 
                }

                int i = 0;
                while (i < archiColorati.size()) {
                    Arco arco = archiColorati.get(i++);

                    //Prendi il primo arco della lista ed esci
                    if (!arco.getA().equals(nodo) && !processato[arco.getA().getChiave()]) {
                        mlst.addArco(arco);
                        break;
                    } else if (!arco.getDa().equals(nodo) && !processato[arco.getDa().getChiave()]) {
                        mlst.addArco(arco);
                        break;
                    }
                }
            }

        }

        if (gestoreMlst.connesso()) {
            System.out.println("Connesso");
        }
        return mlst;
    }
}
