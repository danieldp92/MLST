/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import greedy.Greedy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author Dasteor
 */
public class Crossover {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;
    private Greedy greedy;

    public Crossover(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();

        this.grafo = grafo.clone();
        this.greedy = new Greedy(this.grafo);
    }
    
    public ArrayList<Cromosoma> crossover(ArrayList<Cromosoma> genitori) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        
        Cromosoma figlio = new Cromosoma();
        figlio.addAll(genitori.get(0));
        figli.add(figlio);
        
        figlio = null;
        
        for (int i = 1; i < genitori.size(); i+=2) {
            Cromosoma genitore1 = genitori.get(i);
            Cromosoma genitore2 = genitori.get(i+1);
            figlio = new Cromosoma();
            
            Set<Integer> tmpUnioneGenitori = new HashSet<>();
            tmpUnioneGenitori.addAll(genitore1);
            tmpUnioneGenitori.addAll(genitore2);
            
            ArrayList<Integer> unioneGenitori = new ArrayList<>(tmpUnioneGenitori);
            ordinaColoriPerRicorrenza(unioneGenitori);
            
            ArrayList<Integer> mezzaUnione = new ArrayList<>();
            for (int j = 0; j < unioneGenitori.size()/2; j++)
                mezzaUnione.add(unioneGenitori.get(j));
            
            figlio.addAll(this.greedy.esegui(mezzaUnione));
            
            figli.add(figlio);
        }
        
        return figli;
    }

    private void ordinaColoriPerRicorrenza(ArrayList<Integer> listaColori) {
        //Sort
        Collections.sort(listaColori, new Comparator<Integer>() {
            LinkedHashMap<Integer, Integer> listaColoriOrdinatiPerRicorrenza = grafo.getListaColoriOrdinataPerRicorrenza();

            @Override
            public int compare(Integer oolore1, Integer oolore2) {
                return listaColoriOrdinatiPerRicorrenza.get(oolore2).
                        compareTo(listaColoriOrdinatiPerRicorrenza.get(oolore1));
            }

        });
    }
    
    private GrafoColorato creaGrafoUsandoUnPrincipioDiGreedy (ArrayList<Integer> listaColori) {
        GrafoColorato grafo = this.grafo.clone();
        GrafoColorato mlst = new GrafoColorato(grafo.getCopiaNodi(), grafo.getColori().size());
        
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
        
        boolean nuovoColore = true;
        
        while (!gestoreMlst.connesso()) {
            int colorePiùFrequente = 0;
            //Avvio prelevamento colore dalla lista
            if (nuovoColore) {
                colorePiùFrequente = listaColori.remove(0);
                nuovoColore = false;
            }
        }
        
        return mlst;
    }
}
