/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboEmpleado {
    private int idEmpleado;
    private String correo;

    public ComboEmpleado(int idEmpleado, String correo) {
        this.idEmpleado = idEmpleado;
        this.correo = correo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return correo;
    }
}
