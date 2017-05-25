/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cplex;

import gestore.GeneratoreGrafo;
import gestore.GestoreGrafo;
import grafo.Grafo;
import grafo.GrafoColorato;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author user
 */
public class Test {

    public static void main() throws IloException, IOException {

        GrafoColorato g = GeneratoreGrafo.generaGrafo(new File("src/GrafiColorati3Colori/50_200_50_13_1.mlst"));
        
        int V = g.getNodi().size();
        int E = g.getArchi().size();
        int C = g.getColori().size();

        IloCplex modello = new IloCplex();

        IloIntVar[] c_k = modello.boolVarArray(C);
        modello.addMinimize(modello.sum(c_k));

        IloIntVar[] x_e = modello.boolVarArray(E);
        modello.addEq(modello.sum(x_e), V - 1);

        //Vincolo sulla selezione dei colori
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < 3; j++) {
                modello.addGe(c_k[g.getArchi().get(i).getColori().get(j)], x_e[i]);
            }
        }

        IloNumVar[] f_ij = modello.numVarArray(E * 2, 0, V);

        IloLinearNumExpr v3 = modello.linearNumExpr();
        for (int i = 0; i < E; i++) {
            if ((g.getArchi().get(i).getDa().getChiave()== g.getNodi().get(0).getChiave()|| (g.getArchi().get(i).getA().getChiave()== g.getNodi().get(0).getChiave()))) {
                v3.addTerm(f_ij[i], 1);
            }
        }
        modello.addEq(v3, V - 1);

        IloLinearNumExpr v4 = modello.linearNumExpr();
        for (int i = 0; i < E; i++) {
            if ((g.getArchi().get(i).getDa().getChiave()== g.getNodi().get(0).getChiave()|| (g.getArchi().get(i).getA().getChiave()== g.getNodi().get(0).getChiave()))) {
                v3.addTerm(f_ij[i + E], 1);
            }
        }
        modello.addEq(v4, 0);

        for (int i = 1; i < V; i++) {
            IloLinearNumExpr v5 = modello.linearNumExpr();
            for (int j = 0; j < E; j++) {
                if (g.getArchi().get(j).getDa().getChiave()== i) {
                    v5.addTerm(f_ij[j], 1); //outcome
                    v5.addTerm(f_ij[j + E], -1); //income  
                } else if (g.getArchi().get(j).getA().getChiave()== i) {
                    v5.addTerm(f_ij[j], -1);    //income 
                    v5.addTerm(f_ij[j + E], 1); //outcome      
                }
            }
            modello.addEq(v5, 1);
        }

        /*for(int i=0; i<E-1; i++){
         for(int j=i+1; j<E; j++){
        
         }
         }*/
        for (int i = 0; i < E; i++) {
            IloLinearIntExpr v6 = modello.linearIntExpr();
            v6.addTerm(x_e[i], V);
            modello.addLe(f_ij[i], v6);
            modello.addLe(f_ij[i + E], v6);
        }

        if (modello.solve()) {
            System.out.println("Soluzione:" + (modello.getObjValue()));
            System.out.println("Num Archi" + modello.sum(x_e));
            for(int i=0; i<x_e.length; i++){
                if(modello.getValue(x_e[i])==1){
                    System.out.println("Value:" + modello.getValue(x_e[i]));
                    System.out.println("Arches" + g.getArchi().get(i).getDa().getChiave()+ " " + g.getArchi().get(i).getA().getChiave());
                }
            }
        } else {
            System.out.println("Errore:" + modello.getStatus());
        }

        modello.end();

    }

}
