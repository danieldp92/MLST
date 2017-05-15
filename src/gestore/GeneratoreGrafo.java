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
import java.util.Random;
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
    
    /*
    public static Grafo generaGrafoRandom(int pNumNodi, int pNumArchi, int pSeme) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.colori = new HashSet<>();

        for (int i = 0; i < pNumNodi; i++) {
            Nodo nd = new Nodo(i);
            this.getNodi().add(nd);
        }

        Random r = new Random(pSeme);
        HashSet<String> archi = new HashSet<>(pNumArchi * 2);
        if (pNumArchi > (pNumNodi * (pNumNodi - 1) / 2)) {
            pNumArchi = (pNumNodi * (pNumNodi - 1) / 2);
        }

        while (pNumArchi > 0) {
            int da = r.nextInt(pNumNodi - 1);
            int a = da + 1;
            if (a < pNumNodi - 1) {
                a += r.nextInt((pNumNodi - 1) - (da));
            }
            String ck = da + "-" + a;
            if (!archi.contains(ck)) {
                pNumArchi--;
                archi.add(ck);
                Arco ar = new Arco(this.getArchi().size(), this.getNodi().get(da), this.getNodi().get(a));
                this.getNodi().get(da).getIncidenti().add(ar);
                this.getNodi().get(a).getIncidenti().add(ar);
                this.getArchi().add(ar);
            }
        }
    }
    
    public static Grafo generaGrafoRandomConColori(int pNumNodi, int pNumArchi, int pNumColor, int pSeme) {
        this.nodi = new ArrayList<>();
        this.archi = new ArrayList<>();
        this.numColor = pNumColor;

        for (int i = 0; i < pNumNodi; i++) {
            Nodo nd = new Nodo(i);
            this.getNodi().add(nd);
        }

        Random r = new Random(pSeme);
        HashSet<String> archi = new HashSet<>(pNumArchi * 2);
        if (pNumArchi > (pNumNodi * (pNumNodi - 1) / 2)) {
            pNumArchi = (pNumNodi * (pNumNodi - 1) / 2);
        }

        while (pNumArchi > 0) {
            int da = r.nextInt(pNumNodi - 1);
            int a = da + 1;
            if (a < pNumNodi - 1) {
                a += r.nextInt((pNumNodi - 1) - (da));
            }
            String ck = da + "-" + a;
            if (!archi.contains(ck)) {
                pNumArchi--;
                archi.add(ck);
                Arco ar = new Arco(this.getArchi().size(), this.getNodi().get(da), this.getNodi().get(a), pNumColor);
                this.getNodi().get(da).getIncidenti().add(ar);
                this.getNodi().get(a).getIncidenti().add(ar);
            }
        }

        this.setArchiDallaListaNodi();
    }*/
}
