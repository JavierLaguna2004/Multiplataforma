/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.time.LocalDate;

public class ControlCaja {
    private int id;
    private LocalDate fecha;
    private double montoInicial;
    private double montoFinal;
    private int idEmpleado;

    // Constructor
    public ControlCaja(int id, LocalDate fecha, double montoInicial, double montoFinal, int idEmpleado) {
        this.id = id;
        this.fecha = fecha;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.idEmpleado = idEmpleado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }
}