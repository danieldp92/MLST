/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
import gestore.XlsGrafo;
import grafo.GrafoColorato;
import greedy.Greedy;
import greedy.MultiThreadGreedy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rhobar
 */
public class TestMultistart {

    private static final int NUM_ITERATE = 300;
    private static final int THREAD_IN_PARALLELO = 100;

    public static void test() throws IOException, InterruptedException {
        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);

        ArrayList<String> listaGrafi = listaFile();

        for (String s : listaGrafi) {
            //Carico il grafo
            System.out.println(s);
            GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + s));
            grafo.nomeGrafo = s;

            long inizio = System.nanoTime();

            ArrayList<GrafoColorato> listaMlst = new ArrayList<>();
            Greedy greedy = new Greedy(grafo);

            //Ottengo un MLST eseguendo l'algoritmo greedy sul grafo, iterativamente, al fine di trovare la soluzione migliore
            MultiThreadGreedy multiThread = new MultiThreadGreedy(NUM_ITERATE, THREAD_IN_PARALLELO, greedy);
            listaMlst = multiThread.avviaMultiThreadGreedy(null);
            
            ArrayList<Integer> soluzioni = new ArrayList<>();
            for (GrafoColorato mlst : listaMlst)
                soluzioni.add(mlst.getListaColori().size());
            
            //Trovo la soluzione migliore
            int iterataSoluzione = indiceMiglioreSoluzione(soluzioni);
            int totaleColoriMigliori = soluzioni.get(iterataSoluzione);

            xls.addInfoGrafo(grafo.nomeGrafo, "multistart", ((double)(System.nanoTime() - inizio) / 1000000000), totaleColoriMigliori, iterataSoluzione);
            xls.salva(pathTabellaRisultati);
            System.out.println("Numero colori: " + totaleColoriMigliori);
            System.out.println("Thread soluzione: " + iterataSoluzione);
            System.out.println("Tempo di esecuzione: " + ((double)(System.nanoTime() - inizio) / 1000000000));

        }

        xls.salva(pathTabellaRisultati);
    }

    private static int indiceMiglioreSoluzione(ArrayList<Integer> lista) {
        int indiceMiglioreSoluzione = -1;
        int soluzioneMigliore = 1000000;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) < soluzioneMigliore) {
                indiceMiglioreSoluzione = i;
                soluzioneMigliore = lista.get(i);
            }
        }

        return indiceMiglioreSoluzione;
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

}
