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
import greedy.Statistiche;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rhobar
 */
public class TestMultistart {

    public static void test() throws IOException {
        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);

        // ArrayList<String> listaGrafi = listaFile();
        ArrayList<String> listaGrafi = listaFile1Colore();

        for (String s : listaGrafi) {
            //Carico il grafo
            System.out.println(s);
            // GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + s));
            GrafoColorato grafo = GeneratoreGrafo.generaGrafoConUnColore(new File("src/GrafiColorati1Colore/gruppo1/" + s));
            grafo.nomeGrafo = s;
            Statistiche statistiche = null;

            long inizio = System.currentTimeMillis();
            Greedy greedy = new Greedy(grafo);
            GrafoColorato mlst = null;
            int totaleColoriMigliori = grafo.getListaColori().size() + 1;
            int iterataSoluzione = -1;

            //Ottengo un MLST eseguendo l'algoritmo greedy sul grafo, iterativamente, al fine di trovare la soluzione migliore
            int iterata = 0;
            int maxIterateDalMigliore = 80;
            int numeroIterateEffettuateDalMigliore = 0;

            while (iterata++ < 300 && numeroIterateEffettuateDalMigliore < maxIterateDalMigliore) {
                mlst = greedy.esegui(true);

                if (mlst.getListaColori().size() < totaleColoriMigliori) {
                    totaleColoriMigliori = mlst.getListaColori().size();
                    numeroIterateEffettuateDalMigliore = 0;
                    iterataSoluzione = iterata;
                }

                numeroIterateEffettuateDalMigliore++;
            }

            statistiche = greedy.getStatistiche();
            //xls.addInfoGrafo(grafo.nomeGrafo, "multistart", (System.currentTimeMillis() - inizio), totaleColoriMigliori, iterataSoluzione);
            //xls.salva(pathTabellaRisultati);
            System.out.println("Numero colori: " + totaleColoriMigliori);
            //System.out.println("Tempo di esecuzione: " + statistiche.tempoDiEsecuzione);
        }

        xls.salva(pathTabellaRisultati);
    }

    public static ArrayList<String> listaFile() {
        ArrayList<String> listaFile = new ArrayList<>();

        //Archi da 25 100 25 Creati da Stefano
        for (int i = 1; i <= 10; i++) {
            listaFile.add("grafo_25_100_25_" + i + ".mlst");
        }
        /*
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
         */
        return listaFile;
    }

    public static ArrayList<String> listaFile1Colore() {
        ArrayList<String> listaFile = new ArrayList<>();
        File cartella = new File("src\\GrafiColorati1Colore\\gruppo1");
        File[] files = cartella.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                if (files[i].getName().startsWith("50")) {
                    listaFile.add(files[i].getName());
                }
            }
        }

        return listaFile;
    }
}
