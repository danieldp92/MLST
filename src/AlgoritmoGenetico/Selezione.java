package AlgoritmoGenetico;

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
    
    public ArrayList<Cromosoma> mySelezionePerRiproduzione(Popolazione popolazione) {
        int sizeCromosoma = this.impostazioni.sizeCromosoma;

        ArrayList<Integer> indiceCromosomiSelezionati = new ArrayList<>();
        ArrayList<Cromosoma> listaCromosomiSelezionati = new ArrayList<>();
        ArrayList<Double> probabilitaDiSelezione;

        //50 coppie
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
        
        return listaCromosomiSelezionati;
    }
    
    public ArrayList<Cromosoma> mySelezionePerSopravvivenza (Popolazione popolazione) {
        int sizeCromosoma = this.impostazioni.sizeCromosoma;

        ArrayList<Cromosoma> listaCromosomiSelezionati = new ArrayList<>();
        ArrayList<Double> probabilitaDiSelezione;

        for (int i = 0; i < this.impostazioni.sizePopolazione; i++) {
            probabilitaDiSelezione = getProbabilitaDiSelezione(popolazione);
            
            int index = 0;
            double random = Math.random();

            while (random > probabilitaDiSelezione.get(index)) {
                index++;
            }
            
            //Aumento il valore di fitness del cromosoma, per diminuire 
            //la probabilità che venga scelto nuovamente
            popolazione.getCromosoma(index).incrementaValoreFunzioneDiFitness(sizeCromosoma/20);
            
            //Inserisco la coppia nel vettore degli indici da selezionare
            listaCromosomiSelezionati.add(popolazione.getCromosoma(index));
        }
        
        for (Cromosoma cromosoma : listaCromosomiSelezionati)
            cromosoma.ripristinaValoreFunzioneDiFitness();
        
        return listaCromosomiSelezionati;
    }
    
    private ArrayList<Double> getProbabilitaDiSelezione (Popolazione popolazione) {
        int sizeCromosoma = this.impostazioni.sizeCromosoma;
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
    
}
