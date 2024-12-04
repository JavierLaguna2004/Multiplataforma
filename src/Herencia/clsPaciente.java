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
public class clsPaciente extends clsPersona{
    private String telefono;

    public clsPaciente(String nombre, String apellido, String correo, String direccion, String telefono) {
        super(nombre, apellido, correo, direccion);
        this.telefono = telefono;
    }

    public void mostrarInformacion() {
        String mensaje = "Nombre: " + getNombre() + "\n"
                + "Apellido: " + getApellido() + "\n"
                + "Correo: " + getCorreo() + "\n"
                + "Dirección: " + getDireccion() + "\n"
                + "Teléfono: " + telefono;
        JOptionPane.showMessageDialog(null, mensaje, "Información del Paciente", JOptionPane.INFORMATION_MESSAGE);
    }
}
