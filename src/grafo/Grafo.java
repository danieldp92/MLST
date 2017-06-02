package grafo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Grafo {

    ArrayList<Nodo> nodi;
    ArrayList<Arco> archi;
    ArrayList<Colore> colori;
    public ArrayList<Integer> listaColoriOrdinataPerRicorrenzaMaggiore;

    //Costruttori
    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.colori = new ArrayList<>();
    }

    public Grafo(ArrayList<Nodo> pNodi) {
        this.nodi = pNodi;
        this.archi = new ArrayList<>();
        this.colori = new ArrayList<>();
    }

    /**
     * Crea un grafo, partendo da una lista di nodi e da un numero prefissato di
     * colori, a cui, inizialmente, non verrà associato nessun arco
     *
     * @param pNodi, lista di nodi
     * @param pNumColori, lista di colori
     */
    public Grafo(ArrayList<Nodo> pNodi, int pNumColori) {
        this.nodi = pNodi;
        this.archi = new ArrayList<>();

        this.colori = new ArrayList<>();
        for (int i = 0; i < pNumColori; i++) {
            this.colori.add(new Colore(i));
        }
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = new ArrayList<>();
        for (Arco arco : archi) {
            for (int colore : arco.getColori()) {
                Colore c = new Colore(colore);
                if (!colori.contains(c)) {
                    colori.add(c);
                }
            }
        }
    }

    public Grafo(ArrayList<Nodo> pNodi, ArrayList<Arco> pArchi, ArrayList<Colore> pColori) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = pColori;
    }

    //Get
    public ArrayList<Arco> getArchi() {
        return archi;
    }

    /**
     * Questo metodo ritorna la lista degli archi correlati ad una lista di nodi
     * data
     *
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
        for (Arco originale : this.archi) {
            Arco copia = new Arco(originale.getDa(), originale.getA(), originale.getColori());
            copiaArchi.add(copia);
        }
        return copiaArchi;
    }

    public ArrayList<Nodo> getNodi() {
        return nodi;
    }

    public ArrayList<Nodo> getCopiaNodi() {
        ArrayList<Nodo> copiaNodi = new ArrayList();
        for (Nodo nodo : this.nodi) {
            copiaNodi.add(new Nodo(nodo.getChiave()));
        }
        return copiaNodi;
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

        try {
            nodo = this.nodi.get(pChiave);
        } catch (IndexOutOfBoundsException ex) {
            nodo = null;
        }

        return nodo;
    }

    /**
     * Questo metodo restituisce i colori del grafo, indipendentemente dal fatto
     * che essi siano stati utilizzati o meno
     *
     * @return
     */
    public ArrayList<Colore> getColori() {
        return colori;
    }

    public void addColore(Colore pColore) {
        this.colori.add(pColore);
    }

    /**
     * Questo metodo restituisce la lista dei colori utilizzati nel grafo
     *
     * @return la lista dei colori utilizzati
     */
    public ArrayList<Integer> getListaColori() {
        ArrayList<Integer> listaColori = new ArrayList<>();

        for (Colore c : this.colori) {
            if (c.isUsed()) {
                listaColori.add(c.getColore());
            }
        }

        return listaColori;
    }

    public ArrayList<Integer> getListaColoriOrdinataPerRicorrenza() {
        if (this.listaColoriOrdinataPerRicorrenzaMaggiore == null) {
            setListaColoriOrdinataPerRicorrenza();
        }

        return listaColoriOrdinataPerRicorrenzaMaggiore;
    }

    //Add
    public void addArchi(ArrayList<Arco> pArchi) {

        for (Arco arco : pArchi) {
            addArco(arco);
        }
    }

    public void addArco(Arco pArco) {

        this.archi.add(pArco);

        getNodo(pArco.getDa().getChiave()).addNodoAdiacente(getNodo(pArco.getA().getChiave()));
        getNodo(pArco.getDa().getChiave()).addArcoIncidente(pArco);
        getNodo(pArco.getA().getChiave()).addNodoAdiacente(getNodo(pArco.getDa().getChiave()));
        getNodo(pArco.getA().getChiave()).addArcoIncidente(pArco);

        //PROVA STEFANO
        /*for (int colore : pArco.getColori()) {
            Colore c = new Colore(colore);
            if (!colori.contains(c)) {
                colori.add(c);
            }
        }*/
        ////FINE PROVA
        /*for (int colore : pArco.getColori()) {
            this.colori.get(colore).addIndiceArcoCollegato(this.archi.size() - 1);
        }*/
    }

    public void addNodi(ArrayList<Nodo> pNodi) {
        this.nodi.addAll(pNodi); //Da modificare per evitare di inserire nodi uguali
    }

    public void addNodo(Nodo pNodo) {
        if (!this.nodi.contains(pNodo)) {
            this.nodi.add(pNodo);
        }
    }

    //Rimuovi
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
        //this.archi.removeAll(pArchi);
        for (Arco arcoDaRimuovere : pArchi) {
            rimuoviArco(arcoDaRimuovere);
        }
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

    public void rimuoviColore(int pColore) {
        //Determino gli indici degli archi in cui è presente pColore
        for (int i : this.colori.get(pColore).getIndiciArchiCollegati()) {
            this.archi.get(i).rimuoviColore(pColore);
        }

        //Elimino ogni riferimento di pColore da ogni arco associato
        this.getColori().get(pColore).getIndiciArchiCollegati().clear();
    }

    //Altro
    /**
     * Restituisce la dimensione del grafo intesa come numero di nodi.
     *
     * @return la dimensione del grafo
     */
    public int dimensione() {
        return this.nodi.size();
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

        for (Colore colore : this.colori) {
            colore.getIndiciArchiCollegati().clear();
        }
    }

    /**
     * Effettuo una copia del grafo, andando ad associare, ad ogni variabile, un
     * nuovo indirizzo
     *
     * @return il grafo clonato
     */
    public Grafo clone() {
        ArrayList<Nodo> copiaNodi = new ArrayList<>(this.nodi);
        ArrayList<Arco> copiaArchi = this.getCopiaArchi();
        ArrayList<Colore> copiaColori = new ArrayList<>(this.colori);

        return new Grafo(copiaNodi, copiaArchi, copiaColori);
    }

    private void setListaColoriOrdinataPerRicorrenza() {
        ArrayList<Colore> tmpColori = new ArrayList<>(this.colori);
        this.listaColoriOrdinataPerRicorrenzaMaggiore = new ArrayList<>();

        int indexColoreMaxRicorrente = -1;
        int coloreMaxRicorrente = -1;
        int maxRicorrenza = -1;

        while (tmpColori.size() > 0) {
            indexColoreMaxRicorrente = 0;
            for (int i = 0; i < tmpColori.size(); i++) {
                if (tmpColori.get(i).getOccorrenze() > maxRicorrenza) {
                    maxRicorrenza = tmpColori.get(i).getOccorrenze();
                    coloreMaxRicorrente = tmpColori.get(i).getColore();
                    indexColoreMaxRicorrente = i;
                }
            }

            tmpColori.remove(indexColoreMaxRicorrente);
            maxRicorrenza = -1;
            this.listaColoriOrdinataPerRicorrenzaMaggiore.add(coloreMaxRicorrente);
        }
    }
}
