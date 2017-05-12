/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;

/**
 *
 * @author carmine
 */
public class Nodo {
   int key;
   ArrayList<Arco> incidenti;

   public Nodo(int key) {
      this.key = key;
      this.incidenti = new ArrayList<>();
   }

   public int getKey() {
      return key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public ArrayList<Arco> getIncidenti() {
      return incidenti;
   }

   @Override
   public String toString() {
      return key+"";
   }

  
   
}
