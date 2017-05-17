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

    public Grafo esegui() {
        long inizio = System.currentTimeMillis();
        Grafo mlst = new Grafo(grafo.getNodi());
        mlst.clear();
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        ArrayList<Arco> archiTmp = grafo.getCopiaArchi();

        ArrayList<Arco> edgeWithMinColors;
        ArrayList<Arco> archiOriginali = new ArrayList<>();
        int m = 1;

        
        long time = 0;
        long timeRecuperoArchiConColoreMinimo = 0;
        long timeRecuperoArchiConColoreMinimoCount = 0;
        long timeDeterminazioneColorePiuRicorrente = 0;
        long timeDeterminazioneColorePiuRicorrenteCount = 0;
        long timeRimozioneColorePiuRicorrente = 0;
        long timeRimozioneColorePiuRicorrenteCount = 0;
        long timeInserimentoArchiSenzaCiclo = 0;
        long timeInserimentoArchiSenzaCicloCount = 0;
        
        //Ciclo, fin quando mlst non e' connesso
        while (!gestoreMlst.connesso()) {
            System.out.println("Iterata " + m++ + ": " + (System.currentTimeMillis() - inizio));
            //Prendi gli archi con il minor numero di colori 
            edgeWithMinColors = getEdgesWithMinNumberOfColors(archiTmp);
            
            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {
                
                for (Arco a : edgeWithMinColors) {

                    Arco originale = grafo.getArco(a.getDa(), a.getA());
                    archiOriginali.add(originale);
                    //mlst.addArco(originale);

                    //grafo.rimuoviArco(a);
                    archiTmp.remove(a);
                }

                time = System.currentTimeMillis();
                gestoreMlst.addArchiSenzaInserireCicli(archiOriginali);
                timeInserimentoArchiSenzaCiclo += System.currentTimeMillis() - time;
                timeInserimentoArchiSenzaCicloCount++;
                System.out.println("Tempo di inserimento archi senza ciclo: " + (System.currentTimeMillis() - time));
                
                archiOriginali.clear();

            } else {
                time = System.currentTimeMillis();
                //Determino il colore più ricorrente in edgeWithMinColors
                int mostCommonColor = mostCommonColor(edgeWithMinColors);
                timeDeterminazioneColorePiuRicorrente += System.currentTimeMillis() - time;
                timeDeterminazioneColorePiuRicorrenteCount++;
                System.out.println("Tempo di determinazione colore più ricorrente: " + (System.currentTimeMillis() - time));
                
                //Elimino il colore dagli archi (temporanei)
                for (Arco arco : archiTmp) {
                    arco.rimuoviColore(mostCommonColor);
                }
            }
        }

        double meanTimeIterate = (System.currentTimeMillis() - inizio) / --m;
        double meanTimeDeterminazioneColorePiuRicorrente = timeDeterminazioneColorePiuRicorrente / timeDeterminazioneColorePiuRicorrenteCount;
        double meanTimeInserimentoArchiSenzaCiclo = timeInserimentoArchiSenzaCiclo / timeInserimentoArchiSenzaCicloCount;
        
        System.out.println();
        System.out.println("Numero Iterate: " + m);
        System.out.println("Media tempo iterata: " + meanTimeIterate);
        System.out.println("Media determinazione colore più ricorrente: " + meanTimeDeterminazioneColorePiuRicorrente);
        System.out.println("Media inserimento archi senza ciclo: " + meanTimeInserimentoArchiSenzaCiclo);
        
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
                prevSumColor = sumColor;
                //System.out.println("Colore dominante");
            }
            
            sumColor = 0;
        }
        return mostCommonColor;
    }
}
