/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GeneratoreGrafo;
import grafo.GrafoColorato;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Algoritmo {

    private GrafoColorato grafo;

    public Popolazione popolazione;
    private Selezione selection;
    private Crossover crossover;
    private Mutazione mutazione;

    private Impostazioni impostazioni;

    private int generazione;
    private double mediaFF;
    private int prevMigliorFF;
    private int migliorFF;
    private int iterateSenzaMiglioramenti;
    private int maxIterateSenzaMiglioramenti;

    public Algoritmo() {
        this.impostazioni = new Impostazioni();

        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.getNomeGrafo()));

        //Creo la popolazione iniziale
        this.popolazione = new Popolazione(this.grafo);

        //Operatori
        this.selection = new Selezione();
        this.crossover = new Crossover(this.grafo);
        this.mutazione = new Mutazione(this.grafo);

        //Variabili
        this.generazione = 0;
        this.mediaFF = 0;
        this.iterateSenzaMiglioramenti = 0;
        
        this.maxIterateSenzaMiglioramenti = 300;
        if (this.impostazioni.getNumeroColori() >= 10000)
            this.maxIterateSenzaMiglioramenti = 10;
        
        this.prevMigliorFF = this.impostazioni.getNumeroColori() + 1;
        this.migliorFF = this.impostazioni.getNumeroColori() + 1;
    }

    public Popolazione execute() {
        int maxValutazioni = this.impostazioni.getMaxValutazioni();
        long start = System.currentTimeMillis();
        long time;

        boolean controlloSulleIterate = false;
        if (this.impostazioni.getNumeroColori() > 100) {
            controlloSulleIterate = true;
        }

        while (this.generazione++ < maxValutazioni && 
                iterateSenzaMiglioramenti < this.maxIterateSenzaMiglioramenti && 
                (System.currentTimeMillis() - start) < 1200000) {
            
            System.out.println("ITERATA " + generazione);
            System.out.println("ITER " + iterateSenzaMiglioramenti);

            //Valutazione iniziale della popolazione
            valutaPopolazione();

            time = System.currentTimeMillis();
            //Selezione per riproduzione
            ArrayList<Cromosoma> genitori = this.selection.selezionePerRiproduzione(this.popolazione);
            System.out.println("TEMPO SELEZIONE RIPRODUZIONE: " + (double) (System.currentTimeMillis() - time) / 1000);

            time = System.currentTimeMillis();
            //Crossover
            ArrayList<Cromosoma> figli = this.crossover.multiThreadCrossover(genitori);
            System.out.println("TEMPO CROSSOVER: " + (double) (System.currentTimeMillis() - time) / 1000);

            //Mutazione
            time = System.currentTimeMillis();
            this.mutazione.multiThreadMutazione(figli);
            System.out.println("TEMPO MUTAZIONE: " + (double) (System.currentTimeMillis() - time) / 1000);
            this.popolazione.getCromosomi().addAll(figli);

            //Nuova valutazione
            valutaPopolazione();

            //Selezione per sopravvivenza
            time = System.currentTimeMillis();
            ArrayList<Cromosoma> sopravvissuti = this.selection.selezionePerSopravvivenza(this.popolazione);
            this.popolazione.setCromosomi(sopravvissuti);
            System.out.println("TEMPO SELEZIONE SOPRAVVIVENZA: " + (double) (System.currentTimeMillis() - time) / 1000);

            aggiornaInfoMediaFF();

            if(controlloSulleIterate)
                controlloIterate();
        }

        valutaPopolazione();

        return popolazione;
    }

    private void valutaPopolazione() {
        for (Cromosoma cromosoma : this.popolazione.getCromosomi()) {
            cromosoma.setValoreFunzioneDiFitness(valutaCromosoma(cromosoma));
        }

    }

    private int valutaCromosoma(Cromosoma cromosoma) {
        return cromosoma.size();
    }

    private int migliorValoreDiFitness() {
        int miglioreFF = this.impostazioni.getNumeroColori() + 1;

        for (Cromosoma cromosoma : this.popolazione.getCromosomi()) {
            if (cromosoma.getValoreFunzioneDiFitness() < miglioreFF) {
                miglioreFF = cromosoma.getValoreFunzioneDiFitness();
            }
        }

        return miglioreFF;
    }

    private void controlloIterate() {
        this.migliorFF = migliorValoreDiFitness();
        if (this.migliorFF < this.prevMigliorFF) {
            this.iterateSenzaMiglioramenti = 0;
        } else {
            this.iterateSenzaMiglioramenti++;

        }

        this.prevMigliorFF = this.migliorFF;
    }

    private void aggiornaInfoMediaFF() {
        mediaFF = 0;
        for (int i = 0; i < this.popolazione.size(); i++) {
            mediaFF += this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
        }
        mediaFF = mediaFF / this.popolazione.size();

        System.out.println("ACTUALMEDIA: " + mediaFF);
    }
}
