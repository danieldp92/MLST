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
 * Test greedy Krumke
 *
 * @author Stefano Dalla Palma
 */
public class TestGreedy {

    public static void test() throws IOException {
        XlsGrafo xls = new XlsGrafo();
        String pathTabellaRisultati = "src/Risultati/TabellaRisultati.xls";
        xls.carica(pathTabellaRisultati);
        
        ArrayList<String> listaGrafi = listaFile();
        
        for (String s : listaGrafi) {
            //Carico il grafo
            System.out.println(s);
            long i = System.currentTimeMillis();
            GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/" + s));
            grafo.nomeGrafo = s;
            System.out.println("Tempo generazione grafo: " + (System.currentTimeMillis() - i));
            //Ottengo un MLT eseguendo l'algoritmo greedy sul grafo
            Greedy greedy = new Greedy(grafo);
            GrafoColorato mlst = greedy.esegui();

            Statistiche statistiche = greedy.getStatistiche();
            
            xls.addInfoGrafo(grafo.nomeGrafo, "greedy", statistiche.tempoDiEsecuzione, mlst.getListaColori().size());

            System.out.println("Tempo di esecuzione: " + statistiche.tempoDiEsecuzione);
            System.out.println("Numero iterate: " + statistiche.iter);
            System.out.println("Tempo Medio a iterata: " + statistiche.meanTimeIterate);
            System.out.println("Tempo Medio recupero archi con colore minimo: " + statistiche.meanTimeRecuperoArchiConColoreMinimo);
            System.out.println("Tempo Medio inserimento archi senza generare cicli: " + statistiche.meanTimeInserimentoArchiSenzaCiclo);
            System.out.println("Tempo Medio rimozione archi dal grafo originale che generano cicli nell'mlst: " + statistiche.meanTimeRimozioneArchi);
            System.out.println("Tempo Medio determinazione colore più ricorrente: " + statistiche.meanTimeDeterminazioneColorePiuRicorrente);
            System.out.println("Tempo Medio rimozione colore più ricorrente: " + statistiche.meanTimeRimozioneColorePiuRicorrente);
            
        }

        xls.salva(pathTabellaRisultati);
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
