package mlst;

import gestore.GeneratoreGrafo;
import gestore.XlsGrafo;
import grafo.GrafoColorato;
import Pilot.Pilot;
import ilog.concert.IloException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Orazio
 */
public class FrPilot {

    private static final int LIVELLI = 2;

    public static void test() throws IOException, IloException {

        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);
        
        ArrayList<String> listaGrafi = listaFile();
        double value;
        
        //Per ogni grafico
        for (int i = 0; i < listaGrafi.size(); i++) {

            GrafoColorato g = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
            int numCol = g.getColori().size();
            ArrayList<Integer> colorsToDelete = new ArrayList<>();
            int sol;
            ArrayList<ArrayList<Integer>> solutionArray = new ArrayList<>();
            
            switch (numCol){
                case 50:
                    value = 100.0;
                    break;
                case 100:
                    value = 100.0;
                    break;
                case 500:
                    value = 50.0;
                    break;
                case 1000:
                    value = 10.0;
                    break;
                case 10000:
                    value = 0.05;
                    break;
                default:
                    value = 100;
            }
            
            int colorLimit = (int) (numCol*(value/100));

            long startTime = System.currentTimeMillis();
            
            HashMap<Integer, Integer> fr = CalcolaFrequenze(g);
            int sommaFr = 0;
            for (int it = 0; it < fr.size(); it++) {
                sommaFr += fr.get(it);
            }
            HashMap<Integer, Double> normfr = new HashMap<>();
            for (int it = 0; it < fr.size(); it++) {
                normfr.put(it, (double) fr.get(it) / sommaFr);
            }
            HashMap<Integer, Double> sortedNormFr = sortHashMapByValues(normfr);

            List<Integer> keys = new ArrayList<>(sortedNormFr.keySet());
            Collections.reverse(keys);

            //Primo livello
            for (int z = 0; z < colorLimit; z++) {
                GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));
                colorsToDelete.add(keys.get(z));
                Pilot pilot = new Pilot(grafo);
                sol = pilot.esegui(colorsToDelete);

                ArrayList<Integer> current = new ArrayList<>();
                current.add(keys.get(z));
                current.add(sol);
                solutionArray.add(current);

                colorsToDelete.clear();
            }

            ArrayList<ArrayList<Integer>> minimi = cercaMinimi(solutionArray);
            solutionArray.clear();

            //Livelli successivi
            for (int liv = 0; liv < LIVELLI - 1; liv++) {
                for (int j = 0; j < minimi.size(); j++) {
                    for (int z = 0; z < colorLimit; z++) {
                        if (!CheckPrecedenti(keys.get(z), j, minimi)) {
                            if (minimi.get(j).get(0) != keys.get(z)) {
                                GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + listaGrafi.get(i)));

                                for (int index = 0; index < liv + 1; index++) {
                                    colorsToDelete.add(minimi.get(j).get(index));
                                }
                                colorsToDelete.add(keys.get(z));

                                Pilot pilot = new Pilot(grafo);
                                sol = pilot.esegui(colorsToDelete);

                                ArrayList<Integer> current = new ArrayList<>();
                                for (int index = 0; index < liv + 1; index++) {
                                    current.add(minimi.get(j).get(index));
                                }
                                current.add(keys.get(z));
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

            xls.addInfoGrafo(listaGrafi.get(i), "frpilot", endTime, minimumSolution);
            xls.salva(pathTabellaRisultati);
        }

        xls.salva(pathTabellaRisultati);
    }

    private static ArrayList<String> listaFile() {
        ArrayList<String> listaFile = new ArrayList<>();

    //    listaFile.add("50_200_50_13_1.mlst");
   
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

    private static HashMap<Integer, Integer> CalcolaFrequenze(GrafoColorato grafo) {
        HashMap<Integer, Integer> frequenze = new HashMap<>();

        for (int i = 0; i < grafo.getListaColori().size(); i++) {
            frequenze.put(i, 0);
        }

        for (int i = 0; i < grafo.getArchi().size(); i++) {
            frequenze.put(grafo.getArchi().get(i).getDa().getChiave(), (frequenze.get(grafo.getArchi().get(i).getDa().getChiave())) + 1);
            frequenze.put(grafo.getArchi().get(i).getA().getChiave(), (frequenze.get(grafo.getArchi().get(i).getA().getChiave())) + 1);
        }

        return frequenze;
    }

    private static HashMap<Integer, Integer> CalcolaPesoArchi(GrafoColorato g, HashMap<Integer, Integer> frequenze) {
        HashMap<Integer, Integer> pesoArchi = new HashMap<>();

        for (int i = 0; i < g.getArchi().size(); i++) {
            int somma = 0;
            for (int j = 0; j < g.getArchi().get(i).getColori().size(); j++) {
                somma += frequenze.get(g.getArchi().get(i).getColori().get(j));
            }
            pesoArchi.put(i, somma);
        }

        return pesoArchi;
    }

    private static LinkedHashMap<Integer, Double> sortHashMapByValues(HashMap<Integer, Double> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Double> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                int key = keyIt.next();
                double comp1 = passedMap.get(key);
                double comp2 = val;

                if (comp1 == comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

}
