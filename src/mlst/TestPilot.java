/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
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

        ArrayList<String> listaGrafi = listaFile();
        int count = 0;
        ArrayList<Integer> colorsToDelete = new ArrayList<>();
        int sol;
        ArrayList<Integer> solArray = new ArrayList<>();

        for (String s : listaGrafi) {
            GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + s));
            colorsToDelete.add(count);
            Pilot pilot = new Pilot(grafo);
            sol = pilot.esegui(colorsToDelete);
            solArray.add(sol);
            colorsToDelete.clear();
            count++;
        }

        /*        for (int i = 0; i < solArray.size(); i++) {
         System.out.println("Colore " + i + ": " + solArray.get(i));
         }*/
        
        ArrayList<Integer> mins = CercaMins(solArray);

        /*System.out.println("");
        for (int i = 0; i < mins.size(); i++) {
        System.out.println(mins.get(i));
        }
        System.out.println("");*/
        
        solArray.clear();
        count = 0;

        LinkedList<LinkedList<Integer>> solList = new LinkedList<LinkedList<Integer>>();

        for (int i = 0; i < mins.size(); i++) {
            for (String s : listaGrafi) {
                if (mins.get(i) != count) {
                    GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + s));
                    colorsToDelete.add(mins.get(i));
                    colorsToDelete.add(count);

                    Pilot pilot = new Pilot(grafo);
                    sol = pilot.esegui(colorsToDelete);
                    solArray.add(sol);

                    LinkedList<Integer> currentSol = new LinkedList<>();
                    currentSol.add(mins.get(i));
                    currentSol.add(count);
                    currentSol.add(sol);
                    solList.add(currentSol);

                   // System.out.println("Colore " + mins.get(i) + "," + count + ": " + sol);
                    colorsToDelete.clear();
                    count++;
                } else {
                    count++;
                }
            }
            count = 0;
        }

        /*for(int i=0; i<solList.size(); i++){
         System.out.println("Colore " + solList.get(i).get(0) + "," + solList.get(i).get(1) + ": " + solList.get(i).get(2));
         }*/
        /*ArrayList<Integer> mins2 = CercaMins(solArray);
         System.out.println("");
         for (int i = 0; i < mins2.size(); i++) {
         System.out.println(mins.get(i));
         }*/
        //System.out.println("");

        int minSol = CercaMin(solArray);
        for (int i = 0; i < solList.size(); i++) {
            if (solList.get(i).get(2) == minSol) {
                System.out.println("Colore " + solList.get(i).get(0) + "," + solList.get(i).get(1) + ": " + solList.get(i).get(2));
            }
        }

    }

    private static ArrayList<String> listaFile() {
        ArrayList<String> listaFile = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            listaFile.add("50_200_50_13_1.mlst");
        }

        return listaFile;
    }

    private static ArrayList<Integer> CercaMins(ArrayList<Integer> solArray) {
        int min = Integer.MAX_VALUE;
        ArrayList<Integer> mins = new ArrayList<>();
        for (int i = 0; i < solArray.size(); i++) {
            if (solArray.get(i) < min) {
                min = solArray.get(i);
            }
        }
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
