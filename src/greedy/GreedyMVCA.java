package greedy;

import gestore.GestoreGrafo;
import gestore.Ricerca;
import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import grafo.MLST;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Stefano Dalla Palma
 */
public class GreedyMVCA {

    Grafo grafo;

    public GreedyMVCA(Grafo pGrafo) {
        this.grafo = pGrafo;
    }

    public MLST esegui() {
        boolean[] colori = new boolean[this.grafo.getColori().size()];

        //Grafo mlst = new Grafo(this.grafo.getNodi());
        //MLST mlst = new MLST(this.grafo.getCopiaNodi(), this.grafo.getColori().size());
        MLST mlst = new MLST(this.grafo.getNodi(), this.grafo.getColori().size());
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        Ricerca ricerca = new Ricerca(mlst);
        boolean[] bfsArray = ricerca.bfsArray(mlst.getNodo(0));
        int j = 0;
        while (j < bfsArray.length && bfsArray[j]) {
            j++;
        }

        if (j == bfsArray.length) {
            System.out.println("Connesso = true");
        }

//H sottografo ristretto ai nodi in V e archi con colore pColore dove E(C)={e∈E:L(e)∈C}E(C)={e∈E:L(e)∈C};
        //Sia Comp(C) il numero di componenti connesse di H=(V,E(C)));
        //colori.add(new Colore(coloreIniziale));      //aggiungo il primo colore
        HashSet<Colore> coloriTotali = new HashSet(this.grafo.getColori());

        do {
            ArrayList<Grafo> componenti = new ArrayList();
            //trova sottografi con colore c
            int cstar = -1;

            long inizio = System.currentTimeMillis();
            double media = 0;
            for (Colore colore : coloriTotali) {
                //if (!mlst.getColori().contains(colore)) {
                long inizio2 = System.currentTimeMillis();
                if (!colori[colore.getColore()]) {
                    ArrayList<Grafo> componentiConnesse = gestoreMlst.getComponentiConnesse(colore);

                    if (componenti.isEmpty()) {
                        componenti = componentiConnesse;
                        cstar = colore.getColore();
                    } else if (componenti.size() > componentiConnesse.size()) {
                        componenti = componentiConnesse;    //Minor numero di componenti
                        cstar = colore.getColore();
                    }
                }
                media += (System.currentTimeMillis() - inizio2);
            }
            System.out.println("Componenti connesse 2: " + gestoreMlst.getComponentiConnesse().size());

            System.out.println("Tempo calcolo componenti connesse per colore " + (System.currentTimeMillis() - inizio));
            System.out.println("Tempo medio per iterata: " + (media / this.grafo.getColori().size()));
            colori[cstar] = true;

            inizio = System.currentTimeMillis();
            //Provare ad ordinare le componenti per numero di colori crescente
            int i = 0;
            while (mlst.getArchi().size() != mlst.dimensione() && i < componenti.size()) {
                for (Arco arco : componenti.get(i++).getArchi()) {
                    gestoreMlst.addArcoSenzaInserireCicli(arco);
                }
            }
            System.out.println("Tempo aggiunta archi " + (System.currentTimeMillis() - inizio));
            System.out.println("Componenti connesse: " + gestoreMlst.getComponentiConnesse().size());

        } while (gestoreMlst.getComponentiConnesse().size() > 1);

        /*for (int i = 0; i < colori.length; i++) {
        if (colori[i]) {
        mlst.addColore(new Colore(i));
        }
        }*/
        return mlst;
    }

}
