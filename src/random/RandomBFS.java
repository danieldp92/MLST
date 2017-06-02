package random;

import grafo.Arco;
import grafo.GrafoColorato;
import grafo.Nodo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author Stefano Dalla Palma
 */
public class RandomBFS {

    GrafoColorato grafo;
    ArrayList<Arco> archi;

    public RandomBFS(GrafoColorato grafo) {
        this.grafo = grafo;
        archi = new ArrayList();
    }

    public GrafoColorato esegui(int chiaveNodoPartenza) {

        GrafoColorato mlst = new GrafoColorato(grafo.getCopiaNodi());
        boolean ok = bfs(mlst.getNodi().get(chiaveNodoPartenza));

        if (ok) {
            mlst.addArchi(archi);
        } else {
            return null;
        }

        return mlst;
    }

    private void aggiornaComponentiDiRiferimento(int vecchioValore, int nuovoValore) {
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

    private boolean bfs(Nodo pRadice) {

        boolean[] visitato = new boolean[this.grafo.dimensione()];

        Queue<Nodo> coda = new LinkedList();
        coda.add(pRadice);

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                LinkedList<Nodo> adiacenti = new LinkedList((Collection) nodo.getAdiacenti().clone());
                LinkedList<Arco> incidenti = new LinkedList((Collection) nodo.getIncidenti().clone());

                for (Nodo adiacente : adiacenti) {
                    coda.add(adiacente);
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
