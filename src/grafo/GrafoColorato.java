package grafo;

import java.util.ArrayList;

public class GrafoColorato extends Grafo {

    ArrayList<Colore> colori;
    public ArrayList<Integer> listaColoriOrdinataPerRicorrenzaMaggiore;

    //Costruttori
    public GrafoColorato() {
        super();
        this.colori = new ArrayList<>();
    }

    public GrafoColorato(ArrayList<Nodo> nodi) {
        super(nodi);
        this.colori = new ArrayList<>();
    }

    /**
     * Crea un grafo, partendo da una lista di nodi e da un numero prefissato di
     * colori, a cui, inizialmente, non verrà associato nessun arco
     *
     * @param nodi, lista di nodi
     * @param numeroColori, lista di colori
     */
    public GrafoColorato(ArrayList<Nodo> nodi, int numeroColori) {
        super(nodi);
        this.colori = new ArrayList<>();
        for (int c = 0; c < numeroColori; c++) {
            this.colori.add(new Colore(c));
        }
    }

    public GrafoColorato(ArrayList<Nodo> nodi, ArrayList<Arco> archi) {
        super(nodi, archi);
        this.colori = new ArrayList<>();
        for (Arco arco : archi) {
            for (int c : arco.getColori()) {
                Colore colore = new Colore(c);
                if (!colori.contains(colore)) {
                    this.colori.add(colore);
                }
            }
        }
    }

    public GrafoColorato(ArrayList<Nodo> nodi, ArrayList<Arco> archi, ArrayList<Colore> colori) {
        super(nodi, archi);
        this.colori = colori;
    }

    @Override
    public ArrayList<Arco> getCopiaArchi() {
        ArrayList<Arco> copiaArchi = new ArrayList();
        for (Arco originale : this.archi) {
            Arco copia = new Arco(originale.getDa(), originale.getA(), originale.getColori());
            copiaArchi.add(copia);
        }
        return copiaArchi;
    }

    /**
     * Questo metodo restituisce i colori del grafo, indipendentemente dal fatto
     * che essi siano stati utilizzati o meno
     *
     * @return tutti i colori del grafo
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

        for (Colore colore : this.colori) {
            if (colore.usato()) {
                listaColori.add(colore.getId());
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

    @Override
    public void addArchi(ArrayList<Arco> archi) {

        for (Arco arco : archi) {
            addArco(arco);
        }
    }

    @Override
    public void addArco(Arco arco) {
        this.archi.add(arco);
        getNodo(arco.getDa().getChiave()).addNodoAdiacente(getNodo(arco.getA().getChiave()));
        getNodo(arco.getDa().getChiave()).addArcoIncidente(arco);
        getNodo(arco.getA().getChiave()).addNodoAdiacente(getNodo(arco.getDa().getChiave()));
        getNodo(arco.getA().getChiave()).addArcoIncidente(arco);

        //PROVA STEFANO
        /*for (int colore : pArco.getColori()) {
            Colore c = new Colore(colore);
            if (!colori.contains(c)) {
                colori.add(c);
            }
        }*/
        ////FINE PROVA
        // Aggiungo i colori al grafo
        for (int c : arco.getColori()) {
            Colore colore = new Colore(c);

            if (!this.colori.contains(colore)) {
                this.colori.add(colore);
            }
            //this.colori.get(colore).addIndiceArcoCollegato(this.archi.size() - 1);
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

    /**
     * Questa funzione va ad eliminare tutti gli archi e tutti i suoi
     * riferimenti
     */
    @Override
    public void clear() {
        this.archi.clear();

        for (Nodo nodo : this.nodi) {
            nodo.getIncidenti().clear();
            nodo.getAdiacenti().clear();
        }

        this.colori.forEach((colore) -> {
            colore.getIndiciArchiCollegati().clear();
        });
    }

    /**
     * Effettua una copia del grafo associando ad ogni variabile un nuovo
     * indirizzo.
     *
     * @return il grafo clonato
     */
    public GrafoColorato clona() {
        //ArrayList<Nodo> copiaNodi = new ArrayList((ArrayList)this.nodi.clone());
        ArrayList<Nodo> copiaNodi = getCopiaNodi();
        ArrayList<Arco> copiaArchi = getCopiaArchi();
        ArrayList<Colore> copiaColori = new ArrayList<>(this.colori);

        return new GrafoColorato(copiaNodi, copiaArchi, copiaColori);
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
                    coloreMaxRicorrente = tmpColori.get(i).getId();
                    indexColoreMaxRicorrente = i;
                }
            }

            tmpColori.remove(indexColoreMaxRicorrente);
            maxRicorrenza = -1;
            this.listaColoriOrdinataPerRicorrenzaMaggiore.add(coloreMaxRicorrente);
        }
    }
}
