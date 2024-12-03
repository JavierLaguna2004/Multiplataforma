/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboProveedor {
    private int idProveedor;
    private String nombreProveedor;

    public ComboProveedor(int idProveedor, String nombreProveedor) {
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getnombreProveedor() {
        return nombreProveedor;
    }

    @Override
    public String toString() {
        return nombreProveedor;
    }
}
