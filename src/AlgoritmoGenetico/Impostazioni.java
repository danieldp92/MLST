/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Impostazioni {

    public static final String NOME_GRAFO = "nomeGrafo";
    public static final String TOT_COLORI = "totColori";
    public static final String VALUTAZIONI = "maxValutazioni";
    public static final String POPOLAZIONE = "popolazione";
    public static final String CROSSOVER_RATE = "crossoverRate";
    public static final String MUTATION_RATE = "mutationRate";

    public LinkedHashMap<String, String> parametri;

    public Impostazioni() {
        this.parametri = new LinkedHashMap<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Rhobar\\Documents\\GitHub\\MLST\\src\\AlgoritmoGenetico\\settings.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(" ");
                String key = tmp[0].substring(0, tmp[0].length() - 1);
                String value = tmp[1];

                this.parametri.put(key, value);
            }

        } catch (IOException ex) {
            System.out.println("FILE NON TROVATO!!!");
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }
    }
    
    public String getNomeGrafo() {
        return getParametro(NOME_GRAFO);
    }
    
    public int getNumeroColori() {
        return Integer.valueOf(getParametro(TOT_COLORI));
    }
    
    public int getMaxValutazioni() {
        return Integer.valueOf(getParametro(VALUTAZIONI));
    }
    
    public int getPopolazione() {
        return Integer.valueOf(getParametro(POPOLAZIONE));
    }
    
    public double getCrossoverRate() {
        return Double.valueOf(getParametro(CROSSOVER_RATE));
    }
    
    public double getMutationRate() {
        return Double.valueOf(getParametro(MUTATION_RATE));
    }
    
    public String getParametro(String parametro) {
        return this.parametri.get(parametro);
    }

    public void setParametro(String parametro, String valore) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        ArrayList<String> file = getFile();
        
        try {
            File fout = new File("C:\\Users\\Rhobar\\Documents\\GitHub\\MLST\\src\\AlgoritmoGenetico\\settings.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            
            
            for (String line : file) {
                String[] tmp = line.split(" ");
                String key = tmp[0].substring(0, tmp[0].length() - 1);

                if (key.equals(parametro)) {
                    line = tmp[0] + " " + valore;
                    
                    this.parametri.put(parametro, valore);
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException ex) {
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                System.out.println("SCRITTURA NON EFFETTUATA!!!");
            }
        }

    }

    private ArrayList<String> getFile() {
        ArrayList<String> file = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Rhobar\\Documents\\GitHub\\MLST\\src\\AlgoritmoGenetico\\settings.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                file.add(line);
            }

        } catch (IOException ex) {
            System.out.println("FILE NON TROVATO!!!");
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }

        return file;
    }
}
