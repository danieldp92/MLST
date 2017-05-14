package mlst;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import gestore.InfoGrafo;
import gestore.Ricerca;
import graph.Arco;
import graph.Grafo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class main {

    public static int NUM_COLOR;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/7_11_4.mlst"));
        //Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/7_11_4_1.mlst"));
        //Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/10000_160000_10000_625_1.mlst"));

        long inizio = System.currentTimeMillis();
        System.out.print("Caricamento grafo... ");
        Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/7_11_4_1.mlst"));
        System.out.format("fatto (%d ms)\n", System.currentTimeMillis() - inizio);
        NUM_COLOR = grafo.getColori().size();
        //GestoreGrafo gestore = new GestoreGrafo(grafo);
        //inizio = System.currentTimeMillis();
        //System.out.println("Connesso:" + gestore.connesso());
        //System.out.println("Ciclo:" + gestore.ciclo());
        //System.out.println("tempo: " + (System.currentTimeMillis() - inizio));

        Grafo mlst = new Grafo(grafo.getNodi());
        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        ArrayList<Integer> colorList = new ArrayList<>();
        ArrayList<Arco> edgeWithMinColors = new ArrayList<>();
        int m = 1;

        //Ciclo, fin quando mlst non e' connesso
        while (!gestoreMlst.connesso()) {
            System.out.println("Iterata " + m++);
            //Prendi gli archi con il minor numero di colori 
            edgeWithMinColors = getEdgesWithMinNumberOfColors(grafo.getArchi());

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (sum(edgeWithMinColors.get(0).getColori()) == 0) {
                //mlst.addArchi(edgeWithMinColors);
                gestoreMlst.addArchiSenzaInserireCicli(edgeWithMinColors);

                for (Arco a : edgeWithMinColors) {
                    grafo.rimuoviArco(a);
                }
                //Elimino eventuali cicli
                //while (tmpMlstGraph.checkCycle()) {
                //Da implementare
                //}
            } else {
                //Determino il colore più ricorrente in edgeWithMinColors
                int mostCommonColor = mostCommonColor(edgeWithMinColors);
                //Elimino il colore
                grafo.rimuoviColore(mostCommonColor);
                //Aggiungo il colore alla lista dei colori presi
                colorList.add(mostCommonColor);
            }
        }
        System.out.println();
        InfoGrafo infoMlst = new InfoGrafo(mlst);
        infoMlst.stampaNodi();
        infoMlst.stampaArchi();
        System.out.println();
        System.out.println("Colori usati: " + colorList.size());
        for (int i : colorList) {
            System.out.println(i);
        }
    }

    private static int sum(ArrayList<Integer> pList) {
        int sum = 0;
        for (int i : pList) {
            sum += i;
        }
        return sum;
    }

    private static ArrayList<Arco> getEdgesWithMinNumberOfColors(ArrayList<Arco> pEdges) {
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

    private static int mostCommonColor(ArrayList<Arco> pEdges) {
        int mostCommonColor = -1;
        int prevSumColor = -1;
        int sumColor = 0;
        int index = 0;
        for (int i = 0; i < NUM_COLOR; i++) {
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

    public static int getPosMinValue(ArrayList<Arco> pEdge, ArrayList<Integer> pTotColorForEdge, ArrayList<Integer> pTotEdgeForColor) {
        int index = 0;
        boolean find = false;
        int posMaxColor = -1;
        int posMinValue = -1;
        int minvalue = NUM_COLOR + 1;
        for (int i = 0; i < pTotColorForEdge.size(); i++) {
            if (pEdge.get(i) != null) {
                if (pTotColorForEdge.get(i) < minvalue) {
                    posMinValue = i;
                    minvalue = pTotColorForEdge.get(i);
                } else if (pTotColorForEdge.get(i) == minvalue) {
                    ArrayList<Integer> tmpTotEdgeForColorList = new ArrayList<>(pTotEdgeForColor);
                    while (!find) {
                        if (index++ < pTotEdgeForColor.size()) {
                            //Find the max color
                            posMaxColor = posMaxValue(tmpTotEdgeForColorList);
                            //Controllo se i due archi che sto confrontando hanno quel colore
                            if (pEdge.get(posMinValue).getColori().get(posMaxColor) == 1) {
                                find = true;
                            } else if (pEdge.get(i).getColori().get(posMaxColor) == 1) {
                                posMinValue = i;
                                find = true;
                            } else {
                                //Delete from list
                                tmpTotEdgeForColorList.set(posMaxColor, 0);
                            }
                        }
                    }
                    index = 0;
                    posMaxColor = -1;
                    find = false;
                }
            }
        }
        System.out.println();
        System.out.println("Arco minimo: " + posMinValue);
        System.out.println();
        if (posMinValue != -1) {
            pEdge.set(posMinValue, null);
        }
        return posMinValue;
    }

    public static int posMaxValue(ArrayList<Integer> pColorList) {
        int pos = -1;
        int maxValue = -1;
        for (int i = 0; i < NUM_COLOR; i++) {
            if (pColorList.get(i) > maxValue) {
                maxValue = pColorList.get(i);
                pos = i;
            }
        }
        return pos;
    }

}
