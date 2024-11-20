/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Hilos.FabricaArt;
import Hilos.LineaDeProduccionControl;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sonic
 */

public class LineaDeProduccionGUI extends JFrame {
    // Labels para mostrar el contador de artículos ingresados y empaquetados
    private JLabel contadorIngresados;
    private JLabel contadorEmpaquetados;

    // Botones para iniciar y detener el proceso
    private JButton iniciarProceso;
    private JButton detenerProceso;

    // Panel para visualizar la banda de producción
    private JPanel panelBanda;

    // Lista para almacenar los artículos en movimiento en la banda de producción
    private List<JLabel> articulos;
    
    //controlador
    LineaDeProduccionControl ldpc = new LineaDeProduccionControl();

    public LineaDeProduccionGUI() {
        // Configuración inicial de la ventana
        setTitle("Linea de Producción");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel para los contadores de artículos
        JPanel panelContadores = new JPanel(new GridLayout(1, 2));
        contadorIngresados = new JLabel("Artículos ingresados: 0");
        contadorEmpaquetados = new JLabel("Artículos empaquetados: 0");
        panelContadores.add(contadorIngresados);
        panelContadores.add(contadorEmpaquetados);

        // Inicialización de botones para controlar el proceso
        iniciarProceso = new JButton("Iniciar Proceso");
        detenerProceso = new JButton("Detener Proceso");

        //Listener
        iniciarProceso.addActionListener(e ->{
         String arts = JOptionPane.showInputDialog("Ingrese la cantidad de artículos:");   
                            try {
                    // Convierte arts en un número entero
                    int cantidad = Integer.parseInt(arts);
                    contadorIngresados.setText("Artículos ingresados: " + cantidad);  // Actualiza el contador en la ventana

                    // Crea y empieza el hilo de la fábrica
                    FabricaArt fabricaArt = new FabricaArt(this, cantidad, ldpc);
                    fabricaArt.start();  // Inicia la producción de artículos en la línea
                } catch (NumberFormatException ex) {
                    // Muestra un mensaje de error si el input no es un número válido
                    JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.");
                }
        });
        
        detenerProceso.addActionListener(e -> System.exit(0));
        
        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(iniciarProceso);
        panelBotones.add(detenerProceso);

        // Panel que representa la banda de producción
        panelBanda = new JPanel();
        panelBanda.setLayout(null);
        panelBanda.setBackground(Color.LIGHT_GRAY);

        // Añadiendo paneles al frame
        add(panelContadores, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelBanda, BorderLayout.CENTER);

        // Inicializando la lista de artículos
        articulos = new ArrayList<>();
    }

    // Métodos getter para acceder a los componentes desde fuera de la clase
    public JLabel getContadorIngresados() {
        return contadorIngresados;
    }

    public JLabel getContadorEmpaquetados() {
        return contadorEmpaquetados;
    }

    public JButton getIniciarProceso() {
        return iniciarProceso;
    }

    public JButton getDetenerProceso() {
        return detenerProceso;
    }

    public JPanel getPanelBanda() {
        return panelBanda;
    }

    // Método para agregar un artículo a la banda de producción
    public void agregarArticulo(JLabel articulo) {
        articulos.add(articulo); // Añadir el artículo a la lista
        panelBanda.add(articulo); // Añadir el artículo al panel
        panelBanda.revalidate(); 
        panelBanda.repaint();    // Repintar para reflejar los cambios
    }

    // Método para eliminar un artículo de la banda de producción
    public void eliminarArticulo(JLabel articulo) {
        articulos.remove(articulo); // Eliminar el artículo de la lista
        panelBanda.remove(articulo); // Eliminar el artículo del panel
        panelBanda.revalidate();
        panelBanda.repaint();    // Repintar para reflejar los cambios
    }
}
