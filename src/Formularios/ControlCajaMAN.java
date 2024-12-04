/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.*;
import java.time.LocalDate;

public class ControlCajaMAN {
    private Connection connection;

    public ControlCajaMAN(Connection connection) {
        ConexionSQL cone = new ConexionSQL();
        this.connection = cone.establecerConexion();
    }

    public ControlCaja obtenerControlCajaPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM ControlCaja WHERE Fecha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ControlCaja(
                    rs.getInt("IdControlCaja"),
                    rs.getDate("Fecha").toLocalDate(),
                    rs.getDouble("MontoInicial"),
                    rs.getDouble("MontoFinal"),
                    rs.getInt("IdEmpleado")
                );
            }
        }
        return null;
    }

    public void crearControlCaja(LocalDate fecha, double montoInicial, double montoFinal, int idEmpleado) throws SQLException {
        String sql = "INSERT INTO ControlCaja (Fecha, MontoInicial, MontoFinal, IdEmpleado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setDouble(2, montoInicial);
            stmt.setDouble(3, montoFinal);
            stmt.setInt(4, idEmpleado);
            stmt.executeUpdate();
        }
    }

    public void actualizarControlCaja(int id, double nuevoMontoFinal) throws SQLException {
        String sql = "UPDATE ControlCaja SET MontoFinal = ? WHERE IdControlCaja = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoMontoFinal);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarMontoInicial(int id, double nuevoMontoInicial) throws SQLException {
        String sql = "UPDATE ControlCaja SET MontoInicial = ? WHERE IdControlCaja = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoMontoInicial);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    
    public void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }
        }
    }
}
