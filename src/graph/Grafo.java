package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    
    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = new HashSet<>();
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi, Set<Integer> pColori) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = pColori;
    }

    public ArrayList<Arco> getArchi() {
        return archi;
    }
    
    /**
     * Questo metodo ritorna la lista degli archi correlati ad una lista di nodi data
     * @param pListaNodi la lista dei nodi data in input
     * @return la lista degli archi correlati
     */
    public ArrayList<Arco> getArchi(ArrayList<Nodo> pListaNodi) {
        ArrayList<Arco> listaArchi = new ArrayList<>();
        boolean trovato = false;

        for (int i = 0; i < pListaNodi.size(); i++) {
            listaArchi.addAll(pListaNodi.get(i).getIncidenti());
        }

        //Delete copies
        for (int i = 0; i < listaArchi.size() - 1; i++) {
            int j = i + 1;
            while (!trovato && j < listaArchi.size()) {
                if (listaArchi.get(i).getDa().getChiave()
                        == listaArchi.get(j).getDa().getChiave()
                        && listaArchi.get(i).getA().getChiave()
                        == listaArchi.get(j).getA().getChiave()) {
                    listaArchi.remove(j);
                    trovato = true;
                }
                j++;
            }

            trovato = false;
        }
        
        return listaArchi;
    }
   
    public ArrayList<Arco> getCopiaArchi() {
        ArrayList<Arco> copiaArchi = new ArrayList();
        for(Arco originale : this.archi){
            Arco copia = new Arco(originale.getDa(), originale.getA(), originale.getColori());
            copiaArchi.add(copia);
        }
        return copiaArchi;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
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
        int indice = this.nodi.indexOf(new Nodo(pChiave));

        if (indice != -1) {
            return this.nodi.get(indice);
        }

        return null;
    }

    public void addArchi(ArrayList<Arco> pArchi) {

        for (Arco arco : pArchi) {
            addArco(arco);
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

    public void addNodi(ArrayList<Nodo> pNodi) {
        this.nodi.addAll(pNodi); //Da modificare per evitare di inserire nodi uguali
    }

    public void addNodo(Nodo pNodo) {
        if (!this.nodi.contains(pNodo)) {
            this.nodi.add(pNodo);
        }
    }

    public void rimuoviArco(Arco pArco) {
        rimuoviArco(pArco.getDa(), pArco.getA());
    }

    public void rimuoviArco(Nodo pNodoDa, Nodo pNodoA) {
        Arco arcoDaRimuovere = this.getArco(pNodoDa, pNodoA);

        if (arcoDaRimuovere != null) {
            Nodo nodo1 = this.nodi.get(this.nodi.indexOf(pNodoDa));
            Nodo nodo2 = this.nodi.get(this.nodi.indexOf(pNodoA));

            nodo1.rimuoviArcoIncidente(arcoDaRimuovere);
            nodo2.rimuoviArcoIncidente(arcoDaRimuovere);
            nodo1.rimuoviNodoAdiacente(nodo2);
            nodo2.rimuoviNodoAdiacente(nodo1);

            this.archi.remove(arcoDaRimuovere);
        }
    }

    public void rimuoviArchi(ArrayList<Arco> pArchi) {
        this.archi.removeAll(pArchi);
    }

    public void rimuoviNodo(Nodo pNodo) {

        if (this.nodi.contains(pNodo)) {
            Nodo nodoDaRimuovere = this.nodi.get(this.nodi.indexOf(pNodo));
            ArrayList<Arco> archiDaRimuovere = new ArrayList<>();
            archiDaRimuovere.addAll(nodoDaRimuovere.getIncidenti());

            System.out.println("Tot Archi: " + archiDaRimuovere.size()); //TODO da eliminare

            for (Arco arco : archiDaRimuovere) {
                this.archi.remove(arco);
            }

            this.nodi.remove(nodoDaRimuovere);
        }
    }

    public Set<Integer> getColori() {
        return this.colori;
    }

    public void rimuoviColore(int pColore) {
        this.colori.remove(pColore);
        for (Arco arco : this.archi) {
            arco.rimuoviColore(pColore);
        }
    }

    public void rimuoviColori(HashSet pColori) {
        this.colori.removeAll(pColori);
    }

    /**
     * Restituisce la dimensione del grafo intesa come numero di nodi.
     *
     * @return la dimensione del grafo
     */
    public int dimensione() {
        return this.nodi.size();
    }

    public int numeroColori() {
        return this.colori.size();
    }  
}
