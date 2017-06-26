/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Daniel
 */
public class Crossover {
    
    private Impostazioni impostazioni;
    
    public Crossover () {
        this.impostazioni = new Impostazioni();
    }
    
    public ArrayList<Cromosoma> myCrossover (ArrayList<Cromosoma> genitori) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        
        for (int i = 0; i < genitori.size(); i+=2) {
            Cromosoma genitore1 = genitori.get(i);
            Cromosoma genitore2 = genitori.get(i+1);
            Cromosoma figlio = new Cromosoma();
            
            Cromosoma intersezione = new Cromosoma();
            for (int j = 0; j < genitore1.size(); j++) {
                int k = 0;
                boolean trovato = false;
                
                while (k < genitore2.size() && !trovato) {
                    if (genitore1.get(j) == genitore2.get(k)) {
                        intersezione.add(genitore1.get(j));
                        trovato = true;
                    }
                    k++;
                }
            }
            
            Cromosoma unione = new Cromosoma();
            
            Cromosoma tmpUnione = new Cromosoma();
            tmpUnione.addAll(genitore1);
            tmpUnione.addAll(genitore2);
            
            while (!tmpUnione.isEmpty()) {
                int primoElemento = tmpUnione.get(0);
                tmpUnione.removeAll(Arrays.asList(primoElemento));
                unione.add(primoElemento);
            }
            
            for (int j : intersezione)
                unione.remove(Integer.valueOf(j));
            
            //Fai il crossover
            
            //Parto dal figlio con l'intersezione dei genitori
            figlio.addAll(intersezione);
            
            //Creo un cromosoma valido
            GestoreCromosoma gestoreFiglio = new GestoreCromosoma(figlio);
            GrafoColorato mlst = gestoreFiglio.getMLSTdaCromosoma();
            GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
            
            while (!gestoreMlst.connesso()) {
                int indiceColoreRandomDaUnione = (int) (Math.random() * unione.size());
                int coloreRandomDaUnione = unione.remove(indiceColoreRandomDaUnione);
                
                figlio.add(coloreRandomDaUnione);
                gestoreFiglio.aggiornaCromosoma(figlio);
                mlst = gestoreFiglio.getMLSTdaCromosoma();
                gestoreMlst.aggiornaGrafo(mlst);
            }
            
            figli.add(figlio);
        }
        
        return figli;
    }
}
