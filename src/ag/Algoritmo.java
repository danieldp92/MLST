/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

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
        
        int iterateUguali = 0;
        double prevMediaFF = 0;
        double actualMediaFF = 0;
        
        while (iterateUguali < 10 && generazione++ < 20000) {
            System.out.println("Iterata " + generazione);
            valutaPopolazione();
            
            //Selezione
            ArrayList<Cromosoma> listaGenitori = this.selection.MySelezionePerRiproduzione(this.popolazione);
            
            
            //Crossover
            ArrayList<Cromosoma> listaFigli = this.crossover.myCrossoverOttimizzato(listaGenitori);
            
            //Aggiungo alla popolazione i nuovi figli
            this.popolazione.getCromosomi().addAll(listaFigli);
            //valutaPopolazione();
            /*
            //Selezione dei restanti cromosomi (10)
            ArrayList<Cromosoma> restantiCromosomiDaInserire = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int index = (int) (Math.random() * popolazione.getCromosomi().size());
                restantiCromosomiDaInserire.add(popolazione.getCromosomi().remove(index));
            }
            
            //Creazione nuova popolazione
            ArrayList<Cromosoma> nuovaPopolazione = new ArrayList<>();
            nuovaPopolazione.addAll(listaGenitori);
            nuovaPopolazione.addAll(listaFigli);
            */
            
            //Mutazione
            mutazione.myMutazione(this.popolazione);
            
            //popolazione.setCromosomi(nuovaPopolazione);
            valutaPopolazione();
            
            
            //Selezione per sopravvivenza
            ArrayList<Cromosoma> sopravvissuti = selection.mySelezionePerSopravvivenza(popolazione);
            popolazione.setCromosomi(sopravvissuti);
            
            for (int i = 0; i < popolazione.size(); i++) {
                actualMediaFF += popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
            }
            actualMediaFF = actualMediaFF / popolazione.size();
            
            if (actualMediaFF == prevMediaFF)
                iterateUguali++;
            else
                iterateUguali = 0;
            
            prevMediaFF = actualMediaFF;
            actualMediaFF = 0;
        }
        
        valutaPopolazione();
        
        return popolazione;
    }
    
    public void valutaPopolazione () {
        int fitnessCromosoma = 0;
        int i = 0;
        for (Cromosoma cromosoma : this.popolazione.getCromosomi()) {
            fitnessCromosoma = valutaCromosoma(cromosoma);
            cromosoma.setValoreFunzioneDiFitness(fitnessCromosoma);
        }
    }
    
    /*
    public int valutaCromosoma (Cromosoma cromosoma) {
        int valoreDiFitness = 0;
        
        for (Integer i : cromosoma)
            valoreDiFitness += i;
        
        return valoreDiFitness;
    }*/
    
    public int valutaCromosoma (Cromosoma cromosoma) {
        int valoreDiFitness = 0;
        GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosoma);
        GrafoColorato mlst = gestoreCromosoma.getMLSTdaCromosoma();
        GestoreGrafo gestoreMLST = new GestoreGrafo(mlst);
        
        if (gestoreMLST.connesso()) {
            //Se è connesso, la funzione di fitness non viene penalizzata
            for (Integer i : cromosoma)
                valoreDiFitness += i;
        } else {
            valoreDiFitness = cromosoma.size() + (2 * mlst.getTotaleSottoComponenti());
        }
        
        return valoreDiFitness;
    }
}
