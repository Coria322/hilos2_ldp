/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

/**
 *
 * @author sonic
 */

public class LineaDeProduccionControl {
    private boolean enPausa; // Controla si los artículos deben detenerse o no

    // Constructor del estado de la línea
    public LineaDeProduccionControl() {
        this.enPausa = false; // Inicialmente la línea no está en pausa
    }

    // Método para pausar la línea de producción
    public synchronized void pausar() {
        enPausa = true; // Cambia el estado a pausado
    }

    // Método para reanudar la línea de producción
    public synchronized void reanudar() {
        enPausa = false; // Cambia el estado a no pausado
        notifyAll(); // Notifica a todos los hilos esperando para que se reanuden
    }

    // Método que permite a los hilos verificar si deben esperar
    public synchronized void esperarSiEstaPausado() {
        while (enPausa) { // Si está en pausa, el hilo espera
            try {
                wait(); // Espera hasta que la línea sea reanudada
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaura el estado de interrupción del hilo
            }
        }
    }
}
