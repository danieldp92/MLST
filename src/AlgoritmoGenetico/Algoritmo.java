/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Algoritmo {
    public Popolazione popolazione;
    private Selezione selection;
    private Crossover crossover;
    private Mutazione mutazione;
    
    private Impostazioni impostazioni;
    
    public Algoritmo () {
        this.impostazioni = new Impostazioni();
        
        //Creo la popolazione iniziale
        this.popolazione = new Popolazione();
        
        //Operatori
        this.selection = new Selezione();
        this.crossover = new Crossover();
        this.mutazione = new Mutazione();
    }
    
    public Popolazione execute () {
        int generazione = 0;
        
        while (generazione++ < this.impostazioni.maxValutazioni) {
            System.out.println("Iterata " + generazione);
            
            //Valutazione della popolazione
            valutaPopolazione();
            
            //Selezione per riproduzione
            ArrayList<Cromosoma> genitori = this.selection.mySelezionePerRiproduzione(this.popolazione);
            
            //Crossover
            ArrayList<Cromosoma> figli = this.crossover.myCrossover(genitori);
            this.popolazione.getCromosomi().addAll(figli);
            
            //Mutazione
            //this.mutazione.myMutazione(this.popolazione);
            
            //Nuova valutazione
            valutaPopolazione();
            
            //Selezione per sopravvivenza
            ArrayList<Cromosoma> sopravvissuti = this.selection.mySelezionePerSopravvivenza(this.popolazione);
            this.popolazione.setCromosomi(sopravvissuti);
        }
        
        valutaPopolazione();
        
        int posMaxFF = -1;
        int FfMax = -1;
        
        System.out.println("FF");
        for (int i = 0; i < this.popolazione.size(); i++) {
            System.out.println("Cromosoma " + (i+1) + ": " + this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness());
            if (this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness() > FfMax) {
                FfMax = this.popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
                posMaxFF = i;
            }
        }
        
        System.out.println("Best Cromosoma");
        System.out.println("POS: " + posMaxFF);
        System.out.println("FF Value: " + FfMax);
        
        return popolazione;
    }
    
    public void valutaPopolazione () {
        for (Cromosoma cromosoma : this.popolazione.getCromosomi())
            cromosoma.setValoreFunzioneDiFitness(valutaCromosoma(cromosoma));
    }
    
    public int valutaCromosoma (Cromosoma cromosoma) {
        return cromosoma.size();
    }
}
