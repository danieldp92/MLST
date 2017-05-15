package graph;

import java.util.ArrayList;
import java.util.Objects;

public class Arco {

    Nodo da, a;
    ArrayList<Integer> colori;

    public Arco(Nodo da, Nodo a) {
        this.da = da;
        this.a = a;
        this.colori = new ArrayList<>();
    }

    public Arco(Nodo da, Nodo a, ArrayList<Integer> pColori) {
        this.da = da;
        this.a = a;
        this.colori = new ArrayList(pColori);
    }

    public Nodo getDa() {
        return da;
    }

    public void setDa(Nodo da) {
        this.da = da;
    }

    public Nodo getA() {
        return a;
    }

    public void setA(Nodo a) {
        this.a = a;
    }

    public ArrayList<Integer> getColori() {
        return colori;
    }

    public void setColori(ArrayList<Integer> colori) {
        this.colori = colori;
    }

    public void rimuoviColore(int pColore) {
        this.colori.remove(Integer.valueOf(pColore));
    }

    /*
    private ArrayList<Integer> randomColors () {
        boolean atLeastOneColor = false;
        double random = 0;
        ArrayList<Integer> randomColorList = new ArrayList<>();

        for (int i = 0; i < this.numColori); i++) {
            random = Math.random();

            if (random < 0.5) {
                randomColorList.add(0);
            } else {
                randomColorList.add(1);
                atLeastOneColor = true;
            }
        }

        if (!atLeastOneColor) {
            int indexToChange = (int) (Math.random() * this.numColori);
            randomColorList.set(indexToChange, 1);
        }

        return randomColorList;
    }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arco other = (Arco) obj;
        if (!Objects.equals(this.da, other.da)) {
            return false;
        }
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
       
        return true;
    }
}
