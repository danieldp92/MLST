package gestore;

import grafo.Arco;
import grafo.Colore;
import grafo.Grafo;
import grafo.GrafoColorato;
import grafo.Nodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public static GrafoColorato generaGrafo(File pGrafo) {
        ArrayList<Nodo> nodi = new ArrayList<>();
        LinkedHashMap<Integer, Arco> archi = new LinkedHashMap<>();
        LinkedHashMap<Integer, Colore> colori = new LinkedHashMap<>();

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
                
                //Colori
                int numColori = Integer.parseInt(line[2]) + 1;
                for (int j = 0; j < numColori; j++)
                    colori.put(j, new Colore(j));

            } else {
                primoNodo = nodi.get(Integer.parseInt(line[0]));
                secondoNodo = nodi.get(Integer.parseInt(line[1]));
                
                primoNodo.addIndiceArcoIncidente(i-1);
                primoNodo.addIndiceArcoUscente(i-1);
                secondoNodo.addIndiceArcoIncidente(i-1);
                secondoNodo.addIndiceArcoEntrante(i-1);
                primoNodo.addNodoAdiacente(secondoNodo);
                secondoNodo.addNodoAdiacente(primoNodo);
                
                Arco arco = new Arco(primoNodo, secondoNodo, new ArrayList(coloriArco));
                
                for (int j = 2; j < line.length; j++) {
                    int colore = Integer.parseInt(line[j]);
                    arco.addColore(colore);
                    colori.get(colore).addIndiceArcoCollegato(i-1);
                }
                
                
                archi.put((i-1), arco);
                coloriArco.clear();
            }
        }
        return new GrafoColorato(nodi, archi, colori);
    }
}
