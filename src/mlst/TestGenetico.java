/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import AlgoritmoGenetico.Algoritmo;
import AlgoritmoGenetico.Cromosoma;
import AlgoritmoGenetico.Impostazioni;
import AlgoritmoGenetico.Popolazione;
import gestore.XlsGrafo;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Dasteor
 */
public class TestGenetico {

    public static void test() throws IOException {
        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);

        ArrayList<String> listaGrafi = listaFile();

        for (String nomeGrafo : listaGrafi) {
            System.out.println(nomeGrafo);
            
            Impostazioni impostazioni = new Impostazioni();
            impostazioni.setParametro(Impostazioni.NOME_GRAFO, nomeGrafo);
            impostazioni.setParametro(Impostazioni.TOT_COLORI, nomeGrafo.split("_")[2]);
            impostazioni.setParametro(Impostazioni.POPOLAZIONE, String.valueOf(10));
            impostazioni.setParametro(Impostazioni.VALUTAZIONI, String.valueOf(1000));
            impostazioni.setParametro(Impostazioni.CROSSOVER_RATE, String.valueOf(0.8));
            impostazioni.setParametro(Impostazioni.MUTATION_RATE, String.valueOf(0.2));
            
            long inizio = System.currentTimeMillis();
            
            Algoritmo algoritmo = new Algoritmo();
            Popolazione popolazione = algoritmo.execute();
            Cromosoma soluzione = migliorSoluzione(popolazione);
            
            xls.addInfoGrafo(nomeGrafo, "ag", ((double) (System.currentTimeMillis() - inizio) / 1000), soluzione.size());
            xls.salva(pathTabellaRisultati);
            
            System.out.println("Numero colori: " + soluzione.size());
            System.out.println("Tempo di esecuzione: " + (double) (System.currentTimeMillis() - inizio) / 1000);
        }
        
        xls.salva(pathTabellaRisultati);

    }
    
    private static Cromosoma migliorSoluzione (Popolazione popolazione) {
        int posMinFF = -1;
        int FfMin = 10000000;

        System.out.println("FF");
        for (int i = 0; i < popolazione.size(); i++) {
            System.out.println("Cromosoma " + (i + 1) + ": " + popolazione.getCromosoma(i).getValoreFunzioneDiFitness());
            if (popolazione.getCromosoma(i).getValoreFunzioneDiFitness() < FfMin) {
                FfMin = popolazione.getCromosoma(i).getValoreFunzioneDiFitness();
                posMinFF = i;
            }
        }
        
        return popolazione.getCromosoma(posMinFF);
    }

    public static ArrayList<String> listaFile() {
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
