package greedy;

import graph.Arco;
import graph.Grafo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Stefano Dalla Palma
 */
public class GreedyKrumke {

    Grafo grafo;

    public GreedyKrumke(Grafo pGrafo) {
        this.grafo = pGrafo;
    }

   /* public void esegui() {
        Set colori = new HashSet();
        Grafo mlst = new Grafo();
        
        do {
            int coloreCheMinimizzaNumeroComponenti = 0;
            int minorNumeroComponenti = -1;
            ArrayList<Grafo> componenti = new ArrayList();

            Iterator iterator = grafo.getColori().iterator();
           
            while (iterator.hasNext()) {
                int colore = (int) iterator.next();
                
                if (colori.contains(colore)) {
                    continue;
                }

                ArrayList<Grafo> connectedComponents = grafo.getConnectedComponents(colore);
                int numeroComponenti = connectedComponents.size();

                if (minorNumeroComponenti == -1) {
                    minorNumeroComponenti = numeroComponenti;
                    coloreCheMinimizzaNumeroComponenti = colore;
                    componenti = connectedComponents;
                } else if (numeroComponenti < minorNumeroComponenti) {
                    minorNumeroComponenti = numeroComponenti;
                    coloreCheMinimizzaNumeroComponenti = colore;
                    componenti = connectedComponents;
                }
            }

            System.out.println("colore: "+coloreCheMinimizzaNumeroComponenti);
            
            //Aggiungi nuove componenti al sottografo
            for (Grafo componente : componenti) {
                for (Arco arco : componente.getArchi()) {
                    //Aggiungi l'arco e i suoi nodi al sottografo
                    if(!mlst.getArchi().contains(arco)){
                        mlst.addArco(arco);
                        if(!mlst.getNodi().contains(arco.getDa())){
                            mlst.addNodo(arco.getDa());
                        }
                        if(!mlst.getNodi().contains(arco.getA())){
                            mlst.addNodo(arco.getA());
                        }
                    }
                }
            }
            
            //mlst.addColore(coloreCheMinimizzaNumeroComponenti);
            colori.add(coloreCheMinimizzaNumeroComponenti);

        } while (!mlst.dfs());    //finchè il sottografo è connesso

        if (mlst.getNodi().size() == grafo.getNodi().size()) {

            System.out.println("MLST: \n");
            System.out.println("Numero colori: " + colori.size() + "\n");
            System.out.println("Colori: " + colori.toString() + "\n");
            mlst.stampa();
            mlst.stampaMatriceColori();

        }
    }*/

}
