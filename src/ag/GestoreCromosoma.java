/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.Colore;
import grafo.GrafoColorato;
import java.io.File;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class GestoreCromosoma {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    private Cromosoma cromosoma;

    public GestoreCromosoma(Cromosoma cromosoma) {
        this.impostazioni = new Impostazioni();
        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));

        this.cromosoma = cromosoma;
    }

    public void aggiornaCromosoma(Cromosoma cromosoma) {
        this.cromosoma = cromosoma;
    }

    public GrafoColorato getMLSTdaCromosoma() {
        GrafoColorato mlst = new GrafoColorato(this.grafo.getNodi(), this.grafo.getColori().size());

        //Vettore ricorrenze colori per ogni arco
        int[] coloriPresiPerArco = new int[this.grafo.getArchi().size()];
        for (int i = 0; i < this.grafo.getArchi().size(); i++) {
            coloriPresiPerArco[i] = 0;
        }
        
        //Controllo quanti colori, associati ad ogni arco, vengono presi
        for (int i = 0; i < this.cromosoma.size(); i++) {
            if (this.cromosoma.get(i) == 1) {
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
        
        return mlst;
    }
    
    public Cromosoma getCromosomaValidato() {
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
        
        return cromosoma;
    }
}
