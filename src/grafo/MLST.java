package grafo;

import java.util.ArrayList;

/**
 *
 * @author Stefano Dalla Palma
 */
public class MLST extends GrafoColorato {

    boolean[] bColori;

    public MLST(ArrayList<Nodo> pNodi, int pNumColori) {
        nodi = pNodi;
        bColori = new boolean[pNumColori];
    }

    @Override
    public void addArco(Arco pArco) {

        this.archi.add(pArco);

        getNodo(pArco.getDa().getChiave()).addNodoAdiacente(getNodo(pArco.getA().getChiave()));
        getNodo(pArco.getDa().getChiave()).addArcoIncidente(pArco);
        getNodo(pArco.getA().getChiave()).addNodoAdiacente(getNodo(pArco.getDa().getChiave()));
        getNodo(pArco.getA().getChiave()).addArcoIncidente(pArco);

        for (int c : pArco.getColori()) {
            bColori[c] = true;
        }
    }

    public boolean[] getBColori() {
        return bColori;
    }

    public int getNumeroColori() {
        int numeroColori = 0;

        for (int i = 0; i < bColori.length; i++) {
            if (bColori[i]) {
                numeroColori++;
            }
        }

        return numeroColori;
    }

}
