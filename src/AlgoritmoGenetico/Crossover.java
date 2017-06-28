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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class Crossover {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    public Crossover(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();

        this.grafo = grafo;
    }
    
    public ArrayList<Cromosoma> crossover(ArrayList<Cromosoma> genitori) {
        ArrayList<Cromosoma> figli = new ArrayList<>();
        
        Cromosoma figlio = new Cromosoma();
        ArrayList<Integer> coloriNonPresentiNeiGenitori = new ArrayList<>();
        figlio.addAll(genitori.get(0));
        figlio.setColoriNonPresentiNeiGenitori(coloriNonPresentiNeiGenitori);
        
        figli.add(figlio);
        
        figlio = null;
        coloriNonPresentiNeiGenitori = null;
        
        
        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.sizeCromosoma; i++) {
            listaColori.add(i);
        }

        for (int i = 1; i < genitori.size(); i += 2) {
            Cromosoma genitore1 = genitori.get(i);
            Cromosoma genitore2 = genitori.get(i + 1);
            figlio = new Cromosoma();

            //Array che contiene i colori che non sono presenti nei genitori
            coloriNonPresentiNeiGenitori = new ArrayList<>(listaColori);

            //Array intersezione
            ArrayList<Integer> intersezione = new ArrayList<Integer>();
            intersezione.addAll(genitore1);
            intersezione.retainAll(genitore2);

            Set<Integer> tmpUnione = new HashSet<>();
            tmpUnione.addAll(genitore1);
            tmpUnione.addAll(genitore2);

            coloriNonPresentiNeiGenitori.removeAll(tmpUnione);

            tmpUnione.removeAll(intersezione);

            //Array unione
            ArrayList<Integer> unione = new ArrayList<>();
            unione.addAll(tmpUnione);

            //Sort
            ordinaColoriPerRicorrenza(unione);

            //Fai il crossover
            //Parto dal figlio con l'intersezione dei genitori
            figlio.addAll(intersezione);

            //Creo un cromosoma valido
            GestoreCromosoma gestoreFiglio = new GestoreCromosoma(figlio);
            GrafoColorato mlst = gestoreFiglio.getGrafoDaCromosoma();
            GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

            if (!gestoreMlst.connesso()) {
                int inizio = 0;
                int fine = unione.size();
                
                Cromosoma tmpFiglio = new Cromosoma();
                List<Integer> subUnione;

                if (fine == 1) {
                    figlio.add(unione.get(0));
                } else {
                    while((fine - inizio) > 1) {
                        int indiceDiFine = (inizio + fine) / 2;
                        subUnione = unione.subList(0, indiceDiFine);
                    
                        tmpFiglio.addAll(subUnione);
                    
                        gestoreFiglio.aggiornaCromosoma(tmpFiglio);
                        mlst = gestoreFiglio.getGrafoDaCromosoma();
                        gestoreMlst.aggiornaGrafo(mlst);
                    
                        if (gestoreMlst.connesso()) {
                            fine = indiceDiFine;
                        } else {
                            inizio = indiceDiFine;
                        }
                    }
                
                    subUnione = unione.subList(0, fine);
                    
                    figlio.addAll(subUnione);
                    
                    gestoreFiglio.aggiornaCromosoma(figlio);
                    mlst = gestoreFiglio.getGrafoDaCromosoma();
                    gestoreMlst.aggiornaGrafo(mlst);
                    
                    if (!gestoreMlst.connesso()) {
                        System.out.println("ERRORE");
                        System.err.println("ERRORE");
                    }
                        
                }
                
            }
            
            /*while (!gestoreMlst.connesso()) {
                int coloreDaUnione = unione.remove(0);

                figlio.add(coloreDaUnione);
                gestoreFiglio.aggiornaCromosoma(figlio);
                mlst = gestoreFiglio.getGrafoDaCromosoma();
                gestoreMlst.aggiornaGrafo(mlst);
            }*/

            ordinaColoriPerRicorrenza(coloriNonPresentiNeiGenitori);
            figlio.setColoriNonPresentiNeiGenitori(coloriNonPresentiNeiGenitori);
            figli.add(figlio);
        }

        return figli;
    }
    
    

    public ArrayList<Cromosoma> myCrossover(ArrayList<Cromosoma> genitori) {
        ArrayList<Cromosoma> figli = new ArrayList<>();

        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.sizeCromosoma; i++) {
            listaColori.add(i);
        }

        for (int i = 0; i < genitori.size(); i += 2) {
            Cromosoma genitore1 = genitori.get(i);
            Cromosoma genitore2 = genitori.get(i + 1);
            Cromosoma figlio = new Cromosoma();

            //Array che contiene i colori che non sono presenti nei genitori
            ArrayList<Integer> coloriNonPresentiNeiGenitori = new ArrayList<>(listaColori);

            //Array intersezione
            ArrayList<Integer> intersezione = new ArrayList<Integer>();
            intersezione.addAll(genitore1);
            intersezione.retainAll(genitore2);

            Set<Integer> tmpUnione = new HashSet<>();
            tmpUnione.addAll(genitore1);
            tmpUnione.addAll(genitore2);

            coloriNonPresentiNeiGenitori.removeAll(tmpUnione);

            tmpUnione.removeAll(intersezione);

            //Array unione
            ArrayList<Integer> unione = new ArrayList<>();
            unione.addAll(tmpUnione);

            //Sort
            ordinaColoriPerRicorrenza(unione);

            //Fai il crossover
            //Parto dal figlio con l'intersezione dei genitori
            figlio.addAll(intersezione);

            //Creo un cromosoma valido
            GestoreCromosoma gestoreFiglio = new GestoreCromosoma(figlio);
            GrafoColorato mlst = gestoreFiglio.getGrafoDaCromosoma();
            GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

            while (!gestoreMlst.connesso()) {
                int coloreDaUnione = unione.remove(0);

                figlio.add(coloreDaUnione);
                gestoreFiglio.aggiornaCromosoma(figlio);
                mlst = gestoreFiglio.getGrafoDaCromosoma();
                gestoreMlst.aggiornaGrafo(mlst);
            }

            ordinaColoriPerRicorrenza(coloriNonPresentiNeiGenitori);
            figlio.setColoriNonPresentiNeiGenitori(coloriNonPresentiNeiGenitori);
            figli.add(figlio);
        }

        return figli;
    }

    private void ordinaColoriPerRicorrenza(ArrayList<Integer> listaColori) {
        //Sort
        Collections.sort(listaColori, new Comparator<Integer>() {
            LinkedHashMap<Integer, Integer> listaColoriOrdinatiPerRicorrenza = grafo.getListaColoriOrdinataPerRicorrenza();

            @Override
            public int compare(Integer oolore1, Integer oolore2) {
                return listaColoriOrdinatiPerRicorrenza.get(oolore2).
                        compareTo(listaColoriOrdinatiPerRicorrenza.get(oolore1));
            }

        });
    }
}
