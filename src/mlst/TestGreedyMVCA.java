/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlst;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.Colore;
import grafo.GrafoColorato;
import grafo.MLST;
import greedy.GreedyMVCA;
import java.io.File;

/**
 *
 * @author Stefano Dalla Palma
 */
public class TestGreedyMVCA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //for (int i = 0; i < 50; i++) {
           
            GrafoColorato grafo = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/1000_8000_1000_125_1.mlst"));

            
            GreedyMVCA greedy = new GreedyMVCA(grafo);
            long inizio = System.currentTimeMillis();
            //Grafo mlst = greedy.esegui();
            MLST mlst = greedy.esegui();
            long tempoEsecuzione = System.currentTimeMillis()-inizio;
            GestoreGrafo gestore = new GestoreGrafo(mlst);
            System.out.println("Ciclo: " + gestore.ciclo());
            System.out.println("Connesso: " + gestore.connesso());
            //System.out.println("Numero colori: " + mlst.getColori().size());
            System.out.println("Numero colori: " + mlst.getNumeroColori());
            System.out.println("Tempo esecuzione greedy: " + tempoEsecuzione+"ms.");
            /*for(Colore c : mlst.getColori()){
        System.out.println(c.getColore());
        }*/
       // }
    }

}
