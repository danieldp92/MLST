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
    
    private Impostazioni impostazioni;
    
    public Crossover () {
        this.impostazioni = new Impostazioni();
    }
    
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
    
    public ArrayList<Cromosoma> myCrossoverOttimizzato (ArrayList<Cromosoma> listaCoppie) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        
        for (int i = 0; i < listaCoppie.size(); i+=2) {
            Cromosoma genitore1 = listaCoppie.get(i);
            Cromosoma genitore2 = listaCoppie.get(i+1);
            Cromosoma figlio = new Cromosoma();
            
            //Genero un figlio con 1 massimi, in modo da cercare di ridurre le sottocomponenti
            if (genitore1.getValoreFunzioneDiFitness() > this.impostazioni.sizeCromosoma &&
                    genitore2.getValoreFunzioneDiFitness() > this.impostazioni.sizeCromosoma) {
                for (int j = 0; j < this.impostazioni.sizeCromosoma; j++) {
                    if (genitore1.get(j) == 0 && genitore2.get(j) == 0)
                        figlio.add(0);
                    else
                        figlio.add(1);
                }
                
                figli.add(figlio);
            }
            //Entrambi i genitori sono validi: genero un figlio, in cui minimizzo gli 1
            else if (genitore1.getValoreFunzioneDiFitness() <= this.impostazioni.sizeCromosoma &&
                    genitore2.getValoreFunzioneDiFitness() <= this.impostazioni.sizeCromosoma) {
                for (int j = 0; j < this.impostazioni.sizeCromosoma; j++) {
                    if (genitore1.get(j) == 1 && genitore2.get(j) == 1)
                        figlio.add(1);
                    else
                        figlio.add(0);
                }
                
                figli.add(figlio);
            }
            //Un genitore valido e uno no: genero 2 figli, uno in cui minimizzo gli 1 per minimizzare i colori e uno 
            //in cui massimizz gli 1
            else {
                //Massimizzo gli 1
                for (int j = 0; j < this.impostazioni.sizeCromosoma; j++) {
                    if (genitore1.get(j) == 0 && genitore2.get(j) == 0)
                        figlio.add(0);
                    else
                        figlio.add(1);
                }
                
                figli.add(figlio);
    
                figlio = new Cromosoma();
                //Minimizzo gli 1
                for (int j = 0; j < this.impostazioni.sizeCromosoma; j++) {
                    if (genitore1.get(j) == 1 && genitore2.get(j) == 1)
                        figlio.add(1);
                    else
                        figlio.add(0);
                }
                
                figli.add(figlio);
            }
        }
        
        return figli;
    }
    
    public ArrayList<Cromosoma> myCrossoverOttimizzato2 (ArrayList<Cromosoma> listaCoppie) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        
        for (int i = 0; i < listaCoppie.size(); i+=2) {
            Cromosoma genitore1 = listaCoppie.get(i);
            Cromosoma genitore2 = listaCoppie.get(i+1);
            Cromosoma figlio = new Cromosoma();
            
            for (int j = 0; j < genitore1.size(); j++) {
                if (j < genitore1.size()/2)
                    figlio.add(genitore1.get(j));
                else
                    figlio.add(genitore2.get(j));
            }
            
            figli.add(figlio);
        }
        
        return figli;
    }
}
