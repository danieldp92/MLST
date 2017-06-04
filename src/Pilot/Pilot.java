package Pilot;

import grafo.GrafoColorato;
import greedy.Statistiche;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Pilot {

    //private static List<Integer> coloriProcessati;
    //private static List<Integer> numeroColoriSelezionati;
    private GrafoColorato grafo;
    private Statistiche statistiche;

    public Pilot(GrafoColorato grafo) {
        this.grafo = grafo;
        //coloriProcessati = Collections.synchronizedList(new LinkedList<>());
        //numeroColoriSelezionati = Collections.synchronizedList(new LinkedList<>());
    }

    public GrafoColorato esegui() {
        GreedyPilot greedy = new GreedyPilot(grafo);
        GrafoColorato mlst;

        ArrayList<Integer> listaColori = grafo.getListaColori();

        LinkedList coloriSelezionati = new LinkedList();
        int profondita = 0;

        long inizio = System.currentTimeMillis();

        int profonditaSoluzioneMigliore = 0;
        int soluzioneMigliore = grafo.getColori().size();

        do {
            LinkedList<Integer> coloriProcessati = new LinkedList();    //Mappa il colore selezionato ed il numero di colori trovati
            LinkedList<Integer> numeroColoriSelezionati = new LinkedList();    //Mappa il colore selezionato ed il numero di colori trovati
            LinkedList<Integer> cloniColoriDiPartenza = new LinkedList(coloriSelezionati);

            /// PROVA EXECUTOR SERVICE ________________
            /* ExecutorService executorService = Executors.newFixedThreadPool(2);

            for (Integer colore : listaColori) {
                if (!coloriSelezionati.contains(colore)) {
                    cloniColoriDiPartenza.add(colore);
                    executorService.execute(new GreedyPilot(grafo, new LinkedList((LinkedList<Integer>) cloniColoriDiPartenza.clone()), colore));
                    System.out.println("P: " + profondita + ", colore: " + colore);
                    cloniColoriDiPartenza.removeLast();
                }
            }
            executorService.shutdown();

            try {
                executorService.awaitTermination(600, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pilot.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (!executorService.isTerminated()) {
            }

            ////_____________FINE PROVA*/
            for (Integer colore : listaColori) {
                if (!coloriSelezionati.contains(colore)) {
                    cloniColoriDiPartenza.add(colore);
                    mlst = greedy.esegui(cloniColoriDiPartenza);
                    //Aggiorno profondita della soluzione
                    int numeroColoriMlst = mlst.getListaColori().size();
                    if (numeroColoriMlst < soluzioneMigliore) {
                        profonditaSoluzioneMigliore = profondita;
                        soluzioneMigliore = numeroColoriMlst;
                    }

                    System.out.println("P: " + profondita + ", colore: " + colore + ", Numero colori: " + numeroColoriMlst);
                    cloniColoriDiPartenza.removeLast();

                    //Mappo il colore con il numero di colori generati
                    coloriProcessati.add(colore);
                    numeroColoriSelezionati.add(mlst.getListaColori().size());
                }
            }
            //Cerco il colore che permette di selezionare il numero minore di colori
            int indice = -1;
            int ncoloriMinimo = 0;
            for (int i = 0; i < numeroColoriSelezionati.size(); i++) {
                int ncolori = numeroColoriSelezionati.get(i);
                if (i == 0) {
                    ncoloriMinimo = ncolori;
                    indice = 0;
                } else if (ncolori < ncoloriMinimo) {
                    ncoloriMinimo = ncolori;
                    indice = i;
                }
            }

            //Prendo il colore minimo e lo aggiungo alla lista dei colori di partenza
            if (indice != -1) {
                coloriSelezionati.add(coloriProcessati.get(indice));
            }

            //Clear
            coloriProcessati.clear();
            numeroColoriSelezionati.clear();

            System.out.print("Colori selezionati: ");
            for (Object colore : coloriSelezionati) {
                System.out.print(colore + " ");
            }
            System.out.println();

            profondita++;
        } while (profondita < 10);

        mlst = greedy.esegui(coloriSelezionati);

        this.statistiche = greedy.getStatistiche();
        this.statistiche.profonditaSoluzione = profonditaSoluzioneMigliore;
        this.statistiche.tempoDiEsecuzione = (System.currentTimeMillis() - inizio);

        return mlst;
    }

    public Statistiche getStatistiche() {
        return statistiche;
    }

    /* public static synchronized void addColore(Integer colore, Integer numeroColori) {
    coloriProcessati.add(colore);
    numeroColoriSelezionati.add(numeroColori);
    }*/
}
