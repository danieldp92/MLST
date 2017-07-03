/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Cromosoma extends ArrayList<Integer> {
    
    private int valoreDiFitness;
    private int valoreDiFitnessDiBackup;
    private ArrayList<Integer> coloriGenitori;
    
    public Cromosoma () { 
        super();
        this.valoreDiFitness = 0;
        this.valoreDiFitnessDiBackup = 0;
        this.coloriGenitori = new ArrayList<>();
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

    public ArrayList<Integer> getColoriGenitori() {
        return coloriGenitori;
    }

    public void setColoriGenitori(ArrayList<Integer> coloriGenitori) {
        this.coloriGenitori = coloriGenitori;
    }
    
    public boolean èFiglio() {
        if(this.coloriGenitori.isEmpty())
            return false;
        
        return true;
    }
    
    @Override
    public Cromosoma clone() {
        Cromosoma cromosoma = new Cromosoma();
        cromosoma.addAll(this);
        cromosoma.setValoreFunzioneDiFitness(this.valoreDiFitness);
        
        return cromosoma;
    }
}
