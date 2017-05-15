/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;

public class Nodo {

    int chiave;
    ArrayList<Nodo> adiacenti;
    ArrayList<Arco> incidenti;

    public Nodo(int pChiave) {
        this.chiave = pChiave;
        this.adiacenti = new ArrayList<>();
        this.incidenti = new ArrayList<>();
    }

    public int getChiave() {
        return chiave;
    }

    public ArrayList<Nodo> getAdiacenti() {
        return adiacenti;
    }

    public ArrayList<Arco> getIncidenti() {
        return incidenti;
    }

    public void setChiave(int chiave) {
        this.chiave = chiave;
    }

    public void addNodoAdiacente(Nodo pNodo) {
        this.adiacenti.add(pNodo);
    }

    public void rimuoviNodoAdiacente(Nodo pNodo) {
        if (!this.adiacenti.contains(pNodo)) {
            this.adiacenti.remove(pNodo);
        }
    }

    public void addArcoIncidente(Arco pArco) {
        if (!this.incidenti.contains(pArco)) {
            this.incidenti.add(pArco);
        }
    }

    public void rimuoviArcoIncidente(Arco pArco) {
        if (!this.incidenti.contains(pArco)) {
            this.incidenti.remove(pArco);
        }
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

