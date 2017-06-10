/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Selezione {
    private Impostazioni impostazioni;
    
    public Selezione() {
        this.impostazioni = new Impostazioni();
    }

    public ArrayList<Cromosoma> MySelezione(Popolazione popolazione) {
        int sizeCromosoma = popolazione.cromosomi.get(0).size();

        ArrayList<Integer> indiceCromosomiSelezionati = new ArrayList<>();
        ArrayList<Cromosoma> listaCromosomiSelezionati = new ArrayList<>();
        ArrayList<Double> probabilitaDiSelezione;

        //30 coppie
        for (int i = 0; i < 30; i++) {
            probabilitaDiSelezione = getProbabilitaDiSelezione(popolazione);
            
            int index = 0;
            double random = Math.random();

            //Primo genitore
            while (random > probabilitaDiSelezione.get(index)) {
                index++;
            }
            
            int primo = index;

            //Secondo genitore
            int secondo = 0;
            do {
                random = Math.random();
                index = 0;
                
                while (random > probabilitaDiSelezione.get(index)) {
                    index++;
                }
                
                secondo = index;
            } while (secondo == primo);
            
            //Aumento il valore di fitness dei due genitori, per diminuire 
            //la probabilità che vengano scelti nuovamente
            popolazione.getCromosoma(primo).incrementaValoreFunzioneDiFitness(sizeCromosoma/20);
            popolazione.getCromosoma(secondo).incrementaValoreFunzioneDiFitness(sizeCromosoma/20);
            
            //Inserisco la coppia nel vettore degli indici da selezionare
            listaCromosomiSelezionati.add(popolazione.getCromosoma(primo));
            listaCromosomiSelezionati.add(popolazione.getCromosoma(secondo));
            
            indiceCromosomiSelezionati.add(primo);
            indiceCromosomiSelezionati.add(secondo);
        }
        
        for (Cromosoma cromosoma : listaCromosomiSelezionati)
            cromosoma.ripristinaValoreFunzioneDiFitness();
        
        //Elimino i cromosomi scelti dalla popolazione
        for (Integer i : indiceCromosomiSelezionati)
            popolazione.getCromosomi().set(i, null);
        for (int i = 0; i < popolazione.getCromosomi().size(); i++)
            if (popolazione.getCromosoma(i) == null)
                popolazione.getCromosomi().remove(i--);
        
        return listaCromosomiSelezionati;
    }
    
    public ArrayList<Cromosoma> MySelezionePerRiproduzione(Popolazione popolazione) {
        int sizeCromosoma = popolazione.cromosomi.get(0).size();

        ArrayList<Integer> indiceCromosomiSelezionati = new ArrayList<>();
        ArrayList<Cromosoma> listaCromosomiSelezionati = new ArrayList<>();
        ArrayList<Double> probabilitaDiSelezione;

        //30 coppie
        for (int i = 0; i < 50; i++) {
            probabilitaDiSelezione = getProbabilitaDiSelezione(popolazione);
            
            int index = 0;
            double random = Math.random();

            //Primo genitore
            while (random > probabilitaDiSelezione.get(index)) {
                index++;
            }
            
            int primo = index;

            //Secondo genitore
            int secondo = 0;
            do {
                random = Math.random();
                index = 0;
                
                while (random > probabilitaDiSelezione.get(index)) {
                    index++;
                }
                
                secondo = index;
            } while (secondo == primo);
            
            //Aumento il valore di fitness dei due genitori, per diminuire 
            //la probabilità che vengano scelti nuovamente
            popolazione.getCromosoma(primo).incrementaValoreFunzioneDiFitness(sizeCromosoma/20);
            popolazione.getCromosoma(secondo).incrementaValoreFunzioneDiFitness(sizeCromosoma/20);
            
            //Inserisco la coppia nel vettore degli indici da selezionare
            listaCromosomiSelezionati.add(popolazione.getCromosoma(primo));
            listaCromosomiSelezionati.add(popolazione.getCromosoma(secondo));
            
            indiceCromosomiSelezionati.add(primo);
            indiceCromosomiSelezionati.add(secondo);
        }
        
        for (Cromosoma cromosoma : listaCromosomiSelezionati)
            cromosoma.ripristinaValoreFunzioneDiFitness();
        
        //Elimino i cromosomi scelti dalla popolazione
        for (Integer i : indiceCromosomiSelezionati)
            popolazione.getCromosomi().set(i, null);
        for (int i = 0; i < popolazione.getCromosomi().size(); i++)
            if (popolazione.getCromosoma(i) == null)
                popolazione.getCromosomi().remove(i--);
        
        return listaCromosomiSelezionati;
    }
    
    private ArrayList<Cromosoma> mySelezionePerSopravvivenza (Popolazione popolazione) {
        ArrayList<Cromosoma> cromosomiSopravvissuti = new ArrayList<>();
        
        ArrayList<Integer> listaFFCromosomi = new ArrayList<>();
        for (int i = 0; i < popolazione.getCromosomi().size(); i++)
            listaFFCromosomi.add(popolazione.getCromosoma(i).getValoreFunzioneDiFitness());
        
        ArrayList<Integer> listaIndiciOrdinata = getIndiciDelleFFOrdinati(listaFFCromosomi);
        
        for (int i = 0; i < this.impostazioni.sizePopolazione; i++)
            cromosomiSopravvissuti.add(popolazione.getCromosoma(listaIndiciOrdinata.get(i)));
        
        return cromosomiSopravvissuti;
    }
    
    private ArrayList<Double> getProbabilitaDiSelezione (Popolazione popolazione) {
        int sizeCromosoma = popolazione.cromosomi.get(0).size();

        ArrayList<Double> probabilitaDiSelezione = new ArrayList<>();

        //Somma ff
        int sommaFF = 0;
        for (int i = 0; i < popolazione.size(); i++) {
            sommaFF += popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
        }

        sommaFF = (sizeCromosoma * popolazione.size()) - sommaFF;

        double probabilità = 0;
        for (Cromosoma cromosoma : popolazione.getCromosomi()) {
            probabilità += (double) (sizeCromosoma - cromosoma.getValoreFunzioneDiFitness()) / sommaFF;
            probabilitaDiSelezione.add(probabilità);
        }
        
        return probabilitaDiSelezione;
    }
    
    private ArrayList<Integer> getIndiciDelleFFOrdinati (ArrayList<Integer> lista) {
        ArrayList<Integer> listaIndici = new ArrayList<>();
        int daOrdinare = lista.size();
        
        while (daOrdinare > 0) {
            int posMax = -1;
            int max = -1;
            
            for (int i = 0; i < daOrdinare; i++) {
                if (lista.get(i) > max) {
                    max = lista.get(i);
                    posMax = i;
                }
            }
            
            lista.remove(posMax);
            listaIndici.add(posMax);
            
            daOrdinare--;
        }
        
        return listaIndici;
    }
}
