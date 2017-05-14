package gestore;

import graph.Arco;
import graph.Grafo;
import graph.Nodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Questa classe permette di generare un grafo.
 *
 * @author Stefano Dalla Palma
 */
public class GeneratoreGrafo {

    /**
     * Genera un grafo dato in input un file di nodi archi colori
     *
     * @param pGrafo il File contente le istruzioni per la costruzione del Grafo
     * @return un Grafo
     */
    public static Grafo generaGrafo(File pGrafo) {
        ArrayList<Nodo> nodi = new ArrayList<>();
        ArrayList<Arco> archi = new ArrayList<>();
        Set<Integer> coloriGrafo = new HashSet<>();

        Nodo primoNodo;
        Nodo secondoNodo;
        ArrayList<Integer> coloriArco = new ArrayList<>();
        int numeroNodi = 0;

        ArrayList<String> builder = new ArrayList();

        //Leggo da file
        try (BufferedReader br = new BufferedReader(new FileReader(pGrafo))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.add(line);
            }

        } catch (IOException ex) {
            Logger.getLogger(GeneratoreGrafo.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < builder.size(); i++) {
            String[] line = builder.get(i).split(" ");

            if (i == 0) {
                //Lista nodi
                numeroNodi = Integer.parseInt(line[0]);
                for (int j = 0; j < numeroNodi; j++) {
                    nodi.add(new Nodo(j));
                }

            } else {
                primoNodo = nodi.get(Integer.parseInt(line[0]));
                secondoNodo = nodi.get(Integer.parseInt(line[1]));

                for (int j = 2; j < line.length; j++) {
                    int colore = Integer.parseInt(line[j]);
                    coloriGrafo.add(colore);
                    coloriArco.add(colore);
                }
                Arco arco = new Arco(primoNodo, secondoNodo, new ArrayList(coloriArco));
                archi.add(arco);
                coloriArco.clear();

                primoNodo.addArcoIncidente(arco);
                secondoNodo.addArcoIncidente(arco);
                primoNodo.addNodoAdiacente(secondoNodo);
                secondoNodo.addNodoAdiacente(primoNodo);
            }
        }
        return new Grafo(nodi, archi, coloriGrafo);
    }
}
