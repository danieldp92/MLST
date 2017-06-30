package VecchioAlgoritmoGenetico;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Selezione {

    private Impostazioni impostazioni;

    public Selezione() {
        this.impostazioni = new Impostazioni();
    }

    public ArrayList<Cromosoma> selezionePerRiproduzione(Popolazione popolazione) {
        ArrayList<Cromosoma> popolazioneOrdinataPerFitness = getListaCromosomiOrdinatiPerFitness(popolazione.getCromosomi());

        ArrayList<Cromosoma> genitori = new ArrayList<>();
        genitori.add(popolazioneOrdinataPerFitness.get(0));

        for (int i = 1; i < popolazioneOrdinataPerFitness.size(); i++) {
            genitori.add(popolazioneOrdinataPerFitness.get(i));
            int indiceCromosomaRandom = (int) (Math.random() * i);

            indiceCromosomaRandom = (int) (Math.random() * i);
            
            genitori.add(popolazioneOrdinataPerFitness.get(indiceCromosomaRandom));
        }

        return genitori;
    }

    public ArrayList<Cromosoma> selezionePerSopravvivenza(Popolazione popolazione) {
        ArrayList<Cromosoma> sopravvissuti = new ArrayList<>();

        //Lista cromosomi ordinati per fitness
        ArrayList<Cromosoma> cromosomiOrdinatiPerFitness = getListaCromosomiOrdinatiPerFitness(popolazione.getCromosomi());

        ArrayList<Cromosoma> stessaFitness = new ArrayList<>();
        ArrayList<Cromosoma> residui = new ArrayList<>();

        ArrayList<ArrayList<Cromosoma>> gruppiDiFitness = new ArrayList<>();

        //Gruppi di fitness
        while (!cromosomiOrdinatiPerFitness.isEmpty()) {
            ArrayList<Cromosoma> tmpLista = new ArrayList<>();
            int valoreAttualeDiFitness = cromosomiOrdinatiPerFitness.get(0).getValoreFunzioneDiFitness();

            tmpLista.add(cromosomiOrdinatiPerFitness.remove(0));

            while (!cromosomiOrdinatiPerFitness.isEmpty()
                    && cromosomiOrdinatiPerFitness.get(0).getValoreFunzioneDiFitness() == valoreAttualeDiFitness) {
                tmpLista.add(cromosomiOrdinatiPerFitness.remove(0));
            }

            tmpLista = getListaCromosomiOrdinatiPerVecchiaia(tmpLista);

            //Aggiungo i cromosomi con vecchiaia 10 o superiore ai residui
            for (int j = 0; j < tmpLista.size(); j++) {
                if (tmpLista.get(j).getVecchiaia() >= 10) {
                    residui.add(tmpLista.remove(j--));
                }
            }

            gruppiDiFitness.add(tmpLista);
        }

        int i = 0;
        while (i < this.impostazioni.sizePopolazione) {
            ArrayList<Cromosoma> cromosomiDaAggiungere = gruppiDiFitness.remove(0);

            if ((i + cromosomiDaAggiungere.size()) < this.impostazioni.sizePopolazione) {
                sopravvissuti.addAll(cromosomiDaAggiungere);
                i += cromosomiDaAggiungere.size();
            } else {
                while (i < this.impostazioni.sizePopolazione) {
                    sopravvissuti.add(cromosomiDaAggiungere.remove(0));
                    i++;
                }
            }
        }

        return sopravvissuti;
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
            popolazione.getCromosoma(primo).incrementaValoreFunzioneDiFitness(sizeCromosoma / 10);
            popolazione.getCromosoma(secondo).incrementaValoreFunzioneDiFitness(sizeCromosoma / 10);

            //Inserisco la coppia nel vettore degli indici da selezionare
            listaCromosomiSelezionati.add(popolazione.getCromosoma(primo));
            listaCromosomiSelezionati.add(popolazione.getCromosoma(secondo));

            indiceCromosomiSelezionati.add(primo);
            indiceCromosomiSelezionati.add(secondo);
        }

        for (Cromosoma cromosoma : listaCromosomiSelezionati) {
            cromosoma.ripristinaValoreFunzioneDiFitness();
        }

        return listaCromosomiSelezionati;
    }

    public ArrayList<Cromosoma> mySelezionePerSopravvivenza(Popolazione popolazione) {
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
            popolazione.getCromosoma(index).incrementaValoreFunzioneDiFitness(sizeCromosoma / 20);

            //Inserisco la coppia nel vettore degli indici da selezionare
            listaCromosomiSelezionati.add(popolazione.getCromosoma(index));
        }

        for (Cromosoma cromosoma : listaCromosomiSelezionati) {
            cromosoma.ripristinaValoreFunzioneDiFitness();
        }

        return listaCromosomiSelezionati;
    }

    private ArrayList<Double> getProbabilitaDiSelezione(Popolazione popolazione) {
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

    //Dal più giocvane al più vecchio
    private ArrayList<Cromosoma> getListaCromosomiOrdinatiPerVecchiaia(ArrayList<Cromosoma> cromosomiPopolazione) {
        ArrayList<Cromosoma> cromosomi = new ArrayList<>();
        cromosomi.addAll(cromosomiPopolazione);

        ArrayList<Cromosoma> popolazioneOrdinata = new ArrayList<>();

        int posMaxCromosoma;
        int minVecchiaia;

        while (!cromosomi.isEmpty()) {
            posMaxCromosoma = -1;
            minVecchiaia = this.impostazioni.maxValutazioni + 1;
            for (int i = 0; i < cromosomi.size(); i++) {
                if (cromosomi.get(i).getVecchiaia() < minVecchiaia) {
                    minVecchiaia = cromosomi.get(i).getVecchiaia();
                    posMaxCromosoma = i;
                }
            }
            popolazioneOrdinata.add(cromosomi.remove(posMaxCromosoma));
        }

        return popolazioneOrdinata;
    }

    //Dal valore di fitness più alto al più basso
    private ArrayList<Cromosoma> getListaCromosomiOrdinatiPerFitness(ArrayList<Cromosoma> cromosomiPopolazione) {
        ArrayList<Cromosoma> cromosomi = new ArrayList<>();
        cromosomi.addAll(cromosomiPopolazione);

        ArrayList<Cromosoma> popolazioneOrdinata = new ArrayList<>();

        int posMaxCromosoma;
        int minFF;

        while (!cromosomi.isEmpty()) {
            posMaxCromosoma = -1;
            minFF = this.impostazioni.sizeCromosoma + 1;
            for (int i = 0; i < cromosomi.size(); i++) {
                if (cromosomi.get(i).getValoreFunzioneDiFitness() < minFF) {
                    minFF = cromosomi.get(i).getValoreFunzioneDiFitness();
                    posMaxCromosoma = i;
                }
            }
            popolazioneOrdinata.add(cromosomi.remove(posMaxCromosoma));
        }

        return popolazioneOrdinata;
    }
}
