/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Daniel
 */
public class Crossover {
    
    public Crossover () {}
    
    public ArrayList<Cromosoma> myCrossover (ArrayList<Cromosoma> listaCoppie) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        GestoreCromosoma gC;
        
        for (int i = 0; i < listaCoppie.size(); i+=2) {
            Cromosoma genitore1 = listaCoppie.get(i);
            Cromosoma genitore2 = listaCoppie.get(i+1);
            Cromosoma figlio = new Cromosoma();
            
            //Crossover atto a generare l'individuo più performante
            for (int j = 0; j < genitore1.size(); j++) {
                if (genitore1.get(j) == 1 && genitore2.get(j) == 1)
                    figlio.add(1);
                else
                    figlio.add(0);
            }
            
            figlio = new GestoreCromosoma(figlio).getCromosomaValidato();
            
            figli.add(figlio);
        }
        
        return figli;
    }
    
    public ArrayList<Cromosoma> myCrossover2 (ArrayList<Cromosoma> listaCoppie) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        GestoreCromosoma gestoreCromosoma;
        GestoreGrafo gestoreMLST;
        
        int andLogico = 0;
        int orLogico = 0;
        
        for (int i = 0; i < listaCoppie.size(); i+=2) {
            Cromosoma genitore1 = listaCoppie.get(i);
            Cromosoma genitore2 = listaCoppie.get(i+1);
            Cromosoma figlio = new Cromosoma();
            
            //Crossover atto a generare l'individuo più performante
            for (int j = 0; j < genitore1.size(); j++) {
                andLogico = genitore1.get(j) * genitore2.get(j);
                
                if (andLogico == 1)
                    figlio.add(1);
                else
                    figlio.add(0);
            }
            
            gestoreCromosoma = new GestoreCromosoma(figlio);
            gestoreMLST = new GestoreGrafo(gestoreCromosoma.getMLSTdaCromosoma());
            
            ArrayList<Integer> codaIndiciDaMutare = new ArrayList<>();
            boolean primaIterata = true;
            
            while (!gestoreMLST.connesso()) {
                
                if (primaIterata) {
                    //Prendo la lista di tutti gli indici di figlio che hanno come valore 0,
                    //derivante da un and logico in cui almeno uno dei genitori avesse 1
                    for (int j = 0; j < figlio.size(); j++) {
                        if (figlio.get(j) == 0) {
                            orLogico = genitore1.get(j) + genitore2.get(j);
                            
                            if (orLogico == 1)
                                codaIndiciDaMutare.add(j);
                        }
                    }
                    
                    primaIterata = false;
                }
                
                int indiceRandom = (int) (Math.random() * codaIndiciDaMutare.size());
                int indiceDaMutare = codaIndiciDaMutare.remove(indiceRandom);
                
                figlio.set(indiceDaMutare, 1);
                gestoreCromosoma.aggiornaCromosoma(figlio);
                gestoreMLST.aggiornaGrafo(gestoreCromosoma.getMLSTdaCromosoma());
            }
            
            figli.add(figlio);
        }
        
        return figli;
    }
}
