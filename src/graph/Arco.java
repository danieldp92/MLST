/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.Objects;



public class Arco {
   Nodo da,a;
   int numColors;
   ArrayList<Integer> colori;

   public Arco(Nodo da, Nodo a) {
      this.da = da;
      this.a = a;
      this.numColors = 0;
      this.colori = new ArrayList<>();
   }
   
   public Arco(Nodo da, Nodo a, ArrayList<Integer> pColors) {
      this.da = da;
      this.a = a;
      
      if (pColors != null)
          this.numColors = pColors.size();
      else
          this.numColors = 0;
      
      this.colori = pColors;
   }
   
   //Set an edge with random colori
   public Arco(Nodo da, Nodo a, int pNumColors) {
      this.da = da;
      this.a = a;
      this.numColors = pNumColors;
      this.colori = randomColors();
   }

   public Nodo getDa() {
      return da;
   }

   public void setDa(Nodo da) {
      this.da = da;
   }

   public Nodo getA() {
      return a;
   }
   
   public void setA(Nodo a) {
      this.a = a;
   }

    public ArrayList<Integer> getColori() {
        return colori;
    }

    public void setColori(ArrayList<Integer> colori) {
        this.colori = colori;
    }

    public void rimuoviColore (int pColore) {
       this.colori.remove(Integer.valueOf(pColore));
    }

    private ArrayList<Integer> randomColors () {
        boolean atLeastOneColor = false;
        double random = 0;
        ArrayList<Integer> randomColorList = new ArrayList<>();
        
        for (int i = 0; i < this.numColors; i++) {
            random = Math.random();
            
            if (random < 0.5)
                randomColorList.add(0);
            else {
                randomColorList.add(1);
                atLeastOneColor = true;
            }
        }
        
        if (!atLeastOneColor) {
            int indexToChange = (int)(Math.random() * this.numColors);
            randomColorList.set(indexToChange, 1);
        }
        
        return randomColorList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Arco other = (Arco) obj;
        if (this.numColors != other.numColors) {
            return false;
        }
        if (!Objects.equals(this.da, other.da)) {
            return false;
        }
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.colori, other.colori)) {
            return false;
        }
        return true;
    }
    
    
}
