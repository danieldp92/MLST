package mlst;

import graph.Arco;
import graph.Grafo;
import graph.Nodo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class main {

    public static int NUM_COLOR;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Grafo grafo = new Grafo(5, 10, 3, 1);
        Grafo grafo = new Grafo("src/GrafiColorati3Colori/1000_8000_1000_125_1.mlst");
        System.out.println("LOAD FINISHED!!!!");
        NUM_COLOR = grafo.numColor;
        //grafo.stampa();
        
        Grafo mlstGraph = new Grafo(grafo.getNodi().size(), grafo.numColor);
        Grafo tmpMlstGraph = new Grafo(grafo.getNodi().size(), grafo.numColor);
        ArrayList<Integer> colorList = new ArrayList<>();
        ArrayList<Arco> edgeWithMinColors = new ArrayList<>();

        int m = 1;
        //Ciclo, fin quando mlst non e' connesso
        while (!mlstGraph.dfs()) {
            System.out.println("Iterata " + m++);
            //Prendi gli archi con il num colori minore
            edgeWithMinColors = getEdgesWithMinNumberOfColors(grafo.getArchi());

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (sum(edgeWithMinColors.get(0).getColors()) == 0) {
                for (Arco a : edgeWithMinColors) {
                    grafo.rimuoviArco(a);
                }
                
                mlstGraph.addArchiSenzaInserireCicli(edgeWithMinColors);
                //Elimino eventuali cicli
                //while (tmpMlstGraph.checkCycle()) {
                //Da implementare
                //}
            } else {
                //Determino il colore più ricorrente in edgeWithMinColors
                int mostCommonColor = mostCommonColor(edgeWithMinColors);

                //Elimino il colore
                grafo.deleteColor(mostCommonColor);

                //Aggiungo il colore alla lista dei colori presi
                colorList.add(mostCommonColor);
            }
        }

        System.out.println();
        mlstGraph.stampa();
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
                totColorForEdge = a.getColors().size();

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
                    if (a.getColors().get(index) == i) {
                        sumColor++;
                    }
                } while (a.getColors().get(index) != i && ++index < a.getColors().size());
                
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
                            if (pEdge.get(posMinValue).getColors().get(posMaxColor) == 1) {
                                find = true;
                            } else if (pEdge.get(i).getColors().get(posMaxColor) == 1) {
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
