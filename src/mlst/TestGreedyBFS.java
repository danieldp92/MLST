/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import graph.Grafo;
import greedy.Greedy;
import greedy.GreedyBFS;
import greedy.GreedyFirstFit;
import java.io.File;

/**
 *
 * @author Stefano Dalla Palma
 */
public class TestGreedyBFS {

    public static void main() {
        System.out.print("Caricamento grafo... ");
        //Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/50_200_50_13_1.mlst"));
        Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/10000_160000_10000_625_1.mlst"));
        //Grafo grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/7_11_4.mlst"));

        //Ottengo un MLT eseguendo l'algoritmo greedy sul grafo
        //Grafo mlst = new GreedyBFS(grafo).esegui();
        Grafo mlst = new GreedyFirstFit(grafo).esegui();
        GestoreGrafo gG = new GestoreGrafo(mlst);

        System.out.println("Colori usati: " + mlst.getColori().size());
        System.out.println("MLST numero nodi: " + mlst.dimensione());
        System.out.println("MLST numero archi: " + mlst.getArchi().size());
        System.out.println("Connesso: " + gG.connesso());
        boolean f = gG.ciclo(mlst);
        System.out.println("Ciclo: " + f);

        //Stampo i colori usati
        for (int colore : mlst.getColori()) {
            System.out.println(colore);
        }
    }
}
