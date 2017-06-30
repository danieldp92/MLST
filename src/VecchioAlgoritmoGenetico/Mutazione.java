/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VecchioAlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class Mutazione {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    public Mutazione(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();
        this.grafo = grafo;
    }
    
    public void myMutazione(Popolazione popolazione) {
        for (int i = 0; i < popolazione.size(); i++) {
            if (Math.random() < this.impostazioni.mutationRate) {
                System.out.println("MUTAZIONE CROMOSOMA " + (i+1));
                
                Cromosoma cromosomaDaMutare = popolazione.getCromosoma(i).clone();
                int posPrimoColoreMenoFrequente;
                int posSecondoColoreMenoFrequente;
                int primoColoreMenoFrequente = 0;
                int secondoColoreMenoFrequente = 0;
                
                //Prendo il colore con meno frequenza
                posPrimoColoreMenoFrequente = getPosizioneColoreMenoFrequente(cromosomaDaMutare);
                primoColoreMenoFrequente = cromosomaDaMutare.remove(posPrimoColoreMenoFrequente);
                
                //Ottengo l'mlst
                GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosomaDaMutare);
                GrafoColorato mlst = gestoreCromosoma.getGrafoDaCromosoma();
                GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
                
                if (gestoreMlst.connesso()) {
                    //Rimozione colore -> mutazione avvenuta con successo
                    popolazione.setCromosoma(i, cromosomaDaMutare);
                } else {
                    if (cromosomaDaMutare.èFiglio()) {
                        //Elimino il secondo colore meno ricorrente
                        posSecondoColoreMenoFrequente = getPosizioneColoreMenoFrequente(cromosomaDaMutare);
                        secondoColoreMenoFrequente = cromosomaDaMutare.remove(posSecondoColoreMenoFrequente);
                        
                        //Aggiungo il colore più ricorrente
                        int colorePiuRicorrente = cromosomaDaMutare.getColoriNonPresentiNeiGenitori().remove(0);
                        cromosomaDaMutare.add(colorePiuRicorrente);
                        
                        //Test di connessione
                        //Se è connesso, ho scartato due colori per aggiungerne 1 -> guadagno di 1 colore
                        //Se non è connesso, riaggiungo il secondo colore meno frequente
                        //Se non è connesso, rimuovo il secondo colore meno frequente, aggiungo il primo e riprovo
                        //Se non è ancora connesso, non muto
                        gestoreCromosoma.aggiornaCromosoma(cromosomaDaMutare);
                        mlst = gestoreCromosoma.getGrafoDaCromosoma();
                        gestoreMlst.aggiornaGrafo(mlst);
                        
                        if (gestoreMlst.connesso()) {
                            popolazione.setCromosoma(i, cromosomaDaMutare);
                        } else {
                            //Aggiungo il secondo colore meno frequente
                            cromosomaDaMutare.add(secondoColoreMenoFrequente);
                            
                            gestoreCromosoma.aggiornaCromosoma(cromosomaDaMutare);
                            mlst = gestoreCromosoma.getGrafoDaCromosoma();
                            gestoreMlst.aggiornaGrafo(mlst);
                            
                            if (gestoreMlst.connesso()) {
                                popolazione.setCromosoma(i, cromosomaDaMutare);
                            } else {
                                //Rimuovo il secondo e aggiunog il primo
                                cromosomaDaMutare.remove(Integer.valueOf(secondoColoreMenoFrequente));
                                cromosomaDaMutare.add(primoColoreMenoFrequente);
                                
                                gestoreCromosoma.aggiornaCromosoma(cromosomaDaMutare);
                                mlst = gestoreCromosoma.getGrafoDaCromosoma();
                                gestoreMlst.aggiornaGrafo(mlst);
                                
                                if (gestoreMlst.connesso()) {
                                    popolazione.setCromosoma(i, cromosomaDaMutare);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void myMutazione2(Popolazione popolazione) {
        for (int i = 0; i < popolazione.size(); i++) {
            if (Math.random() < this.impostazioni.mutationRate) {
                System.out.println("MUTAZIONE CROMOSOMA " + (i+1));
                
                Cromosoma cromosomaDaMutare = popolazione.getCromosoma(i).clone();
                int posColoreMenoFrequente;
                int coloreMenoFrequente = 0;
                
                GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosomaDaMutare);
                GrafoColorato mlst = gestoreCromosoma.getGrafoDaCromosoma();
                GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
                
                while (gestoreMlst.connesso()) {
                    //Rimuovi colore meno frequente
                    posColoreMenoFrequente = getPosizioneColoreMenoFrequente(cromosomaDaMutare);
                    coloreMenoFrequente = cromosomaDaMutare.remove(posColoreMenoFrequente);
                    
                    gestoreCromosoma.aggiornaCromosoma(cromosomaDaMutare);
                    mlst = gestoreCromosoma.getGrafoDaCromosoma();
                    gestoreMlst.aggiornaGrafo(mlst);
                }
                
                if (cromosomaDaMutare.èFiglio()) {
                    cromosomaDaMutare.add(cromosomaDaMutare.getColoriNonPresentiNeiGenitori().get(0));
                    
                    gestoreCromosoma.aggiornaCromosoma(cromosomaDaMutare);
                    mlst = gestoreCromosoma.getGrafoDaCromosoma();
                    gestoreMlst.aggiornaGrafo(mlst);
                    
                    if (!gestoreMlst.connesso())
                        cromosomaDaMutare.add(coloreMenoFrequente);
                } else {
                    cromosomaDaMutare.add(coloreMenoFrequente);
                }
                
                
                popolazione.setCromosoma(i, cromosomaDaMutare);
            }
        }
    }
    
    public void myMutazione3(ArrayList<Cromosoma> figli) {
        //Lista colori
        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.sizeCromosoma; i++) {
            listaColori.add(i);
        }

        for (int i = 0; i < figli.size(); i++) {
            if (Math.random() < this.impostazioni.mutationRate) {
                System.out.println("MUTAZIONE CROMOSOMA " + (i+1));
                
                //Colori non presenti
                ArrayList<Integer> coloriNonPresenti = new ArrayList<>(listaColori);

                Set<Integer> unioneColoriCromosoma = new HashSet<>();
                unioneColoriCromosoma.addAll(figli.get(i));
                unioneColoriCromosoma.addAll(figli.get(i).getColoriNonPresentiNeiGenitori());

                coloriNonPresenti.removeAll(unioneColoriCromosoma);

                //Colori cromosoma
                ArrayList<Integer> coloriCromosoma = new ArrayList<>();
                coloriCromosoma.addAll(figli.get(i));
                ordinaColoriPerRicorrenza(coloriCromosoma);

                Cromosoma cromosoma = new Cromosoma();
                cromosoma.addAll(figli.get(i).getColoriNonPresentiNeiGenitori());

                GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosoma);
                //GrafoColorato mlst = gestoreCromosoma.getGrafoDaCromosoma();
                //GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
                cromosoma = gestoreCromosoma.getNuovoCromosomaDaPartenzaNonAmmissibile(coloriCromosoma);
                
                gestoreCromosoma = new GestoreCromosoma(cromosoma);
                GrafoColorato mlst = gestoreCromosoma.getGrafoDaCromosoma();
                GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
                
                /*while (!gestoreMlst.connesso() && !coloriCromosoma.isEmpty()) {
                    int coloreDaInserire = coloriCromosoma.remove(0);
                    cromosoma.add(coloreDaInserire);

                    gestoreCromosoma.aggiornaCromosoma(cromosoma);
                    mlst = gestoreCromosoma.getGrafoDaCromosoma();
                    gestoreMlst.aggiornaGrafo(mlst);
                }*/
                
                if (!gestoreMlst.connesso()) {
                    ordinaColoriPerRicorrenza(coloriNonPresenti);
                    
                    cromosoma = gestoreCromosoma.getNuovoCromosomaDaPartenzaNonAmmissibile(coloriNonPresenti);
                    /*while (!gestoreMlst.connesso()) {
                        int coloreDaInserire = coloriNonPresenti.remove(0);
                        cromosoma.add(coloreDaInserire);

                        gestoreCromosoma.aggiornaCromosoma(cromosoma);
                        mlst = gestoreCromosoma.getGrafoDaCromosoma();
                        gestoreMlst.aggiornaGrafo(mlst);
                    }*/
                }
                
                figli.set(i, cromosoma);
            }

        }

    }
    
    public void STRONGMUTATION(Popolazione popolazione) {
        for (int i = 0; i < popolazione.size(); i++) {
            if (Math.random() < this.impostazioni.strongMutationRate) {
                System.out.println("STRONG MUTATION Cromosoma " + (i+1));
                
                Cromosoma cromosoma = creaCromosomaRandom();
                
                popolazione.setCromosoma(i, cromosoma);
            }
        }
    }
    
    private int getPosizioneColoreMenoFrequente (Cromosoma cromosoma) {
        LinkedHashMap<Integer, Integer> listaColoriOrdinatiPerRicorrenza = this.grafo.getListaColoriOrdinataPerRicorrenza();
        
        int posColoreMinimo = 0;
        int minOccorrenze = listaColoriOrdinatiPerRicorrenza.get(cromosoma.get(0));
        
        for (int i = 1; i < cromosoma.size(); i++) {
            if (listaColoriOrdinatiPerRicorrenza.get(cromosoma.get(i)) < minOccorrenze) {
                posColoreMinimo = i;
                minOccorrenze = listaColoriOrdinatiPerRicorrenza.get(cromosoma.get(i));
            }
        }
        
        return posColoreMinimo;
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
    
    private Cromosoma creaCromosomaRandom() {
        Cromosoma cromosoma = new Cromosoma();
        
        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.sizeCromosoma; i++)
            listaColori.add(i);
        
        GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(cromosoma);
        
        //RANDOM SHUFFLE
        long seed = System.nanoTime();
        Collections.shuffle(listaColori, new Random(seed));
        
        cromosoma = gestoreCromosoma.getNuovoCromosomaDaPartenzaNonAmmissibile(listaColori);
        /*while (!gestoreMlst.connesso()) {
            int indiceColoreDaAggiungere = (int)(Math.random() * listaColori.size());
            int coloreDaAggiungere = listaColori.remove(indiceColoreDaAggiungere);
            
            cromosoma.add(coloreDaAggiungere);
            
            gestoreCromosoma.aggiornaCromosoma(cromosoma);
            mlst = gestoreCromosoma.getGrafoDaCromosoma();
            gestoreMlst.aggiornaGrafo(mlst);
        }*/
        
        return cromosoma;
    }
}
