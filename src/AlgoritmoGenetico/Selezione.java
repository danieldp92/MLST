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

        for (int i = 0; i < this.impostazioni.sizePopolazione; i++)
            sopravvissuti.add(cromosomiOrdinatiPerFitness.get(i));

        return sopravvissuti;
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
