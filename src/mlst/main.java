package mlst;

import ilog.concert.IloException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class main {

    public static void main(String[] args) throws IOException, IloException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("TEST MLST");
        System.out.println();
        System.out.println("1) CPLEX");
        System.out.println("2) Greedy");
        System.out.println("3) ND-Greedy");
        System.out.println("4) Multistart");
        System.out.println("5) Pilot");
        System.out.println("6) Algoritmo Genetico");
        System.out.println("7) TUTTI (tranne CPLEX)");
        System.out.println("0) Esci");

        System.out.println();

        int scelta = scanner.nextInt();

        System.out.println();

        switch (scelta) {
            case 1:
                ArrayList<String> esitoGrafi = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    esitoGrafi.add("");
                }

                //Scelta tra i primi 20 grafi
                int sceltaGrafo;
                do {
                    System.out.println("Quale grafo vuoi testare con CPLEX?");
                    System.out.println();
                    ArrayList<String> listaGrafi = listaNomiGrafi();

                    for (int i = 0; i < 10; i++) {
                        System.out.println((i + 1) + ") " + listaGrafi.get(i) + " " + esitoGrafi.get(i));
                    }
                    System.out.println();
                    System.out.println("11) Blocco da 200 archi");
                    System.out.println("0) TERMINA ESECUZIONE");
                    System.out.println();

                    sceltaGrafo = scanner.nextInt();

                    if (sceltaGrafo > 11) {
                        sceltaGrafo = 0;
                    }

                    if (sceltaGrafo != 0) {
                        if (sceltaGrafo <= 20) {

                            if (!esitoGrafi.get(sceltaGrafo - 1).equals("(COMPLETATO)")) {
                                TestCPLEX.test(listaGrafi.get(sceltaGrafo - 1));
                                esitoGrafi.set(sceltaGrafo - 1, "(COMPLETATO)");
                            } else {
                                System.out.println("Grafo " + listaGrafi.get(sceltaGrafo - 1) + " gia' analizzato. ANALISI NON NECESSARIA!!!");
                            }

                        } else {
                            for (int i = 0; i < 10; i++) {
                                if (!esitoGrafi.get(i).equals("(COMPLETATO)")) {
                                    TestCPLEX.test(listaGrafi.get(i));
                                    esitoGrafi.set(i, "(COMPLETATO)");
                                } else {
                                    System.out.println("Grafo " + listaGrafi.get(i) + " gia' analizzato. ANALISI NON NECESSARIA!!!");
                                }
                            }
                        }

                        System.out.println();
                        System.out.println("VUOI CONTINUARE? (Y/N)");
                        String continua = scanner.next();

                        if (!continua.toUpperCase().equals("Y")) {
                            sceltaGrafo = 0;
                        }
                    }

                } while (sceltaGrafo != 0);

                break;
            case 2:
                TestGreedy.test();
                break;
            case 3:
                TestNDGreedy.test();
                break;
            case 4:
                TestMultistart.test();
                break;
            case 5:
                TestPilot.test();
                break;
            case 6:
                TestGenetico.test();
                break;
            case 7:
                TestGreedy.test();
                TestNDGreedy.test();
                TestMultistart.test();
                TestPilot.test();
                TestGenetico.test();
                break;
            default:
                break;
        }
    }

    public static ArrayList<String> listaNomiGrafi() {
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
