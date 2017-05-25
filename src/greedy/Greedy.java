package greedy;

import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import grafo.GrafoColorato;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    
    public Statistiche getStatistiche () {
        return statistiche;
    }

    public GrafoColorato esegui() {
        long inizio = System.currentTimeMillis();
        GrafoColorato mlst = new GrafoColorato(grafo.getNodi(), grafo.getListaColori().size());
        ArrayList<Arco> tmpArchi = grafo.getCopiaArchi();
        ArrayList<Colore> tmpColori = grafo.getCopiaColori();
        
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
        long timeRimozioneArchi = 0;
        long timeRimozioneArchiCount = 0;

        //Ciclo, fin quando mlst non e' connesso
        while (!gestoreMlst.connesso()) {
            ++iter;
            time = System.currentTimeMillis();

            //Prelevo gli indici degli archi con colore minimo, poichè le operazioni effettuate su un arraylist 
            //con gli indici (get, set) hanno complessità O(1), diversamente dalle operazioni effettuate con gli oggetti
            indexOfEdgeWithMinColors = getIndexOfEdgesWithMinNumberOfColors(tmpArchi);

            //Prendo gli archi con il minor numero di colori
            edgeWithMinColors = getEdgesWithMinNumberOfColors(tmpArchi, indexOfEdgeWithMinColors);

            timeRecuperoArchiConColoreMinimo += System.currentTimeMillis() - time;
            timeRecuperoArchiConColoreMinimoCount++;

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {

                time = System.currentTimeMillis();

                for (int i : indexOfEdgeWithMinColors) {

                    Arco originale = grafo.getArco(i);
                    gestoreMlst.addArcoSenzaInserireCicli(i, originale);

                    //In questo modo, lavoriamo con complessità O(1)
                    tmpArchi.set(i, null);
                }
                
                timeInserimentoArchiSenzaCiclo += System.currentTimeMillis() - time;
                timeInserimentoArchiSenzaCicloCount++;

                time = System.currentTimeMillis();
                rimuoviArchiCheGeneranoCicli(tmpArchi, tmpColori, mlst);

                timeRimozioneArchi += System.currentTimeMillis() - time;
                timeRimozioneArchiCount++;
                
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
                rimuoviColore(tmpArchi, tmpColori, mostCommonColor);

                timeRimozioneColorePiuRicorrente += System.currentTimeMillis() - time;
                timeRimozioneColorePiuRicorrenteCount++;
            }
        }

        this.statistiche.iter = iter;
        this.statistiche.meanTimeIterate = (System.currentTimeMillis() - inizio) / iter;
        this.statistiche.meanTimeRecuperoArchiConColoreMinimo = timeRecuperoArchiConColoreMinimo / timeRecuperoArchiConColoreMinimoCount;
        this.statistiche.meanTimeInserimentoArchiSenzaCiclo = timeInserimentoArchiSenzaCiclo / timeInserimentoArchiSenzaCicloCount;
        this.statistiche.meanTimeRimozioneArchi = timeRimozioneArchi / timeRimozioneArchiCount;
        this.statistiche.meanTimeDeterminazioneColorePiuRicorrente = timeDeterminazioneColorePiuRicorrente / timeDeterminazioneColorePiuRicorrenteCount;
        this.statistiche.meanTimeRimozioneColorePiuRicorrente = timeRimozioneColorePiuRicorrente / timeRimozioneColorePiuRicorrenteCount;
        
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
                } else //Successivi
                {
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
    
    public void rimuoviColore(ArrayList<Arco> archi, ArrayList<Colore> colori, int pColore) {
        //Determino gli indici degli archi in cui è presente pColore
        for (int i : colori.get(pColore).getIndiciArchiCollegati())
            archi.get(i).rimuoviColore(pColore);

        //Elimino ogni riferimento di pColore da ogni arco associato
        colori.get(pColore).getIndiciArchiCollegati().clear();
    }

    private void rimuoviArchiCheGeneranoCicli(ArrayList<Arco> archi, ArrayList<Colore> colori, Grafo mlst) {
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;

        for (int i = 0; i < archi.size(); i++) {
            if (archi.get(i) != null) {
                componenteDiRiferimentoNodo1 = mlst.getNodo(archi.get(i).getDa().getChiave()).getComponenteDiRiferimento();
                componenteDiRiferimentoNodo2 = mlst.getNodo(archi.get(i).getA().getChiave()).getComponenteDiRiferimento();

                //Se l'arco genera cicli
                if (componenteDiRiferimentoNodo1 == componenteDiRiferimentoNodo2) {
                    //Rimuovo l'indice dell'arco dai colori associati
                    for (int colore : archi.get(i).getColori()) {
                        colori.get(colore).rimuoviIndiceArcoCollegato(i);
                    }
                    archi.set(i, null);
                }
            }

        }
    }
}
