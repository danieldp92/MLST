/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greedy;

import grafo.GrafoColorato;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Rhobar
 */
public class MultiThreadGreedy {
    private int totThread;
    private int threadDaEseguireInParallelo;
    private static int threadEseguiti;
    
    private static ReentrantLock lock;
    
    private Greedy greedy;
     
    public MultiThreadGreedy (int totaleThread, int threadInParallelo, Greedy greedy) {
        this.totThread = totaleThread;
        this.threadDaEseguireInParallelo = threadInParallelo;
        
        this.greedy = greedy;
    }
    
    public ArrayList<GrafoColorato> avviaMultiThreadGreedy () throws InterruptedException {
        ArrayList<GrafoColorato> listaMlst = new ArrayList<>();
        ArrayList<GreedyThread> listaThread = new ArrayList<>();
        
        CountDownLatch latch = null;
        lock = new ReentrantLock();
        
        int i = 0;
        int numeroDiThreadDaCreare = 0;
        
        while (i < totThread) {
            if((totThread - i) >= threadDaEseguireInParallelo) {
                numeroDiThreadDaCreare = threadDaEseguireInParallelo;
            } else {
                numeroDiThreadDaCreare = totThread - i;
            }
            
            latch = new CountDownLatch(1);
            threadEseguiti = 0;
                
            //Creo i thread da eseguire in parallelo
            for (int j = 0; j < numeroDiThreadDaCreare; j++) {
                listaThread.add(new GreedyThread(this.greedy, (i+j), latch));
                new Thread(listaThread.get(j)).start();
            }
                
            latch.countDown();
                
            while (threadEseguiti < numeroDiThreadDaCreare) {                    
                Thread.sleep(1);
            }
                
            for (GreedyThread gThread : listaThread)
                listaMlst.add(gThread.mlst);
                
            listaThread.clear();
            
            i += numeroDiThreadDaCreare;
        }
        
        
        return listaMlst;
    }
    
    private static class GreedyThread implements Runnable {
        private CountDownLatch latch;

        private Greedy greedy;
        protected GrafoColorato mlst;
        private int numeroThread;

        public GreedyThread(Greedy greedy, int numeroThread, CountDownLatch latch) {
            this.latch = latch;

            this.greedy = greedy;
            this.mlst = null;
            this.numeroThread = numeroThread;
        }

        @Override
        public synchronized void run() {
            try {
                latch.await();
            } catch (InterruptedException ex) {}

            this.mlst = greedy.esegui(true);
            incrementa();
        }

    }
    
    /**
     * Accesso esclusivo alla variabile di incremento
     */
    private static void incrementa() {
        lock.lock();
        try {
            threadEseguiti++;
        } finally {
            lock.unlock();
        }
    }
}
