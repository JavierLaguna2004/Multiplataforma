/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboProducto {
    private int idProducto;
    private String nombreProducto;

    public ComboProducto(int idProducto, String nombreProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getnombreProducto() {
        return nombreProducto;
    }

    @Override
    public String toString() {
        return nombreProducto;
    }
}
