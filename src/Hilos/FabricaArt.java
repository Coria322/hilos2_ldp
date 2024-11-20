/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import GUI.LineaDeProduccionGUI;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author sonic
 */

public class FabricaArt extends Thread {
    int num;  // Número de artículos a generar
    LineaDeProduccionGUI ldpg;  //interfaz gráfica de la línea de producción
    LineaDeProduccionControl ldpc;  // Control de la línea de producción para manejar pausas y reanudaciones
    private int empaquetados = 0;  // Contador de artículos empaquetados

    // Constructor
    public FabricaArt(LineaDeProduccionGUI ldpg, int num, LineaDeProduccionControl ldpc){
        this.ldpc = ldpc;
        this.ldpg = ldpg;
        this.num = num;
    }

    // Método para generar los artículos en la línea de producción
    public void generar() throws InterruptedException{
        int cajas = num;  // Inicializa con el numero a producir

        // Ciclo que continúa mientras queden artículos por generar
        while (cajas > 0) {
            ldpc.esperarSiEstaPausado();  // Espera si la línea de producción está en pausa

            // Crea un nuevo hilo para cada artículo en la línea de producción
            ArticuloHilo articuloHilo = new ArticuloHilo(this.ldpg , 100, this.ldpc, this);
            articuloHilo.start();  // Inicia el hilo del artículo

            Thread.sleep(1000);  // Pausa entre la generación de cada artículo
            //Funciona para dar distancia entre los articulos
            cajas--;  // Reduce el contador de artículos restantes
        }
    }

    // Método para aumentar el contador de artículos empaquetados
    public synchronized void incrementarEmpaquetados() {
        empaquetados++;  // Aumenta el contador de artículos empaquetados
        ldpg.getContadorEmpaquetados().setText("Articulos Empaquetados: " + empaquetados); //Incrementar el contador en pantalla
        
        // Si todos los artículos han sido empaquetados, muestra un mensaje
        if (empaquetados == num) {
            // Notificar que todos los artículos han sido empaquetados
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(ldpg, "¡El proceso ha terminado!");
                // Resetear los contadores después de que se haya empaquetado todo
                ldpg.getContadorIngresados().setText("Artículos ingresados: 0");
                ldpg.getContadorEmpaquetados().setText("Artículos empaquetados: 0");
            });
        }
    }

    // Método `run` que se ejecuta cuando se inicia el hilo de FabricaArt
    @Override
    public void run(){
        try {
            generar();  // Llama al método de generación de artículos
        } catch (InterruptedException e) {
            e.printStackTrace();  // Imprime la traza de la excepción si ocurre una interrupción
        }
    }
}
