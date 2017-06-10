/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

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
        valutaPopolazione();
        int generazione = 0;
        
        while (generazione++ < 1000) {
            //Selezione
            ArrayList<Cromosoma> listaGenitori = this.selection.MySelezionePerRiproduzione(this.popolazione);
            
            
            //Crossover
            ArrayList<Cromosoma> listaFigli = this.crossover.myCrossover2(listaGenitori);
            System.out.println("Iterata " + generazione);
            
            /*Selezione dei restanti cromosomi (10)
            ArrayList<Cromosoma> restantiCromosomiDaInserire = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int index = (int) (Math.random() * popolazione.getCromosomi().size());
                restantiCromosomiDaInserire.add(popolazione.getCromosomi().remove(index));
            }*/
            
            //Creazione nuova popolazione
            ArrayList<Cromosoma> nuovaPopolazione = new ArrayList<>();
            nuovaPopolazione.addAll(listaGenitori);
            nuovaPopolazione.addAll(listaFigli);
            
            //Mutazione
            mutazione.myMutazione(nuovaPopolazione);
            
            popolazione.setCromosomi(nuovaPopolazione);
            valutaPopolazione();
            
            
            //Selezione per sopravvivenza
            ArrayList<Cromosoma> sopravvissuti = selection.MySelezionePerRiproduzione(popolazione);
            popolazione.setCromosomi(sopravvissuti);
            valutaPopolazione();
        }
        
        return popolazione;
    }
    
    public void valutaPopolazione () {
        int fitnessCromosoma = 0;
        
        for (Cromosoma cromosoma : this.popolazione.getCromosomi()) {
            fitnessCromosoma = valutaCromosoma(cromosoma);
            cromosoma.setValoreFunzioneDiFitness(fitnessCromosoma);
        }
    }
    
    public int valutaCromosoma (Cromosoma cromosoma) {
        int valoreDiFitness = 0;
        
        for (Integer i : cromosoma)
            valoreDiFitness += i;
        
        return valoreDiFitness;
    }
}
