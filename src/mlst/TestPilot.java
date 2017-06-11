/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
import gestore.XlsGrafo;
import grafo.GrafoColorato;
import ilog.concert.IloException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import pilot.Pilot;

/**
 *
 * @author Orazio
 */
public class TestPilot {

    public static void test() throws IOException, IloException {

        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);

        ArrayList<String> listaGrafi = listaFile();
        
        //Per ogni grafico
        for (int i = 0; i < listaGrafi.size(); i++) {
            long startTime = System.currentTimeMillis();

            GrafoColorato g = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
            int numCol = g.getColori().size();

            int count = 0;
            ArrayList<Integer> colorsToDelete = new ArrayList<>();
            int sol;
            ArrayList<Integer> solArray = new ArrayList<>();

            //Primo livello
            for (int z = 0; z < numCol; z++) {   
                GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
                colorsToDelete.add(count);
                Pilot pilot = new Pilot(grafo);
                sol = pilot.esegui(colorsToDelete);
                solArray.add(sol);
                colorsToDelete.clear();
                count++;
                if(System.currentTimeMillis()-startTime>600000) break;
            }

            /*for (int i = 0; i < solArray.size(); i++) {
             System.out.println("Colore " + i + ": " + solArray.get(i));
             }*/
                        
            ArrayList<Integer> mins = CercaMins(solArray); //cerca i colori con la soluzione migliore

            /*System.out.println("");
             for (int i = 0; i < mins.size(); i++) {
             System.out.println(mins.get(i));
             }
             System.out.println("");*/
            solArray.clear();
            count = 0;

           // LinkedList<LinkedList<Integer>> solList = new LinkedList<>();

            //Secondo livello
            for (int j = 0; j < mins.size(); j++) {
                for (int z = 0; z < numCol; z++) {
                    if (mins.get(j) != count) {
                        GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
                        colorsToDelete.add(mins.get(j));
                        colorsToDelete.add(count);
                        Pilot pilot = new Pilot(grafo);
                        sol = pilot.esegui(colorsToDelete);
                        solArray.add(sol);

                        /*LinkedList<Integer> currentSol = new LinkedList<>();
                        currentSol.add(mins.get(j));
                        currentSol.add(count);
                        currentSol.add(sol);
                        solList.add(currentSol);*/

                        // System.out.println("Colore " + mins.get(i) + "," + count + ": " + sol);
                        colorsToDelete.clear();
                        count++;
                    } else {
                        count++;
                    }
                    if(System.currentTimeMillis()-startTime>600000) break;
                }
                count = 0;
                if(System.currentTimeMillis()-startTime>600000) break;
            }

            /*for(int i=0; i<solList.size(); i++){
             System.out.println("Colore " + solList.get(i).get(0) + "," + solList.get(i).get(1) + ": " + solList.get(i).get(2));
             }*/
            /*ArrayList<Integer> mins2 = CercaMins(solArray);
            /* System.out.println("");
             for (int i = 0; i < mins2.size(); i++) {
             System.out.println(mins.get(i));
             }*/
            //System.out.println("");
            int minSol = CercaMin(solArray);
            /*for (int k = 0; k < solList.size(); k++) {
             if (solList.get(k).get(2) == minSol) {
             System.out.println("Colore " + solList.get(k).get(0) + "," + solList.get(k).get(1) + ": " + solList.get(k).get(2));
             }
             }*/

            float endTime = System.currentTimeMillis() - startTime;
            float timeInSec = endTime / 1000;

            System.out.println("Grafo:" + listaGrafi.get(i) + " Sol:" + minSol + " Tempo s:" + endTime);
            System.out.println("");

            xls.addInfoGrafo(listaGrafi.get(i), "pilot", timeInSec, minSol);
            xls.salva(pathTabellaRisultati);
        }

        xls.salva(pathTabellaRisultati);

    }

    private static ArrayList<String> listaFile() {
        ArrayList<String> listaFile = new ArrayList<>();

        //Archi da 50 200 50
        for (int i = 1; i <= 10; i++) {
            listaFile.add("50_200_50_13_" + i + ".mlst");
        }

        //Archi da 50 1000 50
        for (int i = 1; i <= 10; i++) {
            listaFile.add("50_1000_50_3_" + i + ".mlst");
        }

        //Archi da 100 400 100
        for (int i = 1; i <= 10; i++) {
            listaFile.add("100_400_100_25_" + i + ".mlst");
        }

        //Archi da 100 800 100
        for (int i = 1; i <= 5; i++) {
            listaFile.add("100_800_100_13_" + i + ".mlst");
        }

        //Archi da 100 1000 100
        for (int i = 1; i <= 10; i++) {
            listaFile.add("100_1000_100_10_" + i + ".mlst");
        }

        //Archi da 100 2000 100
        for (int i = 1; i <= 10; i++) {
            listaFile.add("100_2000_100_5_" + i + ".mlst");
        }

        //Archi da 100 3000 100
        for (int i = 1; i <= 10; i++) {
            listaFile.add("100_3000_100_4_" + i + ".mlst");
        }

        //Archi da 500 2000 500
        for (int i = 1; i <= 5; i++) {
            listaFile.add("500_2000_500_125_" + i + ".mlst");
        }

        //Archi da 500 4000 500
        for (int i = 1; i <= 5; i++) {
            listaFile.add("500_4000_500_63_" + i + ".mlst");
        }

        //Archi da 1000 4000 1000
        for (int i = 1; i <= 5; i++) {
            listaFile.add("1000_4000_1000_250_" + i + ".mlst");
        }

        //Archi da 1000 8000 1000
        for (int i = 1; i <= 5; i++) {
            listaFile.add("1000_8000_1000_125_" + i + ".mlst");
        }

        //Archi da 10000 40000 10000
        for (int i = 1; i <= 5; i++) {
            listaFile.add("10000_40000_10000_2500_" + i + ".mlst");
        }

        //Archi da 10000 80000 10000
        for (int i = 1; i <= 5; i++) {
            listaFile.add("10000_80000_10000_1250_" + i + ".mlst");
        }

        //Archi da 10000 160000 10000
        for (int i = 1; i <= 5; i++) {
            listaFile.add("10000_160000_10000_625_" + i + ".mlst");
        }

        return listaFile;
    }

    private static ArrayList<Integer> CercaMins(ArrayList<Integer> solArray) {
        int min = CercaMin(solArray);
        ArrayList<Integer> mins = new ArrayList<>();
        for (int i = 0; i < solArray.size(); i++) {
            if (solArray.get(i) == min) {
                mins.add(i);
            }
        }
        return mins;
    }

    private static int CercaMin(ArrayList<Integer> solArray) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < solArray.size(); i++) {
            if (solArray.get(i) < min) {
                min = solArray.get(i);
            }
        }
        return min;
    }

}
