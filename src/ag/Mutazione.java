/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import gestore.GestoreGrafo;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Mutazione {

    private Impostazioni impostazioni;

    public Mutazione() {
        this.impostazioni = new Impostazioni();
    }

    public void myMutazione(ArrayList<Cromosoma> cromosomi) {
        ArrayList<Cromosoma> cromosomiMutati = new ArrayList<>();

        GestoreCromosoma gestoreCromosoma = new GestoreCromosoma(null);
        GestoreGrafo gestoreMLST = new GestoreGrafo(null);

        for (int i = 0; i < cromosomi.size(); i++) {
            //Avvia il processo di mutazione
            if (Math.random() < this.impostazioni.mutationRate) {
                System.out.println("MUTAZIONE DEL CROMOSOMA " + (i + 1));

                //Generazione lista indici random
                ArrayList<Integer> listaRandomIndiciDaMutare = listaIndiciRandomDaMutare();

                //Muto
                for (int j = 0; j < listaRandomIndiciDaMutare.size(); j++) {
                    if (cromosomi.get(i).get(listaRandomIndiciDaMutare.get(j)) == 1) {
                        cromosomi.get(i).set(listaRandomIndiciDaMutare.get(j), 0);
                    } else {
                        cromosomi.get(i).set(listaRandomIndiciDaMutare.get(j), 1);
                    }
                }

                gestoreCromosoma.aggiornaCromosoma(cromosomi.get(i));
                gestoreMLST.aggiornaGrafo(gestoreCromosoma.getMLSTdaCromosoma());
                
                while (!listaRandomIndiciDaMutare.isEmpty() && !gestoreMLST.connesso()) {
                    //Reset di metà indici
                    int halfSize = (listaRandomIndiciDaMutare.size()/2);
                    if (listaRandomIndiciDaMutare.size() % 2 == 1) 
                        halfSize += 1;
                    
                    for (int j = 0; j < halfSize; j++) {
                        int indiceElementoDaResettare = listaRandomIndiciDaMutare.remove((int)(Math.random() * listaRandomIndiciDaMutare.size()));
                        if (cromosomi.get(i).get(indiceElementoDaResettare) == 1)
                            cromosomi.get(i).set(indiceElementoDaResettare, 0);
                        else
                            cromosomi.get(i).set(indiceElementoDaResettare, 1);
                    }
                    
                    gestoreCromosoma.aggiornaCromosoma(cromosomi.get(i));
                    gestoreMLST.aggiornaGrafo(gestoreCromosoma.getMLSTdaCromosoma());
                }
            }
        }
    }

    private ArrayList<Integer> listaIndiciRandomDaMutare() {
        ArrayList<Integer> listaIndiciRandomDaMutare = new ArrayList<>();
        int sizeIndiciDaMutare = this.impostazioni.sizeCromosoma / 10;

        int j;
        for (j = 0; j < this.impostazioni.sizeCromosoma; j++) {
            listaIndiciRandomDaMutare.add(j);
        }

        while (j > 0) {
            //Inserimento in coda
            if (j > (this.impostazioni.sizeCromosoma - sizeIndiciDaMutare)) {
                int elementoDaInserireInCoda = listaIndiciRandomDaMutare.remove((int) (Math.random() * j));
                listaIndiciRandomDaMutare.add(elementoDaInserireInCoda);
            } else {
                listaIndiciRandomDaMutare.remove(0);
            }

            j--;
        }

        return listaIndiciRandomDaMutare;
    }
}
