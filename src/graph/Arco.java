/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;



public class Arco {
   Nodo da,a;
   int numColors;
   int index;
   ArrayList<Integer> colors;

   public Arco(int pIndex, Nodo da, Nodo a) {
      this.da = da;
      this.a = a;
      this.numColors = 0;
      this.index = pIndex;
      this.colors = new ArrayList<>();
   }
   
   public Arco(Nodo da, Nodo a, ArrayList<Integer> pColors) {
      this.da = da;
      this.a = a;
      
      if (pColors != null)
          this.numColors = pColors.size();
      else
          this.numColors = 0;
      
      this.colors = pColors;
   }
   
   //Set an edge with random colors
   public Arco(int pIndex, Nodo da, Nodo a, int pNumColors) {
      this.da = da;
      this.a = a;
      this.numColors = pNumColors;
      this.index = pIndex;
      this.colors = randomColors();
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

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    public void removeColor (int pColor) {
       this.colors.set(pColor, 0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
}
