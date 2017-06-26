package grafo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Grafo {

    public String nomeGrafo;
    protected LinkedHashMap<Integer, ArrayList<Integer>> listaNodiConnessiAComponente;
    ArrayList<Nodo> nodi;
    LinkedHashMap<Integer, Arco> archi;
    
    public Grafo() {
        this.nodi = new ArrayList<>();
        this.archi = new LinkedHashMap<>();
    }

    public Grafo(ArrayList<Nodo> pNodi) {
        this.nodi = pNodi;
        this.archi = new LinkedHashMap<>();
        this.listaNodiConnessiAComponente = new LinkedHashMap<>();
        
        //Clear nodi
        for (Nodo nodo : this.nodi) {
            nodo.getIndiciArchiIncidenti().clear();
            nodo.getIndiciArchiEntranti().clear();
            nodo.getIndiciArchiUscenti().clear();
            nodo.getAdiacenti().clear();
            
            
            ArrayList<Integer> listaNodi = new ArrayList<>();
            listaNodi.add(nodo.getChiave());
            listaNodiConnessiAComponente.put(nodo.getChiave(), listaNodi);
        }
    }

    public Grafo(ArrayList<Nodo> pNodi, LinkedHashMap<Integer, Arco> pArchi) {
        this.nodi = pNodi;
        this.archi = pArchi;
        
        /*
        //Creazione sottocomponenti
        this.listaNodiConnessiAComponente = new LinkedHashMap<>();
        //Settaggio iniziale
        for (Nodo nodo : this.nodi) {
            ArrayList<Integer> listaNodi = new ArrayList<>();
            listaNodi.add(nodo.getChiave());
            listaNodiConnessiAComponente.put(nodo.getChiave(), listaNodi);
        }
        
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;
        
        //Calcolo sottocomponenti
        int i = 0;
        for (Map.Entry<Integer, Arco> entry : this.archi.entrySet()) {
            Integer key = entry.getKey();
            Arco arco = entry.getValue();
            System.out.println(++i);
            if (i == 15)
                System.out.println("OK");
            Nodo nodoDa = arco.getDa();
            Nodo nodoA = arco.getA();
            
            componenteDiRiferimentoNodo1 = nodoDa.getComponenteDiRiferimento();
            componenteDiRiferimentoNodo2 = nodoA.getComponenteDiRiferimento();
            
            if (componenteDiRiferimentoNodo1 != componenteDiRiferimentoNodo2) {
                if (this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo1).size() > 
                        this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo2).size()) {
                    this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo1).addAll(this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo2));
                    this.listaNodiConnessiAComponente.remove(componenteDiRiferimentoNodo2);
                    
                    nodoA.setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                } else {
                    this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo2).addAll(this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo1));
                    this.listaNodiConnessiAComponente.remove(componenteDiRiferimentoNodo1);
                    
                    nodoDa.setComponenteDiRiferimento(componenteDiRiferimentoNodo2);
                }
            }
        }*/
    }
    
    

    //GET
    public LinkedHashMap<Integer, ArrayList<Integer>> getListaNodiConnessiAComponente() {
        return listaNodiConnessiAComponente;
    }
    
    public int getTotaleSottoComponenti() {
        return listaNodiConnessiAComponente.size();
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
        } catch (IndexOutOfBoundsException ex) { nodo = null; }

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
        
        
        //Sottocomponenti
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;

        Nodo nodo1 = getNodo(pArco.getDa().getChiave());
        Nodo nodo2 = getNodo(pArco.getA().getChiave());
        componenteDiRiferimentoNodo1 = nodo1.getComponenteDiRiferimento();
        componenteDiRiferimentoNodo2 = nodo2.getComponenteDiRiferimento();

        //Se l'arco non genera cicli
        if (componenteDiRiferimentoNodo1 != componenteDiRiferimentoNodo2) {
            ArrayList<Integer> listaNodiComponente1 = this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo1);
            ArrayList<Integer> listaNodiComponente2 = this.listaNodiConnessiAComponente.get(componenteDiRiferimentoNodo2);

            if (listaNodiComponente1.size() < listaNodiComponente2.size()) {
                for (int indiceNodo : listaNodiComponente1) {
                    getNodo(indiceNodo).setComponenteDiRiferimento(componenteDiRiferimentoNodo2);
                }
                
                listaNodiComponente2.addAll(listaNodiComponente1);
                
                this.listaNodiConnessiAComponente.remove(componenteDiRiferimentoNodo1);
                this.listaNodiConnessiAComponente.put(componenteDiRiferimentoNodo2, listaNodiComponente2);
            } else {
                for (int indiceNodo : listaNodiComponente2) {
                    getNodo(indiceNodo).setComponenteDiRiferimento(componenteDiRiferimentoNodo1);
                }
                
                listaNodiComponente1.addAll(listaNodiComponente2);
                
                this.listaNodiConnessiAComponente.remove(componenteDiRiferimentoNodo2);
                this.listaNodiConnessiAComponente.put(componenteDiRiferimentoNodo1, listaNodiComponente1);
            }
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