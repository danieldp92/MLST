/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.GrafoColorato;
import random.RandomBFS;
import java.io.File;
import java.util.Random;

/**
 *
 * @author Stefano Dalla Palma
 */
public class TestRandomBFS {

    public static void main(String[] args) {

        GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/10000_160000_10000_625_1.mlst"));

        for (int i = 0; i < 10; i++) {
            RandomBFS greedy = new RandomBFS(grafo.clona());
            long inizio = System.currentTimeMillis();
            GrafoColorato mlst = greedy.esegui(new Random().nextInt(grafo.dimensione()));
            long tempoEsecuzione = System.currentTimeMillis() - inizio;
            GestoreGrafo gestore = new GestoreGrafo(mlst);
            System.out.println("Numero archi: " + mlst.getArchi().size());
            System.out.println("Ciclo: " + gestore.ciclo());
            System.out.println("Connesso: " + gestore.connesso());
            System.out.println("Numero colori: " + mlst.getColori().size());
            System.out.println("Tempo esecuzione greedy: " + tempoEsecuzione + "ms.");
        }
    }

}
