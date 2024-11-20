/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import GUI.LineaDeProduccionGUI;
import java.awt.Image;
import javax.swing.*;

/**
 *
 * @author sonic
 */

public class ArticuloHilo extends Thread {
    // Referencia a la GUI para actualizar elementos visuales
    private LineaDeProduccionGUI gui;
    private JLabel articulo;  // JLabel que del artículo en movimiento
    private int velocidad;  // Velocidad de desplazamiento del artículo
    private LineaDeProduccionControl control;  // Control de la linea
    private ImageIcon imagenArticulo;  // Imagen del artículo en movimiento
    private ImageIcon imagenEmpaquetado;  // Imagen del artículo empaquetado
    private FabricaArt fabricaArt;  // Control de fábrica de empaquetados

    // Constructor recibe la GUI, velocidad, control de línea y fábrica
    public ArticuloHilo(LineaDeProduccionGUI gui, int velocidad, LineaDeProduccionControl control, FabricaArt fabricaArt) {
        this.gui = gui;
        this.velocidad = velocidad;
        this.control = control;
        this.fabricaArt = fabricaArt;

        // Cargar las imágenes de artículo y empaquetado
        ImageIcon originalArticulo = new ImageIcon(getClass().getResource("/GUI/articulo.png"));  // Ruta a tu imagen de artículo
        ImageIcon originalEmpaquetado = new ImageIcon(getClass().getResource("/GUI/empaquetado.png"));  // Ruta a tu imagen de empaquetado

        // Redimensionar las imágenes para que se ajusten al JLabel
        imagenArticulo = new ImageIcon(originalArticulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        imagenEmpaquetado = new ImageIcon(originalEmpaquetado.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        // Configurar el JLabel con la imagen inicial del artículo
        articulo = new JLabel(imagenArticulo);
        articulo.setSize(50, 50);  // Tamaño del JLabel
        articulo.setLocation(0, gui.getPanelBanda().getHeight() / 2 - 25);  // Posición inicial en la banda, poco debajo de la mitad
    }

    @Override
    public void run() {
        gui.agregarArticulo(articulo);  // Agregar el artículo a la GUI

        //mover el artículo hasta el final de la banda
        while (articulo.getX() < gui.getPanelBanda().getWidth() - articulo.getWidth() - 10) {
            control.esperarSiEstaPausado();  // Esperar si la línea está pausada

            // Mover el artículo hacia la derecha
            try {
                Thread.sleep(velocidad);  // Pausa para ajustar la velocidad de movimiento
                articulo.setLocation(articulo.getX() + 10, articulo.getY());  // Actualizar posición del artículo
            } catch (InterruptedException e) {
                e.printStackTrace();  //error si ocurre una excepción en el hilo
            }
        }

        // Pausar toda la línea cuando el artículo llega al final de la banda
        control.pausar();

        // Cambiar imagen del artículo al llegar al final para simular empaquetado
        SwingUtilities.invokeLater(() -> articulo.setIcon(imagenEmpaquetado));  // Cambiar a imagen de empaquetado
        try {
            Thread.sleep(1000);  // Pausa para mostrar el empaquetado
        } catch (InterruptedException e) {
            e.printStackTrace();  // Imprimir error si ocurre una excepción durante la pausa
        }

        // Eliminar el artículo de la GUI después de mostrarlo empaquetado
        gui.eliminarArticulo(articulo);

        fabricaArt.incrementarEmpaquetados();  // Incrementar el contador de artículos empaquetados en la fábrica
        // Reanudar la línea después de empaquetar y eliminar el artículo
        control.reanudar();
    }
}
