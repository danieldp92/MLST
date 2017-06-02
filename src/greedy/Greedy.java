package greedy;

import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Greedy {

    Grafo grafo;
    PrintWriter writer;

    public Greedy(Grafo grafo) {
        this.grafo = grafo;
    }
    
    public Greedy(Grafo grafo, PrintWriter pWriter) {
        this.grafo = grafo;
        this.writer = pWriter;
    }

    public Grafo esegui() {
        long inizio = System.currentTimeMillis();
        Grafo mlst = new Grafo(grafo.getNodi(), grafo.getListaColori().size());
        Grafo tmpGrafo = this.grafo.clone();
        
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);
        
        ArrayList<Arco> edgeWithMinColors;
        ArrayList<Integer> indexOfEdgeWithMinColors;
        int iter = 0;

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
            ++iter;
            time = System.currentTimeMillis();
            
            //Prelevo gli indici degli archi con colore minimo, poichè le operazioni effettuate su un arraylist 
            //con gli indici (get, set) hanno complessità O(1), diversamente dalle operazioni effettuate con gli oggetti
            indexOfEdgeWithMinColors = getIndexOfEdgesWithMinNumberOfColors(tmpGrafo.getArchi());
            
            //Prendo gli archi con il minor numero di colori
            edgeWithMinColors = getEdgesWithMinNumberOfColors(tmpGrafo.getArchi(), indexOfEdgeWithMinColors);
                
            timeRecuperoArchiConColoreMinimo += System.currentTimeMillis() - time;
            timeRecuperoArchiConColoreMinimoCount++;

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {

                time = System.currentTimeMillis();
                
                for (int i : indexOfEdgeWithMinColors) {

                    Arco originale = grafo.getArchi().get(i);
                    gestoreMlst.addArcoSenzaInserireCicli(originale);
                    
                    //In questo modo, lavoriamo con complessità O(1)
                    tmpGrafo.getArchi().set(i, null);
                }

                timeInserimentoArchiSenzaCiclo += System.currentTimeMillis() - time;
                timeInserimentoArchiSenzaCicloCount++;

            } else {
                time = System.currentTimeMillis();
                
                //Determino il colore più ricorrente in edgeWithMinColors
                
                //Estrazione colori più ricorrenti solo dagli archi con numero colori minimo
                int mostCommonColor = mostCommonColor(edgeWithMinColors);
                
                //Estrazione colori più ricorrenti da tutti gli archi
                //int mostCommonColor = tmpGrafo.getListaColoriOrdinataPerRicorrenza().remove(0);   
                
                timeDeterminazioneColorePiuRicorrente += System.currentTimeMillis() - time;
                timeDeterminazioneColorePiuRicorrenteCount++;

                time = System.currentTimeMillis();
                
                //Elimino il colore dagli archi (temporanei)
                tmpGrafo.rimuoviColore(mostCommonColor);
                
                timeRimozioneColorePiuRicorrente += System.currentTimeMillis() - time;
                timeRimozioneColorePiuRicorrenteCount++;
            }
        }
        

        double meanTimeIterate = (System.currentTimeMillis() - inizio) / iter;
        double meanTimeDeterminazioneColorePiuRicorrente = timeDeterminazioneColorePiuRicorrente / timeDeterminazioneColorePiuRicorrenteCount;
        double meanTimeInserimentoArchiSenzaCiclo = timeInserimentoArchiSenzaCiclo / timeInserimentoArchiSenzaCicloCount;
        double meanTimeRecuperoArchiConColoreMinimo = timeRecuperoArchiConColoreMinimo / timeRecuperoArchiConColoreMinimoCount;
        double meanTimeRimozioneColorePiuRicorrente = timeRimozioneColorePiuRicorrente / timeRimozioneColorePiuRicorrenteCount;
        
        this.writer.printf("Tempo di esecuzione: %.3fs\n", (float)(System.currentTimeMillis() - inizio)/1000);
        this.writer.println("Numero Iterate: " + iter);
        this.writer.println("Colori usati: " + mlst.getListaColori().size());
        this.writer.printf("Media tempo iterata: %.3fs\n", (float)meanTimeIterate/1000);
        this.writer.printf("Media determinazione colore più ricorrente: %.3fs\n", (float)meanTimeDeterminazioneColorePiuRicorrente/1000);
        this.writer.printf("Media inserimento archi senza ciclo: %.3fs\n", (float)meanTimeInserimentoArchiSenzaCiclo/1000);
        this.writer.println();

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
        
        for (int i : pIndexOfEdge)
            pArchiConColoriMinimi.add(pEdges.get(i));
        
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
    
    private int mostCommonColor(Grafo pGrafo) {
        int mostCommonColor = -1;
        int tmpOccorrenzeMassime = 0;
        
        for (Colore colore : pGrafo.getColori())
            if (colore.isUsed() && colore.getOccorrenze() > tmpOccorrenzeMassime) {
                tmpOccorrenzeMassime = colore.getOccorrenze();
                mostCommonColor = colore.getColore();
            }
        
        return mostCommonColor;
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
