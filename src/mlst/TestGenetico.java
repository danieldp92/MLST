/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import VecchioAlgoritmoGenetico.Algoritmo;
import VecchioAlgoritmoGenetico.Popolazione;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Dasteor
 */
public class TestGenetico {
    public static void test() throws IOException {
        Algoritmo algoritmo = new Algoritmo();
        Popolazione popolazione = algoritmo.execute();
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
