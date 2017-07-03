/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedy;

import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.GrafoColorato;
import java.util.ArrayList;

/**
 *
 * @author Rhobar
 */
public class GreedyPesato {

    GrafoColorato grafo;
    Statistiche statistiche;

    //private int[][] matriceDiCorrelazione;
    private ArrayList<ArrayList<Integer>> archiIncidentiPerNodo;
    private ArrayList<Double> pesiArchi;
    private ArrayList<Integer> listaIndiciArchiConZeroColori;
    private int[] frequenzeTotaliColoriPerArco;

    public GreedyPesato(GrafoColorato grafo) {
        this.grafo = grafo;
        this.statistiche = new Statistiche();

        //this.matriceDiCorrelazione = new int[this.grafo.numeroColori()][this.grafo.numeroColori()];
        this.archiIncidentiPerNodo = new ArrayList<>();
        this.pesiArchi = new ArrayList<>();
        this.listaIndiciArchiConZeroColori = new ArrayList<>();
        this.frequenzeTotaliColoriPerArco = new int[this.grafo.getArchi().size()];
    }

    public GrafoColorato esegui() {
        long inizio = System.currentTimeMillis();
        GrafoColorato mlst = new GrafoColorato(grafo.getCopiaNodi(), grafo.getListaColori().size());
        ArrayList<Arco> tmpArchi = grafo.getCopiaArchi();
        ArrayList<Colore> tmpColori = grafo.getCopiaColori();

        inizializza(tmpArchi);

        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
        

        return mlst;
    }

    private void inizializza(ArrayList<Arco> archi) {
        //Inizializza vettore frequenze
        for (int i = 0; i < archi.size(); i++) {
            this.frequenzeTotaliColoriPerArco[i] = 1;
            for (int colore : archi.get(i).getColori()) {
                this.frequenzeTotaliColoriPerArco[i] *= this.grafo.getColore(colore).getOccorrenze();
            }
        }

        //Inizializza la matrice di correlazione
        /*for (Arco arco : archi) {
            for (int i = 0; i < arco.getColori().size()-1; i++) {
                for (int j = i+1; j < arco.getColori().size(); j++) {
                    this.matriceDiCorrelazione[arco.getColori().get(i)][arco.getColori().get(j)]++;
                }
            }
        }
        
        for (int i = 0; i < this.grafo.numeroColori()-1; i++) {
            for (int j = i+1; j < this.grafo.numeroColori(); j++) {
                this.matriceDiCorrelazione[i][j] += this.matriceDiCorrelazione[j][i];
            }
        }*/
        //Inizializzo il nuemro di archi incidenti
        for (int i = 0; i < this.grafo.dimensione(); i++) {
            this.archiIncidentiPerNodo.add(new ArrayList<>(this.grafo.getNodo(i).getIndiciArchiIncidenti()));
        }

        //Inizializza pesi archi
        for (int i = 0; i < archi.size(); i++) {
            this.pesiArchi.add((double)1);
            setPesoArco(i, archi);
        }
    }

    private void setPesoArco(int indice, ArrayList<Arco> archi) {
        double peso = 1;
        int chiaveNodoDa = archi.get(indice).getDa().getChiave();
        int chiaveNodoA = archi.get(indice).getA().getChiave();

        peso *= this.archiIncidentiPerNodo.get(chiaveNodoDa).size() * this.archiIncidentiPerNodo.get(chiaveNodoA).size();
        peso /= this.frequenzeTotaliColoriPerArco[indice];

        this.pesiArchi.set(indice, peso);
    }

    private int getIndiceArcoPesoMassimo(ArrayList<Double> listaPesi) {
        int indice = -1;

        double pesoMassimo = 0;

        for (int i = 0; i < listaPesi.size(); i++) {
            //-1 indica che l'arco associato non esiste più
            if (listaPesi.get(i) != -1) {
                if (listaPesi.get(i) > pesoMassimo) {
                    indice = i;
                    pesoMassimo = listaPesi.get(i);
                }
            }
        }

        return indice;
    }

    private void rimuoviArco(int indice, ArrayList<Arco> archi) {
        Arco arco = archi.get(indice);

        
        int chiaveNodoDa = arco.getDa().getChiave();
        int chiaveNodoA = arco.getA().getChiave();

        this.archiIncidentiPerNodo.get(chiaveNodoDa).remove(Integer.valueOf(indice));
        this.archiIncidentiPerNodo.get(chiaveNodoA).remove(Integer.valueOf(indice));
        
        
        
        //Array colori da rimuovere
        ArrayList<Integer> coloriDaRimuovere = arco.getColori();
        if (!coloriDaRimuovere.isEmpty()) {
            for (int colore : coloriDaRimuovere) {
                Colore coloreDaRimuovere = this.grafo.getColore(colore);
                ArrayList<Integer> listaIndiciArchiCollegati = coloreDaRimuovere.getIndiciArchiCollegati();
                for (int indiceArco : listaIndiciArchiCollegati) {
                    this.frequenzeTotaliColoriPerArco[indiceArco] /= coloreDaRimuovere.getOccorrenze();
                    if (this.frequenzeTotaliColoriPerArco[indiceArco] == 1 && indice != indiceArco)
                        listaIndiciArchiConZeroColori.add(indiceArco);
                }
            }
        }
        
        
        
        
        //Modifica pesi degli archi incidenti in Da e A
        for (int indiceArcoNodoDa : this.archiIncidentiPerNodo.get(chiaveNodoDa)) {
            setPesoArco(indiceArcoNodoDa, archi);
        }
        
        for (int indiceArcoNodoA : this.archiIncidentiPerNodo.get(chiaveNodoA)) {
            setPesoArco(indiceArcoNodoA, archi);
        }

        archi.set(indice, null);
        this.pesiArchi.set(indice, (double) (-1));

    }

    private void rimuoviArchiCheGeneranoCicli(ArrayList<Arco> archi, ArrayList<Colore> colori, GrafoColorato mlst) {
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;

        for (int i = 0; i < archi.size(); i++) {
            if (archi.get(i) != null) {
                componenteDiRiferimentoNodo1 = mlst.getNodo(archi.get(i).getDa().getChiave()).getComponenteDiRiferimento();
                componenteDiRiferimentoNodo2 = mlst.getNodo(archi.get(i).getA().getChiave()).getComponenteDiRiferimento();

                //Se l'arco genera cicli
                if (componenteDiRiferimentoNodo1 == componenteDiRiferimentoNodo2) {
                    //Rimuovo l'indice dell'arco dai colori associati
                    for (int colore : archi.get(i).getColori()) {
                        colori.get(colore).rimuoviIndiceArcoCollegato(i);
                    }
                    archi.set(i, null);
                    this.pesiArchi.set(i, (double) (-1));
                }
            }

        }
    }
}
