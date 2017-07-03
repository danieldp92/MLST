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
    private double prevMediaFF;
    private double actualMediaFF;
    private int iter;

    public Algoritmo() {
        this.impostazioni = new Impostazioni();

        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));

        //Creo la popolazione iniziale
        this.popolazione = new Popolazione(this.grafo);

        //Operatori
        this.selection = new Selezione();
        this.crossover = new Crossover(this.grafo);
        this.mutazione = new Mutazione(this.grafo);

        //Variabili
        this.generazione = 0;
        this.prevMediaFF = 0;
        this.actualMediaFF = 0;
        this.iter = 0;
    }

    public Popolazione execute() {
        long time;
        
        while (this.generazione++ < 200){
            System.out.println("ITERATA " + generazione);
            //Valutazione della popolazione
            valutaPopolazione();

            time = System.currentTimeMillis();
            //Selezione per riproduzione
            ArrayList<Cromosoma> genitori = this.selection.selezionePerRiproduzione(this.popolazione);
            System.out.println("TEMPO SELEZIONE RIPRODUZIONE: " +  + (System.currentTimeMillis()-time));
            
            time = System.currentTimeMillis();
            //Crossover
            ArrayList<Cromosoma> figli = this.crossover.multiThreadCrossover(genitori);
            System.out.println("TEMPO CROSSOVER: " +  + (System.currentTimeMillis()-time));

            //Mutazione
            time = System.currentTimeMillis();
            this.mutazione.multiThreadMutazione(figli);
            System.out.println("TEMPO MUTAZIONE: " +  + (System.currentTimeMillis()-time));
            this.popolazione.getCromosomi().addAll(figli);

            //Nuova valutazione
            valutaPopolazione();

            //Selezione per sopravvivenza
            time = System.currentTimeMillis();
            ArrayList<Cromosoma> sopravvissuti = this.selection.selezionePerSopravvivenza(this.popolazione);
            this.popolazione.setCromosomi(sopravvissuti);
            System.out.println("TEMPO SELEZIONE SOPRAVVIVENZA: " +  + (System.currentTimeMillis()-time));
            
            aggiornaInfoMediaFF();
        }

        valutaPopolazione();

        int posMaxFF = -1;
        int FfMax = 10000000;

        System.out.println("FF");
        for (int i = 0; i < this.popolazione.size(); i++) {
            System.out.println("Cromosoma " + (i + 1) + ": " + this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness());
            if (this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness() < FfMax) {
                FfMax = this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
                posMaxFF = i;
            }
        }

        System.out.println("Best Cromosoma");
        System.out.println("POS: " + posMaxFF);
        System.out.println("FF Value: " + FfMax);

        return popolazione;
    }

    public void valutaPopolazione() {
        for (Cromosoma cromosoma : this.popolazione.getCromosomi()) {
            cromosoma.setValoreFunzioneDiFitness(valutaCromosoma(cromosoma));
        }

    }

    public int valutaCromosoma(Cromosoma cromosoma) {
        return cromosoma.size();
    }

    public void stampa() {
        int posMaxFF = -1;
        int FfMax = 100001;

        for (int i = 0; i < this.popolazione.size(); i++) {
            System.out.println("Cromosoma " + (i + 1) + ": " + this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness());
            if (this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness() < FfMax) {
                FfMax = this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
                posMaxFF = i;
            }
        }

        System.out.println("Best Cromosoma");
        System.out.println("POS: " + posMaxFF);
        System.out.println("FF Value: " + FfMax);
    }

    private void aggiornaInfoMediaFF() {
        for (int i = 0; i < this.popolazione.size(); i++) {
            actualMediaFF += this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
        }
        actualMediaFF = actualMediaFF / this.popolazione.size();

        //System.out.println("PREVMEDIA: " + prevMediaFF);
        System.out.println("ACTUALMEDIA: " + actualMediaFF);
        
        /*if (Math.abs(prevMediaFF - actualMediaFF) < 0.1) {
            iter++;
        } else {
            iter = 0;
            prevMediaFF = actualMediaFF;
        }*/

        
    }
}
