package grafo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Grafo {

    public String nomeGrafo;
    ArrayList<Nodo> nodi;
    LinkedHashMap<Integer, Arco> archi;

    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new LinkedHashMap<>();
    }

    public Grafo(ArrayList<Nodo> pNodi) {
        this.nodi = pNodi;
        this.archi = new LinkedHashMap<>();

        //Clear nodi
        for (Nodo nodo : this.nodi) {
            nodo.getIndiciArchiIncidenti().clear();
            nodo.getIndiciArchiEntranti().clear();
            nodo.getIndiciArchiUscenti().clear();
            nodo.getAdiacenti().clear();

            //Imposta componente di riferimento
            nodo.setComponenteDiRiferimento(nodo.getChiave());
        }

    }

    public Grafo(ArrayList<Nodo> pNodi, LinkedHashMap<Integer, Arco> pArchi) {
        this.nodi = pNodi;
        this.archi = pArchi;
    }

    //GET
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
            nodo.getIndiciArchiIncidenti().forEach((incidente) -> {
                nodoCopia.addIndiceArcoIncidente(incidente);
            });

            // Aggiungo tutti gli archi entranti nel nodo
            nodo.getIndiciArchiEntranti().forEach((entrante) -> {
                nodoCopia.addIndiceArcoEntrante(entrante);
            });

            // Aggiungo tutti gli archi uscenti nel nodo
            nodo.getIndiciArchiUscenti().forEach((uscente) -> {
                nodoCopia.addIndiceArcoUscente(uscente);
            });

            // Copio la componenti di riferimento
            nodoCopia.setComponenteDiRiferimento(nodo.getComponenteDiRiferimento());

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

    public LinkedHashMap<Integer, Arco> getArchi() {
        return archi;
    }

    /**
     * Questo metodo ritorna la lista degli archi correlati ad una lista di nodi
     * data.
     *
     * @param pListaNodi
     * @return la lista degli archi correlati
     */
    public LinkedHashMap<Integer, Arco> getArchi(ArrayList<Nodo> pListaNodi) {
        LinkedHashMap<Integer, Arco> listaArchi = new LinkedHashMap<>();
        ArrayList<Integer> listaIndiciArchi = new ArrayList<>();

        for (int i = 0; i < pListaNodi.size(); i++) {
            listaIndiciArchi.addAll(pListaNodi.get(i).getIndiciArchiIncidenti());
        }

        while (!listaIndiciArchi.isEmpty()) {
            int indice = listaIndiciArchi.get(0);

            listaArchi.put(indice, this.archi.get(indice));
            listaIndiciArchi.removeAll(Arrays.asList(indice));
        }

        return listaArchi;
    }

    public ArrayList<Arco> getCopiaArchi() {
        ArrayList<Arco> copiaArchi = new ArrayList();

        for (Map.Entry<Integer, Arco> entry : archi.entrySet()) {
            Arco arco = entry.getValue();
            copiaArchi.add(new Arco(arco.getDa(), arco.getA()));
        }

        return copiaArchi;
    }

    public Arco getArco(Nodo da, Nodo a) {
        for (Map.Entry<Integer, Arco> entry : archi.entrySet()) {
            Integer key = entry.getKey();
            Arco arco = entry.getValue();

            if ((arco.getDa().equals(da) && arco.getA().equals(a))
                    || (arco.getA().equals(da) && arco.getDa().equals(a))) {
                return arco;
            }

        }

        return null;
    }

    public Arco getArco(int chiaveDa, int chiaveA) {
        return getArco(getNodo(chiaveDa), getNodo(chiaveA));
    }

    public Arco getArco(int indice) {
        return this.archi.get(indice);
    }

    //ADD
    /**
     * Questa funzione aggiunge nuovi archi al grafo, creando nuovi indici
     *
     * @param archi
     */
    public void addArchi(ArrayList<Arco> archi) {
        //Trova l'indice più grande
        int maxIndice = -1;
        for (int i : this.archi.keySet()) {
            if (i > maxIndice) {
                maxIndice = i;
            }
        }

        for (Arco arco : archi) {
            addArco(++maxIndice, arco);
        }
    }

    public void addArco(int indiceArco, Arco pArco) {
        this.archi.put(indiceArco, pArco);

        Nodo da = getNodo(pArco.getDa().getChiave());
        Nodo a = getNodo(pArco.getA().getChiave());

        da.addNodoAdiacente(a);
        da.addIndiceArcoIncidente(indiceArco);
        da.addIndiceArcoUscente(indiceArco);
        a.addNodoAdiacente(da);
        a.addIndiceArcoIncidente(indiceArco);
        a.addIndiceArcoEntrante(indiceArco);
    }

    public void addNodi(ArrayList<Nodo> pNodi) {
        this.nodi.addAll(pNodi); //Da modificare per evitare di inserire nodi uguali
    }

    public void addNodo(Nodo pNodo) {
        if (!this.nodi.contains(pNodo)) {
            this.nodi.add(pNodo);
        }
    }

    //RIMUOVI
    public void rimuoviArchi(ArrayList<Arco> pArchi) {
        //this.archi.removeAll(pArchi);
        for (Arco arcoDaRimuovere : pArchi) {
            rimuoviArco(arcoDaRimuovere);
        }
    }

    public void rimuoviArco(int indiceArco) {
        this.archi.remove(indiceArco);
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
        Arco tmpArco = null;

        for (int i : this.archi.keySet()) {
            tmpArco = this.archi.get(i);

            if ((tmpArco.getDa().equals(nodoDa) && tmpArco.getA().equals(nodoA))
                    || (tmpArco.getA().equals(nodoDa) && tmpArco.getDa().equals(nodoA))) {
                Nodo nodo1 = this.nodi.get(this.nodi.indexOf(nodoDa));
                Nodo nodo2 = this.nodi.get(this.nodi.indexOf(nodoA));

                nodo1.rimuoviIndiceArcoIncidente(i);
                nodo2.rimuoviIndiceArcoIncidente(i);
                nodo1.rimuoviNodoAdiacente(nodo2);
                nodo2.rimuoviNodoAdiacente(nodo1);

                this.archi.remove(i);

                return;
            }
        }
    }

    public void rimuoviNodo(Nodo nodo) {
        Arco arco = null;
        if (this.nodi.contains(nodo)) {
            Nodo nodoDaRimuovere = this.nodi.get(this.nodi.indexOf(nodo));
            ArrayList<Integer> indiciArchiDaRimuovere = new ArrayList<>(nodoDaRimuovere.getIndiciArchiIncidenti());

            for (int indiceArco : indiciArchiDaRimuovere) {
                arco = this.archi.get(indiceArco);

                //Rimozione info arco dal nodo non eliminato
                if (arco.getDa().equals(nodo)) {
                    arco.getA().rimuoviIndiceArcoIncidente(indiceArco);
                    arco.getA().rimuoviNodoAdiacente(nodo);
                } else {
                    arco.getDa().rimuoviIndiceArcoIncidente(indiceArco);
                    arco.getDa().rimuoviNodoAdiacente(nodo);
                }

                //Rimozione arco
                this.archi.remove(indiceArco);
            }

            this.nodi.remove(nodoDaRimuovere);
        }
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
     * Questa funzione va ad eliminare tutti gli archi e tutti i suoi
     * riferimenti
     */
    public void clear() {
        this.archi.clear();
        for (Nodo nodo : this.nodi) {
            nodo.getIndiciArchiIncidenti().clear();
            nodo.getAdiacenti().clear();
        }
    }
}

