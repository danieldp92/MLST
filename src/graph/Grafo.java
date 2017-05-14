package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grafo {

    ArrayList<Nodo> nodi;
    ArrayList<Arco> archi;
    Set<Integer> colori;
    int numeroColori;

    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.colori = new HashSet<>();
        this.numeroColori = 0;
    }

    public Grafo(int pNumeroColori) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.colori = new HashSet<>(pNumeroColori);
        this.numeroColori = pNumeroColori;
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi, Set<Integer> pColori) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = pColori;
        this.numeroColori = pColori.size();
    }

    public ArrayList<Arco> getArchi() {
        return archi;
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
        this.archi.addAll(pArchi); //Da modificare per evitare di inserire archi uguali
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
        if (this.archi.contains(pArco)) {
            rimuoviArco(pArco.getDa(), pArco.getA());
        }
    }

    private void rimuoviArco(Nodo pNodoDa, Nodo pNodoA) {
        Arco arcoDaRimuovere = this.getArco(pNodoDa, pNodoA);

        Nodo nodo1 = this.nodi.get(this.nodi.indexOf(pNodoDa));
        Nodo nodo2 = this.nodi.get(this.nodi.indexOf(pNodoA));

        nodo1.rimuoviArcoIncidente(arcoDaRimuovere);
        nodo2.rimuoviArcoIncidente(arcoDaRimuovere);

        this.archi.remove(arcoDaRimuovere);
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

    public Set<Integer> getColori(){
        return this.colori;
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
}
