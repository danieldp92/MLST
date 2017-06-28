/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GeneratoreGrafo;
import grafo.Arco;
import grafo.GrafoColorato;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class GestoreCromosoma {

    private Impostazioni impostazioni;
    private GrafoColorato grafo;
    private GrafoColorato mlst;

    private Cromosoma cromosoma;

    public GestoreCromosoma(Cromosoma cromosoma) {
        this.impostazioni = new Impostazioni();
        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));
        this.mlst = new GrafoColorato(this.grafo.getNodi(), this.grafo.getColori().size());
        creaGrafo(cromosoma);

        this.cromosoma = cromosoma;
    }

    public void aggiornaCromosoma(Cromosoma cromosoma) {
        this.cromosoma = cromosoma;
        this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));

        creaGrafo(cromosoma);
    }

    public GrafoColorato getGrafoDaCromosoma() {
        return this.mlst;
    }

    private void creaGrafo(Cromosoma cromosoma) {
        int [] coloriUsatiPerArco = new int[this.grafo.getArchi().size()];
        
        int indiceGene = 0;
        int colore = 0;

        while (indiceGene < cromosoma.size()) {
            //Preleva archi relativi al colore i-esimo
            colore = cromosoma.get(indiceGene);
            ArrayList<Integer> listaArchi = this.grafo.getColore(colore).getIndiciArchiCollegati();

            for (Integer indiceArco : listaArchi) {
                Arco arco = this.grafo.getArco(indiceArco);
                coloriUsatiPerArco[indiceArco]++;
                if (coloriUsatiPerArco[indiceArco] == arco.getColori().size()) {
                    mlst.addArco(indiceArco, arco);
                }
            }
            indiceGene++;
        }
    }
}