/*
public class Grafo {

    
    public Grafo(ArrayList<Nodo> pNodi, int pNumColori) {
        this.nodi = pNodi;
        this.archi = new LinkedHashMap<>();

        this.colori = new ArrayList<>();
        for (int i = 0; i < pNumColori; i++) {
            this.colori.add(new Colore(i));
        }
    }

    public Grafo(ArrayList<Nodo> pNodi, LinkedHashMap<Integer, Arco> pArchi, ArrayList<Colore> pColori) {
        this.nodi = pNodi;
        this.archi = pArchi;
        this.colori = pColori;
    }


    


    public ArrayList<Colore> getColori() {
        return colori;
    }
    
    public ArrayList<Colore> getCopiaColori () {
        ArrayList<Colore> copiaColori = new ArrayList<>();
        Colore tmpColore = null;
        
        for (Colore colore : this.colori) {
            tmpColore = new Colore(colore.getId(), new ArrayList<>(colore.getIndiciArchiCollegati()));
            copiaColori.add(tmpColore);
        }
        
        return copiaColori;
    }

    public ArrayList<Integer> getListaColoriUsati() {
        ArrayList<Integer> listaColori = new ArrayList<>();

        for (Colore c : this.colori) {
            if (c.isUsed()) {
                listaColori.add(c.getId());
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

    //Rimuovi
    
    public void rimuoviColore(int pColore) {
        //Determino gli indici degli archi in cui è presente pColore
        for (int i : this.colori.get(pColore).getIndiciArchiCollegati())
            this.archi.get(i).rimuoviColore(pColore);

        //Elimino ogni riferimento di pColore da ogni arco associato
        this.getColori().get(pColore).getIndiciArchiCollegati().clear();
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
}*/
