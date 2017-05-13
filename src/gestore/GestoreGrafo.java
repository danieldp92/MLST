package gestore;

import graph.Grafo;
import graph.Nodo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
        boolean[] visitati = inizializzaRicerca();

        return false;
    }

    private boolean[] inizializzaRicerca() {
        boolean[] visitati = new boolean[grafo.dimensione()];
        for (int i = 0; i < visitati.length; i++) {
            visitati[i] = false;
        }
        return visitati;
    }

    private boolean dfs() {

        if (this.grafo.dimensione() == 0) {
            return false;
        }

        if (this.grafo.getArchi().size() < (this.grafo.dimensione() - 1)) {
            return false;
        }

        if (this.grafo.getArchi().size() == ((this.grafo.dimensione() * (this.grafo.dimensione() - 1)) / 2)) {
            return true;
        }

        // Grafo nodiVIsitati = getConnectedComponent(this.getNodi().get(0));
        //  if (nodiVIsitati.getNodi().size() != this.getNodi().size()) {
        //      return false;
        //  }
          return true;
    }

    /**
     * Ricava le componenti connesse del grafo.
     *
     * @return una lista di componenti connesse - sottografi.
     */
    public ArrayList<Grafo> getComponentiConnesse() {
        ArrayList<Grafo> sottografi = new ArrayList<>();
        ArrayList<Integer> verticiDaEsaminare = new ArrayList<>();

        for (Nodo n : this.grafo.getNodi()) {
            verticiDaEsaminare.add(n.getChiave());
        }

        /*while (verticiDaEsaminare.size() > 0) {
            Grafo sottografo = this.getComponenteConnessa(this.grafo.getNodo(verticiDaEsaminare.get(0)));

            for (Nodo n : sottografo.getNodi()) {
                verticiDaEsaminare.removeAll(Arrays.asList(n.getChiave()));
            }

            sottografi.add(sottografo);
        }*/

        return sottografi;
    }

   /* private Grafo getComponenteConnessa(Nodo pInizio) {
        Grafo sottografo = new Grafo(this.grafo.numeroColori());
        Stack s = new Stack();

        LinkedHashMap<Integer, Boolean> vertexVisited = new LinkedHashMap<>();

        for (int i = 0; i < this.getNodi().size(); i++) {
            vertexVisited.put(this.getNodi().get(i).getKey(), false);
        }

        s.push(pInizio);

        while (!s.isEmpty()) {
            Nodo tmp = (Nodo) s.pop();

            vertexVisited.put(tmp.getKey(), true);
            ArrayList<Nodo> vertexAdj = getAdjVertex(tmp);

            for (Nodo n : vertexAdj) {
                if (!vertexVisited.get(n.getKey()) && (s.isEmpty() || !s.isInStack(n))) {
                    s.push(n);
                }
            }
        }

        for (int i : vertexVisited.keySet()) {
            if (vertexVisited.get(i)) {
                //Aggiungi il nodo i al sottografo
                sottografo.addNodo(this.getNodo(i));
            }
        }

        sottografo.setArchiDallaListaNodi();

        return sottografo;
    }

    /**
     * Ricava le componenti connesse i cui archi hanno il colore specificato.
     *
     * @param colore che deve essere presente in tutti gli archi delle
     * componenti connesse
     * @return una lista di componenti connesse - sottografi.
     */
    public ArrayList<Grafo> getComponentiConnesse(int colore) {
        return new ArrayList();
    }

    /*
     
    //TODO refactoring --------------------------------------------------------
    private void setArchiDallaListaNodi() {
        this.getArchi().clear();
        boolean find = false;

        for (int i = 0; i < this.getNodi().size(); i++) {
            this.getArchi().addAll(this.getNodi().get(i).getIncidenti());
        }

        //Delete copies
        for (int i = 0; i < this.getArchi().size() - 1; i++) {
            int j = i + 1;
            while (!find && j < this.getArchi().size()) {
                if (this.getArchi().get(i).getDa().getKey()
                        == this.getArchi().get(j).getDa().getKey()
                        && this.getArchi().get(i).getA().getKey()
                        == this.getArchi().get(j).getA().getKey()) {
                    this.getArchi().remove(j);
                    find = true;
                }
                j++;
            }

            find = false;
        }
    }

    public ArrayList<Nodo> getAdjVertex(Nodo pNodo) {
        ArrayList<Nodo> tmpVertexAdj = new ArrayList<>();

        for (Arco a : this.getArchi()) {
            if (a.getDa().getKey() == pNodo.getKey()) {
                tmpVertexAdj.add(a.getA());
            } else if (a.getA().getKey() == pNodo.getKey()) {
                tmpVertexAdj.add(a.getDa());
            }
        }
        return tmpVertexAdj;
    }

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
