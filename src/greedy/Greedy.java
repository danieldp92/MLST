package greedy;

import gestore.GestoreGrafo;
import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import grafo.GrafoColorato;
import grafo.Nodo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Stefano Dalla Palma
 */
public class Greedy {

    GrafoColorato grafo;

    public Greedy(GrafoColorato grafo) {
        this.grafo = grafo;
    }

    public GrafoColorato esegui(boolean random) {
        return esegui(null, random);
    }

    public GrafoColorato esegui(List<Integer> listaColori) {
        return esegui(listaColori, true);
    }
    
    public GrafoColorato esegui (List<Integer> listaColori, boolean random) {

        GrafoColorato mlst = new GrafoColorato(grafo.getCopiaNodi(), grafo.getColori().size());
        ArrayList<Arco> tmpArchi = grafo.getCopiaArchi();
        ArrayList<Colore> tmpColori = grafo.getCopiaColori();

        GestoreGrafo gestoreMlst = new GestoreGrafo(mlst);

        ArrayList<Arco> edgeWithMinColors;
        ArrayList<Integer> indexOfEdgeWithMinColors;
        
        int colorePiùFrequente = 0;

        //Controllo se esiste qualche nodo con 1 solo arco incidente
        for (int i = 0; i < this.grafo.getNodi().size(); i++) {
            if (this.grafo.getNodo(i).getIndiciArchiIncidenti().size() == 1) {
                int indiceArco = this.grafo.getNodo(i).getIndiciArchiIncidenti().get(0);
                for (int colore : this.grafo.getArco(indiceArco).getColori()) {
                    rimuoviColore(tmpArchi, tmpColori, colore);
                }
            }
        }
        
        //Inserisco tutti i colori dati in input
        if (listaColori != null) {
            while (!listaColori.isEmpty()) {
                colorePiùFrequente = listaColori.remove(0);
                rimuoviColore(tmpArchi, tmpColori, colorePiùFrequente);
            }
        }

        //Ciclo, fin quando mlst non e' connesso
        while (!gestoreMlst.connesso()) {
            //Prelevo gli indici degli archi con colore minimo, poichè le operazioni effettuate su un arraylist 
            //con gli indici (get, set) hanno complessità O(1), diversamente dalle operazioni effettuate con gli oggetti
            indexOfEdgeWithMinColors = getIndiciArchiConColoriMinimi(tmpArchi);

            //Prendo gli archi con il minor numero di colori
            edgeWithMinColors = getArchiConColoriMinimi(tmpArchi, indexOfEdgeWithMinColors);

            //Se gli archi minimi non hanno colori, inseriscili nel mlst
            if (edgeWithMinColors.get(0).getColori().isEmpty()) {

                for (int i : indexOfEdgeWithMinColors) {

                    Arco originale = grafo.getArco(i);
                    gestoreMlst.addArcoSenzaInserireCicli(i, originale);
                    
                    //In questo modo, lavoriamo con complessità O(1)
                    tmpArchi.set(i, null);
                }
                
                rimuoviArchiCheGeneranoCicli(tmpArchi, tmpColori, mlst);

            } else {
                //Determino il colore più ricorrente in edgeWithMinColors
                //Estrazione colori più ricorrenti solo dagli archi con numero colori minimo
                colorePiùFrequente = colorePiùFrequente2(edgeWithMinColors, random);

                //Elimino il colore dagli archi (temporanei)
                rimuoviColore(tmpArchi, tmpColori, colorePiùFrequente);
            }
        }
        
        return mlst;
    }
    
    

    private ArrayList<Integer> getIndiciArchiConColoriMinimi(ArrayList<Arco> pEdges) {
        ArrayList<Integer> indexOfEdgesWithMinColors = new ArrayList<>();
        int previousTotColorEdge = -1;
        int totColorForEdge = -1;

        for (int i = 0; i < pEdges.size(); i++) {
            if (pEdges.get(i) != null) {
                totColorForEdge = pEdges.get(i).getColori().size();
                //Primo arco analizzato
                if (previousTotColorEdge == -1) {
                    previousTotColorEdge = totColorForEdge;
                    indexOfEdgesWithMinColors.add(i);
                } else //Successivi
                {
                    if (totColorForEdge < previousTotColorEdge) {
                        previousTotColorEdge = totColorForEdge;
                        //Svuoto la lista degli archi minori,
                        //poichè ho trovato un nuovo candidato
                        indexOfEdgesWithMinColors.clear();
                        indexOfEdgesWithMinColors.add(i);
                    } else if (previousTotColorEdge == totColorForEdge) {
                        indexOfEdgesWithMinColors.add(i);
                    }
                }
            }
        }
        return indexOfEdgesWithMinColors;
    }

    private ArrayList<Arco> getArchiConColoriMinimi(ArrayList<Arco> pEdges, ArrayList<Integer> pIndexOfEdge) {
        ArrayList<Arco> pArchiConColoriMinimi = new ArrayList<>();

        for (int i : pIndexOfEdge) {
            pArchiConColoriMinimi.add(pEdges.get(i));
        }

        return pArchiConColoriMinimi;
    }

