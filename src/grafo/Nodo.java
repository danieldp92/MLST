/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.util.ArrayList;

public class Nodo {

    int chiave;
    int componenteDiRiferimento;
    ArrayList<Nodo> adiacenti;
    ArrayList<Integer> indiciArchiIncidenti;

    public Nodo(int pChiave) {
        this.chiave = pChiave;
        this.componenteDiRiferimento = pChiave;
        this.adiacenti = new ArrayList<>();
        this.indiciArchiIncidenti = new ArrayList<>();
    }

    public int getChiave() {
        return chiave;
    }

    public ArrayList<Nodo> getAdiacenti() {
        return adiacenti;
    }

    public ArrayList<Integer> getIndiciArchiIncidenti() {
        return indiciArchiIncidenti;
    }

    public void setChiave(int chiave) {
        this.chiave = chiave;
    }

    public void addNodoAdiacente(Nodo pNodo) {
        this.adiacenti.add(pNodo);
    }

    public void rimuoviNodoAdiacente(Nodo pNodo) {
        if (this.adiacenti.contains(pNodo)) {
            this.adiacenti.remove(pNodo);
        }
    }

    public void addIndiceArcoIncidente(int pIndiceArco) {
        if (!this.indiciArchiIncidenti.contains(pIndiceArco)) {
            this.indiciArchiIncidenti.add(pIndiceArco);
        }
    }

    public void rimuoviIndiceArcoIncidente(int pIndiceArco) {
        if (this.indiciArchiIncidenti.contains(pIndiceArco)) {
            this.indiciArchiIncidenti.remove(pIndiceArco);
        }
    }

    public int getComponenteDiRiferimento() {
        return componenteDiRiferimento;
    }

    public void setComponenteDiRiferimento(int componenteDiRiferimento) {
        this.componenteDiRiferimento = componenteDiRiferimento;
    }

    @Override
    public String toString() {
        return chiave + "";
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Nodo other = (Nodo) obj;
        if (this.chiave != other.chiave) {
            return false;
        }
        return true;
    }
}
