/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Herencia;

import javax.swing.JOptionPane;

/**
 *
 * @author joser
 */
public class clsEmpleado extends clsPersona {
    private String jornada;
    private String sucursal;
    private String puesto;

    public clsEmpleado(String nombre, String apellido, String correo, String direccion, String jornada, String sucursal, String puesto) {
        super(nombre, apellido, correo, direccion);
        this.jornada = jornada;
        this.sucursal = sucursal;
        this.puesto = puesto;
    }

    public void mostrarInformacion() {
        String mensaje = "Nombre: " + getNombre() + "\n"
                + "Apellido: " + getApellido() + "\n"
                + "Correo: " + getCorreo() + "\n"
                + "Dirección: " + getDireccion() + "\n"
                + "Jornada: " + jornada + "\n"
                + "Sucursal: " + sucursal + "\n"
                + "Puesto: " + puesto;
        JOptionPane.showMessageDialog(null, mensaje, "Información del Empleado", JOptionPane.INFORMATION_MESSAGE);
    }
}
