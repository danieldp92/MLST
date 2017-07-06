/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Popolazione {

    ArrayList<Cromosoma> cromosomi;

    private Impostazioni impostazioni;
    private GrafoColorato grafo;

    public Popolazione(GrafoColorato grafo) {
        this.impostazioni = new Impostazioni();
        this.grafo = grafo;

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
    
    public void setCromosoma(int index, Cromosoma cromosoma) {
        this.cromosomi.set(index, cromosoma);
    }

    public int size() {
        return this.cromosomi.size();
    }

    /**
     * Funzione che crea, in maniera random, la popolazione iniziale Bisogna
     * stare attenti alla validità di un cromosoma: ogni cromosoma deve
     * rappresentare una soluzione valida
     */
    private void creaPopolazioneIniziale() {
        ArrayList<Integer> listaColori = new ArrayList<>();
        for (int i = 0; i < this.impostazioni.getNumeroColori(); i++)
            listaColori.add(i);
        
        for (int i = 0; i < this.impostazioni.getPopolazione(); i++) {
            Cromosoma cromosoma = new Cromosoma();
            GrafoColorato mlst = new GrafoColorato(this.grafo.getNodi(), this.impostazioni.getNumeroColori());
            GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
            ArrayList<Integer> copiaListaColori = new ArrayList<>(listaColori);
            int [] ricorrenzeColoriInArchi = new int[this.grafo.getArchi().size()];
            
            while (!gestoreMlst.connesso()) {
                int indiceColoreDaInserire = (int)(Math.random() * copiaListaColori.size());
                int coloreDaInserire = copiaListaColori.remove(indiceColoreDaInserire);
                
                ArrayList<Integer> listaArchiAssociatiAColore = this.grafo.getColore(coloreDaInserire).getIndiciArchiCollegati();
                for (int j : listaArchiAssociatiAColore) {
                    ricorrenzeColoriInArchi[j]++;
                    
                    if (ricorrenzeColoriInArchi[j] == 3)
                        mlst.addArco(j, this.grafo.getArco(j));
                }
                    
            
                cromosoma.add(coloreDaInserire);
            }
            
            this.cromosomi.add(cromosoma);
        }
    }
}
