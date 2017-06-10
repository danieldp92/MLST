/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.GrafoColorato;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Popolazione {

    ArrayList<Cromosoma> cromosomi;

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    public Popolazione() {
        this.impostazioni = new Impostazioni();
        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));

        this.cromosomi = new ArrayList<>();

        creaPopolazioneIniziale();
    }

    public ArrayList<Cromosoma> getCromosomi() {
        return cromosomi;
    }

    public Cromosoma getCromosoma(int index) {
        return this.cromosomi.get(index);
    }

    public void setCromosomi(ArrayList<Cromosoma> cromosomi) {
        this.cromosomi = cromosomi;
    }
    
    public int size () {
        return this.cromosomi.size();
    }

    /**
     * Funzione che crea, in maniera random, la popolazione iniziale Bisogna
     * stare attenti alla validità di un cromosoma: ogni cromosoma deve
     * rappresentare una soluzione valida
     */
    private void creaPopolazioneIniziale() {
        //Creazione popolazione random
        Random random = new Random(1);
        for (int i = 0; i < this.impostazioni.sizePopolazione; i++) {
            Cromosoma tmpCromosoma = new Cromosoma();

            for (int j = 0; j < this.impostazioni.sizeCromosoma; j++) {
                if (random.nextDouble() < 0.5) {
                    tmpCromosoma.add(0);
                } else {
                    tmpCromosoma.add(1);
                }
            }

            this.cromosomi.add(tmpCromosoma);
        }

        //Check di validità di ogni cromosoma
        for (Cromosoma cromosoma : this.cromosomi) {
            validaCromosoma(cromosoma);
        }
    }

    public void validaCromosoma(Cromosoma cromosoma) {
        //Vettore ricorrenze colori per ogni arco
        int[] coloriPresiPerArco = new int[this.grafo.getArchi().size()];
        for (int i = 0; i < this.grafo.getArchi().size(); i++) {
            coloriPresiPerArco[i] = 0;
        }

        //Creo l'mlst relativo al cromosoma
        GrafoColorato mlst = new GrafoColorato(this.grafo.getNodi(), this.grafo.getColori().size());
        GestoreGrafo gestoreMLST = new GestoreGrafo(mlst);

        //Controllo quanti colori, associati ad ogni arco, vengono presi
        for (int i = 0; i < cromosoma.size(); i++) {
            if (cromosoma.get(i) == 1) {
                Colore tmpColore = this.grafo.getColore(i);
                for (Integer indiceArco : tmpColore.getIndiciArchiCollegati()) {
                    coloriPresiPerArco[indiceArco]++;
                }
            }
        }

        //Inserisco nell'mlst solo gli archi i cui colori sono stati tutti selezionati
        for (int i = 0; i < coloriPresiPerArco.length; i++) {
            if (coloriPresiPerArco[i] == this.grafo.getArco(i).getColori().size()) {
                mlst.addArco(i, this.grafo.getArco(i));
            }
        }

        //Ciclo finché non ho un cromosoma valido
        Random random = new Random(1);
        while (!gestoreMLST.connesso()) {
            //Muto un colore settato a 0 ad 1, al fine di trovare un cromosoma valido
            boolean mutato = false;
            int coloreMutato = 0;
            while (!mutato) {
                coloreMutato = (int) (random.nextDouble() * cromosoma.size());
                if (cromosoma.get(coloreMutato) == 0) {
                    cromosoma.set(coloreMutato, 1);
                    mutato = true;
                }
            }

            //Aggiorno i colori presi per ogni arco
            Colore tmpColore = this.grafo.getColore(coloreMutato);
            for (Integer indiceArco : tmpColore.getIndiciArchiCollegati()) {
                coloriPresiPerArco[indiceArco]++;
                
                if (coloriPresiPerArco[indiceArco] == this.grafo.getArco(indiceArco).getColori().size())
                    mlst.addArco(indiceArco, this.grafo.getArco(indiceArco));
            }
        }
    }
}