    private int colorePiùFrequente(ArrayList<Arco> pEdges, boolean random) {
        int ricorrenzaMaggiore = -1;

        int[] ricorrenzeColori = new int[this.grafo.getColori().size()];

        //Scorro tutti gli archi e memorizzo le ricorrenze
        //Compessità: O(m * 3)      m -> num. archi, 3 -> num colori per arco
        for (Arco a : pEdges) {
            for (int indiceColore : a.getColori()) {
                ++ricorrenzeColori[indiceColore];
            }
        }

        //Trovo il colore più ricorrente
        //Complessità: O(c)         c -> num. colori
        ArrayList<Integer> coloriPiuComuni = new ArrayList();
        for (int i = 0; i < ricorrenzeColori.length; i++) {
            if (ricorrenzeColori[i] > ricorrenzaMaggiore) {
                coloriPiuComuni.clear();
                coloriPiuComuni.add(i);
                ricorrenzaMaggiore = ricorrenzeColori[i];
            } else if (ricorrenzeColori[i] == ricorrenzaMaggiore) {
                coloriPiuComuni.add(i);
            }
        }

        int indiceDaSelezionare = 0;

        if (random) {
            indiceDaSelezionare = (int) (Math.random() * coloriPiuComuni.size());
        }

        //return colorePiùFrequente;
        return coloriPiuComuni.get(indiceDaSelezionare);
    }
    
    private int colorePiùFrequente2(ArrayList<Arco> pEdges, boolean random) {
        double ricorrenzaMaggiore = -1;

        double[] ricorrenzeColori = new double[this.grafo.getColori().size()];

        double [] rapportoMaxIncidenti = new double[pEdges.size()];
        for (int i = 0; i < pEdges.size(); i++)
            rapportoMaxIncidenti[i] = (double) 1 / Math.max(pEdges.get(i).getDa().getIndiciArchiIncidenti().size(), pEdges.get(i).getA().getIndiciArchiIncidenti().size());
        
        
        //Scorro tutti gli archi e memorizzo le ricorrenze
        //Compessità: O(m * 3)      m -> num. archi, 3 -> num colori per arco
        for (int i = 0; i < pEdges.size(); i++) {
            for (int indiceColore : pEdges.get(i).getColori()) {
                ricorrenzeColori[indiceColore] += rapportoMaxIncidenti[i];
            }
        }
        
        
        
        //Trovo il colore più ricorrente
        //Complessità: O(c)         c -> num. colori
        ArrayList<Integer> coloriPiuComuni = new ArrayList();
        for (int i = 0; i < ricorrenzeColori.length; i++) {
            if (ricorrenzeColori[i] > ricorrenzaMaggiore) {
                coloriPiuComuni.clear();
                coloriPiuComuni.add(i);
                ricorrenzaMaggiore = ricorrenzeColori[i];
            } else if (ricorrenzeColori[i] == ricorrenzaMaggiore) {
                coloriPiuComuni.add(i);
            }
        }

        int indiceDaSelezionare = 0;

        if (random) {
            indiceDaSelezionare = (int) (Math.random() * coloriPiuComuni.size());
        }

        //return colorePiùFrequente;
        return coloriPiuComuni.get(indiceDaSelezionare);
    }

    public void rimuoviColore(ArrayList<Arco> archi, ArrayList<Colore> colori, int pColore) {
        //Determino gli indici degli archi in cui è presente pColore
        for (int i : colori.get(pColore).getIndiciArchiCollegati()) {
            archi.get(i).rimuoviColore(pColore);
        }

        //Elimino ogni riferimento di pColore da ogni arco associato
        colori.get(pColore).getIndiciArchiCollegati().clear();
    }

    private void rimuoviArchiCheGeneranoCicli(ArrayList<Arco> archi, ArrayList<Colore> colori, Grafo mlst) {
        int componenteDiRiferimentoNodo1 = 0;
        int componenteDiRiferimentoNodo2 = 0;

        for (int i = 0; i < archi.size(); i++) {
            if (archi.get(i) != null) {
                componenteDiRiferimentoNodo1 = mlst.getNodo(archi.get(i).getDa().getChiave()).getComponenteDiRiferimento();
                componenteDiRiferimentoNodo2 = mlst.getNodo(archi.get(i).getA().getChiave()).getComponenteDiRiferimento();

                //Se l'arco genera cicli
                if (componenteDiRiferimentoNodo1 == componenteDiRiferimentoNodo2) {
                    //Rimuovo l'indice dell'arco dai colori associati
                    for (int colore : archi.get(i).getColori()) {
                        colori.get(colore).rimuoviIndiceArcoCollegato(i);
                    }
                    
                    archi.set(i, null);
                }
            }

        }
    }

    private GrafoColorato generaGrafoSenzaCicli(GrafoColorato mlst) {
        ArrayList<Integer> listaNodiDiPartenza = new ArrayList<>();
        for (int i = 0; i < mlst.dimensione(); i++) {
            listaNodiDiPartenza.add(-1);
        }

        ArrayList<Arco> listaArchiCandidati = new ArrayList<>();
        boolean[] visitato = new boolean[mlst.dimensione()];

        Queue<Nodo> coda = new LinkedList();
        coda.add(mlst.getNodo(0));

        while (!coda.isEmpty()) {
            Nodo nodo = coda.poll();

            if (!visitato[nodo.getChiave()]) {
                visitato[nodo.getChiave()] = true;

                if (nodo.getChiave() != 0) {
                    Arco tmp = mlst.getArco(nodo.getChiave(), listaNodiDiPartenza.get(nodo.getChiave()));
                    listaArchiCandidati.add(tmp);
                }

                for (Nodo adiacente : nodo.getAdiacenti()) {
                    if (!visitato[adiacente.getChiave()]) {
                        coda.add(adiacente);
                        listaNodiDiPartenza.set(adiacente.getChiave(), nodo.getChiave());
                    }
                }
            }
        }

        ArrayList<Nodo> nodiMlst = new ArrayList<>();
        for (int i = 0; i < mlst.dimensione(); i++) {
            nodiMlst.add(new Nodo(i));
        }

        GrafoColorato mlstSenzaCicli = new GrafoColorato(nodiMlst, mlst.getColori().size());

        int index = 0;
        for (Arco arco : listaArchiCandidati) {
            mlstSenzaCicli.addArco(index++, arco);
        }

        return mlstSenzaCicli;
    }
}
