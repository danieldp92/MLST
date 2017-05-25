package greedy;

/**
 * Questa classe permette di memorizzare le statistiche sull'elaborazione degli
 * algoritmi
 *
 * @author Stefano Dalla Palma
 */
public class Statistiche {

    public int iter;
    public long tempoRecuperoArchiConColoreMinimo;
    public long tempoRecuperoArchiConColoreMinimoCount;
    public long tempoDeterminazioneColorePiuRicorrente;
    public long tempoDeterminazioneColorePiuRicorrenteCount;
    public long tempoRimozioneColorePiuRicorrente;
    public long tempoRimozioneColorePiuRicorrenteCount;
    public long tempoInserimentoArchiSenzaCiclo;
    public long tempoInserimentoArchiSenzaCicloCount;
    public double tempoMedioIterate;
    public double tempoMedioDeterminazioneColorePiuRicorrente;
    public double tempoMedioInserimentoArchiSenzaCiclo;
    public double meanTimeRecuperoArchiConColoreMinimo;
    public double meanTimeRimozioneColorePiuRicorrente;
    public float tempoEsecuzione;

    public Statistiche() {
    }

}
