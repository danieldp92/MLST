package greedy;

import gestore.GestoreGrafo;
import graph.Arco;
import graph.Grafo;
import java.util.ArrayList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Greedy {
    
    Grafo grafo;

    public Greedy(Grafo grafo) {
        this.grafo = grafo;
    }
    
    public Grafo esegui(){
        Grafo mlst = new Grafo(grafo.getNodi());
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        ArrayList<Arco> archiTmp = grafo.getCopiaArchi();
        
        ArrayList<Arco> edgeWithMinColors;
        int m = 1;

        //Ciclo, fin quando mlst non e' connesso
        while (!gestoreMlst.connesso()) {
            //Prendi gli archi con il minor numero di colori 
            edgeWithMinColors = getEdgesWithMinNumberOfColors(archiTmp);

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {
               
                for (Arco a : edgeWithMinColors) {

                    //mlst.getArco(a).setColore(grafo.getColore(a)
                    Arco originale = grafo.getArco(a.getDa(), a.getA());
                    mlst.addArco(originale);

                    //grafo.rimuoviArco(a);
                    archiTmp.remove(a);
                }

            } else {
                //Determino il colore più ricorrente in edgeWithMinColors
                int mostCommonColor = mostCommonColor(edgeWithMinColors);
                
                //Elimino il colore dagli archi (temporanei)
                for (Arco arco : archiTmp) {
                    arco.rimuoviColore(mostCommonColor);
                }
            }
        }
        
        return mlst;
    }
    
     private ArrayList<Arco> getEdgesWithMinNumberOfColors(ArrayList<Arco> pEdges) {
        ArrayList<Arco> edgesWithMinColors = new ArrayList<>();
        int previousTotColorEdge = -1;
        int totColorForEdge = -1;
        for (Arco a : pEdges) {
            if (a != null) {
                totColorForEdge = a.getColori().size();
                //Primo arco analizzato
                if (previousTotColorEdge == -1) {
                    previousTotColorEdge = totColorForEdge;
                    edgesWithMinColors.add(a);
                } else //Successivi
                {
                    if (totColorForEdge < previousTotColorEdge) {
                        previousTotColorEdge = totColorForEdge;
                        //Svuoto la lista degli archi minori,
                        //poichè ho trovato un nuovo candidato
                        edgesWithMinColors.clear();
                        edgesWithMinColors.add(a);
                    } else if (previousTotColorEdge == totColorForEdge) {
                        edgesWithMinColors.add(a);
                    }
                }
            }
        }
        return edgesWithMinColors;
    }

    private int mostCommonColor(ArrayList<Arco> pEdges) {
        int mostCommonColor = -1;
        int prevSumColor = -1;
        int sumColor = 0;
        int index = 0;
        for (int i = 0; i < grafo.getColori().size(); i++) {
            for (Arco a : pEdges) {
                index = 0;
                do {
                    if (a.getColori().get(index) == i) {
                        sumColor++;
                    }
                } while (a.getColori().get(index) != i && ++index < a.getColori().size());
            }
            //System.out.println("Totale colore " + i + ": " + sumColor);
            if (sumColor > prevSumColor) {
                mostCommonColor = i;
                //System.out.println("Colore dominante");
            }
            prevSumColor = sumColor;
            sumColor = 0;
        }
        return mostCommonColor;
    }
}
