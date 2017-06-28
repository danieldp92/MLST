/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Daniel
 */
public class Cromosoma extends ArrayList<Integer> {
    
    private int valoreDiFitness;
    private int valoreDiFitnessDiBackup;
    private int vecchiaia;
    private ArrayList<Integer> coloriNonPresentiNeiGenitori; //Ordinati per ricorrenza: dal più alto al più basso
    
    public Cromosoma () { 
        super();
        this.valoreDiFitness = 0;
        this.valoreDiFitnessDiBackup = 0;
        this.vecchiaia = 0;
        this.coloriNonPresentiNeiGenitori = null;
    }
    
    public int getValoreFunzioneDiFitness () {
        return this.valoreDiFitness;
    }
    
    public void setValoreFunzioneDiFitness (int ff) {
        this.valoreDiFitness = ff;
        this.valoreDiFitnessDiBackup = ff;
    }
    
    public void incrementaValoreFunzioneDiFitness (int incremento) {
        this.valoreDiFitness += incremento;
    }
    
    public void ripristinaValoreFunzioneDiFitness () {
        this.valoreDiFitness = this.valoreDiFitnessDiBackup;
    }

    public ArrayList<Integer> getColoriNonPresentiNeiGenitori() {
        return coloriNonPresentiNeiGenitori;
    }

    public void setColoriNonPresentiNeiGenitori(ArrayList<Integer> coloriNonPresentiNeiGenitori) {
        this.coloriNonPresentiNeiGenitori = coloriNonPresentiNeiGenitori;
    }
    
    public int getVecchiaia() {
        return this.vecchiaia;
    }
    
    public void incrementaVecchiaia() {
        this.vecchiaia++;
    }
    
    public void respawn() {
        this.vecchiaia = 0;
    }
    
    public boolean èFiglio() {
        if (this.coloriNonPresentiNeiGenitori != null)
            return true;
        return false;
    }
    
    public Cromosoma clone() {
        Cromosoma cromosoma = new Cromosoma();
        cromosoma.addAll(this);
        cromosoma.setValoreFunzioneDiFitness(this.valoreDiFitness);
        cromosoma.setColoriNonPresentiNeiGenitori(this.coloriNonPresentiNeiGenitori);
        
        return cromosoma;
    }
}
