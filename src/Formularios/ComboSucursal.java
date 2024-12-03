/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboSucursal {
    private int idSucursal;
    private String nombreSucursal;

    public ComboSucursal(int idProveedor, String nombreProveedor) {
        this.idSucursal = idProveedor;
        this.nombreSucursal = nombreProveedor;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getnombreSucursal() {
        return nombreSucursal;
    }

    @Override
    public String toString() {
        return nombreSucursal;
    }
}
