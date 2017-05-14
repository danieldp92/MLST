package gestore;

import graph.Grafo;
import graph.Nodo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import stack.Stack;

/**
 * Questa classe permette di ricavare informazioni sulla strutta del Grafo
 *
 * @author Stefano Dalla Palma
 */
public class GestoreGrafo {

    Grafo grafo;

    public GestoreGrafo(Grafo grafo) {
        this.grafo = grafo;
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

        boolean[] visitati = inizializzaRicerca();
        return bsf(visitati, this.grafo.getNodo(0));    //Inizio la ricerca in ampiezza dal nodo avente chiave 0
        //return dsf(visitati, this.grafo.getNodo(0));    //Inizio la ricerca in ampiezza dal nodo avente chiave 0
    }

    private boolean[] inizializzaRicerca() {
        boolean[] visitati = new boolean[grafo.dimensione()];
        for (int i = 0; i < visitati.length; i++) {
            visitati[i] = false;
        }
        return visitati;
    }

    private boolean bsf(boolean[] pVisitati, Nodo pRadice) {
        boolean[] visitato = pVisitati;

        Queue<Nodo> coda = new LinkedList();
        coda.add(pRadice);

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                //Aggiungi in coda tutti i nodi adiacenti al nodo
                //for (Integer adiacente : getNodiAdiacenti(nodo)) {
                //    coda.add(this.grafo.getNodo(adiacente));
                //}
                for (Nodo adiacente : getNodiAdiacenti(nodo)) {
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

    private boolean dsf(boolean[] pVisitati, Nodo pRadice) {
        boolean[] visitato = pVisitati;

        Stack<Nodo> pila = new Stack();
        pila.push(pRadice);

        while (!pila.isEmpty()) {
            Nodo nodo = pila.pop();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;
                //Aggiungi in coda tutti i nodi adiacenti al nodo
                for (Nodo adiacente : getNodiAdiacenti(nodo)) {
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

    /**
     * Ricava le componenti connesse i cui archi hanno il colore specificato.
     *
     * @param colore che deve essere presente in tutti gli archi delle
     * componenti connesse
     * @return una lista di componenti connesse - sottografi.
     */
    public ArrayList<Grafo> getComponentiConnesse(Nodo pRadice, int colore) {
        return new ArrayList();
    }

    public ArrayList<Nodo> getNodiAdiacenti(Nodo pNodo) {
        return this.grafo.getNodo(pNodo.getChiave()).getAdiacenti();
    }

    /*
      private void setArchiDallaListaNodi() {
        this.grafo.getArchi().clear();
        boolean find = false;

        for (int i = 0; i < this.grafo.dimensione(); i++) {
            this.grafo.getArchi().addAll(this.grafo.getNodi().get(i).getIncidenti());
        }

        //Delete copies
        for (int i = 0; i < this.grafo.getArchi().size() - 1; i++) {
            int j = i + 1;
            while (!find && j < this.grafo.getArchi().size()) {
                if (this.grafo.getArchi().get(i).getDa().equals(this.grafo.getArchi().get(j).getDa())
                        && this.grafo.getArchi().get(i).getA().equals(this.grafo.getArchi().get(j).getA().getChiave())) {
                    this.grafo.getArchi().remove(j);
                    find = true;
                }
                j++;
            }

            find = false;
        }
    }

    
    //TODO refactoring --------------------------------------------------------
    

   

    public ArrayList<Nodo> getAdjVertexWithColor(Nodo pNodo, int colore) {
        ArrayList<Nodo> tmpVertexAdj = new ArrayList<>();

        for (Arco a : this.getArchi()) {
            if (a.getDa().getKey() == pNodo.getKey()) {
                if (a.getColori().contains(colore)) {
                    tmpVertexAdj.add(a.getA());
                }
            } else if (a.getA().getKey() == pNodo.getKey()) {
                if (a.getColori().contains(colore)) {
                    tmpVertexAdj.add(a.getDa());
                }
            }
        }
        return tmpVertexAdj;
    }

    public void deleteColor(int pColor) {
        int index = 0;

        for (Arco a : this.getArchi()) {
            a.getColori().remove(Integer.valueOf(pColor));
        }
    }

    

    public boolean checkCycle() {
        ArrayList<Grafo> listaSottoGrafi = this.getConnectedComponents();

        for (Grafo g : listaSottoGrafi) {
            if (g.getArchi().size() >= g.getNodi().size()) {
                return true;
            }
        }

        return false;
    }

    public void destroyCycle(Grafo previousGraph) {
        ArrayList<Arco> archiNuovi = new ArrayList<>();
        ArrayList<ArrayList<Arco>> splittedEdges = new ArrayList<>();
        boolean find = false;
        int split = 2;

        //Prendi gli archi che non sono presenti nel previous graph
        for (Arco a : this.getArchi()) {
            int i = 0;
            find = false;

            while (!find && i < previousGraph.getArchi().size()) {
                if (a.getDa().getKey() == previousGraph.getArchi().get(i).getDa().getKey()
                        && a.getA().getKey() == previousGraph.getArchi().get(i).getA().getKey()) {
                    find = true;
                }
                i++;
            }

            if (!find) {
                archiNuovi.add(a);
            }
        }

        while (archiNuovi.size() > 0) {
            //Splitta lista archi
            int splitSize = archiNuovi.size() / split;

            for (int i = 0; i < split; i++) {

            }
        }

    }

    public int minColorEdge() {
        int minTotColor = numColor + 1;

        int cont = 0;
        for (Arco a : this.getArchi()) {
            for (int i : a.getColori()) {
                cont += i;
            }

            if (cont < minTotColor) {
                minTotColor = cont;
            }

            cont = 0;
        }

        return minTotColor;
    }

    public void stampa() {
        System.out.println(nodi.size() + " " + archi.size());
        for (Arco arco : archi) {
            System.out.print(arco.da + " " + arco.a + " Colors:");

            for (int i = 0; i < arco.getColori().size(); i++) {
                System.out.print(" " + arco.getColori().get(i));
            }

            System.out.println();
        }
    }

    public void stampaMatriceColori() {
        System.out.print("\t\t Colori\n\t\t");

        for (int i = 0; i < this.archi.get(0).getColori().size(); i++) {
            System.out.print(" " + i);
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < this.archi.size(); i++) {
            System.out.print("Arco " + i + ": \t");

            for (int j = 0; j < this.archi.get(i).getColori().size(); j++) {
                System.out.print(" " + this.archi.get(i).getColori().get(j));
            }

            System.out.println();
        }
    }

    public void stampaMatriceColori(ArrayList<Integer> pColorForEdge) {
        System.out.print("\t\t Colori\t        Tot Colori\n\t\t");

        for (int i = 0; i < this.archi.get(0).getColori().size(); i++) {
            System.out.print(" " + i);
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < this.archi.size(); i++) {
            System.out.print("Arco " + i + ": \t");

            for (int j = 0; j < this.archi.get(i).getColori().size(); j++) {
                System.out.print(" " + this.archi.get(i).getColori().get(j));
            }

            System.out.print("\t" + pColorForEdge.get(i));
            System.out.println();
        }
    }

     */
    /**
     * Ricava le componenti connesse del grafo aventi colore c
     *
     * @return una lista di componenti connesse aventi colore c
     */
    /*  public ArrayList<Grafo> getConnectedComponents(int colore) {
        ArrayList<Grafo> listaSottoGrafi = new ArrayList<>();

        ArrayList<Integer> verticiDaEsaminare = new ArrayList<>();
        for (Nodo n : this.getNodi()) {
            verticiDaEsaminare.add(n.getKey());
        }

        while (verticiDaEsaminare.size() > 0) {
            Grafo sottoGrafo = this.getConnectedComponent(this.getNodo(verticiDaEsaminare.get(0)), colore);

            for (Nodo n : sottoGrafo.getNodi()) {
                verticiDaEsaminare.removeAll(Arrays.asList(n.getKey()));
            }

            listaSottoGrafi.add(sottoGrafo);
        }

        return listaSottoGrafi;
    }
     */
    /**
     * Trova la componente connessa i cui archi hanno il colore richiesto
     * partendo da un nodo
     *
     * @param pStart il nodo di partenza
     * @param colore il colore che deve trovarsi su tutti gli archi della
     * componente connessa
     * @return
     */
    /* private Grafo getConnectedComponent(Nodo pStart, int colore) {
        Grafo sottoGrafo = new Grafo(this.numColor);
        Stack s = new Stack();

        LinkedHashMap<Integer, Boolean> vertexVisited = new LinkedHashMap<>();

        for (int i = 0; i < this.getNodi().size(); i++) {
            vertexVisited.put(this.getNodi().get(i).getKey(), false);
        }

        s.push(pStart);

        while (!s.isEmpty()) {
            Nodo tmp = (Nodo) s.pop();

            vertexVisited.put(tmp.getKey(), true);
            ArrayList<Nodo> vertexAdj = getAdjVertexWithColor(tmp, colore);

            for (Nodo n : vertexAdj) {
                if (!vertexVisited.get(n.getKey()) && (s.isEmpty() || !s.isInStack(n))) {
                    s.push(n);
                }
            }
        }

        for (int i : vertexVisited.keySet()) {
            if (vertexVisited.get(i)) {
                //Aggiungi il nodo i al sottografo
                sottoGrafo.addNodo(this.getNodo(i));
                sottoGrafo.addArchi(getNodo(i).incidenti);
            }
        }

        sottoGrafo.setArchiDallaListaNodi();

        return sottoGrafo;
    }

    public void addColore(int colore) {
        this.colori.add(colore);
    }

    public HashSet getColori() {
        return (HashSet) this.colori;
    }

     */
}
