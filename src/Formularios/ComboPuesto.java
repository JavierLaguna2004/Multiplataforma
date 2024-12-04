/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboPuesto {
    private int idPuesto;
    private String nombrePuesto;

    public ComboPuesto(int idPuesto, String nombrePuesto) {
        this.idPuesto = idPuesto;
        this.nombrePuesto = nombrePuesto;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public String getnombrePuesto() {
        return nombrePuesto;
    }

    @Override
    public String toString() {
        return nombrePuesto;
    }
} 
