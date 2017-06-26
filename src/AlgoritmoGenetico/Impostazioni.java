/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;


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
    double mutationRateChangeColor;
    double mutationRateDecreaseColor;
    
    public Impostazioni () {
        this.nomeGrafo = "100_1000_100_10_1.mlst";
        this.maxValutazioni = 500;
        this.sizePopolazione = 100;
        this.sizeCromosoma = 100;
        this.crossoverRate = 0.8;
        this.mutationRate = 0.005;
        this.mutationRateDecreaseColor = 0.01;
        this.mutationRateChangeColor = this.mutationRateDecreaseColor + 0.01;
    }
}
