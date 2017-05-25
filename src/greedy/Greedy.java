package greedy;

import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.GrafoColorato;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Greedy {

    GrafoColorato grafo;
    Statistiche statistiche;

    public Greedy(GrafoColorato grafo) {
        this.grafo = grafo;
        this.statistiche = new Statistiche();
    }

    public GrafoColorato esegui() {
        long inizio = System.currentTimeMillis();
        GrafoColorato mlst = new GrafoColorato(grafo.getNodi(), grafo.getListaColori().size());
        GrafoColorato tmpGrafo = this.grafo.clona();

        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        ArrayList<Arco> edgeWithMinColors;
        ArrayList<Integer> indexOfEdgeWithMinColors;

        //Ciclo, fin quando mlst non e' connesso
        long tempo = 0;
        while (!gestoreMlst.connesso()) {
            ++statistiche.iter;
            tempo = System.currentTimeMillis();

            //Prelevo gli indici degli archi con colore minimo, poichè le operazioni effettuate su un arraylist 
            //con gli indici (get, set) hanno complessità O(1), diversamente dalle operazioni effettuate con gli oggetti
            indexOfEdgeWithMinColors = getIndexOfEdgesWithMinNumberOfColors(tmpGrafo.getArchi());

            //Prendo gli archi con il minor numero di colori
            edgeWithMinColors = getEdgesWithMinNumberOfColors(tmpGrafo.getArchi(), indexOfEdgeWithMinColors);

            statistiche.tempoRecuperoArchiConColoreMinimo += System.currentTimeMillis() - tempo;
            statistiche.tempoRecuperoArchiConColoreMinimoCount++;

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {

                tempo = System.currentTimeMillis();

                for (int i : indexOfEdgeWithMinColors) {

                    Arco originale = grafo.getArchi().get(i);
                    gestoreMlst.addArcoSenzaInserireCicli(originale);

                    //In questo modo, lavoriamo con complessità O(1)
                    tmpGrafo.getArchi().set(i, null);
                }

                statistiche.tempoInserimentoArchiSenzaCiclo += System.currentTimeMillis() - tempo;
                statistiche.tempoInserimentoArchiSenzaCicloCount++;

            } else {
                tempo = System.currentTimeMillis();

                //Determino il colore più ricorrente in edgeWithMinColors
                //Estrazione colori più ricorrenti solo dagli archi con numero colori minimo
                int mostCommonColor = mostCommonColor(edgeWithMinColors);

                //Estrazione colori più ricorrenti da tutti gli archi
                //int mostCommonColor = tmpGrafo.getListaColoriOrdinataPerRicorrenza().remove(0);   
                statistiche.tempoDeterminazioneColorePiuRicorrente += System.currentTimeMillis() - tempo;
                statistiche.tempoDeterminazioneColorePiuRicorrenteCount++;

                tempo = System.currentTimeMillis();

                //Elimino il colore dagli archi (temporanei)
                tmpGrafo.rimuoviColore(mostCommonColor);

                statistiche.tempoRimozioneColorePiuRicorrente += System.currentTimeMillis() - tempo;
                statistiche.tempoRimozioneColorePiuRicorrenteCount++;
            }
        }

        statistiche.tempoMedioIterate = (System.currentTimeMillis() - inizio) / statistiche.iter;
        statistiche.tempoMedioDeterminazioneColorePiuRicorrente = statistiche.tempoDeterminazioneColorePiuRicorrente / statistiche.tempoDeterminazioneColorePiuRicorrenteCount;
        statistiche.tempoMedioInserimentoArchiSenzaCiclo = statistiche.tempoInserimentoArchiSenzaCiclo / statistiche.tempoInserimentoArchiSenzaCicloCount;
        statistiche.meanTimeRecuperoArchiConColoreMinimo = statistiche.tempoRecuperoArchiConColoreMinimo / statistiche.tempoRecuperoArchiConColoreMinimoCount;
        statistiche.meanTimeRimozioneColorePiuRicorrente = statistiche.tempoRimozioneColorePiuRicorrente / statistiche.tempoRimozioneColorePiuRicorrenteCount;
        statistiche.tempoEsecuzione = (System.currentTimeMillis() - inizio) / 1000;

        return mlst;
    }

    private ArrayList<Integer> getIndexOfEdgesWithMinNumberOfColors(ArrayList<Arco> pEdges) {
        ArrayList<Integer> indexOfEdgesWithMinColors = new ArrayList<>();
        int previousTotColorEdge = -1;
        int totColorForEdge = -1;

        for (int i = 0; i < pEdges.size(); i++) {
            if (pEdges.get(i) != null) {
                totColorForEdge = pEdges.get(i).getColori().size();
                //Primo arco analizzato
                if (previousTotColorEdge == -1) {
                    previousTotColorEdge = totColorForEdge;
                    indexOfEdgesWithMinColors.add(i);
                } else {    //Successivi
                    if (totColorForEdge < previousTotColorEdge) {
                        previousTotColorEdge = totColorForEdge;
                        //Svuoto la lista degli archi minori,
                        //poichè ho trovato un nuovo candidato
                        indexOfEdgesWithMinColors.clear();
                        indexOfEdgesWithMinColors.add(i);
                    } else if (previousTotColorEdge == totColorForEdge) {
                        indexOfEdgesWithMinColors.add(i);
                    }
                }
            }
        }
        return indexOfEdgesWithMinColors;
    }

    private ArrayList<Arco> getEdgesWithMinNumberOfColors(ArrayList<Arco> pEdges, ArrayList<Integer> pIndexOfEdge) {
        ArrayList<Arco> pArchiConColoriMinimi = new ArrayList<>();

        for (int i : pIndexOfEdge) {
            pArchiConColoriMinimi.add(pEdges.get(i));
        }

        return pArchiConColoriMinimi;
    }

    private int mostCommonColor(ArrayList<Arco> pEdges) {
        int mostCommonColor = -1;
        int ricorrenzaMaggiore = -1;

        int[] ricorrenzeColori = new int[this.grafo.getColori().size()];

        //Scorro tutti gli archi e memorizzo le ricorrenze
        //Compessità: O(m * 3)      m -> num. archi, 3 -> num colori per arco
        for (Arco a : pEdges) {
            for (int indiceColore : a.getColori()) {
                ++ricorrenzeColori[indiceColore];
            }
        }

        //Trovo il colore più ricorrente
        //Complessità: O(c)         c -> num. colori
        for (int i = 0; i < ricorrenzeColori.length; i++) {
            if (ricorrenzeColori[i] > ricorrenzaMaggiore) {
                ricorrenzaMaggiore = ricorrenzeColori[i];
                mostCommonColor = i;
            }
        }

        return mostCommonColor;
    }

    private int mostCommonColor(GrafoColorato pGrafo) {
        int mostCommonColor = -1;
        int tmpOccorrenzeMassime = 0;

        for (Colore colore : pGrafo.getColori()) {
            if (colore.usato() && colore.getOccorrenze() > tmpOccorrenzeMassime) {
                tmpOccorrenzeMassime = colore.getOccorrenze();
                mostCommonColor = colore.getId();
            }
        }

        return mostCommonColor;
    }

    public Statistiche getStatistiche() {
        return this.statistiche;
    }

    /* 
    VECCHIA VERSIONE. EVENTUALE BACKUP
    
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
    }*/
}
