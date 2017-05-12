package graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import stack.Stack;

public class Grafo {

    ArrayList<Nodo> nodi;
    ArrayList<Arco> archi;
    public int numColor;

    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = 0;
    }

    public Grafo(int pNumColor) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = pNumColor;
    }

    //Crea grafo non orientato, con colori
    public Grafo(int pNumNodi, int pNumArchi, int pNumColor, int pSeme) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = pNumColor;

        for (int i = 0; i < pNumNodi; i++) {
            Nodo nd = new Nodo(i);
            this.getNodi().add(nd);
        }

        Random r = new Random(pSeme);
        HashSet<String> archi = new HashSet<>(pNumArchi * 2);
        if (pNumArchi > (pNumNodi * (pNumNodi - 1) / 2)) {
            pNumArchi = (pNumNodi * (pNumNodi - 1) / 2);
        }

        while (pNumArchi > 0) {
            int da = r.nextInt(pNumNodi - 1);
            int a = da + 1;
            if (a < pNumNodi - 1) {
                a += r.nextInt((pNumNodi - 1) - (da));
            }
            String ck = da + "-" + a;
            if (!archi.contains(ck)) {
                pNumArchi--;
                archi.add(ck);
                Arco ar = new Arco(this.getArchi().size(), this.getNodi().get(da), this.getNodi().get(a), pNumColor);
                this.getNodi().get(da).getIncidenti().add(ar);
                this.getNodi().get(a).getIncidenti().add(ar);
            }
        }

        this.setArchiDallaListaNodi();
    }

    //Crea grafo non orientato, senza colori
    public Grafo(int pNumNodi, int pNumArchi, int pSeme) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = 0;

        for (int i = 0; i < pNumNodi; i++) {
            Nodo nd = new Nodo(i);
            this.getNodi().add(nd);
        }

        Random r = new Random(pSeme);
        HashSet<String> archi = new HashSet<>(pNumArchi * 2);
        if (pNumArchi > (pNumNodi * (pNumNodi - 1) / 2)) {
            pNumArchi = (pNumNodi * (pNumNodi - 1) / 2);
        }

        while (pNumArchi > 0) {
            int da = r.nextInt(pNumNodi - 1);
            int a = da + 1;
            if (a < pNumNodi - 1) {
                a += r.nextInt((pNumNodi - 1) - (da));
            }
            String ck = da + "-" + a;
            if (!archi.contains(ck)) {
                pNumArchi--;
                archi.add(ck);
                Arco ar = new Arco(this.getArchi().size(), this.getNodi().get(da), this.getNodi().get(a));
                this.getNodi().get(da).getIncidenti().add(ar);
                this.getNodi().get(a).getIncidenti().add(ar);
                this.getArchi().add(ar);
            }
        }
    }

    //Grafo senza archi
    public Grafo(int numNodi, int pNumColor) {
        this.nodi = new ArrayList<>();
        for (int i = 0; i < numNodi; i++) {
            this.nodi.add(new Nodo(i));
        }

        this.archi = new ArrayList<>();
        this.numColor = pNumColor;
    }

    public Grafo(String path) throws FileNotFoundException, IOException {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = 0;

        Nodo primoNodo = null;
        Nodo secondoNodo = null;
        ArrayList<Integer> listaColori = new ArrayList<>();
        int numNodi = 0;

        BufferedReader br = new BufferedReader(new FileReader(path));

        ArrayList<String> file = new ArrayList<>();
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                file.add(line);
            }
        } finally {
            br.close();
        }

        for (int i = 0; i < file.size(); i++) {
            String[] split = file.get(i).split(" ");

            if (i == 0) {
                //Lista nodi
                numNodi = Integer.parseInt(split[0]);
                for (int j = 0; j < numNodi; j++) {
                    this.nodi.add(new Nodo(j));
                }

                numColor = Integer.parseInt(split[2]) + 1;
                
            } else {
                primoNodo = new Nodo(Integer.parseInt(split[0]));
                secondoNodo = new Nodo(Integer.parseInt(split[1]));

                for (int j = 2; j < split.length; j++)
                    listaColori.add(Integer.parseInt(split[j]));

                this.archi.add(new Arco(primoNodo, secondoNodo, new ArrayList<>(listaColori)));

                listaColori.clear();
            }
        }
    }

    public Nodo getNodo(int pKey) {
        Nodo nodo = null;

        //Se gli indici dei nodi corrispondono alle loro key, evitiamo di ciclare inutilmente
        if (this.getNodi().get(pKey).getKey() == pKey) {
            nodo = this.getNodi().get(pKey);
        } else {
            boolean find = false;
            int i = 0;

            while (!find && i < this.getNodi().size()) {
                if (this.getNodi().get(i).getKey() == pKey) {
                    nodo = this.getNodi().get(i);
                    find = true;
                }
                i++;
            }
        }

        return nodo;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public void addNodi(ArrayList<Nodo> pNodi) {
        this.getNodi().addAll(pNodi);
    }

    public void addNodo(Nodo pNodo) {
        boolean exist = false;

        for (Nodo n : this.getNodi()) {
            if (n.getKey() == pNodo.getKey()) {
                exist = true;
            }
        }

        if (!exist) {
            this.getNodi().add(pNodo);
        }
    }

    public void rimuoviNodo(Nodo pNodo) {
        Nodo vertexToRemove = this.getNodi().get(pNodo.getKey());
        ArrayList<Arco> edgeToRemove = new ArrayList<>();
        edgeToRemove.addAll(vertexToRemove.getIncidenti());

        System.out.println("Tot Archi: " + edgeToRemove.size());
        for (Arco a : edgeToRemove) {
            this.getArchi().remove(a);
        }

        this.getNodi().remove(vertexToRemove);
    }

    public Arco getArco(int keyNodo1, int keyNodo2) {
        Arco tmp = null;

        boolean find = false;
        int i = 0;

        while (!find && i < this.getArchi().size()) {
            if ((this.getArchi().get(i).getDa().getKey() == keyNodo1
                    && this.getArchi().get(i).getA().getKey() == keyNodo2)
                    || (this.getArchi().get(i).getA().getKey() == keyNodo1
                    && this.getArchi().get(i).getDa().getKey() == keyNodo2)) {
                tmp = this.getArchi().get(i);
                find = true;
            }
            i++;
        }

        return tmp;
    }

    public ArrayList<Arco> getArchi() {
        return archi;
    }

    public void addArchi(ArrayList<Arco> pEdges) {
        for (Arco a : pEdges)
            addArco(a);
    }

    public void addArco(Arco pEdge) {
        this.archi.add(pEdge);

        for (Nodo n : this.getNodi()) {
            if (n.getKey() == pEdge.getDa().getKey()
                    || n.getKey() == pEdge.getA().getKey()) {
                n.getIncidenti().add(pEdge);
            }
        }

    }

    public void rimuoviArco(Arco pEdge) {
        rimuoviArco(pEdge.getDa().getKey(), pEdge.getA().getKey());
    }

    public void rimuoviArco(int keyNodo1, int keyNodo2) {
        Arco arcoDaRimuovere = this.getArco(keyNodo1, keyNodo2);

        Nodo nodo1 = this.getNodo(keyNodo1);
        Nodo nodo2 = this.getNodo(keyNodo2);
        

        nodo1.getIncidenti().remove(arcoDaRimuovere);
        nodo2.getIncidenti().remove(arcoDaRimuovere);

        this.getArchi().remove(arcoDaRimuovere);
        
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

    public void deleteColor(int pColor) {
        int index = 0;
        
        for (Arco a : this.getArchi())
            a.getColors().remove(Integer.valueOf(pColor));
    }

    public boolean dfs() {

        if (this.getNodi().size() == 0) {
            return false;
        }
        
        if (this.getArchi().size() < (this.getNodi().size() - 1))
            return false;
        
        if (this.getArchi().size() == ((this.getNodi().size() * (this.getNodi().size() - 1)) / 2))
            return true;
        

        Grafo vertexVisited = getConnectedComponent(this.getNodi().get(0));

        if (vertexVisited.getNodi().size() != this.getNodi().size()) {
            return false;
        }

        return true;

    }

    public boolean checkCycle() {
        ArrayList<Grafo> listaSottoGrafi = this.getConnectedComponents();
        
        for (Grafo g : listaSottoGrafi)
            if (g.getArchi().size() >= g.getNodi().size())
                return true;
        
        return false;
    }

    public void destroyCycle (Grafo previousGraph) {
        ArrayList<Arco> archiNuovi = new ArrayList<>();
        ArrayList<ArrayList<Arco>> splittedEdges = new ArrayList<>();
        boolean find = false;
        int split = 2;
        
        //Prendi gli archi che non sono presenti nel previous graph
        for (Arco a : this.getArchi()) {
            int i = 0;
            find = false;
            
            while (!find && i < previousGraph.getArchi().size()) {
                if (a.getDa().getKey() == previousGraph.getArchi().get(i).getDa().getKey() &&
                        a.getA().getKey() == previousGraph.getArchi().get(i).getA().getKey())
                    find = true;
                i++;
            }
            
            if (!find)
                archiNuovi.add(a);
        }
        
        
        while (archiNuovi.size() > 0) {
            //Splitta lista archi
            int splitSize = archiNuovi.size() / split;
            
            for (int i = 0; i < split; i++) {
                
            }
        }
        
        
        /*ArrayList<Grafo> listaSottoGrafi = this.getConnectedComponents();
        double random = Math.random();
        
        for (Grafo g : listaSottoGrafi) {
            //Se il sottografo ha un ciclo, ne avvio la rimozione
            if (g.getArchi().size() >= g.getNodi().size()) {
                
            }
        }*/
    }
    
    public int minColorEdge() {
        int minTotColor = numColor + 1;

        int cont = 0;
        for (Arco a : this.getArchi()) {
            for (int i : a.getColors()) {
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

            for (int i = 0; i < arco.getColors().size(); i++) {
                System.out.print(" " + arco.getColors().get(i));
            }

            System.out.println();
        }
    }

    public void stampaMatriceColori() {
        System.out.print("\t\t Colori\n\t\t");

        for (int i = 0; i < this.archi.get(0).getColors().size(); i++) {
            System.out.print(" " + i);
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < this.archi.size(); i++) {
            System.out.print("Arco " + i + ": \t");

            for (int j = 0; j < this.archi.get(i).getColors().size(); j++) {
                System.out.print(" " + this.archi.get(i).getColors().get(j));
            }

            System.out.println();
        }
    }

    public void stampaMatriceColori(ArrayList<Integer> pColorForEdge) {
        System.out.print("\t\t Colori\t        Tot Colori\n\t\t");

        for (int i = 0; i < this.archi.get(0).getColors().size(); i++) {
            System.out.print(" " + i);
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < this.archi.size(); i++) {
            System.out.print("Arco " + i + ": \t");

            for (int j = 0; j < this.archi.get(i).getColors().size(); j++) {
                System.out.print(" " + this.archi.get(i).getColors().get(j));
            }

            System.out.print("\t" + pColorForEdge.get(i));
            System.out.println();
        }
    }

    private ArrayList<Grafo> getConnectedComponents () {
        ArrayList<Grafo> listaSottoGrafi = new ArrayList<>();
        
        ArrayList<Integer> verticiDaEsaminare = new ArrayList<>();
        for (Nodo n : this.getNodi())
            verticiDaEsaminare.add(n.getKey());
        
        while (verticiDaEsaminare.size() > 0) {
            Grafo sottoGrafo = this.getConnectedComponent(this.getNodo(verticiDaEsaminare.get(0)));
            
            for (Nodo n : sottoGrafo.getNodi())
                verticiDaEsaminare.removeAll(Arrays.asList(n.getKey()));
            
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
