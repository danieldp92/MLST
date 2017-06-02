package random;

import grafo.Arco;
import grafo.Grafo;
import grafo.Nodo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Stefano Dalla Palma
 */
public class RandomDFS {

    Grafo grafo;
    ArrayList<Arco> archi;

    public RandomDFS(Grafo grafo) {
        this.grafo = grafo;
        archi = new ArrayList();
    }

    public Grafo esegui(int chiaveNodoPartenza) {

        Grafo mlst = new Grafo(grafo.getCopiaNodi());
        boolean ok = dfs(mlst.getNodi().get(chiaveNodoPartenza));

        if (ok) {
            mlst.addArchi(archi);
        } else {
            return null;
        }

        return mlst;
    }

    public void aggiornaComponentiDiRiferimento(int vecchioValore, int nuovoValore) {
        //Per ogni arco presente nella lista archi, prendo tutti i nodi e setto la componente a valore
        for (Arco arco : this.archi) {
            if (arco.getDa().getComponenteDiRiferimento() == vecchioValore) {
                arco.getDa().setComponenteDiRiferimento(nuovoValore);
            }

            if (arco.getA().getComponenteDiRiferimento() == vecchioValore) {
                arco.getA().setComponenteDiRiferimento(nuovoValore);
            }
        }
    }

    public boolean dfs(Nodo pRadice) {
        boolean[] visitato = new boolean[this.grafo.dimensione()];

        Stack<Nodo> pila = new Stack();
        pila.push(pRadice);

        while (!pila.isEmpty()) {
            Nodo nodo = pila.pop();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;

                LinkedList<Nodo> adiacenti = new LinkedList((Collection) nodo.getAdiacenti().clone());
                //SOSTITUIRE CON GET INDICI ARCHI INCIDENTI E PRENDERE GLI ARCHI CORRISPONDENTI DAL GRAFO
                //LinkedList<Arco> incidenti = new LinkedList((Collection) nodo.getIncidenti().clone());
                LinkedList<Integer> indiciIncidenti = new LinkedList((Collection) nodo.getIndiciArchiIncidenti().clone());
                LinkedList<Arco> incidenti = new LinkedList();

                for (Integer indice : indiciIncidenti) {
                    incidenti.add(grafo.getArco(indice));
                }

                //Aggiungi in coda tutti i nodi adiacenti al nodo
                for (Nodo adiacente : adiacenti) {
                    pila.push(adiacente);
                }

                //Scegli un arco a caso. Se non è possibile scegliere un arco vai avanti
                Random generator = new Random(1);   //TODO togliere il seme
                boolean scelto = false;

                while (!scelto && !adiacenti.isEmpty() && !incidenti.isEmpty()) {
                    int indiceArco = generator.nextInt(adiacenti.size());
                    Arco arco = incidenti.get(indiceArco);
                    Nodo adiacente;
                    if (arco.getDa().equals(nodo)) {
                        adiacente = arco.getA();
                    } else {
                        adiacente = arco.getDa();
                    }

                    if (arco.getDa().getComponenteDiRiferimento() == arco.getA().getComponenteDiRiferimento()) {
                        //adiacenti.remove(adiacenti.indexOf(adiacente));
                        adiacenti.remove(indiceArco);
                        incidenti.remove(indiceArco);

                    } else if (nodo.getComponenteDiRiferimento() > adiacente.getComponenteDiRiferimento()) {
                        int vecchioValore = nodo.getComponenteDiRiferimento();

                        arco.getDa().setComponenteDiRiferimento(adiacente.getComponenteDiRiferimento());
                        arco.getA().setComponenteDiRiferimento(adiacente.getComponenteDiRiferimento());
                        archi.add(arco);
                        if (nodo.getChiave() != nodo.getComponenteDiRiferimento()) {
                            aggiornaComponentiDiRiferimento(vecchioValore, adiacente.getComponenteDiRiferimento());
                        }
                        scelto = true;
                    } else if (nodo.getComponenteDiRiferimento() < adiacente.getComponenteDiRiferimento()) {

                        int vecchioValore = adiacente.getComponenteDiRiferimento();
                        arco.getDa().setComponenteDiRiferimento(nodo.getComponenteDiRiferimento());
                        arco.getA().setComponenteDiRiferimento(nodo.getComponenteDiRiferimento());
                        archi.add(arco);

                        if (adiacente.getChiave() != adiacente.getComponenteDiRiferimento()) {
                            aggiornaComponentiDiRiferimento(vecchioValore, nodo.getComponenteDiRiferimento());
                        }
                        scelto = true;
                    }
                }
            }
        }
        int i = 0;
        while (i < visitato.length) {
            if (!visitato[i++]) {
                return false;
            }
        }

        return true;
    }
}
