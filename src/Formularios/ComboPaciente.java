/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

/**
 *
 * @author joser
 */
public class ComboPaciente {
       private int idPaciente;
    private String nombrePaciente;

    public ComboPaciente(int idPaciente, String nombrePaciente) {
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getnombrePaciente() {
        return nombrePaciente;
    }

    @Override
    public String toString() {
        return nombrePaciente;
    }
}
