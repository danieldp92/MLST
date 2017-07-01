/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class Mutazione {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    public Mutazione(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();
        this.grafo = grafo;
    }

    public void mutazione(ArrayList<Cromosoma> figli) {

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
