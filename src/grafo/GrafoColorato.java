package grafo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GrafoColorato extends Grafo {

    LinkedHashMap<Integer, Colore> colori;
    public LinkedHashMap<Integer, Integer> listaColoriOrdinataPerRicorrenzaMaggiore;

    //Costruttori
    public GrafoColorato() {
        super();
        this.colori = new LinkedHashMap<>();
    }

    public GrafoColorato(ArrayList<Nodo> nodi) {
        super(nodi);
        this.colori = new LinkedHashMap<>();
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
        
        this.colori = new LinkedHashMap<>();
        for (int c = 0; c < numeroColori; c++)
            this.colori.put(c, new Colore(c));
    }

    public GrafoColorato(ArrayList<Nodo> nodi, LinkedHashMap<Integer, Arco> archi) {
        super(nodi, archi);
        this.colori = new LinkedHashMap<>();
        
        for (Map.Entry<Integer, Arco> entry : archi.entrySet()) {
            int indiceArco = entry.getKey();
            Arco arco = entry.getValue();
            
            for (int c : arco.getColori()) {
                //Colore esistente: inserimento indice arco
                if (colori.containsKey(c)) {    
                    this.colori.get(c).addIndiceArcoCollegato(indiceArco);
                }
                //Colore non esistente: inserimento colore + indice arco
                else {
                    Colore colore = new Colore(c);
                    colore.addIndiceArcoCollegato(indiceArco);
                    this.colori.put(c, colore);
                }
            }
        }
    }

    public GrafoColorato(ArrayList<Nodo> nodi, LinkedHashMap<Integer, Arco> archi, LinkedHashMap<Integer, Colore> colori) {
        super(nodi, archi);
        this.colori = colori;
        
        setPesiArchi();
    }

    
    //GET
    
    @Override
    public ArrayList<Arco> getCopiaArchi() {
        ArrayList<Arco> copiaArchi = new ArrayList<>();
        
        for (Map.Entry<Integer, Arco> entry : archi.entrySet()) {
            Arco arco = entry.getValue();
            Arco nuovoArco = new Arco(arco.getDa(), arco.getA(), new ArrayList<>(arco.getColori()));
            copiaArchi.add(nuovoArco);
        }
        
        return copiaArchi;
    }

    /**
     * Questo metodo restituisce i colori del grafo, indipendentemente dal fatto
     * che essi siano stati utilizzati o meno
     *
     * @return tutti i colori del grafo
     */
    public LinkedHashMap<Integer, Colore> getColori() {
        return colori;
    }
    
    public Colore getColore (int idColore) {
        return colori.get(idColore);
    }

    public ArrayList<Colore> getCopiaColori() {
        ArrayList<Colore> copiaColori = new ArrayList<>();
        
        for (Map.Entry<Integer, Colore> entry : this.colori.entrySet()) {
            Integer idColore = entry.getKey();
            Colore colore = entry.getValue();
            
            copiaColori.add(new Colore(idColore, new ArrayList<>(colore.getIndiciArchiCollegati())));
        }
        
        return copiaColori;
    }
    
    public ArrayList<Integer> getListaColori() {
        ArrayList<Integer> listaColori = new ArrayList<>();

        for (Map.Entry<Integer, Colore> entry : colori.entrySet()) {
            Integer idColore = entry.getKey();
            Colore colore = entry.getValue();
            
            if (colore.usato())
                listaColori.add(idColore);
        }
        
        return listaColori;
    }
    
    /**
     * Questo metodo restituisce la lista dei colori utilizzati nel grafo
     *
     * @return la lista dei colori utilizzati
     */
    public LinkedHashMap<Integer, Integer> getListaColoriOrdinataPerRicorrenza() {
        if (this.listaColoriOrdinataPerRicorrenzaMaggiore == null) {
            setListaColoriOrdinataPerRicorrenza();
        }

        return listaColoriOrdinataPerRicorrenzaMaggiore;
    }
    
    //ADD
    
    @Override
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

    @Override
    public void addArco(int indiceArco, Arco pArco) {
        super.addArco(indiceArco, pArco);

        for (int colore : pArco.getColori()) {
            this.colori.get(colore).addIndiceArcoCollegato(indiceArco);
        }
    }

    public void addColore(Colore colore) {
        this.colori.put(colore.getId(), colore);
    }

    
    //RIMUOVI
    
    @Override
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
                
                
                //Rimozione info arco dai colori di riferimento
                for (int colore : arco.getColori())
                    this.colori.get(colore).rimuoviIndiceArcoCollegato(indiceArco);
                
                //Rimozione arco
                this.archi.remove(indiceArco);
            }

            this.nodi.remove(nodoDaRimuovere);
        }
    }

    public void rimuoviColore(int pColore) {
        //Determino gli indici degli archi in cui è presente pColore
        ArrayList<Integer> listaIndiciArchi = this.colori.get(pColore).getIndiciArchiCollegati();
        for (int i : listaIndiciArchi) {
            this.archi.get(i).rimuoviColore(pColore);
        }

        //Elimino ogni riferimento di pColore da ogni arco associato
        this.colori.get(pColore).getIndiciArchiCollegati().clear();
    }

    
    
    /**
     * Questa funzione va ad eliminare tutti gli archi e tutti i suoi
     * riferimenti
     */
    @Override
    public void clear() {
        this.archi.clear();

        for (Nodo nodo : this.nodi) {
            nodo.getIndiciArchiIncidenti().clear();
            nodo.getAdiacenti().clear();
        }

        for (Map.Entry<Integer, Colore> entry : colori.entrySet())
            entry.getValue().getIndiciArchiCollegati().clear();
    }
    
    @Override
    public GrafoColorato clone() {
        GrafoColorato grafoColoratoClone = null;
        Grafo grafoClone = super.clone();
        
        ArrayList<Nodo> nodiClone = grafoClone.getNodi();
        LinkedHashMap<Integer, Arco> archiClone = grafoClone.getArchi();
        
        for (Map.Entry<Integer, Arco> entry : archi.entrySet()) {
            Integer indiceArco = entry.getKey();
            Arco arco = entry.getValue();
            
            archiClone.get(indiceArco).setColori(new ArrayList<>(arco.getColori()));
        }
        
        
        LinkedHashMap<Integer, Colore> coloriClone = new LinkedHashMap<>();
        for (Map.Entry<Integer, Colore> entry : this.colori.entrySet()) {
            Integer idColore = entry.getKey();
            Colore colore = entry.getValue();
            
            coloriClone.put(idColore, new Colore(idColore, new ArrayList<>(colore.getIndiciArchiCollegati())));
        }
        
        grafoColoratoClone = new GrafoColorato(nodiClone, archiClone, coloriClone);
        grafoColoratoClone.listaNodiConnessiAComponente = grafoClone.listaNodiConnessiAComponente;
        grafoColoratoClone.setPesiArchi();
        
        return grafoColoratoClone;
    }
    
    private void setListaColoriOrdinataPerRicorrenza() {
        LinkedHashMap<Integer, Integer> listaRicorrenzeColori = new LinkedHashMap<>();
        for (Map.Entry<Integer, Colore> entry : this.colori.entrySet())
            listaRicorrenzeColori.put(entry.getKey(), entry.getValue().getOccorrenze());
        
        
        int coloreMaxRicorrente = -1;
        int maxRicorrenza = -1;
        
        this.listaColoriOrdinataPerRicorrenzaMaggiore = new LinkedHashMap<>();
        
        while (listaRicorrenzeColori.size() > 0) {
            for (Map.Entry<Integer, Integer> entry : listaRicorrenzeColori.entrySet()) {
                Integer colore = entry.getKey();
                Integer occorrenze = entry.getValue();
                
                if (occorrenze > maxRicorrenza) {
                    maxRicorrenza = occorrenze;
                    coloreMaxRicorrente = colore;
                }
                
            }
            
            listaRicorrenzeColori.remove(coloreMaxRicorrente);
            this.listaColoriOrdinataPerRicorrenzaMaggiore.put(coloreMaxRicorrente, maxRicorrenza);
            coloreMaxRicorrente = -1;
            maxRicorrenza = -1;
        }
        
        

        
    }
    
    private void setPesiArchi () {
        for (Map.Entry<Integer, Arco> entry : this.archi.entrySet()) {
            Integer indiceArco = entry.getKey();
            Arco arco = entry.getValue();
            
            int pesoArco = 1;
            for (int colore : arco.getColori()) {
                pesoArco *= this.getColore(colore).getOccorrenze();
            }
            
            this.archi.get(indiceArco).setPeso(pesoArco);
            
        }
    }
    
}