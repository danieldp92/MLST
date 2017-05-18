/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;

/**
 *
 * @author Rhobar
 */
public class Colore {
    private int colore;
    private ArrayList<Integer> indiciArchiCollegati;
    
    public Colore (int pColore) {
        this.colore = pColore;
        this.indiciArchiCollegati = new ArrayList<>();
    }

    public int getColore() {
        return colore;
    }

    public void setColore(int colore) {
        this.colore = colore;
    }

    public ArrayList<Integer> getIndiciArchiCollegati() {
        return indiciArchiCollegati;
    }

    public void addIndiceArcoCollegato(int indiceArcoCollegato) {
        this.indiciArchiCollegati.add(indiceArcoCollegato);
    }
    
    public int getOccorrenze () {
        return this.indiciArchiCollegati.size();
    }
    
    public boolean isUsed () {
        if (getOccorrenze() != 0)
            return true;
        
        return false;
    }
    
    
}
