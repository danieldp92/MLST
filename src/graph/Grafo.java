package graph;

import gestore.Ricerca;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import stack.Stack;

public class Grafo {

    ArrayList<Nodo> nodi;
    ArrayList<Arco> archi;
    Set<Integer> colori;

    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.colori = new HashSet<>();
    }

    public Grafo(ArrayList<Nodo> pNodi) {
        this.nodi = pNodi;
        this.archi = new ArrayList<>();
        this.colori = new HashSet<>();
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi, Set<Integer> pColori) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = pColori;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public ArrayList<Arco> getArchi() {
        return archi;
    }

    public Arco getArco(Nodo pNodoDa, Nodo pNodoA) {
        Arco arco = null;

        boolean trovato = false;
        int i = 0;

        while (!trovato && i < this.getArchi().size()) {
            if ((this.getArchi().get(i).getDa().equals(pNodoDa) && this.getArchi().get(i).getA().equals(pNodoA))
                    || (this.getArchi().get(i).getA().equals(pNodoDa) && this.getArchi().get(i).getDa().equals(pNodoA))) {

                arco = this.getArchi().get(i);
                trovato = true;
            }
            i++;
        }

        return arco;
    }

    public Nodo getNodo(Nodo pNodo) {
        return this.nodi.get(this.nodi.indexOf(pNodo));
    }

    public Nodo getNodo(int pChiave) {
        Nodo nodo = null;

        /*int indice = this.nodi.indexOf(new Nodo(pChiave));

        if (indice != -1) {
            return this.nodi.get(indice);
        }

        return null;*/
        //Se gli indici dei nodi corrispondono alle loro key, evitiamo di ciclare inutilmente
        if (this.getNodi().get(pChiave).getChiave() == pChiave) {
            nodo = this.getNodi().get(pChiave);
        } else {
            boolean find = false;
            int i = 0;

            while (!find && i < this.getNodi().size()) {
                if (this.getNodi().get(i).getChiave() == pChiave) {
                    nodo = this.getNodi().get(i);
                    find = true;
                }
                i++;
            }
        }

        return nodo;
    }

    public void addNodi(ArrayList<Nodo> pNodi) {
        this.getNodi().addAll(pNodi);
    }

    public void addNodo(Nodo pNodo) {
        boolean exist = false;

        for (Nodo n : this.getNodi()) {
            if (n.getChiave() == pNodo.getChiave()) {
                exist = true;
            }
        }

        if (!exist) {
            this.getNodi().add(pNodo);
        }
    }

    public void addArchi(ArrayList<Arco> pEdges) {
        for (Arco a : pEdges) {
            addArco(a);
        }
    }

    public void addArco(Arco pArco) {

        if (!this.archi.contains(pArco)) {
            this.archi.add(pArco);

            for (Nodo n : this.getNodi()) {
                if (n.equals(pArco.getDa()) || n.equals(pArco.getA())) {
                    n.addArcoIncidente(pArco);
                }
            }

            // Aggiungo i colori
            pArco.colori.forEach((colore) -> {
                colori.add(colore);
            });
        }

    }

    public void addArchiSenzaInserireCicli(ArrayList<Arco> pEdges) {
        this.addArchi(pEdges);

        if (this.checkCycle()) {
            for (Arco a : pEdges) {
                this.rimuoviArco(a);
            }

            if (pEdges.size() > 1) {
                ArrayList<Arco> firstHalfEdgeList = new ArrayList<>();
                ArrayList<Arco> lastHalfEdgeList = new ArrayList<>();

                for (int i = 0; i < (pEdges.size() / 2); i++) {
                    firstHalfEdgeList.add(pEdges.get(i));
                }
                for (int i = (pEdges.size() / 2); i < pEdges.size(); i++) {
                    lastHalfEdgeList.add(pEdges.get(i));
                }

                this.addArchiSenzaInserireCicli(firstHalfEdgeList);
                this.addArchiSenzaInserireCicli(lastHalfEdgeList);
            }
        }
    }

    public void rimuoviNodo(Nodo pNodo) {
        Nodo vertexToRemove = this.getNodi().get(pNodo.getChiave());
        ArrayList<Arco> edgeToRemove = new ArrayList<>();
        edgeToRemove.addAll(vertexToRemove.getIncidenti());

        System.out.println("Tot Archi: " + edgeToRemove.size());
        for (Arco a : edgeToRemove) {
            this.getArchi().remove(a);
        }

        this.getNodi().remove(vertexToRemove);
    }

    public void rimuoviArco(Arco pArco) {
        rimuoviArco(pArco.getDa(), pArco.getA());
    }

    private void rimuoviArco(Nodo pNodoDa, Nodo pNodoA) {
        Arco arcoDaRimuovere = this.getArco(pNodoDa, pNodoA);

        if (arcoDaRimuovere != null) {
            Nodo nodo1 = this.nodi.get(this.nodi.indexOf(pNodoDa));
            Nodo nodo2 = this.nodi.get(this.nodi.indexOf(pNodoA));

            nodo1.rimuoviArcoIncidente(arcoDaRimuovere);
            nodo2.rimuoviArcoIncidente(arcoDaRimuovere);

            this.archi.remove(arcoDaRimuovere);
        }
    }

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
                if (this.getArchi().get(i).getDa().getChiave()
                        == this.getArchi().get(j).getDa().getChiave()
                        && this.getArchi().get(i).getA().getChiave()
                        == this.getArchi().get(j).getA().getChiave()) {
                    this.getArchi().remove(j);
                    find = true;
                }
                j++;
            }

            find = false;
        }
    }

    public Set<Integer> getColori() {
        return this.colori;
    }
    
    public void rimuoviColore (int pColore) {
        this.colori.remove(pColore);
        
        for (Arco arco : this.archi)
            arco.rimuoviColore(pColore);
    }
    
    public void rimuoviColori(HashSet pColori) {
        this.colori.removeAll(pColori);
    }
    
    /**
     * Restituisce l dimensione del grafo intesa come numero di nodi.
     *
     * @return la dimensione del grafo
     */
    public int dimensione() {
        return this.nodi.size();
    }

    public int numeroColori() {
        return this.colori.size();
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

    /*
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

        ArrayList<Grafo> listaSottoGrafi = this.getConnectedComponents();
        double random = Math.random();
        
        for (Grafo g : listaSottoGrafi) {
            //Se il sottografo ha un ciclo, ne avvio la rimozione
            if (g.getArchi().size() >= g.getNodi().size()) {
                
            }
        }
    }*/

    private ArrayList<Grafo> getConnectedComponents() {
        ArrayList<Grafo> listaSottoGrafi = new ArrayList<>();

        ArrayList<Integer> verticiDaEsaminare = new ArrayList<>();
        for (Nodo n : this.getNodi()) {
            verticiDaEsaminare.add(n.getChiave());
        }

        while (verticiDaEsaminare.size() > 0) {
            Grafo sottoGrafo = this.getConnectedComponent(this.getNodo(verticiDaEsaminare.get(0)));

            for (Nodo n : sottoGrafo.getNodi()) {
                verticiDaEsaminare.removeAll(Arrays.asList(n.getKey()));
            }

            listaSottoGrafi.add(sottoGrafo);
        }

        return listaSottoGrafi;
    }

    private Grafo getConnectedComponent(Nodo pStart) {
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
                sottoGrafo.addNodo(this.getNodo(i));
            }
        }

        sottoGrafo.setArchiDallaListaNodi();

        return sottoGrafo;
    }

}
