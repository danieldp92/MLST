package gestore;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class XlsGrafo {

    private HSSFWorkbook work;

    private LinkedHashMap<String, Integer> indiciRigheGrafi;
    private int indiceColonnaTempoDiEsecuzioneCPLEX;
    private int indiceColonnaTotColoriCPLEX;
    private int indiceColonnaTempoDiEsecuzioneGreedy;
    private int indiceColonnaTotColoriGreedy;
    private int indiceColonnaTempoDiEsecuzioneNDGreedy;
    private int indiceColonnaTotColoriNDGreedy;
    private int indiceColonnaTempoDiEsecuzioneMultistart;
    private int indiceColonnaTotColoriMultistart;
    private int indiceColonnaIterataSoluzioneMultistart;
    private int indiceColonnaTempoDiEsecuzioneAG;
    private int indiceColonnaTotColoriAG;
    private int indiceColonnaTempoDiEsecuzionePilot;
    private int indiceColonnaTotColoriPilot;
    private int indiceColonnaProfonditaSoluzionePilot;

    public XlsGrafo() {
        setIndiciRigheGrafi();
        setIndiciColonne();
    }

    public void carica(String path) throws FileNotFoundException, IOException {
        FileInputStream fileIn = new FileInputStream(new File(path));
        this.work = new HSSFWorkbook(fileIn);
    }

    public void addInfoGrafo(String nomeGrafo, String tipoAlgoritmo, double tempoDiEsecuzione, int numColori) {
        String algoritmo = tipoAlgoritmo.toLowerCase();
        int indiceRiga = this.indiciRigheGrafi.get(nomeGrafo);
        int indiceColonnaTempo = 0;
        int indiceColonnaColori = 0;

        switch (algoritmo) {
            case "cplex":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneCPLEX;
                indiceColonnaColori = this.indiceColonnaTotColoriCPLEX;
                break;
            case "greedy":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneGreedy;
                indiceColonnaColori = this.indiceColonnaTotColoriGreedy;
                break;
            case "nd-greedy":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneNDGreedy;
                indiceColonnaColori = this.indiceColonnaTotColoriNDGreedy;
                break;
            case "multistart":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneMultistart;
                indiceColonnaColori = this.indiceColonnaTotColoriMultistart;
                break;
            case "pilot":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzionePilot;
                indiceColonnaColori = this.indiceColonnaTotColoriPilot;
                break;
            case "ag":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneAG;
                indiceColonnaColori = this.indiceColonnaTotColoriAG;
                break;
            default:
                break;
        }

        HSSFSheet worksheet = work.getSheetAt(0);

        HSSFRow row = worksheet.getRow(indiceRiga);

        HSSFCell cellaTempo = row.createCell(indiceColonnaTempo);
        cellaTempo.setCellValue(tempoDiEsecuzione);
        HSSFCell cellaTotColori = row.createCell(indiceColonnaColori);
        cellaTotColori.setCellValue(numColori);
    }
    
    public void addInfoGrafo(String nomeGrafo, String tipoAlgoritmo, double tempoDiEsecuzione, int numColori, int valore) {
        String algoritmo = tipoAlgoritmo.toLowerCase();
        int indiceRiga = this.indiciRigheGrafi.get(nomeGrafo);
        int indiceColonnaTempo = 0;
        int indiceColonnaColori = 0;

        switch (algoritmo) {
            case "cplex":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneCPLEX;
                indiceColonnaColori = this.indiceColonnaTotColoriCPLEX;
                break;
            case "greedy":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneGreedy;
                indiceColonnaColori = this.indiceColonnaTotColoriGreedy;
                break;
            case "nd-greedy":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneNDGreedy;
                indiceColonnaColori = this.indiceColonnaTotColoriNDGreedy;
                break;
            case "multistart":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneMultistart;
                indiceColonnaColori = this.indiceColonnaTotColoriMultistart;
                break;
            case "pilot":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzionePilot;
                indiceColonnaColori = this.indiceColonnaTotColoriPilot;
                break;
            case "ag":
                indiceColonnaTempo = this.indiceColonnaTempoDiEsecuzioneAG;
                indiceColonnaColori = this.indiceColonnaTotColoriAG;
                break;
            default:
                break;
        }

        HSSFSheet worksheet = work.getSheetAt(0);

        HSSFRow row = worksheet.getRow(indiceRiga);

        HSSFCell cellaTempo = row.createCell(indiceColonnaTempo);
        cellaTempo.setCellValue(tempoDiEsecuzione);
        HSSFCell cellaTotColori = row.createCell(indiceColonnaColori);
        cellaTotColori.setCellValue(numColori);

        //Se algoritmo = multistart -> valore = iterata soluzione
        //Altrimenti se algoritmo = pilot -> valore = livello di profondità
        if(algoritmo.equals("multistart")) {
            HSSFCell cellaIterataSoluzione = row.createCell(this.indiceColonnaIterataSoluzioneMultistart);
            cellaIterataSoluzione.setCellValue(valore);
        } else if (algoritmo.equals("pilot")) {
            HSSFCell cellaProfondita = row.createCell(this.indiceColonnaProfonditaSoluzionePilot);
            cellaProfondita.setCellValue(valore);
        }
    }

    
    public void salva(String path) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(new File(path));
        this.work.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    private void setIndiciRigheGrafi() {
        this.indiciRigheGrafi = new LinkedHashMap<>();

        //Archi da 50 200 50
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("50_200_50_13_" + i + ".mlst", i + 4);
        }

        //Archi da 50 1000 50
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("50_1000_50_3_" + i + ".mlst", i + 15);
        }

        //Archi da 100 400 100 
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("100_400_100_25_" + i + ".mlst", i + 26);
        }

        //Archi da 100 800 100
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("100_800_100_13_" + i + ".mlst", i + 37);
        }

        //Archi da 100 1000 100
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("100_1000_100_10_" + i + ".mlst", i + 43);
        }

        //Archi da 100 2000 100
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("100_2000_100_5_" + i + ".mlst", i + 54);
        }

        //Archi da 100 3000 100
        for (int i = 1; i <= 10; i++) {
            this.indiciRigheGrafi.put("100_3000_100_4_" + i + ".mlst", i + 65);
        }

        //Archi da 500 2000 500
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("500_2000_500_125_" + i + ".mlst", i + 76);
        }

        //Archi da 500 4000 500
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("500_4000_500_63_" + i + ".mlst", i + 82);
        }

        //Archi da 1000 4000 1000
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("1000_4000_1000_250_" + i + ".mlst", i + 88);
        }

        //Archi da 1000 8000 1000
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("1000_8000_1000_125_" + i + ".mlst", i + 94);
        }

        //Archi da 10000 40000 10000
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("10000_40000_10000_2500_" + i + ".mlst", i + 100);
        }

        //Archi da 10000 80000 10000
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("10000_80000_10000_1250_" + i + ".mlst", i + 106);
        }

        //Archi da 10000 160000 10000
        for (int i = 1; i <= 5; i++) {
            this.indiciRigheGrafi.put("10000_160000_10000_625_" + i + ".mlst", i + 112);
        }
    }

    private void setIndiciColonne() {
        this.indiceColonnaTempoDiEsecuzioneCPLEX = 1;
        this.indiceColonnaTotColoriCPLEX = 2;
        this.indiceColonnaTempoDiEsecuzioneGreedy = 3;
        this.indiceColonnaTotColoriGreedy = 4;
        this.indiceColonnaTempoDiEsecuzioneNDGreedy = 5;
        this.indiceColonnaTotColoriNDGreedy = 6;
        this.indiceColonnaTempoDiEsecuzioneMultistart = 7;
        this.indiceColonnaTotColoriMultistart = 8;
        this.indiceColonnaIterataSoluzioneMultistart = 9;
        this.indiceColonnaTempoDiEsecuzionePilot = 10;
        this.indiceColonnaTotColoriPilot = 11;
        this.indiceColonnaProfonditaSoluzionePilot = 12;
        this.indiceColonnaTempoDiEsecuzioneAG = 13;
        this.indiceColonnaTotColoriAG = 14;
    }

    private void creaListaGrafi() throws FileNotFoundException, IOException {
        HSSFSheet worksheet = work.getSheetAt(0);

        for (Map.Entry<String, Integer> entry : indiciRigheGrafi.entrySet()) {
            HSSFRow row = worksheet.createRow(entry.getValue());
            HSSFCell cellA = row.createCell(0);
            cellA.setCellValue(entry.getKey());
        }
    }

}
