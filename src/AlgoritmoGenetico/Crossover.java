/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import grafo.GrafoColorato;
import greedy.Greedy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
        
        Set<Integer> tmpUnioneGenitori;
        ArrayList<Integer> unioneGenitori;
        List<Integer> mezzaUnione;
        
        //Primo genitore -> genera figlio da solo
        Cromosoma figlio = new Cromosoma();
        
        unioneGenitori = new ArrayList<>(genitori.get(0));
        ordinaColoriPerRicorrenza(unioneGenitori);
        figlio.setColoriGenitori(unioneGenitori);
        mezzaUnione = unioneGenitori.subList(0, (3*unioneGenitori.size())/4);
        figlio.addAll(this.greedy.esegui(mezzaUnione).getListaColori());
        figli.add(figlio);
        
        
        for (int i = 1; i < genitori.size(); i+=2) {
            Cromosoma genitore1 = genitori.get(i);
            Cromosoma genitore2 = genitori.get(i+1);
            figlio = new Cromosoma();
            
            tmpUnioneGenitori = new HashSet<>();
            tmpUnioneGenitori.addAll(genitore1);
            tmpUnioneGenitori.addAll(genitore2);
            
            unioneGenitori = new ArrayList<>(tmpUnioneGenitori);
            ordinaColoriPerRicorrenza(unioneGenitori);
            figlio.setColoriGenitori(unioneGenitori);
            
            mezzaUnione = unioneGenitori.subList(0, unioneGenitori.size()/2);
            
            figlio.addAll(this.greedy.esegui(mezzaUnione).getListaColori());
            
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
}
