package grafo;

import java.util.ArrayList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Grafo {

    ArrayList<Nodo> nodi;
    ArrayList<Arco> archi;

    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
    }

    public Grafo(ArrayList<Nodo> pNodi) {
        this.nodi = pNodi;
        this.archi = new ArrayList<>();
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi) {
        this.nodi = pNodi;
        this.archi = pArchi;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public ArrayList<Nodo> getCopiaNodi() {
        ArrayList<Nodo> copiaNodi = new ArrayList();
        this.nodi.forEach((nodo) -> {
            Nodo nodoCopia = new Nodo(nodo.getChiave());

            // Aggiungo tutti i nodi adiacenti
            nodo.getAdiacenti().forEach((adiacente) -> {
                nodoCopia.addNodoAdiacente(adiacente);
            });

            // Aggiungo tutti gli archi incidenti nel nodo
            nodo.getIncidenti().forEach((incidente) -> {
                nodoCopia.addArcoIncidente(incidente);
            });

            copiaNodi.add(nodoCopia);
        });
        return copiaNodi;
    }

    /**
     * Questo metodo permette di cercare un nodo all'interno del grafo con una
     * determinata chiave.
     *
     * @param chiave la chiave del nodo da cercare.
     * @return il nodo con chiave <i>chiave</i>
     */
    public Nodo getNodo(int chiave) {
        Nodo nodo = null;

        try {
            nodo = this.nodi.get(chiave);
        } catch (IndexOutOfBoundsException ex) {
            nodo = null;
        }

        return nodo;
    }

    public void addNodi(ArrayList<Nodo> nodi) {
        for (Nodo nodo : nodi) {
            addNodo(nodo);
        }
    }

    public void addNodo(Nodo nodo) {
        if (!this.nodi.contains(nodo)) {
            this.nodi.add(nodo);
        }
    }

    public ArrayList<Arco> getArchi() {
        return archi;
    }

    /**
     * Questo metodo ritorna la lista degli archi correlati ad una lista di nodi
     * data.
     *
     * @param listaNodi la lista dei nodi data in input
     * @return la lista degli archi correlati
     */
    public ArrayList<Arco> getArchi(ArrayList<Nodo> listaNodi) {
        ArrayList<Arco> listaArchi = new ArrayList<>();
        boolean trovato = false;

        for (int i = 0; i < listaNodi.size(); i++) {
            listaArchi.addAll(listaNodi.get(i).getIncidenti());
        }

        //Cancella le copie
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
        this.archi.stream().map((originale) -> new Arco(originale.getDa(), originale.getA())).forEachOrdered((copia) -> {
            copiaArchi.add(copia);
        });
        return copiaArchi;
    }

    public Arco getArco(Nodo da, Nodo a) {
        Arco arco = new Arco(da, a);

        int i = this.archi.indexOf(arco);
        if (i != -1) {
            return this.archi.get(i);
        }
        /*
        boolean trovato = false;
        while (!trovato && i < this.getArchi().size()) {
            
             if ((this.getArchi().get(i).getDa().equals(da) && this.getArchi().get(i).getA().equals(a))
            || (this.getArchi().get(i).getA().equals(da) && this.getArchi().get(i).getDa().equals(a))) {
            
            arco = this.getArchi().get(i);
            trovato = true;
            }
            i++;
        }*/
        return null;
    }

    public void addArchi(ArrayList<Arco> archi) {
        for (Arco arco : archi) {
            addArco(arco);
        }
    }

    public void addArco(Arco arco) {
        this.archi.add(arco);
        getNodo(arco.getDa().getChiave()).addNodoAdiacente(getNodo(arco.getA().getChiave()));
        getNodo(arco.getDa().getChiave()).addArcoIncidente(arco);
        getNodo(arco.getA().getChiave()).addNodoAdiacente(getNodo(arco.getDa().getChiave()));
        getNodo(arco.getA().getChiave()).addArcoIncidente(arco);
    }

    /**
     * Restituisce la dimensione del grafo intesa come numero di nodi.
     *
     * @return la dimensione del grafo
     */
    public int dimensione() {
        return nodi.size();
    }

    /**
     * Rimuove uno specifico arco
     *
     * @param arco - l'arco da rimuovere
     */
    public void rimuoviArco(Arco arco) {
        rimuoviArco(arco.getDa(), arco.getA());
    }

    /**
     * Rimuove uno specifico arco dati i due nodi che lo compongono
     *
     * @param nodoDa il primo nodo dell'arco.
     * @param nodoA il secondo nodo dell'arco.
     */
    public void rimuoviArco(Nodo nodoDa, Nodo nodoA) {
        Arco arcoDaRimuovere = this.getArco(nodoDa, nodoA);

        if (arcoDaRimuovere != null) {
            Nodo nodo1 = this.nodi.get(this.nodi.indexOf(nodoDa));
            Nodo nodo2 = this.nodi.get(this.nodi.indexOf(nodoA));

            nodo1.rimuoviArcoIncidente(arcoDaRimuovere);
            nodo2.rimuoviArcoIncidente(arcoDaRimuovere);
            nodo1.rimuoviNodoAdiacente(nodo2);
            nodo2.rimuoviNodoAdiacente(nodo1);

            this.archi.remove(arcoDaRimuovere);
        }
    }

    public void rimuoviArchi(ArrayList<Arco> arco) {
        arco.forEach((arcoDaRimuovere) -> {
            rimuoviArco(arcoDaRimuovere);
        });
    }

    public void rimuoviNodo(Nodo nodo) {

        if (this.nodi.contains(nodo)) {
            Nodo nodoDaRimuovere = this.nodi.get(this.nodi.indexOf(nodo));
            ArrayList<Arco> archiDaRimuovere = new ArrayList<>();
            archiDaRimuovere.addAll(nodoDaRimuovere.getIncidenti());
            archiDaRimuovere.forEach((arco) -> {
                this.archi.remove(arco);
            });

            this.nodi.remove(nodoDaRimuovere);
        }
    }

    /**
     * Questa funzione va ad eliminare tutti gli archi e tutti i suoi
     * riferimenti
     */
    public void clear() {
        this.archi.clear();
        for (Nodo nodo : this.nodi) {
            nodo.getIncidenti().clear();
            nodo.getAdiacenti().clear();
        }
    }
}
