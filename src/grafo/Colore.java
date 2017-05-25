/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.util.ArrayList;

/**
 *
 * @author Rhobar
 */
public class Colore {
    private int id;
    private ArrayList<Integer> indiciArchiCollegati;
    
    public Colore (int pColore) {
        this.id = pColore;
        this.indiciArchiCollegati = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public boolean usato () {
        if (getOccorrenze() != 0)
            return true;
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Colore other = (Colore) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
}
