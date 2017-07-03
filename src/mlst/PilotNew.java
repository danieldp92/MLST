package mlst;

import gestore.GeneratoreGrafo;
import gestore.XlsGrafo;
import grafo.GrafoColorato;
import pilot.Pilot;
import ilog.concert.IloException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Orazio
 */
public class PilotNew {

    private static final int LIVELLI = 2;

    public static void test() throws IOException, IloException {

        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);
        ArrayList<String> listaGrafi = listaFile();

        //Per ogni grafico
        for (int i = 0; i < listaGrafi.size(); i++) {

            GrafoColorato g = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
            int numCol = g.getColori().size();
            ArrayList<Integer> colorsToDelete = new ArrayList<>();
            int sol;
            ArrayList<ArrayList<Integer>> solutionArray = new ArrayList<>();

            long startTime = System.currentTimeMillis();

            //Primo livello
            for (int z = 0; z < numCol; z++) {
                GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
                colorsToDelete.add(z);
                Pilot pilot = new Pilot(grafo);
                sol = pilot.esegui(colorsToDelete);

                ArrayList<Integer> current = new ArrayList<>();
                current.add(z);
                current.add(sol);
                solutionArray.add(current);

                colorsToDelete.clear();
            }

            ArrayList<ArrayList<Integer>> minimi = cercaMinimi(solutionArray);
            solutionArray.clear();

            //Livelli successivi
            for (int liv = 0; liv < LIVELLI - 1; liv++) {
                for (int j = 0; j < minimi.size(); j++) {
                    for (int z = 0; z < numCol; z++) {
                        if (!CheckPrecedenti(z, j, minimi)) {
                            if (minimi.get(j).get(0) != z) {
                                GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));

                                for (int index = 0; index < liv + 1; index++) {
                                    colorsToDelete.add(minimi.get(j).get(index));
                                }
                                colorsToDelete.add(z);

                                Pilot pilot = new Pilot(grafo);
                                sol = pilot.esegui(colorsToDelete);

                                ArrayList<Integer> current = new ArrayList<>();
                                for (int index = 0; index < liv + 1; index++) {
                                    current.add(minimi.get(j).get(index));
                                }
                                current.add(z);
                                current.add(sol);
                                solutionArray.add(current);

                                colorsToDelete.clear();
                            }
                        }
                    }
                }
                minimi = cercaMinimi(solutionArray);
                if (liv != LIVELLI - 2) {
                    solutionArray.clear();
                }
            }

            int minimumSolution = CercaMinimo(solutionArray);
            //ArrayList<ArrayList<Integer>> minimumSolutionColor = cercaMinimi(solutionArray);

            float endTime = System.currentTimeMillis() - startTime;

            System.out.println("Grafo:" + listaGrafi.get(i) + " Sol:" + minimumSolution + " Tempo ms:" + endTime);
            System.out.println("");

            xls.addInfoGrafo(listaGrafi.get(i), "newpilot", endTime, minimumSolution);
            xls.salva(pathTabellaRisultati);
        }

        xls.salva(pathTabellaRisultati);
    }

    private static ArrayList<String> listaFile() {
        ArrayList<String> listaFile = new ArrayList<>();

     //   listaFile.add("50_200_50_13_1.mlst");
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

    private static boolean CheckPrecedenti(int currentColor, int index, ArrayList<ArrayList<Integer>> precedenti) {
        boolean ret = false;
        for (int i = 0; i < index; i++) {
            if (precedenti.get(i).get(0) == currentColor) {
                ret = true;
            }
        }
        return ret;
    }

    private static ArrayList<ArrayList<Integer>> cercaMinimi(ArrayList<ArrayList<Integer>> solutionArray) {
        ArrayList<ArrayList<Integer>> minimi = new ArrayList<>();
        int min = CercaMinimo(solutionArray);

        for (int i = 0; i < solutionArray.size(); i++) {
            if (solutionArray.get(i).get(solutionArray.get(i).size() - 1) == min) {
                minimi.add(solutionArray.get(i));
            }
        }

        return minimi;
    }

    private static int CercaMinimo(ArrayList<ArrayList<Integer>> solutionArray) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < solutionArray.size(); i++) {
            if (solutionArray.get(i).get(solutionArray.get(i).size() - 1) < min) {
                min = solutionArray.get(i).get(solutionArray.get(i).size() - 1);
            }
        }
        return min;
    }

}
