/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.GrafoColorato;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        //this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));
        this.mlst = new GrafoColorato(this.grafo.getNodi(), this.grafo.getColori().size());

        this.cromosoma = cromosoma;
    }

    public void aggiornaCromosoma(Cromosoma cromosoma) {
        this.cromosoma = cromosoma;
        //this.grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + this.impostazioni.nomeGrafo));

        creaGrafo(cromosoma);
    }

    public GrafoColorato getGrafoDaCromosoma() {
        return this.mlst;
    }

    public Cromosoma getNuovoCromosomaDaPartenzaNonAmmissibile(ArrayList<Integer> listaColori) {
        GestoreCromosoma gestoreFiglio = new GestoreCromosoma(this.cromosoma);
        GrafoColorato mlst = gestoreFiglio.getGrafoDaCromosoma();
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        if (!gestoreMlst.connesso()) {
            int inizio = 0;
            int fine = listaColori.size();

            Cromosoma tmpFiglio = new Cromosoma();
            tmpFiglio.addAll(this.cromosoma);
            List<Integer> subUnione;

            if (fine == 1) {
                this.cromosoma.add(listaColori.remove(0));
            } else {
                //Prova a vedere se, inserendo tutta la lista colori, si ottiene
                //una soluzione ammissibile
                tmpFiglio.addAll(listaColori);

                gestoreFiglio.aggiornaCromosoma(tmpFiglio);
                mlst = gestoreFiglio.getGrafoDaCromosoma();
                gestoreMlst.aggiornaGrafo(mlst);

                if (!gestoreMlst.connesso()) {
                    this.cromosoma.addAll(listaColori);
                    listaColori.clear();

                    return this.cromosoma;
                } else {
                    tmpFiglio.clear();
                    
                    while ((fine - inizio) > 1) {
                        int indiceDiFine = (inizio + fine) / 2;
                        subUnione = listaColori.subList(0, indiceDiFine);

                        tmpFiglio.addAll(subUnione);

                        gestoreFiglio.aggiornaCromosoma(tmpFiglio);
                        mlst = gestoreFiglio.getGrafoDaCromosoma();
                        gestoreMlst.aggiornaGrafo(mlst);

                        if (gestoreMlst.connesso()) {
                            fine = indiceDiFine;
                        } else {
                            inizio = indiceDiFine;
                        }

                        tmpFiglio.clear();
                    }

                    subUnione = listaColori.subList(0, fine);

                    this.cromosoma.addAll(subUnione);

                    gestoreFiglio.aggiornaCromosoma(this.cromosoma);
                    mlst = gestoreFiglio.getGrafoDaCromosoma();
                    gestoreMlst.aggiornaGrafo(mlst);
                }
            }
        }

        return this.cromosoma;
    }

    private void creaGrafo(Cromosoma cromosoma) {
        this.mlst = new GrafoColorato(this.grafo.getNodi(), this.grafo.getListaColori().size());
        int[] coloriUsatiPerArco = new int[this.grafo.getArchi().size()];

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
                    this.mlst.addArco(indiceArco, arco);
                }
            }
            indiceGene++;
        }
    }
    
    public GrafoColorato getGrafoIndotto(Cromosoma cromosoma) {
        return null;
    }

}
