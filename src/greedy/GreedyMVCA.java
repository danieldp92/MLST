package greedy;

import gestore.GestoreGrafo;
import graph.Arco;
import graph.Grafo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Stefano Dalla Palma
 */
public class GreedyMVCA {

    Grafo grafo;

    public GreedyMVCA(Grafo pGrafo) {
        this.grafo = pGrafo;
    }

    public void esegui() {
        Set<Integer> colori = new HashSet();
        Grafo mlst = new Grafo(this.grafo.getNodi());
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
        //H sottografo ristretto ai nodi in V e archi con colore pColore dove E(C)={e∈E:L(e)∈C}E(C)={e∈E:L(e)∈C};
        //Sia Comp(C) il numero di componenti connesse di H=(V,E(C)));

        colori.add(0);      //aggiungo il primo colore

        do {
            ArrayList<Grafo> componenti = new ArrayList();
            //trova sottografi con colore c
            for (int colore : this.grafo.getListaColori()) {
                if (!colori.contains(colore)) {
                    ArrayList<Grafo> componentiConnesse = gestoreMlst.getComponentiConnesse(colore);
                    if (componenti.size() < componentiConnesse.size()) {
                        componenti = componentiConnesse;
                    }
                }
            }

            for (Grafo componente : componenti) {
                for(Arco arco : componente.getArchi()){
                    mlst.addArco(arco);
                }
            }
        } while (gestoreMlst.getComponentiConnesse().size() > 1);

    }

}
