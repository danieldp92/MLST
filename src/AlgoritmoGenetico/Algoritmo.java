/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import gestore.XlsGrafo;
import grafo.Arco;
import grafo.GrafoColorato;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.getNomeGrafo()));

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
        int maxValutazioni = this.impostazioni.getMaxValutazioni();
        long start = System.currentTimeMillis();
        long time;
        
        while (this.generazione++ < maxValutazioni && iter < 300 && (System.currentTimeMillis() - start) < 1200000){
            System.out.println("ITERATA " + generazione);
            System.out.println("ITER " + iter);

            //Valutazione iniziale della popolazione
            valutaPopolazione();
            
            time = System.currentTimeMillis();
            //Selezione per riproduzione
            ArrayList<Cromosoma> genitori = this.selection.selezionePerRiproduzione(this.popolazione);
            System.out.println("TEMPO SELEZIONE RIPRODUZIONE: " + (double)(System.currentTimeMillis()-time) / 1000);
            
            time = System.currentTimeMillis();
            //Crossover
            ArrayList<Cromosoma> figli = this.crossover.multiThreadCrossover(genitori);
            System.out.println("TEMPO CROSSOVER: " + (double)(System.currentTimeMillis()-time) / 1000);

            //Mutazione
            time = System.currentTimeMillis();
            this.mutazione.multiThreadMutazione(figli);
            System.out.println("TEMPO MUTAZIONE: " + (double)(System.currentTimeMillis()-time) / 1000);
            this.popolazione.getCromosomi().addAll(figli);

            //Nuova valutazione
            valutaPopolazione();

            //Selezione per sopravvivenza
            time = System.currentTimeMillis();
            ArrayList<Cromosoma> sopravvissuti = this.selection.selezionePerSopravvivenza(this.popolazione);
            this.popolazione.setCromosomi(sopravvissuti);
            System.out.println("TEMPO SELEZIONE SOPRAVVIVENZA: " + (double)(System.currentTimeMillis()-time) / 1000);
            
            aggiornaInfoMediaFF();
        }

        valutaPopolazione();
        
        /*
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
        
        
        
        
        
        
        
        GestoreCromosoma gC = new GestoreCromosoma();
        GrafoColorato mlst = gC.getGrafoDaCromosoma(this.popolazione.getCromosoma(posMaxFF));
        GestoreGrafo gG = new GestoreGrafo(mlst);
        System.out.println("Connesso: " + gG.connesso());
        Set<Integer> totColori = new HashSet<>();
        for (Map.Entry<Integer, Arco> en : mlst.getArchi().entrySet()) {
            Arco arco = en.getValue();
            totColori.addAll(arco.getColori());
            System.out.print(arco.getDa() + " " + arco.getA() + " Colori:");
            for (int colore : arco.getColori())
                System.out.print(" " + colore);
            System.out.print("\n");
            
        }
        Set<Integer> colori = new HashSet<>();
        for (int i = 0; i < 50; i++)
            colori.add(i);
        colori.removeAll(totColori);
        System.out.println("TOT COLORI: " + totColori.size());
        System.out.println("COLORI RESTANTI: " + colori.size());
        colori.forEach(i -> {
            System.out.println(i);
        });
        
        
        
        */
        
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
        int FfMax = 10000001;

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
        actualMediaFF = 0;
        for (int i = 0; i < this.popolazione.size(); i++) {
            actualMediaFF += this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
        }
        actualMediaFF = actualMediaFF / this.popolazione.size();

        System.out.println("PREVMEDIA: " + prevMediaFF);
        System.out.println("ACTUALMEDIA: " + actualMediaFF);
        
        if (Math.abs(prevMediaFF - actualMediaFF) < 0.01) {
            iter++;
        } else {
            iter = 0;
            prevMediaFF = actualMediaFF;
        }

        
    }
}
