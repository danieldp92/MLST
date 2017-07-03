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
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class Mutazione {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;
    private Greedy greedy;

    public Mutazione(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();
        this.grafo = grafo;
        
        this.greedy = new Greedy(this.grafo);
    }

    public void mutazione(ArrayList<Cromosoma> figli, double mutationRate) {
        for (int i = 0; i < figli.size(); i++) {
            if (Math.random() < mutationRate) {
                System.out.println("MUTAZIONE CROMOSOMA " + (i+1));
                
                Cromosoma figlioMutato = new Cromosoma();
                List<Integer> listaColoriGenitori = figli.get(i).getColoriGenitori();
                Collections.shuffle(listaColoriGenitori, new Random(System.nanoTime()));
                
                List<Integer> mezzaLista = listaColoriGenitori.subList(0, listaColoriGenitori.size()/2);
                figlioMutato.addAll(this.greedy.esegui(mezzaLista).getListaColori());
                
                figli.set(i, figlioMutato);
            }
        }
    }

    public void STRONGMUTATION(Popolazione popolazione) {
        for (int i = 0; i < popolazione.size(); i++) {
            if (Math.random() < this.impostazioni.strongMutationRate) {
                System.out.println("STRONG MUTATION Cromosoma " + (i + 1));

                Cromosoma cromosoma = creaCromosomaRandom();

                popolazione.setCromosoma(i, cromosoma);
            }
        }
    }

    private Cromosoma creaCromosomaRandom() {
        Cromosoma cromosoma = new Cromosoma();

        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.sizeCromosoma; i++) {
            listaColori.add(i);
        }

        GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosoma);

        //RANDOM SHUFFLE
        long seed = System.nanoTime();
        Collections.shuffle(listaColori, new Random(seed));

        cromosoma = gestoreCromosoma.getNuovoCromosomaDaPartenzaNonAmmissibile(listaColori);

        return cromosoma;
    }
}
