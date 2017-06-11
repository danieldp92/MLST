/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

/**
 *
 * @author Daniel
 */
public class Impostazioni {
    String nomeGrafo;
    int maxValutazioni;
    int sizePopolazione;
    int sizeCromosoma;
    double crossoverRate;
    double mutationRate;
    
    public Impostazioni () {
        this.nomeGrafo = "1000_8000_1000_125_1.mlst";
        this.maxValutazioni = 10000;
        this.sizePopolazione = 100;
        this.sizeCromosoma = 1000;
        this.crossoverRate = 0.8;
        this.mutationRate = 0.02;
    }
}
